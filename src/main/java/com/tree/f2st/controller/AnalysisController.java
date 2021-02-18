package com.tree.f2st.controller;

import com.tree.f2st.dto.AnalysisDTO;
import com.tree.f2st.dto.TreeDTO;
import com.tree.f2st.service.AnalysisService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("/analysis")
public class AnalysisController {
    @Autowired
    AnalysisService analysisService;

    @RequestMapping(value = "")
    public String analysisHome(){
        return "analysisHome";
    }

    @RequestMapping(value = "/run")
    public String startAnalyzed(@RequestParam(name="path") String filePath, @RequestParam(name = "method") String method){
        System.out.println(filePath+" "+method);
        return "analysisHome";
    }


    @RequestMapping(value = "/analyzeddata")
    public String receiveAnalyzedData(@RequestBody String j, HttpServletRequest request) throws IOException, ParseException {

        String basePath = request.getServletContext().getRealPath("")+"tree_analyzed_image/";

        JSONParser parser = new JSONParser();
        Object o = parser.parse(j);
        JSONObject total_jo = (JSONObject) o;
        JSONObject jo = (JSONObject) total_jo.get("json");
        TreeDTO treeDTO = new TreeDTO(jo.get("tid").toString());

        AnalysisDTO analysisDTO = AnalysisDTO.builder()
                .treeDTO(treeDTO)
                .method(jo.get("method").toString())
                .totalVolume(jo.get("totalVolume").toString())
                .logsVolume(jo.get("logsVolume").toString())
                .logsLength(jo.get("logsLength").toString())
                .logsEdia(jo.get("logsEdia").toString())
                .logsSweep(jo.get("logsSweep").toString())
                .deduction(jo.get("deduction").toString())
                .analyzedImgPath(basePath+total_jo.get("imgName").toString())
                .build();
        System.out.println(analysisDTO.getAnalyzedImgPath());
        analysisService.save(analysisDTO);
        return "analysisHome";
    }
}
