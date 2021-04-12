package com.tree.f2st.service;

import com.sun.source.tree.Tree;
import com.tree.f2st.dto.AnalysisDTO;
import com.tree.f2st.dto.TreeDTO;
import com.tree.f2st.entity.AnalysisEntity;
import com.tree.f2st.entity.TreeEntity;
import com.tree.f2st.repository.AnalysisRepository;
import com.tree.f2st.repository.TreeRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;

@AllArgsConstructor
@Service
public class AnalysisService {
    @Autowired
    AnalysisRepository analysisRepository;


    TreeRepository treeRepository;



    @Transactional
    public String save(AnalysisDTO analysisDTO)
    {
        String r = analysisRepository.save(analysisDTO.toAnalysisEntity()).toString();
        System.out.println("서비스  "+r);
        return r;
    }

    public  List<TreeEntity> getTreeList(){
        return treeRepository.findAll();
    }
    public  List<String> getAnalyzedTreeList(){
        // 각 tid당 method방식별로 나옴
        List<AnalysisEntity> analysisEntityList= analysisRepository.findAll();
        List<String> names = new ArrayList<>();

        List<AnalysisDTO> analysisDTOList = new ArrayList<>();
        for(AnalysisEntity ae:analysisEntityList){
            String s = ae.getTreeEntity().getTid();

            if(names.indexOf(s)==-1)
                names.add(s);
        }

        return names;
    }

    public List<AnalysisDTO> findAll(){
        List<AnalysisDTO> analysisDTOS = new ArrayList<>();
        List<AnalysisEntity> analysisEntityList = analysisRepository.findAll();
        for(AnalysisEntity analysisEntity : analysisEntityList){
            AnalysisDTO analysisDTO = new AnalysisDTO();
            analysisDTO.of(analysisEntity);
            analysisDTOS.add(analysisDTO);
        }
        return analysisDTOS;
    }

    public List<AnalysisDTO> findByTid(String tid){

        TreeDTO treeDTO = new TreeDTO(tid);
        TreeEntity te = treeDTO.toEntity();

        
        ArrayList<AnalysisEntity> t = (ArrayList<AnalysisEntity>) analysisRepository.findByTreeEntity(te);
        if(t!=null) {
            List<AnalysisDTO> analysisDTOS = new ArrayList<>();
            for(AnalysisEntity ae : t) {
                AnalysisDTO analysisDTO = new AnalysisDTO();
                analysisDTO.of(ae);
                analysisDTOS.add(analysisDTO);
            }
            return analysisDTOS;
        }else{
            return null;
        }
    }

    public List<String> findMethodByTid(String tid){

        List<String> result = new ArrayList<>();

        TreeDTO treeDTO = new TreeDTO(tid);
        TreeEntity te = treeDTO.toEntity();

        ArrayList<AnalysisEntity> t = (ArrayList<AnalysisEntity>) analysisRepository.findByTreeEntity(te);
        if(t!=null) {
            List<AnalysisDTO> analysisDTOS = new ArrayList<>();
            for(AnalysisEntity ae : t) {
                AnalysisDTO analysisDTO = new AnalysisDTO();
                analysisDTO.of(ae);

                result.add(analysisDTO.getMethod());
            }
        }

        return result;

    }

    public AnalysisDTO findByTidAndMethod(String tid, String method){
        ArrayList<AnalysisDTO> analysisDTOS = (ArrayList<AnalysisDTO>) findByTid(tid);
        for(AnalysisDTO analysisDTO : analysisDTOS){
            if(analysisDTO.getMethod().equals(method)){
                return analysisDTO;
            }
        }
        return null;
    }



    public boolean analyze(String tid, String method, double dbh, HttpServletRequest request) throws IOException {
        // 오리지널이미지파일 받아서 파이썬 실행 분석 후
        // 분석 이미지를 포함한 결과값 DB저장

        System.out.println("서비스 분석 메소드 호출!!");
        TreeDTO treeDTO = new TreeDTO();
        treeDTO.of(treeRepository.findByTid(tid).get(0));
        String filePath = treeDTO.getImgPath();

        String url = "http://localhost:8081";
        String serviceKey = "fromSpring";
        String decodeFilepath = URLDecoder.decode(filePath, "UTF-8");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text","html", Charset.forName("UTF-8")));    //Response Header to UTF-8
        System.out.println(filePath);
        decodeFilepath=decodeFilepath.substring(2);
        decodeFilepath=decodeFilepath.replace('\\','/');
        System.out.println(decodeFilepath);
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("filePath", decodeFilepath)
                .queryParam("method", method)
                .queryParam("dbh",dbh)
                .build(false);    //자동으로 encode해주는 것을 막기 위해 false

        Object response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity<String>(headers), String.class);

        if(response==null){
            System.out.println("전송실패");
        }else{
            System.out.println("전송성공");
            return true;
        }
       return false;
    }



    public void deleteByTidAndMethod(String tid, String method){
        ArrayList<AnalysisEntity> trees = (ArrayList<AnalysisEntity>) analysisRepository.findByTreeEntity(new TreeDTO(tid).toEntity());
        for(AnalysisEntity ae : trees) {
            if(ae.getMethod()==method) {
                analysisRepository.delete(ae);
                analysisRepository.flush();
            }
        }
    }

}
