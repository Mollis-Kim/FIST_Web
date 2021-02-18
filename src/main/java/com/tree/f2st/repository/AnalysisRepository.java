package com.tree.f2st.repository;


import com.tree.f2st.entity.AnalysisEntity;
import com.tree.f2st.entity.TreeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AnalysisRepository extends JpaRepository<AnalysisEntity, Long> {

    //tid검색
    public List<AnalysisEntity> findByTreeEntity(@Param("tid") TreeEntity te);


}