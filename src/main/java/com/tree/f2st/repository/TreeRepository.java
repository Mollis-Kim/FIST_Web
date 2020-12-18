package com.tree.f2st.repository;

import com.tree.f2st.entity.TreeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TreeRepository extends JpaRepository<TreeEntity, Long> {

    //tid검색
    public List<TreeEntity> findByTid(@Param("tid") String tid);

    //like검색 ~ pid #조사자
    public List<TreeEntity> findByPidLike(@Param("pid") String pid);

    //위치검색

    //기간검색


}
