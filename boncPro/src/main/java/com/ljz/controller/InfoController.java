package com.ljz.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ljz.entity.ParamEntity;
import com.ljz.model.DataRvsdRecordTmp;
import com.ljz.service.impl.ExcelServiceImpl;
import com.ljz.service.impl.InfoImportService;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 *
 */
@Controller
@RequestMapping("/info")
public class InfoController {

    private static final Logger logger = LoggerFactory.getLogger(InfoController.class);

//    @Autowired
//    DataRvsdRecordTmp

    @Autowired
    InfoImportService infoService;

    @Autowired
    ExcelServiceImpl excelService;

    /**
     * 导入接口页面
     * @param model
     * @return
     */
    @RequestMapping("/infoAlert")
    public String importTable(Model model) {
        logger.info("enter into infoAlert page");
        return "alertHtml/infoAlert";
    }

    /**
     * 导入接口
     * @param file
     * @return
     */
    @RequestMapping("/importExcel")
    @ResponseBody
    public Map<String,String> importTableExcel(@RequestParam(value="filename") MultipartFile file, String batchNo) {
        logger.info("batchNo:"+batchNo);
        Map<String,String> map = new HashMap<String,String>();
        if (file.isEmpty()) {
            map.put("msgData", "上传失败，请选择文件");
            map.put("dataSrcAbbr", "");
            return map;
        }
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
        if(!suffix.equals("xlsx")&&!suffix.equals("xls")){
            map.put("msgData", "格式不符,只支持excel");
            map.put("dataSrcAbbr", "");
            return map;
        }
        logger.info("start import info excel...");
        return excelService.importInfo(file,batchNo);
    }

    @ResponseBody
    @RequestMapping(value="/tmpToSaveAll",method = RequestMethod.POST)
    public Map<String,String> tmpToSaveAll(@RequestBody(required=false) String param) throws Exception{
        Map<String,String> map = new HashMap<String,String>();
        Gson gson = new Gson();
        String dataSrcAbbr ="";
        logger.info("param:::"+param);
//        String jsonStr = StringEscapeUtils.unescapeJava(param);
//        logger.info("jsonStr:::"+jsonStr);
        Map<String, String> resultMap = gson.fromJson(param, new TypeToken<Map<String, String>>(){}.getType());
        ParamEntity paramInface = gson.fromJson(resultMap.get("paramInface"), ParamEntity.class);
        ParamEntity paramColumn = gson.fromJson(resultMap.get("paramColumn"), ParamEntity.class);
        ParamEntity paramProc = gson.fromJson(resultMap.get("paramProc"), ParamEntity.class);
//        int size = resultMap.size();
//        String s = String.valueOf(size);
//        logger.info(s);
        logger.info("paramInface:::"+paramInface.toString());
        logger.info("paramColumn:::"+paramColumn.toString());
        logger.info("paramProc:::"+paramProc.toString());
        long start = new Date().getTime();
        try {
//            infoService.batchImportRecord();
            dataSrcAbbr = infoService.batchImportInfaceFinal(paramInface);
            dataSrcAbbr = infoService.batchImportColumnFinal(paramColumn);
            dataSrcAbbr = infoService.batchImportProcFinal(paramProc);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            map.put("dataSrcAbbr", dataSrcAbbr);
            map.put("msgData", "导入失败");
            return map;
        }
        long end = new Date().getTime();
        logger.info("导入用时:"+(end-start)+"毫秒");
        map.put("msgData", "导入成功");
        map.put("dataSrcAbbr", dataSrcAbbr);
        return map;
    }

