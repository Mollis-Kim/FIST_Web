package com.tree.f2st.entity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="tree")
public class TreeEntity {


    @javax.persistence.Id
    @Id
    @Column(name="tid")
    private String tid; // 나무아이디
    @Column(name="dist")
    private String dist; // 거리
    @Column(name="dbh")
    private String dbh; // 흉고직경
    @Column(name="height")
    private String height;  // 수고
    @Column(name="azimuth")
    private String azimuth; //방위각
    @Column(name="latitude")
    private String latitude; // 위도
    @Column(name="longitude")
    private String longitude; // 경도
    @Column(name="pid")
    private String pid; // 조사자
    @Column(name="imgPath")
    private String imgPath; //실제 서버 DTO에서는 이미지패스추가
    @Column(name="investigationPlace")
    private String investigationPlace; // 조사장소(별칭)
    @Column(name="location")
    private String location; //표준지 좌표


    @Column(name="year")
    private String year; //연령
    @Column(name="mai")
    private String mai; //cai
    @Column(name="cai")
    private String cai; // cai


    @Column(name="yearOfForest")
    private String yearOfForest;
    @Column(name="presslerRatio")
    private String presslerRatio;
    @Column(name="presslerAmount")
    private String presslerAmount;
    @Column(name="schneiderRatio")
    private String schneiderRatio;
    @Column(name="schneiderAmount")
    private String schneiderAmount;


    @Builder
    public TreeEntity(
            String tid, String dist, String dbh, String height, String azimuth,
            String latitude,String longitude, String pid,String imgPath, String investigationPlace, String location,
            String year, String mai, String cai){
        this.tid = tid;
        this.dist = dist;
        this.dbh = dbh;
        this.height = height;
        this.azimuth=azimuth;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pid = pid;
        this.imgPath=imgPath;
        this.investigationPlace=investigationPlace;
        this.location=location;
        this.year=year;
        this.mai=mai;
        this.cai=cai;
    }

}
