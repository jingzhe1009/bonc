package com.ljz.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ljz.mapper.attrC2eMapper;
import com.ljz.mapper.entityC2eMapper;
import com.ljz.model.*;
import com.ljz.service.impl.DataSourceServiceImpl;
import com.ljz.service.impl.ExcelServiceImpl;
import com.ljz.util.ExcelUtil;
import com.ljz.util.SqlCacheUtil;
import com.ljz.util.TransUtil;
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
import com.ljz.model.DataInterfaceHistory;
import com.ljz.model.DataInterfaceRecords;
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

	@Autowired
	DataSourceServiceImpl dsService;

	@Resource
	attrC2eMapper aMapper;

	@Resource
	entityC2eMapper eMapper;

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
	@RequestMapping(value="/queryRecord",method = RequestMethod.GET)
    public Map<String, Object> queryRecord(String dataSrcAbbr) {

		DataInterfaceRecords record = new DataInterfaceRecords();
		record.setDataSrcAbbr(dataSrcAbbr);
		List<DataInterfaceRecords> list = intService.queryRecord(record);
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("recordsTotal", list.size());
        resultMap.put("recordsFiltered", list.size());
        resultMap.put("total", list.size());
        resultMap.put("data", list);
        logger.info("query records success,num:"+list.size());
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/queryLastRecord",method = RequestMethod.POST)
    public Map<String, Object> queryLastRecord(@RequestBody(required=false) ParamEntity param) {
		String dataSrcAbbr = param.getDataSrcAbbr();
		DataInterfaceRecords record = new DataInterfaceRecords();
		record.setDataSrcAbbr(dataSrcAbbr);
		List<DataInterfaceRecords> list = intService.queryRecord(record);
		DataInterfaceRecords data = new DataInterfaceRecords();
		SqlCacheUtil cache = SqlCacheUtil.getInstance();
		String state = "";
		if(list.size()>0) {
			data =list.get(0);
			state = "success";
		}else {
			data.setNeedVrsnNbr("V1.0");
			data.setExptSeqNbr("V1.0.0");
			data.setDataSrcAbbr(dataSrcAbbr);
		}
		//存入缓存
		cache.put(dataSrcAbbr+"DataInterfaceRecords",data);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("state", state);
        resultMap.put("data", data);
        logger.info("query records success,data="+data+",dataSrcAbbr="+dataSrcAbbr);
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/queryInterfaceCompare",method = RequestMethod.GET)
    public Map<String, Object> queryInterfaceCompare(String dataSrcAbbr,String batchNo) {
		DataInterfaceHistory record = new DataInterfaceHistory();
		record.setDataSrcAbbr(dataSrcAbbr);
		record.setExptSeqNbr(batchNo);//当前导入临时表的批次号
		logger.info(record.toString());
		List<DataInterfaceHistory> list = intService.queryInterfaceCompare(record);
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("recordsTotal", list.size());
        resultMap.put("recordsFiltered", list.size());
        resultMap.put("total", list.size());
        resultMap.put("data", list);
        logger.info("query interface compare success,num:"+list.size());
        return resultMap;
    }

	@ResponseBody
	@RequestMapping(value="/saveAll",method = RequestMethod.POST)
    public Map<String,String> saveAll(@RequestBody(required=false) ParamEntity param) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		long start = new Date().getTime();
		try {
			logger.info(param.toString());
			String content = intService.saveAll(param);
			if(!"success".equals(content)) {
				throw new Exception();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			map.put("msgData", "导入失败");
			return map;
		}
		long end = new Date().getTime();
		logger.info("导入用时:"+(end-start)+"毫秒");
		map.put("msgData", "导入成功");
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value="/createInterface",method = RequestMethod.POST)
	@Transactional
    public Map<String,String> createInterface(DataInterface record) {
		Map<String,String> map = new HashMap<String,String>();
		StringBuffer stringBuffer = new StringBuffer();
		List<entityC2e> eList = eMapper.queryAll(new entityC2e());
		List<attrC2e> aList = aMapper.queryAll(new attrC2e());
		ExcelUtil obj = ExcelUtil.getInstance();
		obj.initDTable(eList);
		obj.initDCol(aList);
		TransUtil.sb=new StringBuffer();
		if("".equals(record.getIntrnlTableName())) {  //内表表名为空，去词根表找
			record.setIntrnlTableName(TransUtil.transTable(record.getDataInterfaceDesc(), obj.getTableMap()));
			if ("".equals(record.getIntrnlTableName())) {  //词根表查找为空，去词根字段查找
				record.setIntrnlTableName(TransUtil.translateField(obj.getColMap(), record.getDataInterfaceDesc()));
				//obj.getCellValue(record.getIntrnlTableName(),obj.getColMap(),record.getDataInterfaceDesc()));// = obj.getCellValue(row.getCell(11),obj.getColMap(),record.getIntrnlTableName());
				if (record.getIntrnlTableName().equals("")){
					record.setIntrnlTableName("");
				}else if (!record.getIntrnlTableName().startsWith("_")){
					record.setIntrnlTableName(record.getDataSrcAbbr() + "_" + record.getIntrnlTableName().toUpperCase());// = record.getDataSrcAbbr() + "_" + record.getIntrnlTableName().toUpperCase();
				}else {
					record.setIntrnlTableName(record.getDataSrcAbbr() + record.getIntrnlTableName().toUpperCase());// = record.getDataSrcAbbr() + record.getIntrnlTableName().toUpperCase();
				}
			}
		}
//内表表名校验
		String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正则
		Pattern p = Pattern.compile(REGEX_CHINESE);
		Matcher m = p.matcher(record.getIntrnlTableName());
		if (m.find()){
			stringBuffer.append("[内表表名]"+"「"+record.getIntrnlTableName()+"」"+"中文字符在词根找不到映射"+"\n");
		}
		if (!record.getIntrnlTableName().startsWith(record.getDataSrcAbbr()) && !record.getIntrnlTableName().equals("")){
			stringBuffer.append("[内表表名]"+"「"+record.getIntrnlTableName()+"」"+"需前缀数据源"+"\n");
		}else if (!record.getIntrnlTableName().equals("") && record.getIntrnlTableName().endsWith("_TB")  ){
			stringBuffer.append("[内表表名]"+"「"+record.getIntrnlTableName()+"」"+"需前删除后缀'_TB'"+"\n");
		}else if ("".equals(record.getIntrnlTableName())){
			stringBuffer.append("[内表表名]"+"「"+record.getIntrnlTableName()+"」不能为空"+"\n");
		}

		List<DataInterface> list = intService.queryDsAndInfaceName(record.getDataSrcAbbr());
		if (list.stream().anyMatch(e -> e.getDataInterfaceName().equals(record.getDataInterfaceName()))){
			stringBuffer.append("[接口名]「"+record.getDataInterfaceName()+"」已存在\n");
		}else if (list.stream().anyMatch(e -> e.getDataInterfaceDesc().equals(record.getDataInterfaceDesc()))) {
			stringBuffer.append("[接口中文名]「"+record.getDataInterfaceDesc()+"」已存在\n");
		}else if (list.stream().anyMatch(e -> e.getIntrnlTableName().equals(record.getIntrnlTableName()))) {
			stringBuffer.append("[内表表名]「"+record.getIntrnlTableName()+"」已存在\n");
		}

		stringBuffer.append(excelService.verifyInfaceInfo(record.getDataSrcAbbr(),record.getDataInterfaceNo(),record.getDataInterfaceName(),
				record.getDataInterfaceDesc(),record.getDataLoadFreq(),record.getDataLoadMthd(),record.getFiledDelim(),record.getLineDelim(),record.getExtrnlDatabaseName(),
				record.getExtrnlTableName(),record.getIntrnlDatabaseName(),record.getIntrnlTableName().toUpperCase(),record.getTableType(),record.getBucketNumber().toString()));
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
		List<entityC2e> eList = eMapper.queryAll(new entityC2e());
		List<attrC2e> aList = aMapper.queryAll(new attrC2e());
		ExcelUtil obj = ExcelUtil.getInstance();
		obj.initDTable(eList);
		obj.initDCol(aList);
		TransUtil.sb=new StringBuffer();
		if("".equals(record.getIntrnlTableName())) {  //内表表名为空，去词根表找
			record.setIntrnlTableName(TransUtil.transTable(record.getDataInterfaceDesc(), obj.getTableMap()));
			System.out.println("record.getIntrnlTableName()111:::" + record.getIntrnlTableName());
			if ("".equals(record.getIntrnlTableName())) {  //词根表查找为空，去词根字段查找
				record.setIntrnlTableName(TransUtil.translateField(obj.getColMap(), record.getDataInterfaceDesc()));
				//obj.getCellValue(record.getIntrnlTableName(),obj.getColMap(),record.getDataInterfaceDesc()));// = obj.getCellValue(row.getCell(11),obj.getColMap(),record.getIntrnlTableName());
				if (record.getIntrnlTableName().equals("")){
					record.setIntrnlTableName("");
				}else if (!record.getIntrnlTableName().startsWith("_")){
					record.setIntrnlTableName(record.getDataSrcAbbr() + "_" + record.getIntrnlTableName().toUpperCase());// = record.getDataSrcAbbr() + "_" + record.getIntrnlTableName().toUpperCase();
				}else {
					record.setIntrnlTableName(record.getDataSrcAbbr() + record.getIntrnlTableName().toUpperCase());// = record.getDataSrcAbbr() + record.getIntrnlTableName().toUpperCase();
				}
			}
		}
//内表表名校验
		String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正则
		Pattern p = Pattern.compile(REGEX_CHINESE);
		Matcher m = p.matcher(record.getIntrnlTableName());
		if (m.find()){
			stringBuffer.append("[内表表名]"+"「"+record.getIntrnlTableName()+"」"+"中文字符在词根找不到映射"+"\n");
		}
		if (!record.getIntrnlTableName().startsWith(record.getDataSrcAbbr()) && !record.getIntrnlTableName().equals("")){
			stringBuffer.append("[内表表名]"+"「"+record.getIntrnlTableName()+"」"+"需前缀数据源"+"\n");
		}else if (!record.getIntrnlTableName().equals("") && record.getIntrnlTableName().endsWith("_TB")  ){
			stringBuffer.append("[内表表名]"+"「"+record.getIntrnlTableName()+"」"+"需前删除后缀'_TB'"+"\n");
		}else if ("".equals(record.getIntrnlTableName())){
			stringBuffer.append("[内表表名]「"+record.getIntrnlTableName()+"」不能为空"+"\n");
		}

		List<DataInterface> list = intService.queryDsAndInfaceName(record.getDataSrcAbbr());
		List<DataInterface> listDataInterfaceDesc = list.stream().filter(e -> !e.getDataInterfaceDesc().equals(record.getDataInterfaceDesc())).collect(Collectors.toList());

		List<DataInterface> listDataInterfaceName = list.stream().filter(e -> !e.getDataInterfaceName().equals(record.getDataInterfaceName())).collect(Collectors.toList());
		if (listDataInterfaceDesc.stream().anyMatch(e -> e.getDataInterfaceDesc().equals(record.getDataInterfaceDesc()))) {
			stringBuffer.append("[接口中文名]'"+record.getDataInterfaceDesc()+"'已存在\n");
		}else if (listDataInterfaceName.stream().anyMatch(e -> e.getIntrnlTableName().equals(record.getIntrnlTableName()))) {
			stringBuffer.append("[内表表名]'"+record.getIntrnlTableName()+"'已存在\n");
		}

		stringBuffer.append(excelService.verifyInfaceInfo(record.getDataSrcAbbr(),record.getDataInterfaceNo(),record.getDataInterfaceName(),
				record.getDataInterfaceDesc(),record.getDataLoadFreq(),record.getDataLoadMthd(),record.getFiledDelim(),record.getLineDelim(),record.getExtrnlDatabaseName(),
				record.getExtrnlTableName(),record.getIntrnlDatabaseName(),record.getIntrnlTableName().toUpperCase(),record.getTableType(),record.getBucketNumber().toString()));
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
	static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor){
		Map<Object,Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t),Boolean.TRUE) == null;
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
		logger.info("batchNo:::"+batchNo+"\ndataSrcAbbr:::"+dataSrcAbbr);
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
			logger.info("InfaceParam:::"+param.toString());
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
    @RequestMapping(value = "/infaceInfoList",method = RequestMethod.GET)
	public Map<String, Object> infaceInfoList(){
		Map<String,Object> resultMap = new HashMap<>();
		List<Map<String, Object>> dataLoadMtdList = jdbc.queryForList("select item_type,data_struct_code from data_code_config WHERE item_name = '数据加载方式' AND  database_name = 'interface'  AND  database_code = 'tdh' ");
		resultMap.put("dataLoadMtdList", dataLoadMtdList);
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
