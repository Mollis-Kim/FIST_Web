package com.tree.f2st.controller;

import com.tree.f2st.dto.AnalysisDTO;
import com.tree.f2st.dto.TreeDTO;
import com.tree.f2st.entity.TreeEntity;
import com.tree.f2st.service.AnalysisService;
import com.tree.f2st.service.TreeService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Handler;

@Controller
@RequestMapping("/analysis")
public class AnalysisController {
    @Autowired
    AnalysisService analysisService;

    @Autowired
    TreeService treeService;

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

        List<TreeEntity> tes = treeService.findByTid(tid);
        double dbh = Double.parseDouble(tes.get(0).getDbh());
        boolean bool = analysisService.analyze(tid,method, dbh, request);
        //return "redirect:analysis/get?tid="+tid;
        model.addAttribute("completeMsg","display:block;");
        model.addAttribute("sidebar_treeList", analysisService.getAnalyzedTreeList());
        model.addAttribute("formdivstyle","display:Block;");
        return "analysisHome";
    }




    // 조회화면
    @GetMapping("/search")
    public String showSearchView(Model model){
        ArrayList<String> treeNames =  (ArrayList<String>) analysisService.getAnalyzedTreeList();
        model.addAttribute("treeList", treeNames); //  분석된 나무 리스트

        JSONObject jo = new JSONObject();

        for(String treename : treeNames){
            ArrayList<String> methods = (ArrayList<String>) analysisService.findMethodByTid(treename);
            jo.put(treename, methods);
        }
        System.out.println(jo.toJSONString());
        model.addAttribute("jo", jo);



        model.addAttribute("data", null);
        return "searchAnalysisResult";
    }

    private String[] toStringArray(String s){
        String[] result = s.split(",");
        return result;
    }

    // id조회 후 분석결과 출력
    @PostMapping("/get")
    public String detail(Model model, @RequestParam("tid") String tid, @RequestParam("method") String method){

        AnalysisDTO analysisDTO = analysisService.findByTidAndMethod(tid,method);

        if(analysisDTO!=null){
            model.addAttribute("data", analysisDTO);
        }else{
            model.addAttribute("data", null);
        }

        ArrayList<String> treeNames =  (ArrayList<String>) analysisService.getAnalyzedTreeList();
        model.addAttribute("treeList", treeNames); //  분석된 나무 리스트

        JSONObject jo = new JSONObject();

        for(String treename : treeNames){
            ArrayList<String> methods = (ArrayList<String>) analysisService.findMethodByTid(treename);
            jo.put(treename, methods);
        }
        System.out.println(jo.toJSONString());
        model.addAttribute("jo", jo);

        return "searchAnalysisResult";
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
