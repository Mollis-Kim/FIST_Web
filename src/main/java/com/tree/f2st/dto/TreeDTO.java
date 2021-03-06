package com.tree.f2st.dto;

import com.tree.f2st.entity.AnalysisEntity;
import com.tree.f2st.entity.TreeEntity;
import lombok.*;
import org.apache.poi.ss.formula.functions.T;

import javax.persistence.Column;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TreeDTO {

    private String tid; // 나무아이디
    private String dist; // 거리
    private String dbh; // 흉고직경
    private String height;  // 수고
    private String azimuth; //방위각
    private String latitude; // 위도
    private String longitude; // 경도
    private String pid; // 조사자
    private String investigationPlace; // 조사장소
    private String imgPath; //실제 서버 DTO에서는 이미지패스추가
    private String location;

    private String year; //연령
    private String mai; //cai
    private String cai; // cai


    private String yearOfForest;
    private String presslerRatio;
    private String presslerAmount;
    private String schneiderRatio;
    private String schneiderAmount;


    public TreeDTO(String tid){
        this.tid = tid;
        this.dist = "";
        this.dbh = "";
        this.height = "";
        this.azimuth="";
        this.latitude = "";
        this.longitude = "";
        this.pid = "";
        this.imgPath="";
        this.investigationPlace="";
        this.location="";
        this.year="";
        this.mai="";
        this.cai="";
    }

    public TreeEntity toEntity(){
        TreeEntity treeEntity =TreeEntity.builder()
                .tid(tid)
                .dist(dist)
                .dbh(dbh)
                .azimuth(azimuth)
                .height(height)
                .latitude(latitude)
                .longitude(longitude)
                .pid(pid)
                .imgPath(imgPath)
                .investigationPlace(investigationPlace)
                .location(location)
                .year(year)
                .mai(mai)
                .cai(cai)
                .build();
        return treeEntity;
    }

    public void of(TreeEntity te){
        this.setTid(te.getTid());
        this.dist=te.getDist();
        this.dbh=te.getDbh();
        this.azimuth=te.getAzimuth();
        this.height=te.getHeight();
        this.latitude=te.getLatitude();
        this.longitude=te.getLongitude();
        this.pid=te.getPid();
        this.imgPath=te.getImgPath();
        this.investigationPlace=te.getInvestigationPlace();
        this.location=te.getLocation();
        this.year=te.getYear();
        this.mai=te.getMai();
        this.cai=te.getCai();
    }

}