    @RequestMapping("/infoCheck")
    public String infoCheck(Model model) {
        logger.info("enter into infoCheck page");
        return "alertHtml/infoCheck";
    }

//    @ResponseBody
//    @RequestMapping(value="/tmpToSaveFinal",method = RequestMethod.POST)
//    public Map<String,String> tmpToSaveFinal(@RequestBody(required=false) ParamEntity param) throws Exception{
//        Map<String,String> map = new HashMap<String,String>();
//        String dataSrcAbbr ="";
//        long start = new Date().getTime();
//        try {
//            logger.info("ColumnParam:::"+param.toString());
//            dataSrcAbbr=colService.batchImportFinal(param);
//        } catch (Exception e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//            map.put("dataSrcAbbr", dataSrcAbbr);
//            map.put("msgData", "导入失败");
//            return map;
//        }
//        long end = new Date().getTime();
//        logger.info("导入用时:"+(end-start)+"毫秒");
//        map.put("msgData", "导入成功");
//        map.put("dataSrcAbbr", dataSrcAbbr);
//        return map;
//    }


//
//    /**
//     * 接口临时表数据导入正式表
//     * @param param
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value="/tmpToSaveAll",method = RequestMethod.POST)
//    public Map<String,String> tmpToSaveAllProc(@RequestBody(required=false) ParamEntity param) throws Exception{
//        Map<String,String> map = new HashMap<String,String>();
//        String dataSrcAbbr ="";
//        long start = new Date().getTime();
//        try {
//            logger.info(param.toString());
//            dataSrcAbbr = infoService.batchImportProcFinal(param);
//        } catch (Exception e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//            map.put("dataSrcAbbr", dataSrcAbbr);
//            map.put("msgData", "导入失败");
//            return map;
//        }
//        long end = new Date().getTime();
//        logger.info("导入用时:"+(end-start)+"毫秒");
//        map.put("msgData", "导入成功");
//        map.put("dataSrcAbbr", dataSrcAbbr);
//        return map;
//    }












//    /**
//     * 导入字段页面
//     * @param model
//     * @return
//     */
//    @RequestMapping("/importCol")
//    public String importCol(Model model) {
//        return "importCol";
//    }

//    /**
//     * 导入字段
//     * @param file
//     * @return
//     */
//    @RequestMapping("/importColExcel")
//    @ResponseBody
//    public Map<String,String> importColExcel(@RequestParam(value="filename")MultipartFile file,String batchNo) {
//        Map<String,String> msgMap = new HashMap<String,String>();
//        if (file.isEmpty()) {
//            msgMap.put("msgData", "上传失败，请选择文件");
//            msgMap.put("dataSrcAbbr", "");
//            return msgMap;
//        }
//        String fileName = file.getOriginalFilename();
//        String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
//        if(!suffix.equals("xlsx")&&!suffix.equals("xls")){
//            msgMap.put("msgData", "格式不符,只支持excel");
//            msgMap.put("dataSrcAbbr", "");
//            return msgMap;
//        }
//        logger.info("start import column excel...");
//        return excelService.importColumn(file,batchNo);
//    }
//
//    /**
//     * 导入存储过程页面
//     * @param model
//     * @return
//     */
//    @RequestMapping("/importProc")
//    public String importProc(Model model) {
//        return "importProc";
//    }
//
//    /**
//     * 导入数据加载算法
//     * @param file
//     * @return
//     */
//    @RequestMapping("/importProcExcel")
//    @ResponseBody
//    public Map<String,String> importProcExcel(@RequestParam(value="filename")MultipartFile file,String batchNo) {
//        Map<String,String> map = new HashMap<String,String>();
//        if (file.isEmpty()) {
//            map.put("msgData", "上传失败，请选择文件");
//            map.put("dataSrcAbbr", "");
//            return map;
//        }
//        String fileName = file.getOriginalFilename();
//        String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
//        if(!suffix.equals("xlsx")&&!suffix.equals("xls")){
//            map.put("msgData", "格式不符,只支持excel");
//            map.put("dataSrcAbbr", "");
//            return map;
//        }
//        logger.info("start import proc excel...");
//
//        return excelService.importProc(file,batchNo);
//    }

}
