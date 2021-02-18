package com.tree.f2st.repository;

import com.tree.f2st.entity.TreeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface TreeRepository extends JpaRepository<TreeEntity, String> {

    //tid검색
    public List<TreeEntity> findByTid(@Param("tid") String tid);

    //like검색 ~ pid #조사자
    public List<TreeEntity> findByPidLike(@Param("pid") String pid);

    // 아이디 검색 후 이미지 경로 업데이트
    @Transactional
    @Modifying
    @Query(value = "UPDATE TreeEntity TE SET TE.imgPath = ?2 WHERE TE.tid = ?1")
    int updateImgPath(String tid, String imgPath);

    //위치검색

    //기간검색


}
