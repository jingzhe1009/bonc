package com.ljz.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ljz.config.InfoConfig;
import com.ljz.constant.BoncConstant;
import com.ljz.entity.ParamEntity;
import com.ljz.model.DataInterface;
import com.ljz.model.DataInterfaceColumns;
import com.ljz.model.DataInterfaceColumnsTmp;
import com.ljz.model.DataInterfaceTmp;
import com.ljz.model.DataLog;
import com.ljz.model.DataSrc;
import com.ljz.model.TbVersion;
import com.ljz.model.attrC2e;
import com.ljz.model.entityC2e;
import com.ljz.service.impl.DataSourceServiceImpl;
import com.ljz.service.impl.ExcelServiceImpl;
import com.ljz.service.impl.LogServiceImpl;
import com.ljz.service.impl.VersionServiceImpl;
import com.ljz.util.ExcelUtil;
import com.ljz.util.TimeUtil;


@Controller
@RequestMapping("/bonc")
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	DataSourceServiceImpl dsService;
	
	@Autowired
	ExcelServiceImpl excelService;
	
	@Autowired
	VersionServiceImpl versionService;
	
	@Autowired
    InfoConfig config;
	
	@Autowired
	LogServiceImpl logService;
	
	@RequestMapping(value= {"/","/index"})
	public String index() {
		logger.info("hello,world");
		return "index";
	}
	
	@ResponseBody
	@RequestMapping(value="/datasource",method = RequestMethod.GET)
    public Map<String, Object> datasource(String dataSrcAbbr) {
		logger.info("datasource query success");
		List<DataSrc> list = new ArrayList<DataSrc>();
		DataSrc data = dsService.queryById(dataSrcAbbr);
		list.add(data);
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("recordsTotal", list.size());
        resultMap.put("recordsFiltered", list.size());
        resultMap.put("total", list.size());
        resultMap.put("data", list);
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/queryDataSrc",method = RequestMethod.GET)
    public Map<String, Object> queryDataSrc(DataSrc record) {
		logger.info("datasrc query success");
		record.seteDate(new java.sql.Date(new Date().getTime()));
		List<DataSrc> list = dsService.queryAll(record);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//resultMap.put("word", funcService.getWord(record.getDataSrcAbbr(),"1"));
        resultMap.put("data", list);
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/queryDataSrcInfo",method = RequestMethod.GET)
    public Map<String, Object> queryDataSrcInfo() {
		logger.info("datasrcInfo query success");
		
		List list = new ArrayList();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map map = new HashMap();
		map.put("fileName", "data_src.ini");
		map.put("fileFunc", "配置文件，指定目录扫描及文件预处理所需的配置信息");
		map.put("fileLocal", "/cdbetl/ETL/TDHOT/CFG");
		map.put("cmd", "");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("fileName", "源数据文件");
		map.put("fileFunc", "存放各数据源的源数据文件");
		map.put("fileLocal", "/cdbetl/ETL/TDHOT/DATA/DATASRC");
		map.put("cmd", "python file_monitor.py");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("fileName", "结构化数据文件");
		map.put("fileFunc", "结构化数据源文件备份");
		map.put("fileLocal", "/cdbetl/ETL/TDHOT/DATA/BAKUP");
		map.put("cmd", "python file_monitor.py");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("fileName", "非结构化数据文件");
		map.put("fileFunc", "非结构化数据源文件备份");
		map.put("fileLocal", "/cdbetl/ETL/TDHOT/DATA/F_BAKUP");
		map.put("cmd", "python file_monitor.py");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("fileName", "目录扫描后的数据文件");
		map.put("fileFunc", "存放目录扫描后的数据文件");
		map.put("fileLocal", "/cdbetl/ETL/TDHOT/DATA/PEND");
		map.put("cmd", "python file_trans.py");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("fileName", "文件与处理后的数据文件");
		map.put("fileFunc", "存放文件与处理后的数据文件");
		map.put("fileLocal", "/cdbetl/ETL/TDHOT/DATA/QUEUE");
		map.put("cmd", "python file2orc.py");
		list.add(map);
		//resultMap.put("word", funcService.getWord(record.getDataSrcAbbr(),"1"));
        resultMap.put("data", list);
        //System.out.println(resultMap);
        return resultMap;
    }


	@ResponseBody
	@RequestMapping(value="/createDataSrc",method = RequestMethod.POST)
	@Transactional
    public Map createDataSrc(DataSrc record) {
		record.setsDate(new java.sql.Date(new Date().getTime()));
		record.seteDate(ExcelUtil.getInstance().StringToDate("3000-12-31"));
		String dataSrcAbbr = record.getDataSrcAbbr();
		List list = dsService.queryDataSrc();
		Map map = new HashMap();
		for (int i=0;i<list.size();i++){
			if (dataSrcAbbr.equalsIgnoreCase(list.get(i).toString())){
				map.put("message","保存失败，数据源"+dataSrcAbbr+"已存在");
				return map;
			}
		}

		dsService.insert(record);
		//insertVersion(record, "5");
		logger.info("DataSrc insert success"+record.toString());
//        return record.getDataSrcAbbr();
		map.put("message","保存成功");
		return map;
    }

	@ResponseBody
	@RequestMapping(value="/editDataSrc",method = RequestMethod.POST)
	@Transactional
    public Map editDataSrc(DataSrc record) {
		String dataSrcAbbr = record.getDataSrcAbbr();
		List list = dsService.queryDataSrc();
		Map map = new HashMap();
		for (int i=0;i<list.size();i++){
			if (dataSrcAbbr.equals(list.get(i))){
				map.put("message","保存失败，数据源"+dataSrcAbbr+"已存在");
				return map;
			}
		}
		dsService.update(record);
		//insertVersion(record, "5");
		logger.info("DataSrc edit success"+record.getDataSrcAbbr());
//        return record.getDataSrcAbbr();
		map.put("message","保存成功");
		return map;
    }
	
	@ResponseBody
	@RequestMapping(value="/deleteDataSrc",method = RequestMethod.POST)
    public String deleteDataSrc(DataSrc record) {
		dsService.delete(record);
		logger.info("DataSrc delete success");
        return record.getDataSrcAbbr();
    }
	
	
	
	
	@RequestMapping(value="/log")
	public String log(HttpServletResponse response,HttpServletRequest request) {
	   
	    return "log";
	}
	
	@ResponseBody
	@RequestMapping(value="/queryLog",method = RequestMethod.GET)
    public Map<String, Object> queryLog(DataLog record) {
		List<DataLog> list = logService.queryAll(record);
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("recordsTotal", list.size());
        resultMap.put("recordsFiltered", list.size());
        resultMap.put("total", list.size());
        resultMap.put("data", list);
        logger.info("funcRegister query success");
        return resultMap;
    }

	@ResponseBody
	@RequestMapping(value="/version")
	public Object version(TbVersion record) {
		List<TbVersion> list = versionService.queryAll(record);
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("recordsTotal", list.size());
        resultMap.put("recordsFiltered", list.size());
        resultMap.put("total", list.size());
        resultMap.put("data", list);
        logger.info("version query success");
		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping(value="/tmpToSaveBak",method = RequestMethod.POST)
	@Transactional
    public String tmpToSaveBak(@RequestBody(required=false) ParamEntity param) {
		logger.info(param.toString());
		String [] tables = param.getTables();
		if(tables==null||tables.length<1)
			return "字段名不能为空";
		List<DataInterfaceTmp> tmpList = new ArrayList<DataInterfaceTmp>();
		List<DataInterface> colList = new ArrayList<DataInterface>();
		//创建使用单个线程的线程池
		ExecutorService es = Executors.newFixedThreadPool(10);
		
		return "导入成功";
		
    }
	
	@ResponseBody
	@RequestMapping(value="/tmpToSaveBakCol",method = RequestMethod.POST)
	@Transactional
    public String tmpToSaveBakCol(@RequestBody(required=false) ParamEntity param) {
		logger.info(param.toString());
		String [] tables = param.getTables();
		if(tables==null||tables.length<1)
			return "字段名不能为空";
		List<DataInterfaceColumnsTmp> tmpList = new ArrayList<DataInterfaceColumnsTmp>();
		List<DataInterfaceColumns> colList = new ArrayList<DataInterfaceColumns>();
		for(String table:tables) {
			if(!table.contains("-"))
				continue;
			String[] split = table.split("-");
			String ds = split[0];
			String interNo = split[1];
			String importType = split[2];
			String batchNo = split[3];
			String columnNo = split[4];
			DataInterfaceColumnsTmp record = new DataInterfaceColumnsTmp();
			record.setDataSrcAbbr(ds);
			record.setDataInterfaceNo(interNo);
			record.setColumnNo(Integer.parseInt(columnNo));
			record.setBatchNo(batchNo);
			logger.info(record.toString());
			tmpList.add(record);
			if("1".equals(importType)) {
				//导入类型是新增直接插入正式表
			}else if("2".equals(importType)) {
				//导入类型修改先将正式表原记录置为失效
				DataInterfaceColumns record2 = new DataInterfaceColumns();
				record2.setDataSrcAbbr(ds);
				record2.setDataInterfaceNo(interNo);
				record2.setColumnNo(Integer.parseInt(columnNo));
				record2.setsDate(TimeUtil.getTw());
				record2.seteDate(TimeUtil.getTy());
				logger.info(record.toString());
				colList.add(record2);
				/*int update = colService.update(record2);
				logger.info("update column success,num:"+update);*/
			}
		}
		
		return "导入成功";
    }
	/**
	 * 每次修改导入新增时都插入到版本表
	 * @param obj
	 * @param versionType
	 */
	void insertVersion(Object obj,String versionType) {
		String key = "";
		String desc = "";
		if("1".equals(versionType)) {
			DataInterface record = (DataInterface) obj;
			key = record.getDataSrcAbbr()+"|"+record.getDataInterfaceNo();
			desc = record.toString();
		}else if("2".equals(versionType)) {
			DataInterfaceColumns record = (DataInterfaceColumns) obj;
			key = record.getDataSrcAbbr()+"|"+record.getDataInterfaceNo()+"|"+record.getColumnNo();
			desc = record.toString();
		}else if("3".equals(versionType)) {
			entityC2e record = (entityC2e) obj;
			key = record.getCname();
			desc = record.toString();
		}else if("4".equals(versionType)) {
			attrC2e record = (attrC2e) obj;
			key = record.getCname();
			desc = record.toString();
		}
		TbVersion data = new TbVersion();
		data.setKey(key);
		data.setVersionType(versionType);
		data.setCreateDate(ExcelUtil.getInstance().getTime(new Date()));
		data.setCreateUser("admin");
		data.setVersionId(UUID.randomUUID().toString());
		data.setVersionDesc(desc);
		versionService.insert(data);
	}
	
	void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook){
		ServletOutputStream outputStream = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setHeader("content-Type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition",
						"attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
			outputStream = response.getOutputStream();
			workbook.write(outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }

	void downLoadSql(String filePath, String fileName, HttpServletResponse response){
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
//			String fileName = filePath.substring(tablePathCfgService.queryExPath().length());
			response.setCharacterEncoding("UTF-8");
			response.setHeader("content-Type", "text/html;charset=UTF-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
			outputStream = response.getOutputStream();

			//读取文件
			inputStream = new FileInputStream(filePath);
			byte[] buffer = new byte[1024];
			int len;
			while((len = inputStream.read(buffer)) != -1){
				logger.info("len:::"+len+"");
				outputStream.write(buffer, 0, len);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(outputStream != null){
					outputStream.close();
				}
				if(inputStream != null){
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static java.sql.Date getTomorrow() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        return new java.sql.Date(calendar.getTime().getTime());
    }
	
	public static java.sql.Date getToday(){
		return new java.sql.Date(new Date().getTime());
	}
	
	public static java.sql.Date getEdate() {
		return ExcelUtil.getInstance().StringToDate(BoncConstant.CON_E_DATE);
	}
	
	
}
