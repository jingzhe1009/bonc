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
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
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
import com.ljz.model.DataInterface;
import com.ljz.model.DataInterfaceTmp;
import com.ljz.service.IDataInterfaceService;
import com.ljz.util.TimeUtil;
/**
 * 接口配置
 * @author byan
 *
 */
@Controller
@RequestMapping("/interface")
public class InterfaceController extends MainController{
	
	private static final Logger logger = LoggerFactory.getLogger(InterfaceController.class);
	
	@Autowired
	IDataInterfaceService intService;
	
	@Autowired
	JdbcTemplate jdbc;
	
	@Autowired
	ExcelServiceImpl excelService;

	@ResponseBody
	@RequestMapping(value="/queryInterface",method = RequestMethod.GET)

    public Map<String, Object> queryInterface(String dataSrcAbbr,String dataInterfaceNo,Integer start, Integer length) {

		DataInterface record = new DataInterface();
		record.setDataSrcAbbr(dataSrcAbbr);
		record.setDataInterfaceNo(dataInterfaceNo);
		record.seteDate(TimeUtil.getTw());
		logger.info(record.toString());
		List<DataInterface> list = intService.queryAll(record);
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("recordsTotal", list.size());
        resultMap.put("recordsFiltered", list.size());
        resultMap.put("total", list.size());
        resultMap.put("data", list);
        logger.info("query interface success,num:"+list.size());
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/createInterface",method = RequestMethod.POST)
	@Transactional
    public Map<String,String> createInterface(DataInterface record) {
		Map<String,String> map = new HashMap<String,String>();
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(excelService.verifyInfaceInfo(record.getDataSrcAbbr(),record.getDataInterfaceNo(),record.getDataInterfaceName(),
				record.getDataInterfaceDesc(),record.getDataLoadFreq(),record.getDataLoadMthd(),record.getFiledDelim(),record.getLineDelim(),record.getExtrnlDatabaseName(),
				record.getExtrnlTableName(),record.getIntrnlDatabaseName(),record.getIntrnlTableName(),record.getTableType(),record.getBucketNumber().toString()));
		String string = stringBuffer.toString().trim();
		if (string!=null && !string.isEmpty() && !string.isEmpty()){
			map.put("message","保存失败，填入信息有误：\n"+record.getDataInterfaceName()+":\n"+stringBuffer);
			return map;
		}

		record.setsDate(TimeUtil.getTy());
		record.seteDate(TimeUtil.getE());
		record.setDataInterfaceNo(record.getDataInterfaceNo().trim());
		logger.info(record.toString());
		int insert = intService.insert(record);
		//insertVersion(record, "1");
		logger.info("insert interface success num:"+insert);
		map.put("message","保存成功");
		map.put("idx", record.getDataSrcAbbr());
		return map;
    }
	
	@ResponseBody
	@RequestMapping(value="/editInterface",method = RequestMethod.POST)
	@Transactional
    public Map<String,String> editInterface(DataInterface record) {
		Map<String,String> map = new HashMap<String,String>();
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(excelService.verifyInfaceInfo(record.getDataSrcAbbr(),record.getDataInterfaceNo(),record.getDataInterfaceName(),
				record.getDataInterfaceDesc(),record.getDataLoadFreq(),record.getDataLoadMthd(),record.getFiledDelim(),record.getLineDelim(),record.getExtrnlDatabaseName(),
				record.getExtrnlTableName(),record.getIntrnlDatabaseName(),record.getIntrnlTableName(),record.getTableType(),record.getBucketNumber().toString()));
		String string = stringBuffer.toString().trim();
		if (string!=null && !string.isEmpty() && !string.isEmpty()){
			map.put("message","保存失败，填入信息有误：\n"+record.getDataInterfaceName()+":\n"+stringBuffer);
			return map;
		}

		DataInterface data = new DataInterface();
		data.setDataSrcAbbr(record.getDataSrcAbbr());
		data.setDataInterfaceNo(record.getDataInterfaceNo().trim());
		data.setDataInterfaceName(record.getDataInterfaceName().trim());
		//生效日期作为查询条件,查询出当前生效的记录对其修改
		data.setsDate(TimeUtil.getTw());
		//原纪录失效日期改为今天
		data.seteDate(TimeUtil.getTy());
		logger.info(data.toString());
		int update = intService.update(data);
		logger.info("edit interface success,num:"+update);
		if(update>0) {
			record.setsDate(TimeUtil.getTy());
			//新记录失效日期改为无限长
			record.seteDate(TimeUtil.getE());
			logger.info(record.toString());
			int insert = intService.insert(record);
			logger.info("insert interface success,num:"+insert);
		}
		//insertVersion(record, "1");
//        return record.getDataSrcAbbr();
		map.put("message","保存成功");
		map.put("idx", record.getDataSrcAbbr());
		return map;
    }
	
	@ResponseBody
	@RequestMapping(value="/deleteInterface",method = RequestMethod.POST)
    public String deleteInterface(DataInterface record) {
		//生效日期作为查询条件,查询出当前生效的记录对其修改
		record.setsDate(TimeUtil.getTw());
		//原纪录失效日期改为今天
		record.seteDate(TimeUtil.getTy());
		logger.info(record.toString());
		int update = intService.update(record);
		logger.info("update interface success num:"+update);
        return record.getDataSrcAbbr();
    }
	/**
	 * 接口版本查询
	 * @param dataSrcAbbr
	 * @param dataInterfaceNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryInterfaceVersion",method = RequestMethod.GET)
    public Map<String, Object> queryInterfaceVersion(String dataSrcAbbr,String dataInterfaceNo,String dataInterfaceName) {
		DataInterface record = new DataInterface();
		record.setDataSrcAbbr(dataSrcAbbr);
		record.setDataInterfaceNo(dataInterfaceNo);
		record.setDataInterfaceName(dataInterfaceName);
		logger.info(record.toString());
		List<DataInterface> list = intService.queryAllVersion(record);
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("recordsTotal", list.size());
        resultMap.put("recordsFiltered", list.size());
        resultMap.put("total", list.size());
        resultMap.put("data", list);
        logger.info("query interface version success,num:"+list.size());
        return resultMap;
    }
	/**
	 * 接口临时表查询
	 * @param batchNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryInterfaceTmp",method = RequestMethod.GET)
    public Map<String, Object> queryInterfaceTmp(String batchNo,String dataSrcAbbr) {
		DataInterfaceTmp record = new DataInterfaceTmp();
		record.setBatchNo(batchNo);
		record.setDataSrcAbbr(dataSrcAbbr);
		logger.info(record.toString());
		List<DataInterfaceTmp> list = intService.queryAllTmp(record);
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("recordsTotal", list.size());
        resultMap.put("recordsFiltered", list.size());
        resultMap.put("total", list.size());
        resultMap.put("data", list);
        logger.info("query interface tmp success,num:"+list.size());
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/tmpToSaveAll",method = RequestMethod.POST)
    public Map<String,String> tmpToSaveAll(@RequestBody(required=false) ParamEntity param) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String dataSrcAbbr ="";
		long start = new Date().getTime();
		try {
			logger.info(param.toString());
			dataSrcAbbr = intService.batchImportFinal(param);
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
	
	/**
	 * 导入接口页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/importTable")
	public String importTable(Model model) {
		logger.info("enter into importTable page");
		return "importTable";
	}
	
	/**
	 * 导入接口
	 * @param file
	 * @return
	 */
	@RequestMapping("/importTableExcel")
	@ResponseBody
	public Map<String,String> importTableExcel(@RequestParam(value="filename")MultipartFile file,String batchNo) {
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
		logger.info("start import interface excel...");
		return excelService.importTable(file,batchNo);
	}
	
	/**
	 * 导出接口
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/exportTable")
	public String exportTable(HttpServletResponse response,HttpServletRequest request) {
		String filePath = "/static/excel/interfaceExcel.xlsx";
		String fileName = "interfaceExcel.xlsx";
	    try{
			ClassPathResource cpr = new ClassPathResource(filePath);
			InputStream is = cpr.getInputStream();
			Workbook workbook = new XSSFWorkbook(is);
			logger.info("start export interface excel...");
		    downLoadExcel(fileName, response, workbook);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return "importTable";
	}
	
	
	@ResponseBody
	@RequestMapping(value="/queryModel",method = RequestMethod.GET)
    public Map<String, Object> queryModel(String dataSrcAbbr,String dataInterfaceNo,Integer start, Integer length) {
		DataInterface record = new DataInterface();
		record.setDataSrcAbbr(dataSrcAbbr);
		record.setDataInterfaceNo(dataInterfaceNo);
		record.seteDate(TimeUtil.getTw());
		logger.info(record.toString());
		List<DataInterface> list = intService.queryModel(record);
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("recordsTotal", list.size());
        resultMap.put("recordsFiltered", list.size());
        resultMap.put("total", list.size());
        resultMap.put("data", list);
        logger.info("query model success,num:"+list.size());
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/delTable",method = RequestMethod.POST)
	public Map<String,String> dropTable(@RequestBody(required=false) ParamEntity param) {
		Map<String,String> map = new HashMap<String,String>();
		String idx = param.getDataSrcAbbr();
		String [] tables = param.getTables();
		String [] dbs = param.getFuncType();
		for(int i=0;i<tables.length;i++){
			String table = tables[i];
			String db = dbs[i];
			String sql = "select count(*) from "+db+"."+table;
			logger.info("执行sql==="+sql);
			try {
				jdbc.queryForObject(sql, Integer.class);
			} catch (DataAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				/*map.put("msgCode", "1111");
				map.put("msgData", db+"."+table+"表不存在");
				return map;*/
				continue;
			}
			String dropTableSql = "drop table "+db+"."+table;
			logger.info("执行sql==="+dropTableSql);
	        try {
				jdbc.execute(dropTableSql);
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				map.put("msgCode", "1111");
				map.put("msgData", "删除表"+db+"."+table+"失败");
				return map;
			}
        }
        map.put("msgCode", "0000");
        map.put("msgData", "删除表成功");
        map.put("idx", idx);
        return map;
	}

}
