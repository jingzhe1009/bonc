package com.ljz.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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

import com.alibaba.fastjson.JSON;
import com.ljz.entity.ParamEntity;
import com.ljz.model.DataInterface2proc;
import com.ljz.model.DataInterface2procTmp;
import com.ljz.model.Order;
import com.ljz.service.IDataInterfaceService;
import com.ljz.util.InsertDbProduceReturn;
import com.ljz.util.TestProduceReturn;
import com.ljz.util.TimeUtil;

/**
 * 数据模型
 * @author byan
 *
 */
@Controller
@RequestMapping("/model")
public class ModelController extends MainController{
	
	private static final Logger logger = LoggerFactory.getLogger(ModelController.class);
	
	@Autowired
	IDataInterfaceService intService;
	
	@Autowired
	TestProduceReturn testProduce;
	
	@Autowired
	InsertDbProduceReturn dbProduce;
	
	@Autowired
	JdbcTemplate jdbc;
	
	
	@ResponseBody
	@RequestMapping(value="/queryProc",method = RequestMethod.GET)
    public Map<String, Object> queryProc(DataInterface2proc record) {
		record.seteDate(TimeUtil.getTw());
		logger.info(record.toString());
		List<DataInterface2proc> list = intService.queryProc(record);
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("recordsTotal", list.size());
        resultMap.put("recordsFiltered", list.size());
        resultMap.put("total", list.size());
        resultMap.put("data", list);
        logger.info("query proc success,num:"+list.size());
        return resultMap;
    }
	
	@ResponseBody
	@RequestMapping(value="/createProc",method = RequestMethod.POST)
	@Transactional
    public String createProc(DataInterface2proc record) {
		record.setsDate(TimeUtil.getTy());
		record.seteDate(TimeUtil.getE());
		logger.info(record.toString());
		intService.insertProc(record);
		logger.info("proc proc success"+record.toString());
        return record.getDataSrcAbbr();
    }
	
	@ResponseBody
	@RequestMapping(value="/editProc",method = RequestMethod.POST)
	@Transactional
    public String editProc(DataInterface2proc record) {
		DataInterface2proc data = new DataInterface2proc();
		data.setDataSrcAbbr(record.getDataSrcAbbr());
		data.setDataInterfaceNo(record.getDataInterfaceNo());
		//生效日期作为查询条件,查询出当前生效的记录对其修改
		data.setsDate(TimeUtil.getTw());
		//原纪录失效日期改为今天
		data.seteDate(TimeUtil.getTy());
		logger.info(data.toString());
		int update = intService.updateProc(data);
		logger.info("edit proc success,num:"+update);
		if(update>0) {
			record.setsDate(TimeUtil.getTy());
			//新记录失效日期改为无限长
			record.seteDate(TimeUtil.getE());
			logger.info(record.toString());
			int insert = intService.insertProc(record);
			logger.info("insert proc success,num:"+insert);
		}
        return record.getDataSrcAbbr();
    }
	
	@ResponseBody
	@RequestMapping(value="/deleteProc",method = RequestMethod.POST)
    public String deleteProc(DataInterface2proc record) {
		//生效日期作为查询条件,查询出当前生效的记录对其修改
		record.setsDate(TimeUtil.getTw());
		record.seteDate(TimeUtil.getTy());
		logger.info(record.toString());
		int update = intService.updateProc(record);
		logger.info("update proc success num:"+update);
        return record.getDataSrcAbbr();
    }
	
	/**
	 * 导入存储过程页面
	 * @param model
	 * @param tableName
	 * @return
	 */
	@RequestMapping("/importProc")
	public String importProc(Model model) {
		return "importProc";
	}
	
