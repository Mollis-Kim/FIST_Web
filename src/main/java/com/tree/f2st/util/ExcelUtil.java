package com.tree.f2st.util;

import com.tree.f2st.entity.TreeEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelUtil {

    public static ByteArrayInputStream jsonArrayToExcelFile(JSONArray jsonArray){

        try(Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Fist-db");
            Row row = sheet.createRow(0);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            Cell cell = row.createCell(0);
            cell.setCellValue("나무 객체명");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(1);
            cell.setCellValue("측정 거리");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(2);
            cell.setCellValue("흉고직경(dbh)");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(3);
            cell.setCellValue("수고(height)");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(4);
            cell.setCellValue("위도(latitude)");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(5);
            cell.setCellValue("경도(longitude)");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(6);
            cell.setCellValue("연령");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(7);
            cell.setCellValue("임령");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(8);
            cell.setCellValue("CAI");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(9);
            cell.setCellValue("MAI");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(10);
            cell.setCellValue("흉고단면적");
            cell.setCellStyle(headerCellStyle);
            //"doyle", "hanna", "misp", "scrib", "inter"
            cell = row.createCell(11);
            cell.setCellValue("이용재적(doyle)");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(12);
            cell.setCellValue("이용재적(hanna)");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(13);
            cell.setCellValue("이용재적(misp)");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(14);
            cell.setCellValue("이용재적(scrib)");
            cell.setCellStyle(headerCellStyle);


            cell = row.createCell(15);
            cell.setCellValue("이용재적(말구직경)");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(16);
            cell.setCellValue("이용재적(inter)");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(17);
            cell.setCellValue("프레슬러(생장률%)");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(18);
            cell.setCellValue("프레슬러(ha 당 생장량㎥)");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(19);
            cell.setCellValue("슈나이더(생장량%)");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(20);
            cell.setCellValue("슈나이더(ha 당 생장량㎥)");
            cell.setCellStyle(headerCellStyle);

            // Creating data rows for each customer



            for(int i = 0; i < jsonArray.size(); i++) {

                Row dataRow = sheet.createRow(i + 1);
                JSONObject jo = (JSONObject) jsonArray.get(i);
                jo.keySet().forEach(key -> {
                    if(jo.get(key)==null){
                        jo.put(key,"");
                    }
                });

                dataRow.createCell(0).setCellValue(jo.get("tid").toString());
                dataRow.createCell(1).setCellValue(jo.get("dist").toString());
                dataRow.createCell(2).setCellValue(jo.get("dbh").toString());
                dataRow.createCell(3).setCellValue(jo.get("height").toString());
                dataRow.createCell(4).setCellValue(jo.get("latitude").toString());
                dataRow.createCell(5).setCellValue(jo.get("longitude").toString());
                dataRow.createCell(6).setCellValue(jo.get("ageoftree").toString());
                dataRow.createCell(7).setCellValue(jo.get("ageofstand").toString());
                dataRow.createCell(8).setCellValue(jo.get("CAI").toString());
                dataRow.createCell(9).setCellValue(jo.get("MAI").toString());
                dataRow.createCell(10).setCellValue(jo.get("basalarea").toString());//흉고단면적
                dataRow.createCell(11).setCellValue(jo.get("doyle").toString());
                dataRow.createCell(12).setCellValue(jo.get("hanna").toString());
                dataRow.createCell(13).setCellValue(jo.get("misp").toString());
                dataRow.createCell(14).setCellValue(jo.get("scrib").toString());
                dataRow.createCell(15).setCellValue(jo.get("EDSM").toString());
                dataRow.createCell(16).setCellValue(jo.get("inter").toString());
                dataRow.createCell(17).setCellValue(jo.get("프레슬러_생장률").toString());
                dataRow.createCell(18).setCellValue(jo.get("프레슬러_ha당생장량").toString());
                dataRow.createCell(19).setCellValue(jo.get("슈나이더_생장률").toString());
                dataRow.createCell(20).setCellValue(jo.get("슈나이더_ha당생장량").toString());
                
            }

            // Making size of column auto resize to fit with data
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);
            sheet.autoSizeColumn(8);
            sheet.autoSizeColumn(9);
            sheet.autoSizeColumn(10);
            sheet.autoSizeColumn(11);
            sheet.autoSizeColumn(12);
            sheet.autoSizeColumn(13);
            sheet.autoSizeColumn(14);
            sheet.autoSizeColumn(15);
            sheet.autoSizeColumn(16);
            sheet.autoSizeColumn(17);
            sheet.autoSizeColumn(18);
            sheet.autoSizeColumn(19);
            sheet.autoSizeColumn(20);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
