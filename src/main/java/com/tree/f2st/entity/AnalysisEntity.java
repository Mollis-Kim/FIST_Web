package com.tree.f2st.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="analysis")
@Entity
public class AnalysisEntity {

    //@Column(name="seq")
    //@GeneratedValue(strategy=GenerationType.AUTO)
    //private Long seq;


    @ManyToOne(targetEntity = TreeEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "tid")
    private TreeEntity treeEntity;


    @Column(name="method")
    private String method;

    @Column(name="totalVolume")
    private String totalVolume;

    @Column(name="logsVolume")
    private String logsVolume;

    @Column(name="logsLength")
    private String logsLength;

    @Column(name="logsEdia")
    private String logsEdia;

    @Column(name="logsSweep")
    private String logsSweep;

    @Column(name="deduction")
    private String deduction;
    
    @Id
    @Column(name="analyzedImgPath")
    private String analyzedImgPath;

    @Builder
    public AnalysisEntity(TreeEntity treeEntity,
            String method, String totalVolume, String logsVolume,
            String logsLength, String logsEdia, String logsSweep,
                          String deduction, String analyzedImgPath){
        this.treeEntity=treeEntity;
        this.method = method;
        this.totalVolume=totalVolume;
        this.logsVolume=logsVolume;
        this.logsLength =logsLength;
        this.logsEdia = logsEdia;
        this.logsSweep = logsSweep;
        this.deduction = deduction;
        this.analyzedImgPath = analyzedImgPath;
    }

}