	/**
	 * 接口版本查询
	 * @param dataSrcAbbr
	 * @param dataInterfaceNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryProcVersion",method = RequestMethod.GET)
    public Map<String, Object> queryProcVersion(String dataSrcAbbr,String dataInterfaceNo) {
		DataInterface2proc record = new DataInterface2proc();
		record.setDataSrcAbbr(dataSrcAbbr);
		record.setDataInterfaceNo(dataInterfaceNo);
		logger.info(record.toString());
		List<DataInterface2proc> list = intService.queryAllVersionProc(record);
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("recordsTotal", list.size());
        resultMap.put("recordsFiltered", list.size());
        resultMap.put("total", list.size());
        resultMap.put("data", list);
        logger.info("query proc version success,num:"+list.size());
        return resultMap;
    }
	/**
	 * 接口临时表查询
	 * @param batchNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryProcTmp",method = RequestMethod.GET)
    public Map<String, Object> queryProcTmp(String batchNo,String dataSrcAbbr) {
		DataInterface2procTmp record = new DataInterface2procTmp();
		record.setBatchNo(batchNo);
		record.setDataSrcAbbr(dataSrcAbbr);
		logger.info(record.toString());
		List<DataInterface2procTmp> list = intService.queryAllTmpProc(record);
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("recordsTotal", list.size());
        resultMap.put("recordsFiltered", list.size());
        resultMap.put("total", list.size());
        resultMap.put("data", list);
        logger.info("query proc tmp success,num:"+list.size());
        return resultMap;
    }
	/**
	 * 接口临时表数据导入正式表
	 * @param param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/tmpToSaveAll",method = RequestMethod.POST)
    public Map<String,String> tmpToSaveAll(@RequestBody(required=false) ParamEntity param) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String dataSrcAbbr ="";
		long start = new Date().getTime();
		try {
			logger.info(param.toString());
			dataSrcAbbr = intService.batchImportProcFinal(param);
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
	
	
	@ResponseBody
	@RequestMapping(value="/procInfoList",method = RequestMethod.GET)
    public Map<String, Object> procInfoList() {
		List<Map<String, Object>> dbList = jdbc.queryForList("SELECT DISTINCT func_name,func_desc FROM data_func_register WHERE use_type='3'");
		List<Map<String, Object>> procList = jdbc.queryForList("SELECT DISTINCT func_param,func_param_desc FROM data_func_register  WHERE use_type='3'");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("dbList", dbList);
		resultMap.put("procList", procList);
        return resultMap;
    }
	
	/**
	 * 导入存储过程
	 * @param file
	 * @return
	 */
	@RequestMapping("/importProcExcel")
	@ResponseBody
	public Map<String,String> importProcExcel(@RequestParam(value="filename")MultipartFile file,String batchNo) {
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
		logger.info("start import proc excel...");
		
		return excelService.importProc(file,batchNo);
	}
	
