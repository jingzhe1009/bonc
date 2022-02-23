package com.ljz.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ljz.service.impl.ExcelServiceImpl;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ljz.entity.ParamEntity;
import com.ljz.model.DataInterfaceColumns;
import com.ljz.model.DataInterfaceColumnsTmp;
import com.ljz.service.IDataColumnService;
import com.ljz.util.TimeUtil;

/**
 * 字段配置
 * @author byan
 *
 */
@Controller
@RequestMapping("/col")
public class ColumnController extends MainController{
	
	private static final Logger logger = LoggerFactory.getLogger(ColumnController.class);
	
	@Autowired
	IDataColumnService colService;
	
	@Autowired
	ExcelServiceImpl excelService;

	@ResponseBody
	@RequestMapping(value="/queryColumn",method = RequestMethod.GET)
    public Map<String, Object> queryColumn(String dataSrcAbbr,String dataInterfaceNo,String columnNo) {
		DataInterfaceColumns record = new DataInterfaceColumns();
		record.setDataSrcAbbr(dataSrcAbbr);
		record.setDataInterfaceNo(dataInterfaceNo);
		if(columnNo!=null&&!"".equals(columnNo)) {
			record.setColumnNo(Integer.parseInt(columnNo));
		}
		record.seteDate(TimeUtil.getTw());
		logger.info(record.toString());
		List<DataInterfaceColumns> list = colService.queryAll(record);

		/*PageHelper.startPage((start/ length) + 1, length);
        //用PageInfo对结果进行包装
        PageInfo<DataInterfaceColumns> pageInfo = new PageInfo<DataInterfaceColumns>(list);
        //使用DataTables的属性接收分页数据
        PageUtil<DataInterfaceColumns> dataTable = new PageUtil<DataInterfaceColumns>(start,length);*/
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("recordsTotal", list.size());
        resultMap.put("recordsFiltered", list.size());
        resultMap.put("total", list.size());
        resultMap.put("data", list);
        logger.info("query column success,num:"+list.size());
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/createColumn",method = RequestMethod.POST)
	@Transactional
    public Map<String, Object> createColumn(DataInterfaceColumns record) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer stringBuffer = new StringBuffer();
		try{
			if (record.getNullable()!=0 && record.getNullable()!=1){
				stringBuffer.append("[是否可空]应为0/1");
			}
		}catch (NullPointerException e){
		}
		stringBuffer.append(excelService.verifyFieldInfo(record.getDataSrcAbbr(),
				record.getDataInterfaceNo(),record.getDataInterfaceName(),
				record.getColumnNo().toString(), record.getColumnName(), record.getDataType(),
				record.getDataFormat(),record.getComma(),record.getColumnName(),record.getIsbucket()));
		String string = stringBuffer.toString().trim();
        if(!string.equals("") && !string.isEmpty() && string!=null){
            map.put("message", "保存失败：\n"+record.getDataInterfaceName()+":\n"+stringBuffer);
            return map;
        }
		record.setsDate(TimeUtil.getTy());
		record.seteDate(TimeUtil.getE());
		record.setDataInterfaceNo(record.getDataInterfaceNo().trim());
		logger.info(record.toString());
		int insert = colService.insert(record);
		//insertVersion(record, "2");
		logger.info("column insert success"+insert);
		map.put("idx", record.getDataSrcAbbr());
		map.put("interNo", record.getDataInterfaceNo());
		map.put("colNo", record.getColumnNo());
		map.put("message", "保存成功");
		return map;
    }
	
	@ResponseBody
	@RequestMapping(value="/editColumn",method = RequestMethod.POST)
	@Transactional
    public Map<String, Object> editColumn(DataInterfaceColumns record) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer stringBuffer = new StringBuffer();
		try{
			if (record.getNullable()!=0 && record.getNullable()!=1){
				stringBuffer.append("[是否可空]应为0/1/空");
			}
		}catch (NullPointerException e){
		}
		stringBuffer.append(excelService.verifyFieldInfo(record.getDataSrcAbbr(),
				record.getDataInterfaceNo(),record.getDataInterfaceName(),
				record.getColumnNo().toString(), record.getColumnName(), record.getDataType(),
				record.getDataFormat(),record.getComma(),record.getColumnName(),record.getIsbucket()));
		String string = stringBuffer.toString().trim();
		if (string!=null && !string.isEmpty() && !string.isEmpty()){
			map.put("message","保存失败，填入信息有误：\n"+record.getDataInterfaceName()+":\n"+stringBuffer);
			return map;
		}

