package com.tree.f2st.dto;

import com.tree.f2st.entity.AnalysisEntity;
import com.tree.f2st.entity.TreeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@Setter
@NoArgsConstructor
public class AnalysisDTO {

    private String tid;
    private String method;
    private String totalVolume;
    private String logsVolume;
    private String logsLength;
    private String logsEdia;
    private String logsSweep;
    private String deduction;
    private String analyzedImgPath;

    @Builder
    public AnalysisDTO(String tid,
                          String method, String totalVolume, String logsVolume,
                          String logsLength, String logsEdia, String logsSweep,
                          String deduction, String analyzedImgPath){
        this.tid=tid;
        this.method = method;
        this.totalVolume=totalVolume;
        this.logsVolume=logsVolume;
        this.logsLength =logsLength;
        this.logsEdia = logsEdia;
        this.logsSweep = logsSweep;
        this.deduction = deduction;
        this.analyzedImgPath = analyzedImgPath;
    }

    public void of(AnalysisEntity ae){
        this.setTid(ae.getTreeEntity().getTid());
        this.setMethod(ae.getMethod());
        this.setTotalVolume(ae.getTotalVolume());
        this.setLogsVolume(ae.getLogsVolume());
        this.setLogsLength(ae.getLogsLength());
        this.setLogsEdia(ae.getLogsEdia());
        this.setLogsSweep(ae.getLogsSweep());
        this.setDeduction(ae.getDeduction());
        this.setAnalyzedImgPath(ae.getAnalyzedImgPath());
    }

    public AnalysisEntity toAnalysisEntity(){
        AnalysisEntity analysisEntity = AnalysisEntity.builder()
                .treeEntity(new TreeDTO(tid).toEntity())
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