	@RequestMapping(value="/exportProc")
	public String exportProc(HttpServletResponse response,HttpServletRequest request) {
		String filePath = "/static/excel/procExcel.xlsx";
		String fileName = "procExcel.xlsx";
	    try{
			ClassPathResource cpr = new ClassPathResource(filePath);
			InputStream is = cpr.getInputStream();
			Workbook workbook = new XSSFWorkbook(is);
			logger.info("start export proc excel...");
		    downLoadExcel(fileName, response, workbook);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return "importProc";
	}
	/**
	 * 物化
	 * @param model
	 * @param ds
	 * @param ids
	 * @return
	 */
	
	@RequestMapping("/createSqlPage")
	public String createSqlPage(Model model,String ds,String ids) {
		List<String> list = JSON.parseArray(ids,String.class);
		String[] array = list.toArray(new String[list.size()]);
		System.out.println(intService.queryByList(ds, array));
		model.addAttribute("list", intService.queryByList(ds, array));
		return "createSql";
	}
	/**
	 * 生成sql语句
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/createSql")
	@ResponseBody
	public Map<String,String> createSql(@RequestBody(required=false) ParamEntity param) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String [] tables = param.getTables();
		if(tables==null||tables.length<1) {
			map.put("msgCode", "1111");
			map.put("msgData", "表名不能为空");
			return map;
		}
		String dbType = param.getDbType();
		String sql ="";
		try {
			long start = new Date().getTime();
			if("1".equals(dbType)) {
				for(String table:tables) {
					Order order = testProduce.getSql(table,param.getDataSrcAbbr());
					if(order.getSql1()==null||"".equals(order.getSql1())) {
						map.put("msgCode", "1111");
						map.put("msgData", "外表建表语句为空");
						return map;
					}
					sql = order.getSql1() + "\n" + sql;
				}
			}else if("2".equals(dbType)) {
				for(String table:tables) {
					Order order = testProduce.getSql(table,param.getDataSrcAbbr());
					if(order.getSql2()==null||"".equals(order.getSql2())) {
						map.put("msgCode", "1111");
						map.put("msgData", "内表建表语句为空");
						return map;
					}
					sql = order.getSql2() + "\n" + sql;
				}
			}else{
				for(String table:tables) {
					Order order = testProduce.getSql(table,param.getDataSrcAbbr());
					if(order.getSql1()==null||"".equals(order.getSql1())) {
						map.put("msgCode", "1111");
						map.put("msgData", "外表建表语句为空");
						return map;
					}
					sql = sql + order.getSql1() + "\n" + "\n";
					if(order.getSql2()==null||"".equals(order.getSql2())) {
						map.put("msgCode", "1111");
						map.put("msgData", "内表建表语句为空");
						return map;
					}
					sql = sql + order.getSql2() + "\n";
				}
			}
			long end = new Date().getTime();
			logger.info("导入用时:"+(end-start)+"毫秒");
			map.put("msgCode", "0000");
			map.put("msgData", sql);
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("msgCode", "1111");
			map.put("msgData", "查看建表语句操作失败\n1.请查看该表是否有重复字段\n2.是否有分桶字段\n3.建表语句是否有中文符号\n4.建表语句是否有不支持的关键字或字段类型");
			return map;
		}
	}
	
	@RequestMapping("/createFile")
	@ResponseBody
	public Map<String,String> createFile(@RequestBody(required=false) ParamEntity param) throws Exception{
		long start = new Date().getTime();
		String tmpTable[] = param.getTables();
		List<String> list = new ArrayList<String>();
		Map<String,String> map = new HashMap<String,String>();
		String num ="";
		String tb ="";
		for(int i=0;i<tmpTable.length;i++){
			if(tmpTable[i].equals("checkedAll"))
				continue;
			if(!tmpTable[i].contains("-"))
				continue;
			logger.info("执行接口:"+tmpTable[i]);
			String[] split = tmpTable[i].split("-");
			tb = split[0];
			//String state = split[1];
			num = split[2];
			if(num==null||num.equals("null")||"".equals(num))
				break;
			list.add(tb);
		}
		if(num==null||num.equals("null")||"".equals(num)){
			map.put("msgCode", "1111");
			map.put("msgData", "接口"+tb+"没有配置字段");
			return map;
		}
		map = intService.createFile(list, param);
		long end = new Date().getTime();
		logger.info("生成建表语句文件用时:"+(end-start)+"毫秒");
		return map;
	}
	
	@RequestMapping("/insertDb")
	@ResponseBody
	public Map<String,String> insertDb(@RequestBody(required=false) ParamEntity param) throws Exception{
		long start = new Date().getTime();
		Map<String,String> map = new HashMap<String,String>();
		String tmpTable[] = param.getTables();
		List<String> list = new ArrayList<String>();
		List<String> hasList = new ArrayList<String>();
		String tb ="";
		String num = "";
		for(int i=0;i<tmpTable.length;i++){
			if(tmpTable[i].equals("checkedAll"))
				continue;
			if(!tmpTable[i].contains("-"))
				continue;
			logger.info("执行接口:"+tmpTable[i]);
			String[] split = tmpTable[i].split("-");
			tb = split[0];
			String state = split[1];
			num = split[2];
			if(num==null||"null".equals(num)||"".equals(num))
				break;
			if(!state.contains("未")){
				hasList.add(tb);
			}else{
				list.add(tb);
			}
		}
		if(num==null||"null".equals(num)||"".equals(num)){
			map.put("msgCode", "1111");
			map.put("msgData", "接口"+tb+"没有配置字段");
			return map;
		}
		if(list.size()<1){
			map.put("msgCode", "1111");
			map.put("msgData", "选中的接口都已建表,已存在表为："+hasList.toString());
			return map;
		}
		logger.info("需要物化的表:"+list);
		map = intService.insertDb(list,hasList, param);
		long end = new Date().getTime();
		logger.info("建模入库用时:"+(end-start)+"毫秒");
		return map;
	}
	
	/*@RequestMapping("/createFile")
	@ResponseBody
	public Map<String,String> createFile(@RequestBody(required=false) ParamEntity param) throws Exception{
		long start = new Date().getTime();
		Map<String,String> map = new HashMap<String,String>();
		String tmpTable[] = param.getTables();
		List<String> list = new ArrayList<String>();
		List<String> hasList = new ArrayList<String>();
		String num ="";
		String tb ="";
		for(int i=0;i<tmpTable.length;i++){
			if(tmpTable[i].equals("checkedAll"))
				continue;
			if(!tmpTable[i].contains("-"))
				continue;
			logger.info("执行接口:"+tmpTable[i]);
			String[] split = tmpTable[i].split("-");
			tb = split[0];
			String state = split[1];
			num = split[2];
			if(num==null||num.equals("null")||"".equals(num))
				break;
			if(!state.contains("未")){
				hasList.add(tb);
			}else{
				list.add(tb);
			}
			list.add(tb);
		}
		if(num==null||num.equals("null")||"".equals(num)){
			map.put("msgCode", "1111");
			map.put("msgData", "接口"+tb+"没有配置字段");
			return map;
		}
		System.out.println("list:::"+list);
//		String [] tables = param.getTables();
		
//		if(tables==null||tables.length<1) {
//			map.put("msgCode", "1111");
//			map.put("msgData", "表结构为空");
//		}
		String sql1 = "";
		String sql2 = "";
		if(list.size()<1){
			map.put("msgCode", "1111");
			map.put("msgData", "选中的接口都已建表,已存在表："+hasList.toString());
			return map;
		}
		
		String dateStr = FileUtil.formatDate(new Date());
		String filePath="";
		try {
			filePath = config.getFilePath()+param.getDataSrcAbbr()+dateStr+".sql";
			logger.info("建表文件路径:"+filePath);
			filePath2 = config.getFilePath()+param.getDataSrcAbbr()+dateStr+"_inner.sql";
			logger.info("外表路径:"+filePath);
			logger.info("内表路径:"+filePath2);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			map.put("msgCode", "1111");
			map.put("msgData", "文件路径:"+filePath+",文件编码"+config.getFileEncode()+",找不到文件路径");
			return map;
		}
		List<String> sqlList = new ArrayList<String>();
		List<String> failList = new ArrayList<String>();
		//创建使用单个线程的线程池
		ExecutorService es = Executors.newFixedThreadPool(5);
		//创建外表
		try {
			for(String table:list) {
				//使用lambda实现runnable接口
				Runnable task = ()->{
					long start2 = new Date().getTime();
					logger.info(Thread.currentThread().getName()+"创建了一个新的线程执行,创建表");
					try {
						Order order = testProduce.getSql(table,param.getDataSrcAbbr());
						sqlList.add(order.getSql1()+"\n"+order.getSql2()+"\n");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						failList.add(table);
						e.printStackTrace();
					}
					long end2 = new Date().getTime();
					logger.info("存储过程用时:"+(end2-start2)+"毫秒");
					String sql1 =  order.getSql1() +"\n"+sql1;
					String sql2 =  order.getSql2() +"\n"+sql2;
				};
				es.submit(task);
				logger.info("创建表线程开始");
			}
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			map.put("msgCode", "1111");
			map.put("msgData", "生成建表语句文件失败。\n1.请查看该表是否有对应字段\n2.是否有分桶字段\n3.建表语句是否有中文符号\n4.建表语句关键字是否有不支持的关键字");
			return map;
		}
		System.out.println("sqlList.size="+sqlList.size());
		System.out.println("failList.size="+failList.size());
		System.out.println("list.size="+list.size());
		try {
			if(list.size()==sqlList.size()){
				String totalSql="";
				for(String sql :sqlList){
					totalSql = totalSql+sql;
				}
				FileUtil.write(filePath, totalSql,config.getFileEncode());
				logger.info("write extra table file ");
			}
			int i =0;
			reWriteFile(list, sqlList,failList,filePath, i);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("msgCode", "1111");
			map.put("msgData", "文件写入失败,文件路径:"+filePath+",文件编码"+config.getFileEncode());
			return map;
		}
		long end = new Date().getTime();
		logger.info("一共用时:"+(end-start)+"毫秒");
		try {
			if(sql2==null||"".equals(sql2))
				throw new Exception();
			FileUtil.write(filePath2, sql2,config.getFileEncode());
			logger.info("write inner table file ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("msgCode", "1111");
			map.put("msgData", "文件路径:"+filePath+",sql语句"+sql1+",文件编码"+config.getFileEncode()+",外表文件写入失败");
			return map;
		}
		map.put("msgCode", "0000");
		//String str = "建表语句文件生成成功,\n外表文件路径:"+filePath+",\n内表文件路径:"+filePath2;
		String str = "建表语句文件生成成功,\n路径:"+filePath+"\n";
		if(hasList.size()>0){
			str = str+","+hasList.toString()+"已存在,不进行生成文件，如有变更，请先删除表\n";
		}
		if(failList.size()>0){
			str =str +",其中接口"+failList.toString()+"生成建表语句失败";
		}
		map.put("idx", param.getDataSrcAbbr());
		map.put("msgData", str);
		map.put("filePath", filePath);
		return map;
		String filePath = config.getFilePath()+FileUtil.formatDate(new Date())+param.getDataSrcAbbr()+".txt";
		System.out.println("filepath:"+filePath);
		FileUtil.write(filePath, sql);
		//return factory.selectDb(dbType).createSqlAndFile(tables,dbType);
	}*/
	/*public void reWriteFile(List<String> list,List<String> sqlList,List<String> failList,String filePath,int i) throws Exception{
		i++;
		Thread.sleep(300);
		if(list.size()==sqlList.size()+failList.size()){
			String totalSql="";
			for(String sql :sqlList){
				totalSql = totalSql+sql;
			}
			if(!"".equals(totalSql)){
				FileUtil.write(filePath, totalSql,config.getFileEncode());
				logger.info("write table file ");
			}
		}else{
			System.out.println("i="+i);
			if(i>200){
				
			}else{
				reWriteFile(list, sqlList,failList,filePath, i);
			}
		}
	}*/
	
	/*@RequestMapping("/insertDb")
	@ResponseBody
	public Map<String,String> insertDb(@RequestBody(required=false) ParamEntity param) throws Exception{
		long start = new Date().getTime();
		Map<String,String> map = new HashMap<String,String>();
		String tmpTable[] = param.getTables();
		List<String> list = new ArrayList<String>();
		List<String> hasList = new ArrayList<String>();
		String tb ="";
		String num = "";
		for(int i=0;i<tmpTable.length;i++){
			if(tmpTable[i].equals("checkedAll"))
				continue;
			if(!tmpTable[i].contains("-"))
				continue;
			logger.info("执行接口:"+tmpTable[i]);
			String[] split = tmpTable[i].split("-");
			tb = split[0];
			String state = split[1];
			num = split[2];
			if(num==null||"null".equals(num)||"".equals(num))
				break;
			if(!state.contains("未")){
				hasList.add(tb);
			}else{
				list.add(tb);
			}
		}
		if(num==null||"null".equals(num)||"".equals(num)){
			map.put("msgCode", "1111");
			map.put("msgData", "接口"+tb+"没有配置字段");
			return map;
		}
		if(list.size()<1){
			map.put("msgCode", "1111");
			map.put("msgData", "选中的接口都已建表,已存在表为："+hasList.toString());
			return map;
		}
		logger.info("需要物化的表:"+list);
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = sdf.format(new Date());
		List<String> sucTable = new ArrayList<String>();
		List<String> failTable = new ArrayList<String>();
		//创建使用单个线程的线程池
		ExecutorService es = Executors.newFixedThreadPool(5);
		//创建外表
		try {
			for(String table:list) {
				//使用lambda实现runnable接口
				Runnable task = ()->{
					logger.info(Thread.currentThread().getName()+"创建了一个新的线程执行,创建表");
					List<Map<String, Object>> queryForList = jdbc.queryForList("select * from data_log where action_id='"+param.getDataSrcAbbr()+"'"
							+ " and action_user='"+table+"' order by action_time desc limit 1");
					try {
						long start2 = new Date().getTime();
						Order order = dbProduce.getSql(table,param.getDataSrcAbbr());
						if(order.getSql1()==null||"".equals(order.getSql1())) {
							System.out.println(order.getSql1());
						}
						if(order.getSql2()==null||"".equals(order.getSql2())) {
							System.out.println(order.getSql2());
						}
						long end2 = new Date().getTime();
						logger.info("存储过程用时:"+(end2-start2)+"毫秒");
						cacheMap.put(param.getDataSrcAbbr()+table, "1");
						if(queryForList.size()>0){
							jdbc.update(" update data_log set action_desc='1',action_time='"+date+"'"
									+" where action_id='"+param.getDataSrcAbbr()+"'"
											+ " and action_user='"+table+"' order by action_time desc limit 1");
						}else{
							jdbc.update("insert into data_log(action_id,action_user,action_desc,action_time) "
									+ "values ('"+param.getDataSrcAbbr()+"','"+table+"','1','"+date+"')");
						}
						logger.info(table+"建表成功");
						sucTable.add(table);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if(queryForList.size()>0){
							jdbc.update(" update data_log set action_desc='2',action_time='"+date+"'"
									+" where action_id='"+param.getDataSrcAbbr()+"'"
											+ " and action_user='"+table+"' order by action_time desc limit 1");
						}else{
							jdbc.update("insert into data_log(action_id,action_user,action_desc,action_time) "
									+ "values ('"+param.getDataSrcAbbr()+"','"+table+"','2','"+date+"')");
						}
						//cacheMap.put(param.getDataSrcAbbr()+table, "2");
						logger.info(table+"建表失败");
						failTable.add(table);
					}
					//调用submit传递线程任务，开启线程
				};
				es.submit(task);
				logger.info("创建表线程开始");
			}
			//System.out.println("cacheMap="+cacheMap.keySet());
			int i =0;
			complete(list,sucTable,failTable,i);
			long end = new Date().getTime();
			logger.info("一共用时:"+(end-start)+"毫秒");
			map.put("msgCode", "0000");
			map.put("idx", param.getDataSrcAbbr());
			String str = "建表入库执行完成,请关注建表创建状态!本次建表接口名:"+list.toString()+",共计:"+list.size()+"个接口";
					+ ",其中成功接口："+sucTable+",失败接口:"+failTable;
			if(hasList.size()>0){
				str = str +",接口"+hasList.toString()+"已存在,没有执行建表，如有变更，请先删除表";
			}
			map.put("msgData", str);
			
			+ " and action_user='"+table+"'");
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			es.shutdown();
			map.put("msgCode", "1111");
			map.put("msgData", "建表失败,1.请查看该表是否有对应字段\n2.是否有分桶字段\n3.建表语句是否有中文符号\n4.建表语句关键字是否有不支持的关键字");
			return map;
		}
	}*/
	
	
	
	
	/*@RequestMapping("/insertDbBak")
	@ResponseBody
	public Map<String,String> insertDbBak(@RequestBody(required=false) ParamEntity param) throws Exception{
		return null;*/
		/*Map<String,String> map = new HashMap<String,String>();
		String tmpTable[] = param.getTables();
		List<String> list = new ArrayList<String>();
		for(int i=0;i<tmpTable.length;i++){
			if(tmpTable[i].equals("checkedAll"))
				continue;
			list.add(tmpTable[i]);
		}
		logger.info("需要物化的表:"+list);
		map.put("msgCode", "0000");
		map.put("msgData", "建表入库成功!接口名:"+list.toString()+",共计:"+list.size()+"个接口");
		String sql1 = "";
		String sql2 = "";
		try {
			for(String table:list) {
				Order order = dbProduce.getSql(table,param.getDataSrcAbbr());
				if(order.getSql1()==null||"".equals(order.getSql1())) {
					map.put("msgCode", "1111");
					map.put("msgData", "接口"+table+"外表建表语句为空");
					break;
				}
				if(order.getSql2()==null||"".equals(order.getSql2())) {
					map.put("msgCode", "1111");
					map.put("msgData", "接口"+table+"内表建表语句为空");
					break;
				}
				sql1 =  order.getSql1() +"\n"+sql1;
				sql2 =  order.getSql2() +"\n"+sql2;
				
			}
			return map;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			map.put("msgCode", "1111");
			map.put("msgData", "建表失败,1.请查看该表是否有对应字段2.是否有分桶字段3.建表语句是否有中文符号4.建表语句关键字是否有不支持的关键字");
			return map;
		}*/
		/*String dateStr = FileUtil.formatDate(new Date());
		String filePath = config.getFilePath()+param.getDataSrcAbbr()+dateStr+"_extra.sql";
		String filePath2 = config.getFilePath()+param.getDataSrcAbbr()+dateStr+"_inner.sql";
		logger.info("外表路径:"+filePath);
		logger.info("内表路径:"+filePath2);
		try {
			if(sql1==null||"".equals(sql1))
				throw new Exception();
			FileUtil.write(filePath, sql1,config.getFileEncode());
			logger.info("write extra table file ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("msgCode", "1111");
			map.put("msgData", "文件路径:"+filePath+",sql语句"+sql1+",文件编码"+config.getFileEncode()+",外表文件写入失败");
			return map;
		}*/
		/*try {
			if(sql2==null||"".equals(sql2))
				throw new Exception();
			logger.info(" exec cmd :"+BoncConstant.SHELL_CMD);
			String exec = TestShell.exec(BoncConstant.SHELL_CMD+filePath);
			if(exec!=null&&!"".equals(exec)&&exec.contains("1111"))
				throw new Exception();
			logger.info(exec);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("msgCode", "1111");
			map.put("msgData", "创建外表失败,执行命令:"+BoncConstant.SHELL_CMD+filePath);
			return map;
		}*/
		/*try {
			FileUtil.write(filePath2, sql2,config.getFileEncode());
			logger.info("write inner table file ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("msgCode", "1111");
			map.put("msgData", "文件路径:"+filePath2+",sql语句"+sql2+",文件编码"+config.getFileEncode()+",内表文件写入失败");
			return map;
		}*/
		/*try {
			logger.info(" exec cmd :"+BoncConstant.SHELL_CMD_INNER);
			String exec = TestShell.exec(BoncConstant.SHELL_CMD_INNER+filePath2);
			if(exec!=null&&!"".equals(exec)&&exec.contains("1111"))
				throw new Exception();
			logger.info(exec);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("msgCode", "1111");
			map.put("msgData", "创建内表失败执行命令:"+BoncConstant.SHELL_CMD_INNER+filePath2);
			return map;
		}*/
		/*map.put("filePath", filePath);
		map.put("filePath2", filePath2);*/
		
		
		/*Thread.sleep(500);
		//创建使用单个线程的线程池
		ExecutorService es = Executors.newFixedThreadPool(10);
		//创建外表
		try {
			//使用lambda实现runnable接口
			Runnable task = ()->{
				logger.info(Thread.currentThread().getName()+"创建了一个新的线程执行,创建外表");
				try {
					logger.info(" exec cmd :"+BoncConstant.SHELL_CMD);
					String exec = TestShell.exec(BoncConstant.SHELL_CMD+filePath);
					if(exec!=null&&!"".equals(exec)&&exec.contains("1111"))
						throw new Exception();
					logger.info(exec);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//调用submit传递线程任务，开启线程
			};
			es.submit(task);
			logger.info("创建外表线程开始");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			es.shutdown();
			map.put("msgCode", "1111");
			map.put("msgData", "创建外表失败");
			return map;
		}
		try {
			//使用lambda实现runnable接口
			Runnable task = ()->{
				logger.info(Thread.currentThread().getName()+"创建了一个新的线程执行,创建内表");
				try {
					logger.info(" exec cmd :"+BoncConstant.SHELL_CMD_INNER);
					String exec = TestShell.exec(BoncConstant.SHELL_CMD_INNER+filePath2);
					if(exec!=null&&!"".equals(exec)&&exec.contains("1111"))
						throw new Exception();
					logger.info(exec);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//调用submit传递线程任务，开启线程
			};
			es.submit(task);
			logger.info("创建内表线程开始");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			es.shutdown();
			map.put("msgCode", "1111");
			map.put("msgData", "创建内表失败");
			return map;
		}
		Thread.sleep(500);
		map.put("msgCode", "0000");
		map.put("msgData", "建表入库成功!接口名:"+Arrays.toString(tables)+",共计:"+tables.length+"个接口");
		map.put("filePath", filePath);
		map.put("filePath2", filePath2);
		return map;*/
		/*String [] tables = param.getTables();
		String dbType = param.getDbType();
		return factory.selectDb(dbType).insetDb(tables,dbType);*/
	//}
	
	/*@ResponseBody
	@RequestMapping(value="/tmpToSave",method = RequestMethod.POST)
	@Transactional
    public Map<String,String> tmpToSave(@RequestBody(required=false) ParamEntity param) {
		String ds = "";
		Map<String,String> map = new HashMap<String,String>();
		String [] tables = param.getTables();
		if(tables==null||tables.length<1){
			map.put("msgData", "接口名不能为空");
			map.put("dataSrcAbbr", ds);
			return map;
		}
			
		for(String table:tables) {
			if(!table.contains("-"))
				continue;
			logger.info("导入数据加载算法:"+table);
			String[] split = table.split("-");
			ds = split[0];
			String interNo = split[1];
			String importType = split[2];
			String batchNo = split[3];
			//导入类型:1.新增,2.修改
			if("1".equals(importType)) {
				//导入类型是新增直接插入正式表
				DataInterface2procTmp record = new DataInterface2procTmp();
				record.setDataSrcAbbr(ds);
				record.setDataInterfaceNo(interNo);
				record.setBatchNo(batchNo);
				logger.info(record.toString());
				int tmpToSave = intService.tmpToSaveProc(record);
				logger.info("tmpToSave proc success,num:"+tmpToSave);
				if(tmpToSave>0) {
					int delete = intService.deleteProc(record);
					logger.info("delete proc tmp success num:"+delete);
				}else {
					map.put("msgData", "导入失败");
					map.put("dataSrcAbbr", ds);
					return map;
				}
			}else if("2".equals(importType)) {
				//导入类型修改先将正式表原记录置为失效
				DataInterface2proc record = new DataInterface2proc();
				record.setDataSrcAbbr(ds);
				record.setDataInterfaceNo(interNo);
				record.setsDate(TimeUtil.getTw());
				record.seteDate(TimeUtil.getTy());
				logger.info(record.toString());
				int update = intService.updateProc(record);
				logger.info("update proc success,num:"+update);
				if(update>=0) {
					//再从临时表插入新记录到正式表
					DataInterface2procTmp record2 = new DataInterface2procTmp();
					record2.setDataSrcAbbr(ds);
					record2.setDataInterfaceNo(interNo);
					record2.setBatchNo(batchNo);
					logger.info(record.toString());
					int tmpToSave = intService.tmpToSaveProc(record2);
					logger.info("tmpToSave proc success,num:"+tmpToSave);
					if(tmpToSave>0) {
						int delete = intService.deleteProc(record2);
						logger.info("delete proc tmp success,num:"+delete);
					}else {
						map.put("msgData", "导入失败");
						map.put("dataSrcAbbr", ds);
						return map;
					}
				}else {
					map.put("msgData", "导入失败");
					map.put("dataSrcAbbr", ds);
					return map;
				}
			}else if("3".equals(importType)) {
				DataInterface2procTmp record2 = new DataInterface2procTmp();
				record2.setDataSrcAbbr(ds);
				record2.setDataInterfaceNo(interNo);
				record2.setBatchNo(batchNo);
				int delete = intService.deleteProc(record2);
				logger.info("delete proc tmp success,num:"+delete);
			}
		}
		map.put("msgData", "导入成功");
		map.put("dataSrcAbbr", ds);
		return map;
    }*/
	

}
