package com.tree.f2st.dto;

import com.tree.f2st.entity.TreeEntity;
import lombok.*;
import org.apache.poi.ss.formula.functions.T;

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
    private String imgPath; //실제 서버 DTO에서는 이미지패스추가

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
                .build();
        return treeEntity;
    }

    public void show(){
        System.out.println(tid+" "+dist+" "+dbh+" "+height+" "+azimuth+" "+latitude+" "+longitude+" "+pid+" "+imgPath);
    }
}
