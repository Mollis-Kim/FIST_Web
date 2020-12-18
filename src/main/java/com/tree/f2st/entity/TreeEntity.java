package com.tree.f2st.entity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="tree")
public class TreeEntity {


//    @javax.persistence.Id
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long treeNo;

    @javax.persistence.Id
    @Id
    @Column(name="tid")
    private String tid;
    @Column(name="dist")
    private String dist;
    @Column(name="dbh")
    private String dbh;
    @Column(name="height")
    private String height;
    @Column(name="longitude")
    private String longitude;
    @Column(name="latitude")
    private String latitude;
    @Column(name="pid")
    private String pid;

    @Column(name="imgid")
    private String imgid;
    @Column(name="imgloc")
    private String imgloc;
    @Column(name="evolume") @Setter
    private String evolume;

    @Builder
    public TreeEntity(
            String tid, String dist, String dbh, String height,
                      String longitude, String latitude, String pid,
                      String imgid, String imgloc){
        this.tid = tid;
        this.dist = dist;
        this.dbh = dbh;
        this.height = height;
        this.longitude = longitude;
        this.latitude = latitude;
        this.pid = pid;
        this.imgid = imgid;
        this.imgloc = imgloc;
        this.evolume = "0.0";
    }

}
