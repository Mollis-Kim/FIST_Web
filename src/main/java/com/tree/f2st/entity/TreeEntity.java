package com.tree.f2st.entity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Entity
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


    @Builder
    public TreeEntity(
            String tid, String dist, String dbh, String height, String azimuth,
            String latitude,String longitude, String pid,String imgPath){
        this.tid = tid;
        this.dist = dist;
        this.dbh = dbh;
        this.height = height;
        this.azimuth=azimuth;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pid = pid;
        this.imgPath=imgPath;
    }

}
