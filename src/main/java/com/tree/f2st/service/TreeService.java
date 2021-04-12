package com.tree.f2st.service;

import com.sun.source.tree.Tree;
import com.tree.f2st.dto.AnalysisDTO;
import com.tree.f2st.dto.TreeDTO;
import com.tree.f2st.entity.AnalysisEntity;
import com.tree.f2st.entity.TreeEntity;
import com.tree.f2st.repository.AnalysisRepository;
import com.tree.f2st.repository.TreeRepository;
import com.tree.f2st.util.ExcelUtil;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//service - 비즈니스 로직 구현, repository 또는 controller에 data 전달
@AllArgsConstructor
@Service
public class TreeService {
    @Autowired
    private TreeRepository treeRepository;


    final private AnalysisRepository analysisRepository;

    @Transactional
    public String save(TreeDTO treeDTO)
    {
        return treeRepository.save(treeDTO.toEntity()).toString();
    }

    public List<TreeEntity> findAll() {
        List<TreeEntity> trees = new ArrayList<>();
        treeRepository.findAll().forEach(e->trees.add(e));
        return trees;
    }

    public boolean isExist(String tid){
        List<TreeEntity> t = findByTid(tid);

        return (t.size()>0)? true: false;
    }

    public boolean updateAddition(String tid, String year,String mai, String cai){

        treeRepository.updateAdditionalInfo(tid,year,mai,cai);

        return true;
    }
    public boolean updatePlaceAddition(String place, String year,String pressler1,String pressler2,
                                       String schneider1, String schneider2){

        treeRepository.updatePlaceAdditionalInfo(place,year,pressler1, pressler2, schneider1, schneider2);

        return true;
    }
    public  List<TreeEntity> findByTid(String tid){
        List<TreeEntity> t = new ArrayList<>();
        treeRepository.findByTid(tid).forEach(e->t.add(e));
        return t;
    }

    public boolean saveImg(String tid, String imgPath){
        treeRepository.updateImgPath(tid,imgPath);
        return true;
    }


//
//    public void updateById(String tid, String evolume){
//        Optional<TreeEntity> e= treeRepository.findById(treeNo);
//        if(e.isPresent()){
//            e.get().setEvolume(evolume);
//        }
//    }

    public void deleteByTid(String tid){
        List<TreeEntity> trees = treeRepository.findByTid(tid);
        trees.forEach(e->treeRepository.delete(e));
        treeRepository.flush();
    }


    public ByteArrayInputStream load() {
        JSONArray jsonArray = new JSONArray();
        List<TreeEntity> trees = treeRepository.findAll();


        for(TreeEntity tree:trees){
            JSONObject data = new JSONObject();
            data.put("tid",tree.getTid());
            data.put("dist",tree.getDist());
            data.put("dbh",tree.getDbh());
            data.put("height",tree.getHeight());
            data.put("latitude",tree.getLatitude());
            data.put("longitude",tree.getLongitude());
            data.put("ageoftree",tree.getYear());
            data.put("ageofstand",tree.getYearOfForest());
            data.put("CAI",tree.getCai());
            data.put("MAI",tree.getMai());


            double dbh = Double.parseDouble(tree.getDbh());
            double basalArea = ((dbh*dbh)*Math.PI)/40000;
            data.put("basalarea",String.format("%.4f",basalArea));
            data.put("프레슬러_생장률",tree.getPresslerRatio());
            data.put("프레슬러_ha당생장량",tree.getPresslerAmount());
            data.put("슈나이더_생장률",tree.getSchneiderRatio());
            data.put("슈나이더_ha당생장량",tree.getSchneiderAmount());
            List<AnalysisEntity> aTrees = analysisRepository.findByTreeEntity(tree);

            String[] methods = {"doyle", "hanna", "misp", "scrib","EDSM", "inter"};


            if(aTrees.size()>0) {
                for(AnalysisEntity analysisEntity : aTrees) {
                    if(analysisEntity.getTreeEntity().getTid().equals(tree.getTid())) {
                        for (int i = 0; i < 6; i++) {
                            if (analysisEntity.getMethod().equals(methods[i])) {
                                data.put(methods[i], analysisEntity.getTotalVolume());
                            }
                        }
                    }
                }
            }

            for(int i=0; i<6;i++){
                if(data.get(methods[i])==null){
                    data.put(methods[i],"");
                }
            }

            jsonArray.add(data);
        }

        ByteArrayInputStream in = ExcelUtil.jsonArrayToExcelFile(jsonArray);
        return in;
    }

    public List<TreeEntity> findByInvestigationPlace(String investKey){
        List<TreeEntity> trees = treeRepository.findByInvestigationPlace(investKey);
        return trees;
    }


}
