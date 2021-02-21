package com.tree.f2st.controller;

import com.tree.f2st.dto.AnalysisDTO;
import com.tree.f2st.dto.TreeDTO;
import com.tree.f2st.entity.AnalysisEntity;
import com.tree.f2st.entity.TreeEntity;
import com.tree.f2st.service.AnalysisService;
import com.tree.f2st.service.TreeService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/analysis")
public class AnalysisController {
    @Autowired
    AnalysisService analysisService;



    @GetMapping("")
    public String analysisHome(Model model) throws IOException {

//        ClassPathResource resource = new ClassPathResource("python_module/main.py");
//        String pythonPath = resource.getURL().getPath();
//        pythonPath = pythonPath.substring(1);
//        Path p = Path.of(pythonPath);
//        System.out.println(p);

        model.addAttribute("search_analysis", null);
        model.addAttribute("analysis",null);
        model.addAttribute("completeMsg","display:None;");

        if(analysisService.getAnalyzedTreeList().size()>0) {
            model.addAttribute("sidebar_treeList", analysisService.getAnalyzedTreeList());
        }else{
            model.addAttribute("sidebar_treeList", null);
        }
        model.addAttribute("treeList", analysisService.getTreeList());
        model.addAttribute("methodList", new ArrayList<String>(Arrays.asList("doyle", "hanna", "misp", "scrib", "inter")));
        model.addAttribute("formdivstyle","display:Block;");
        return "analysisHome";
    }

    // 분석
    @PostMapping("")
    public String startAnalyzed(@RequestParam(name="tid") String tid, @RequestParam(name = "method") String method, Model model,
                                 HttpServletRequest request) throws IOException {
        System.out.println(tid+" "+method);

        model.addAttribute("treeList", analysisService.getTreeList());
        model.addAttribute("methodList", new ArrayList<String>(Arrays.asList("doyle", "hanna", "misp", "scrib", "inter")));

        boolean bool = analysisService.analyze(tid,method, request);
        //return "redirect:analysis/get?tid="+tid;
        model.addAttribute("completeMsg","display:block;");
        model.addAttribute("sidebar_treeList", analysisService.getAnalyzedTreeList());
        model.addAttribute("formdivstyle","display:Block;");
        return "analysisHome";
    }

    // id조회 후 분석결과 출력
    @GetMapping("/get")
    public String detail(Model model, @RequestParam("tid") String tid){

        ArrayList<AnalysisDTO>  ad = (ArrayList<AnalysisDTO>) analysisService.findByTid(tid);
        System.out.println("디테일함수 호출되고," + tid +" "+ad==null);

        if(ad!=null){
            //한번 이상 분석이 완료된 상황
            model.addAttribute("analysis", analysisService.findByTid(tid));
        }else{
            //treeRepository에는 존재하지만, 분석되지 않은 정보.
            System.out.println("존재XXX");
        }
        model.addAttribute("treeList", analysisService.getTreeList());
        model.addAttribute("methodList", new ArrayList<String>(Arrays.asList("doyle", "hanna", "misp", "scrib", "inter")));
        model.addAttribute("completeMsg","display:None;");
        model.addAttribute("sidebar_treeList", analysisService.getAnalyzedTreeList());
        model.addAttribute("formdivstyle","display:None;");

        return "analysisHome";
    }


    // 분석결과 json받아서 DB저장
    @ResponseBody
    @RequestMapping(value = "/analyzeddata")
    public void receiveAnalyzedData(@RequestBody String j, HttpServletRequest request) throws IOException, ParseException {
        //System.out.println("컨트롤러 /analyzeddata 호출"+" "+ j);
        //String basePath = request.getServletContext().getRealPath("")+"tree_analyzed_image/";

        JSONParser parser = new JSONParser();
        Object o = parser.parse(j);
        JSONObject total_jo = (JSONObject) o;
        JSONObject jo = (JSONObject) total_jo.get("json");


        AnalysisDTO analysisDTO = AnalysisDTO.builder()
                .tid(jo.get("tid").toString())
                .method(jo.get("method").toString())
                .totalVolume(jo.get("totalVolume").toString())
                .logsVolume(jo.get("logsVolume").toString())
                .logsLength(jo.get("logsLength").toString())
                .logsEdia(jo.get("logsEdia").toString())
                .logsSweep(jo.get("logsSweep").toString())
                .deduction(jo.get("deduction").toString())
                .analyzedImgPath(total_jo.get("imgName").toString())
                .build();
        analysisService.save(analysisDTO);
    }

    @ResponseBody
    @GetMapping("/getimg")
    public byte[] getMulti(@RequestParam("id") String tid, @RequestParam("method") String method ) throws Exception {
        System.out.println(tid);


        List<AnalysisDTO> analysisDTOS =  analysisService.findByTid(tid);

        AnalysisDTO analysisDTO = null;
        for(AnalysisDTO analysisDTO1: analysisDTOS){
            if(analysisDTO1.getMethod().equals(method)){
                analysisDTO=analysisDTO1;
            }
        }

        String filePath = analysisDTO.getAnalyzedImgPath();

        File f = new File(filePath);

        byte[] bytes = null;
        if(f.isFile()){
            bytes= Files.readAllBytes(f.toPath());
        }
        System.out.println(f.toPath().toString());
        return bytes;
    }
}
