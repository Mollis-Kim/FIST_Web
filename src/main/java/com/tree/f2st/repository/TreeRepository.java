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

    //조사지 검색
    @Query(value= "select * from tree t where t.investigation_place = ?1", nativeQuery = true)
    public List<TreeEntity> findByInvestigationPlace(String investigationPlace);


    @Query(value ="select t.investigationPlace from TreeEntity T", nativeQuery = true)
    public List<String> getInvestigationPlace();

    // 추가정보 업데이트
    @Transactional
    @Modifying
    @Query(value = "UPDATE TreeEntity TE SET TE.year = ?2,TE.mai = ?3,TE.cai = ?4  WHERE TE.tid = ?1")
    void updateAdditionalInfo(String tid, String year, String mai, String cai);

    @Transactional
    @Modifying
    @Query(value = "UPDATE TreeEntity TE SET TE.yearOfForest = ?2, TE.presslerRatio=?3,TE.presslerAmount=?4,TE.schneiderRatio=?5, TE.schneiderAmount=?6 WHERE TE.investigationPlace = ?1")
    void updatePlaceAdditionalInfo(String place, String year,String pressler1,String pressler2,String schneider1, String schneider2);
}
