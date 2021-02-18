package com.tree.f2st.service;

import com.tree.f2st.dto.AnalysisDTO;
import com.tree.f2st.dto.TreeDTO;
import com.tree.f2st.entity.AnalysisEntity;
import com.tree.f2st.entity.TreeEntity;
import com.tree.f2st.repository.AnalysisRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class AnalysisService {
    @Autowired
    AnalysisRepository analysisRepository;

    @Transactional
    public String save(AnalysisDTO analysisDTO)
    {
        String r = analysisRepository.save(analysisDTO.toAnalysisEntity()).toString();
        System.out.println("서비스  "+r);
        return r;
    }

    public  List<AnalysisEntity> findByTid(String tid){
        TreeDTO treeDTO = new TreeDTO(tid);
        List<AnalysisEntity> t = new ArrayList<>();
        analysisRepository.findByTreeEntity(treeDTO.toEntity()).forEach(e->t.add(e));
        return t;
    }

    public boolean isExist(String tid){
        List<AnalysisEntity> t = findByTid(tid);
        return (t.size()>0)? true: false;
    }

    public void deleteByTid(String tid){
        List<AnalysisEntity> trees = findByTid(tid);
        trees.forEach(e->analysisRepository.delete(e));
        analysisRepository.flush();
    }

}
