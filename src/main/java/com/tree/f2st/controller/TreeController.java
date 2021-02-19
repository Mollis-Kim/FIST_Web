package com.tree.f2st.controller;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tree.f2st.dto.TreeDTO;
import com.tree.f2st.entity.TreeEntity;
import com.tree.f2st.service.TreeService;
import com.tree.f2st.util.ExcelUtil;
import org.apache.commons.compress.utils.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/webfist")
public class TreeController {

    @Autowired
    TreeService treeService;

    @RequestMapping(value = "/map")
    public String home(Model model){
        model.addAttribute("treeList",treeService.findAll());
        return "index";
    }

    @ResponseBody
    @GetMapping("/getimg")
    public byte[] getMulti(@RequestParam("id") String tid) throws Exception {
        System.out.println(tid);
        String filePath;
        List<TreeEntity> tree = treeService.findByTid(tid);
        filePath = tree.get(0).getImgPath();

        File f = new File(filePath);

        byte[] bytes = null;
        if(f.isFile()){
            bytes= Files.readAllBytes(f.toPath());
        }
        System.out.println(f.toPath().toString());
        return bytes;
    }


    public static byte[] inputStreamToByteArray(InputStream is) {
        byte[] resBytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read = -1;
        try {
            while ( (read = is.read(buffer)) != -1 ) {
                bos.write(buffer, 0, read);
            }

            resBytes = bos.toByteArray();
            bos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return resBytes;
    }


    @RequestMapping(value = "/map/{tid}")
    public String detail(Model model, @PathVariable("tid") String tid){
        model.addAttribute("treeList",treeService.findAll());
        model.addAttribute("detail", treeService.findByTid(tid));
        return "index";
    }


    //모든 나무 조회
    @GetMapping(value="/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<TreeEntity>> getAlltrees(){
        List<TreeEntity> tree = treeService.findAll();
        return new ResponseEntity<List<TreeEntity>>(tree, HttpStatus.OK);
    }

    //나무 등록 번호로 하나의 나무 조회
    @GetMapping(value="/{tid}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody ResponseEntity< List<TreeEntity>> getTree(@PathVariable("tid") String tid){
        List<TreeEntity> tree = treeService.findByTid(tid);
        return new ResponseEntity<List<TreeEntity>>(tree, HttpStatus.OK);
    }


    @RequestMapping("/imgsUpload")
    public void uploadMulti(@RequestBody ArrayList<MultipartFile> files, HttpServletRequest request
            , HttpServletResponse response) throws Exception {

        String basePath = request.getServletContext().getRealPath("")+"tree_original_image";
        // 파일 업로드(여러개) 처리 부분
        System.out.println("받기성공"+files.size());

        for (MultipartFile file : files) {
            String originalName = file.getOriginalFilename();

            String id = parseToId(originalName);

            if(treeService.isExist(id)) {
                System.out.println(originalName);
                String filePath = basePath + "/" + originalName;
                File dest = new File(filePath);
                file.transferTo(dest);

                String imgPath = dest.getAbsolutePath();
                boolean rt = treeService.saveImg(id, imgPath);
                System.out.println(id+" "+rt);
            }
        }
    }

    String parseToId(String file){
            int idx = file.indexOf("_") + 1;
            int lidx = file.lastIndexOf(".");
            String treeId = file.substring(idx, lidx);
            return treeId;
    }


    @RequestMapping("/jsonUpload")
    public void uploadMulti2(@RequestBody String files, HttpServletRequest request
            , HttpServletResponse response) throws Exception {

        //rootPath가 바탕화면에 잡혀있고, 바탕화면에 img라는 폴더 생성해야함.
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(files);

        org.json.simple.JSONArray jsonArray = (JSONArray) obj;
        for (Object o : jsonArray) {
            JSONObject jo = (JSONObject) o;
            ObjectMapper objectMapper = new ObjectMapper();
            TreeDTO treeDTO = objectMapper.readValue(jo.toJSONString(), TreeDTO.class);

            //treeDTO.show();
            treeService.save(treeDTO);
        }
    }

    /*
    @PostMapping("/save")
    public @ResponseBody String save(@RequestBody TreeDTO tree){
        String msg = treeService.save(tree);
        return msg;
    }

     */

    //.xlsx file download
    @GetMapping(value = "/download")
    public ResponseEntity<Resource> getFile() {
        String filename = "fist.xlsx";
        InputStreamResource file = new InputStreamResource(treeService.load());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
//    public void downloadXlsx(HttpServletResponse response) throws IOException{
//        response.setContentType("application/octet-stream");
//        response.setHeader("Content-Disposition", "attachment; filename=fist.xlsx");
//        ByteArrayInputStream stream = ExcelUtil.ListToExcelFile(treeService.findAll());
//        IOUtils.copy(stream, response.getOutputStream());
//    }

}