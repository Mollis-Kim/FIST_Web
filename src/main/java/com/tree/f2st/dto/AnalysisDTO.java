package com.tree.f2st.dto;

import com.tree.f2st.entity.AnalysisEntity;
import com.tree.f2st.entity.TreeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
public class AnalysisDTO {

    private TreeEntity treeEntity;
    private String method;
    private String totalVolume;
    private String logsVolume;
    private String logsLength;
    private String logsEdia;
    private String logsSweep;
    private String deduction;
    private String analyzedImgPath;

    @Builder
    public AnalysisDTO(TreeDTO treeDTO,
                          String method, String totalVolume, String logsVolume,
                          String logsLength, String logsEdia, String logsSweep,
                          String deduction, String analyzedImgPath){
        this.treeEntity=treeDTO.toEntity();
        this.method = method;
        this.totalVolume=totalVolume;
        this.logsVolume=logsVolume;
        this.logsLength =logsLength;
        this.logsEdia = logsEdia;
        this.logsSweep = logsSweep;
        this.deduction = deduction;
        this.analyzedImgPath = analyzedImgPath;
    }


    public AnalysisEntity toAnalysisEntity(){
        AnalysisEntity analysisEntity = AnalysisEntity.builder()
                .treeEntity(treeEntity)
                .method(method)
                .totalVolume(totalVolume)
                .logsVolume(logsVolume)
                .logsLength(logsLength)
                .logsEdia(logsEdia)
                .logsSweep(logsSweep)
                .deduction(deduction)
                .analyzedImgPath(analyzedImgPath)
                .build();
        return analysisEntity;
    }
}
