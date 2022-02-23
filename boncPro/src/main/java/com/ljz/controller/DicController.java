package com.ljz.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ljz.model.attrC2e;
import com.ljz.model.entityC2e;
import com.ljz.service.impl.DictionaryServiceImpl;

/**
 * 词根配置
 * @author byan
 *
 */
@Controller
@RequestMapping("/dic")
public class DicController extends MainController{
	
	private static final Logger logger = LoggerFactory.getLogger(DicController.class);
	
	@Autowired
	DictionaryServiceImpl dicService;
	
	@ResponseBody
	@RequestMapping(value="/queryDicColumn",method = RequestMethod.GET)
    public Map<String, Object> queryDicColumn(attrC2e data,Integer start, Integer length) {
		attrC2e record = data;
		List<attrC2e> list = dicService.queryColumn(record);
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("recordsTotal", list.size());
        resultMap.put("recordsFiltered", list.size());
        resultMap.put("total", list.size());
        resultMap.put("data", list);
        logger.info("dictornary column query success");
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/createDicColumn",method = RequestMethod.POST)
	@Transactional
    public Map<String, Object> createDicColumn(attrC2e record) {
		dicService.insertColumn(record);
		insertVersion(record, "4");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("cname", record.getCname());
		resultMap.put("version", record.getVersion());
		logger.info("dictornary column insert success");
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/editDicColumn",method = RequestMethod.POST)
	@Transactional
    public Map<String, Object> editDicColumn(attrC2e record) {
		dicService.updateColumn(record);
		insertVersion(record, "4");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("cname", record.getCname());
		logger.info("dictornary column edit success");
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/deleteDicColumn",method = RequestMethod.POST)
    public Map<String, Object> deleteDicColumn(attrC2e record) {
		dicService.deleteColumn(record);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("cname", record.getCname());
		logger.info("dictornary column delete success");
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/queryDicTable",method = RequestMethod.GET)
    public Map<String, Object> queryDicTable(entityC2e data,Integer start, Integer length) {
		entityC2e record = data;
		List<entityC2e> list = dicService.queryTable(record);
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("recordsTotal", list.size());
        resultMap.put("recordsFiltered", list.size());
        resultMap.put("total", list.size());
        resultMap.put("data", list);
        logger.info("dictornary table query success");
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/createDicTable",method = RequestMethod.POST)
	@Transactional
    public Map<String, Object> createDicTable(entityC2e record) {
		dicService.insertTable(record);
		insertVersion(record, "3");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("cname", record.getCname());
		logger.info("dictornary table insert success");
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/editDicTable",method = RequestMethod.POST)
	@Transactional
    public Map<String, Object> editDicTable(entityC2e record) {
		dicService.updateTable(record);
		insertVersion(record, "3");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("cname", record.getCname());
		logger.info("dictornary table insert success");
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/deleteDicTable",method = RequestMethod.POST)
    public Map<String, Object> deleteDicTable(entityC2e record) {
		dicService.deleteTable(record);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("cname", record.getCname());
		resultMap.put("version", record.getVersion());
		logger.info("dictornary table delete success");
        return resultMap;
    }
	
	/**
	 * 词根页面
	 * @param model
	 * @param tableName
	 * @return
	 */
	@RequestMapping("/dictionary")
	public String importDictionary(Model model,@Param(value="tableName") String tableName) {
		return "importDictionary";
	}
	
	@RequestMapping("/manageDictionary")
	public String manageDictionary(String id,Model model) {
		model.addAttribute("id",id);
		return "manageDictionary";
	}
	/**
	 * 导入词根
	 * @param file
	 * @return
	 */
	@RequestMapping("/importDictionary")
	@ResponseBody
	public String importDictionary(@RequestParam(value="filename")MultipartFile file) {
		if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }
		System.out.println("开始导入词根...");
		String fileName = file.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
		if(!suffix.equals("xlsx")&&!suffix.equals("xls")&&!suffix.equals("xlsm"))
			return "格式不符,只支持excel";
		return excelService.importDictionary(file);
	}
	
	@RequestMapping(value="/exportDictionary")
	public String exportDictionary(HttpServletResponse response,HttpServletRequest request) {
	   try {
	    ClassPathResource cpr = new ClassPathResource("/static/excel/dicExcel.xlsx");
	    InputStream is = cpr.getInputStream();
		Workbook workbook = new XSSFWorkbook(is);
		String fileName = "dicExcel.xlsx";
		downLoadExcel(fileName, response, workbook);
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return "importDictionary";
	}

}
