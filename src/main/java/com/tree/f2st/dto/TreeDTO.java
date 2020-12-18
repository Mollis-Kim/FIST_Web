package com.tree.f2st.dto;

import com.tree.f2st.entity.TreeEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TreeDTO {

    private String tid;
    private String dist;
    private String dbh;
    private String height;
    private String latitude;
    private String longitude;
    private String pid;
    private String imgloc;
    private String imgid;

    public TreeEntity toEntity(){
        TreeEntity treeEntity = TreeEntity.builder()
                .tid(tid)
                .dist(dist)
                .dbh(dbh)
                .height(height)
                .latitude(latitude)
                .longitude(longitude)
                .pid(pid)
                .imgid(imgid)
                .imgloc(imgloc)
                .build();
        return treeEntity;
    }
    @Builder
    public TreeDTO(String tid, String dist, String dbh, String height,
                   String longitude, String latitude){
        this.tid = tid;
        this.dist = dist;
        this.dbh = dbh;
        this.height = height;
        this.longitude = longitude;
        this.latitude = latitude;
        this.pid = "";
        this.imgid = "";
        this.imgloc = "";
    }
//    public TreeDTO(String tid, String dist, String dbh, String height,
//                   String latitude, String longitude, String pid,
//                   String imgid, String imgloc){
//        this.tid = tid;
//        this.dist = dist;
//        this.dbh = dbh;
//        this.height = height;
//        this.longitude = longitude;
//        this.latitude = latitude;
//        this.pid = pid;
//        this.imgid = imgid;
//        this.imgloc = imgloc;
//    }
//    public TreeDTO(String tid, String dist, String dbh, String height){
//        this.tid = tid;
//        this.dist = dist;
//        this.dbh = dbh;
//        this.height = height;
//        this.longitude = "";
//        this.latitude = "";
//        this.pid = "";
//        this.imgid = "";
//        this.imgloc = "";
//    }
}
