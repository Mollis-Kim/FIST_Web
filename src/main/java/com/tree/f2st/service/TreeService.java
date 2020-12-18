package com.tree.f2st.service;

import com.sun.source.tree.Tree;
import com.tree.f2st.dto.TreeDTO;
import com.tree.f2st.entity.TreeEntity;
import com.tree.f2st.repository.TreeRepository;
import com.tree.f2st.util.ExcelUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<TreeEntity> findAll() {
        List<TreeEntity> trees = new ArrayList<>();
        treeRepository.findAll().forEach(e->trees.add(e));
        return trees;
    }

    @Transactional
    public String save(TreeDTO treeDTO)
    {
        return treeRepository.save(treeDTO.toEntity()).toString();
    }

    public  List<TreeEntity> findByTid(String tid){
        List<TreeEntity> t = new ArrayList<>();
        treeRepository.findByTid(tid).forEach(e->t.add(e));
        return t;
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
        List<TreeEntity> trees = treeRepository.findAll();
        ByteArrayInputStream in = ExcelUtil.ListToExcelFile(trees);
        return in;
    }


}