		DataInterfaceColumns data = new DataInterfaceColumns();
		//insertVersion(record, "2");
		data.setDataSrcAbbr(record.getDataSrcAbbr());
		data.setDataInterfaceNo(record.getDataInterfaceNo());
		data.setDataInterfaceName(record.getDataInterfaceName());
		data.setColumnNo(record.getColumnNo());
		//生效日期作为查询条件,查询出当前生效的记录对其修改
		data.setsDate(TimeUtil.getTw());
		//原纪录失效日期改为今天
		data.seteDate(TimeUtil.getTy());
		logger.info(data.toString());
		int update = colService.update(data);
		logger.info("edit column success,num:"+update);
		if(update>0) {
			record.setsDate(TimeUtil.getTy());
			//新记录失效日期改为无限长
			record.seteDate(TimeUtil.getE());
			logger.info(record.toString());
			int insert = colService.insert(record);
			logger.info("insert column success,num:"+insert);
		}
		map.put("idx", record.getDataSrcAbbr());
		map.put("interNo", record.getDataInterfaceNo());
		map.put("colNo", record.getColumnNo());
		map.put("message","保存成功");
//        return resultMap;
		return map;
    }
	
	@ResponseBody
	@RequestMapping(value="/deleteColumn",method = RequestMethod.POST)
    public Map<String, Object> deleteColumn(DataInterfaceColumns record) {
		//生效日期作为查询条件,查询出当前生效的记录对其修改
		record.setsDate(TimeUtil.getTw());
		record.seteDate(TimeUtil.getTy());
		logger.info(record.toString());
		int update = colService.update(record);
		logger.info("update column success num:"+update);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("idx", record.getDataSrcAbbr());
		resultMap.put("interNo", record.getDataInterfaceNo());
		resultMap.put("colName", record.getColumnName());
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/queryColumnVersion",method = RequestMethod.GET)
    public Map<String, Object> queryColumnVersion(String dataSrcAbbr,String dataInterfaceNo,String columnNo,String dataInterfaceName) {
		DataInterfaceColumns record = new DataInterfaceColumns();
		record.setDataSrcAbbr(dataSrcAbbr);
		record.setDataInterfaceNo(dataInterfaceNo);
		record.setDataInterfaceName(dataInterfaceName);
		record.setColumnNo(Integer.parseInt(columnNo));
		logger.info(record.toString());
		List<DataInterfaceColumns> list = colService.queryAllVersion(record);
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("recordsTotal", list.size());
        resultMap.put("recordsFiltered", list.size());
        resultMap.put("total", list.size());
        resultMap.put("data", list);
        logger.info("query column version success,num:"+list.size());
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/queryColumnTmp",method = RequestMethod.GET)
    public Map<String, Object> queryColumnTmp(String batchNo,String dataSrcAbbr) {
		DataInterfaceColumnsTmp record = new DataInterfaceColumnsTmp();
		record.setBatchNo(batchNo);
		record.setDataSrcAbbr(dataSrcAbbr);
		logger.info(record.toString());
		List<DataInterfaceColumnsTmp> list = colService.queryAllTmp(record);
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("recordsTotal", list.size());
        resultMap.put("recordsFiltered", list.size());
        resultMap.put("total", list.size());
        resultMap.put("data", list);
        logger.info("query column tmp success,num:"+list.size());
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/tmpToSave",method = RequestMethod.POST)
	@Transactional
    public String tmpToSave(@RequestBody(required=false) ParamEntity param) {
		logger.info(param.toString());
		/*String [] tables = param.getTables();
		if(tables==null||tables.length<1)
			return "字段名不能为空";
		for(String table:tables) {
			if(!table.contains("-"))
				continue;
			String[] split = table.split("-");
			String ds = split[0];
			String interNo = split[1];
			String importType = split[2];
			String batchNo = split[3];
			String columnNo = split[4];
			if("1".equals(importType)) {
				//导入类型是新增直接插入正式表
				DataInterfaceColumnsTmp record = new DataInterfaceColumnsTmp();
				record.setDataSrcAbbr(ds);
				record.setDataInterfaceNo(interNo);
				record.setColumnNo(Integer.parseInt(columnNo));
				record.setBatchNo(batchNo);
				logger.info(record.toString());
				int tmpToSave = colService.tmpToSave(record);
				logger.info("tmpToSave column success,num:"+tmpToSave);
				if(tmpToSave>0) {
					int delete = colService.delete(record);
					logger.info("delete column tmp success num:"+delete);
				}else {
					return "导入失败";
				}
			}else if("2".equals(importType)) {
				//导入类型修改先将正式表原记录置为失效
				DataInterfaceColumns record = new DataInterfaceColumns();
				record.setDataSrcAbbr(ds);
				record.setDataInterfaceNo(interNo);
				record.setColumnNo(Integer.parseInt(columnNo));
				record.setsDate(TimeUtil.getTw());
				record.seteDate(TimeUtil.getTy());
				logger.info(record.toString());
				int update = colService.update(record);
				logger.info("update column success,num:"+update);
				if(update>=0) {
					//再从临时表插入新记录到正式表
					DataInterfaceColumnsTmp record2 = new DataInterfaceColumnsTmp();
					record2.setDataSrcAbbr(ds);
					record2.setDataInterfaceNo(interNo);
					record2.setColumnNo(Integer.parseInt(columnNo));
					record2.setBatchNo(batchNo);
					logger.info(record.toString());
					int tmpToSave = colService.tmpToSave(record2);
					logger.info("tmpToSave column success,num:"+tmpToSave);
					if(tmpToSave>0) {
						int delete = colService.delete(record2);
						logger.info("delete column tmp success,num:"+delete);
					}else {
						return "导入失败";
					}
				}else {
					return "导入失败";
				}
			}
		}*/
		return "导入成功";
    }
	

	@ResponseBody
	@RequestMapping(value="/tmpToSaveFinal",method = RequestMethod.POST)
    public Map<String,String> tmpToSaveFinal(@RequestBody(required=false) ParamEntity param) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String dataSrcAbbr ="";
		long start = new Date().getTime();
		try {
			logger.info(param.toString());
			dataSrcAbbr=colService.batchImportFinal(param);
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
	
	/*@ResponseBody
	@RequestMapping(value="/tmpToSaveNew",method = RequestMethod.POST)
    public String tmpToSaveNew(@RequestBody(required=false) ParamEntity param) throws Exception{
		
		long start = new Date().getTime();
		try {
			logger.info(param.toString());
			colService.batchImportNew(param);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "导入失败";
		}
		long end = new Date().getTime();
		logger.info("导入用时:"+(end-start)+"毫秒");
		return "导入成功";
	}
	
	@ResponseBody
	@RequestMapping(value="/tmpToSaveAll",method = RequestMethod.POST)
    public String tmpToSaveAll(@RequestBody(required=false) ParamEntity param) throws Exception{
		
		long start = new Date().getTime();
		try {
			logger.info(param.toString());
			colService.batchImportFinal(param);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "导入失败";
		}
		long end = new Date().getTime();
		logger.info("导入用时:"+(end-start)+"毫秒");
		return "导入成功";
	}*/
	/**
	 * 导入字段页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/importCol")
	public String importCol(Model model) {
		return "importCol";
	}
	
	/**
	 * 导入字段
	 * @param file
	 * @return
	 */
	@RequestMapping("/importColExcel")
	@ResponseBody
	public Map<String,String> importColExcel(@RequestParam(value="filename")MultipartFile file,String batchNo) {
		Map<String,String> msgMap = new HashMap<String,String>();
		if (file.isEmpty()) {
            msgMap.put("msgData", "上传失败，请选择文件");
			msgMap.put("dataSrcAbbr", "");
			return msgMap;
        }
		String fileName = file.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
		if(!suffix.equals("xlsx")&&!suffix.equals("xls")){
			msgMap.put("msgData", "格式不符,只支持excel");
			msgMap.put("dataSrcAbbr", "");
			return msgMap;
		}
		logger.info("start import column excel...");
		return excelService.importColumn(file,batchNo);
	}
	
	
	@RequestMapping(value="/exportCol")
	public String exportCol(HttpServletResponse response,HttpServletRequest request) {
		String filePath = "/static/excel/columnExcel.xlsx";
		String fileName = "columnExcel.xlsx";
	    try{
			ClassPathResource cpr = new ClassPathResource(filePath);
			InputStream is = cpr.getInputStream();
			Workbook workbook = new XSSFWorkbook(is);
			logger.info("start export column excel...");
		    downLoadExcel(fileName, response, workbook);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return "importCol";
	}

}
