package com.ljz.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ljz.config.InfoConfig;
import com.ljz.constant.BoncConstant;
import com.ljz.entity.ParamEntity;
import com.ljz.mapper.DataInterface2procMapper;
import com.ljz.mapper.DataInterfaceMapper;
import com.ljz.model.DataInterface;
import com.ljz.model.DataInterface2proc;
import com.ljz.model.DataInterface2procTmp;
import com.ljz.model.DataInterfaceTmp;
import com.ljz.model.Order;
import com.ljz.service.IDataInterfaceService;
import com.ljz.util.ExcelUtil;
import com.ljz.util.FileUtil;
import com.ljz.util.InsertDbProduceReturn;
import com.ljz.util.TestProduceReturn;
import com.ljz.util.TimeUtil;

@Service
public class DataInterfaceServiceImpl implements IDataInterfaceService{

	@Autowired
	DataInterfaceMapper mapper;
	
	@Autowired
	DataInterface2procMapper procMapper;
	
	@Autowired
	JdbcTemplate jdbc;
	
	@Autowired
	TestProduceReturn testProduce;
	
	@Autowired
	InsertDbProduceReturn dbProduce;
	
	@Autowired
	InfoConfig config;
	
	private static final Logger logger = LoggerFactory.getLogger(DataInterfaceServiceImpl.class);

	
	@Override
	public List<DataInterface> queryAll(DataInterface record) {
		// TODO Auto-generated method stub
		return mapper.queryAll(record);
	}

	@Override
	public List<DataInterface> queryByList(String ds, String [] array) {
		// TODO Auto-generated method stub
		return mapper.queryByList(ds, array);
	}

	@Override
	public int insert(DataInterface record) {
		// TODO Auto-generated method stub
		return mapper.insertSelective(record);
	}

	@Override
	public int update(DataInterface record) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int delete(DataInterfaceTmp record) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(record);
	}

	@Override
	public List<DataInterface2proc> queryProc(DataInterface2proc record) {
		// TODO Auto-generated method stub
		return procMapper.queryAll(record);
	}

	@Override
	public int insertProc(DataInterface2proc record) {
		// TODO Auto-generated method stub
		return procMapper.insertSelective(record);
	}

	@Override
	public int updateProc(DataInterface2proc record) {
		// TODO Auto-generated method stub
		return procMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int deleteProc(DataInterface2procTmp record) {
		// TODO Auto-generated method stub
		return procMapper.deleteByPrimaryKey(record);
	}
	
	@Override
	public List<DataInterface> queryAllVersion(DataInterface record) {
		// TODO Auto-generated method stub
		List<DataInterface> list = mapper.queryAllVersion(record);
		List<DataInterface> returnList = new ArrayList<DataInterface>();
		for(int i=0;i<list.size();i++) {
			DataInterface data = list.get(i);
			data.setNum("v1."+(i));
			returnList.add(data);
		}
		return returnList;
	}

	/**
	 * 接口临时表查询
	 */
	@Override
	public List<DataInterfaceTmp> queryAllTmp(DataInterfaceTmp record) {
		// TODO Auto-generated method stub
		List<DataInterfaceTmp> resultList = new ArrayList<DataInterfaceTmp>();
		String dataSrcAbbr = record.getDataSrcAbbr();
		if(dataSrcAbbr!=null&&!"".equals(dataSrcAbbr)){
			logger.info("导入临时表时，数据源是"+record.getDataSrcAbbr());
			ExcelUtil obj = ExcelUtil.getInstance();
			List<DataInterfaceTmp> queryAllTmp = mapper.queryAllTmp(record);
			Map<String,String> interfaceMap =obj.getInterfaceMap(record.getDataSrcAbbr());
			for(DataInterfaceTmp tmp:queryAllTmp){
				String key = tmp.getDataSrcAbbr()+tmp.getDataInterfaceNo()+tmp.getDataInterfaceName();
				if(interfaceMap!=null&&interfaceMap.containsKey(key)){
					if(tmp.toStr().equalsIgnoreCase(interfaceMap.get(key))){//无变化
						tmp.setImportType("3");
					}else{//修改
						tmp.setImportType("2");
					}
				}else{//新增
					tmp.setImportType("1");
				}
				resultList.add(tmp);
			}
		}
		return resultList;
	}

	@Override
	public int tmpToSave(DataInterfaceTmp record) {
		// TODO Auto-generated method stub
		return mapper.tmpToSave(record);
	}

	@Override
	public List<DataInterface2proc> queryAllVersionProc(DataInterface2proc record) {
		// TODO Auto-generated method stub
		List<DataInterface2proc> list = procMapper.queryAllVersion(record);
		List<DataInterface2proc> returnList = new ArrayList<DataInterface2proc>();
		for(int i=0;i<list.size();i++) {
			DataInterface2proc data = list.get(i);
			data.setNum("v1."+(i));
			returnList.add(data);
		}
		return returnList;
	}

	/**
	 * 数据加载算法临时表查询
	 */
	@Override
	public List<DataInterface2procTmp> queryAllTmpProc(DataInterface2procTmp record) {
		// TODO Auto-generated method stub
		List<DataInterface2procTmp> resultList = new ArrayList<DataInterface2procTmp>();
		String dataSrcAbbr = record.getDataSrcAbbr();
		if(dataSrcAbbr!=null&&!"".equals(dataSrcAbbr)){
			logger.info("导入临时表时，数据源是"+record.getDataSrcAbbr());
			ExcelUtil obj = ExcelUtil.getInstance();
			List<DataInterface2procTmp> queryAllTmp = procMapper.queryAllTmp(record);
			Map<String,String> procMap =obj.getProcMap(record.getDataSrcAbbr());
			for(DataInterface2procTmp tmp:queryAllTmp){
				if(procMap!=null&&procMap.containsKey(tmp.getDataSrcAbbr()+tmp.getDataInterfaceNo())){
					if(tmp.toStr().equals(procMap.get(tmp.getDataSrcAbbr()+tmp.getDataInterfaceNo()))){//无变化
						tmp.setImportType("3");
					}else{//修改
						tmp.setImportType("2");
					}
				}else{//新增
					tmp.setImportType("1");
				}
				resultList.add(tmp);
			}
		}
		return resultList;
	}

	@Override
	public int tmpToSaveProc(DataInterface2procTmp record) {
		// TODO Auto-generated method stub
		return procMapper.tmpToSave(record);
	}

	@Override
	public int tmpToSaveBatch(List<DataInterfaceTmp> list) {
		// TODO Auto-generated method stub
		return mapper.tmpToSaveBatch(list);
	}

	@Override
	public int deleteBatch(List<DataInterfaceTmp> list) {
		// TODO Auto-generated method stub
		return mapper.deleteBatch(list);
	}

	@Override
	public int updateBatch(List<DataInterface> list) {
		// TODO Auto-generated method stub
		return mapper.updateBatch(list);
	}

	/**
	 * 数据建模查询
	 */
	@Override
	public List<DataInterface> queryModel(DataInterface record) {
		
		// TODO Auto-generated method stub
		List<DataInterface> queryAll = mapper.queryAll(record);
		//查询接口对应的字段数量
		/*
		 * List<Map<String, Object>> queryIntNum = jdbc.
		 * queryForList("select data_interface_name,count(*) as bucket_number from data_interface_columns "
		 * +
		 * "where data_src_abbr='"+record.getDataSrcAbbr()+"' and e_date='"+BoncConstant
		 * .CON_E_DATE+"'  group by data_interface_name"); //查询外表sdata的表列表
		 * List<Map<String, Object>> sdataList = jdbc.
		 * queryForList("SELECT table_name FROM SYSTEM.tables_v WHERE database_name='"
		 * +BoncConstant.SDATA+"'"); //查询内表odata的表列表 List<Map<String, Object>> odataList
		 * = jdbc.
		 * queryForList("SELECT table_name FROM SYSTEM.tables_v WHERE database_name='"
		 * +BoncConstant.ODATA+"'"); List<DataInterface> resultList = new
		 * ArrayList<DataInterface>(); for(DataInterface data:queryAll){ for(Map<String,
		 * Object> map:queryIntNum){ String data_interface_name = (String)
		 * map.get("data_interface_name"); long bucket_number = (long)
		 * map.get("bucket_number");
		 * if(data_interface_name.equalsIgnoreCase(data.getDataInterfaceName())){
		 * data.setNum(bucket_number+""); } } //创建标识0未创建，1已创建 String flag1 = "0";
		 * for(int i=0;i<sdataList.size();i++){ Map<String, Object> map =
		 * sdataList.get(i); String tb = (String) map.get("table_name");
		 * if(tb.equalsIgnoreCase(data.getExtrnlTableName())){
		 * data.setCondition("外表已创建"); flag1 = "1"; break; }else{
		 * if(i==sdataList.size()-1&&"0".equals(flag1)){ data.setCondition("外表未创建"); } }
		 * } String flag2 = "0"; for(int i=0;i<odataList.size();i++){ Map<String,
		 * Object> map = odataList.get(i); String tb = (String) map.get("table_name");
		 * if(tb.equalsIgnoreCase(data.getIntrnlTableName())){
		 * data.setCondition(data.getCondition()+",内表已创建"); flag2 = "1"; break; }else{
		 * if(i==odataList.size()-1&&"0".equals(flag2)){
		 * data.setCondition(data.getCondition()+",内表未创建"); } } } resultList.add(data);
		 * }
		 */
		return queryAll;
	}

	@Override
	public void batchImport(ParamEntity param) throws Exception{
		// TODO Auto-generated method stub
	}

	/**
	 * 接口批量导入/全部导入
	 */
	@Override
	@Transactional
	public String batchImportFinal(ParamEntity param) throws Exception {
		// TODO Auto-generated method stub
		long start = new Date().getTime();
		String dataSrcAbbr = "";
		//导入批次号
		String batchNo = "";
		List<Object[]> tmpList=new ArrayList<Object[]>();
		List<Object[]> recordList=new ArrayList<Object[]>();
		List<Object[]> delList=new ArrayList<Object[]>();
		String [] tables = param.getTables();
		//methodType 导入方法 1：导入 2：全部导入
		String methodType = param.getDbType();
		for(String table:tables) {
			if(!table.contains("-"))
				continue;
			String[] split = table.split("-");
			if(split.length!=5)
				continue;
			dataSrcAbbr = split[0];
			String dataInterfaceNo = split[1];
			String importType = split[2];
			batchNo = split[3];
			String dataInterfaceName = split[4];
			
			if("1".equals(importType)) {//导入类型是1.新增直接插入正式表，最后删除临时表记录
				tmpList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo,dataInterfaceName});
				delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo,dataInterfaceName});	
			}else if("2".equals(importType)) {
				//导入类型2.修改先将正式表原记录置为失效，在将临时表数据导入正式表，最后删除临时表记录
				recordList.add(new Object[] {new Date(),dataSrcAbbr,dataInterfaceNo,dataInterfaceName,TimeUtil.getTw()});
				tmpList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo,dataInterfaceName});
				delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo,dataInterfaceName});	
			}else if("3".equals(importType)){//导入类型3.无变化不进行操作，直接删除临时表记录
				delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo,dataInterfaceName});	
			}
		}
		logger.info("导入接口开始...数据源=["+dataSrcAbbr+"],批次号=["+batchNo+"],导入方法=["+methodType+"]");
		//修改sql
		String updateSql = "update data_interface set e_date = ? "
				+ "where data_src_abbr = ? and data_interface_no = ? and data_interface_name = ? and e_date >= ? ";
		//临时表insert到正式表sql
		StringBuffer sb = new StringBuffer();
		sb.append("insert into data_interface (data_src_abbr, data_interface_no, data_interface_name,data_interface_desc, ");
		sb.append("data_load_freq, data_load_mthd,filed_delim, line_delim, extrnl_database_name,intrnl_database_name, ");
		sb.append("extrnl_table_name,intrnl_table_name,table_type, bucket_number, s_date, e_date) ");
		sb.append("select tmp.data_src_abbr, tmp.data_interface_no, tmp.data_interface_name,tmp.data_interface_desc, ");
		sb.append("tmp.data_load_freq,tmp.data_load_mthd,tmp.filed_delim, tmp.line_delim, tmp.extrnl_database_name, tmp.intrnl_database_name, ");
		sb.append("tmp.extrnl_table_name,tmp.intrnl_table_name,tmp.table_type, tmp.bucket_number, tmp.s_date, tmp.e_date ");
		sb.append("from data_interface_tmp tmp  ");
		sb.append("where tmp.batch_no = ? and tmp.data_src_abbr = ? and tmp.data_interface_no = ? and data_interface_name = ? ");
		//删除sql
		String delSql = "delete from data_interface_tmp "
				+ "where batch_no = ? and data_src_abbr = ? and data_interface_no = ? and data_interface_name = ? ";
		if("1".equals(methodType)){//导入
			if(recordList.size()>0){
				int[] batchUpdate = jdbc.batchUpdate(updateSql, recordList);
				logger.info("batch update interface success:"+batchUpdate.length);
			}
			if(tmpList.size()>0){
				int[] batchUpdate2 = jdbc.batchUpdate(sb.toString(),tmpList);
				logger.info("batch insert interface from tmp success:"+batchUpdate2.length);
			}
			if(delList.size()>0){
				int[] batchUpdate3 = jdbc.batchUpdate(delSql,delList);
				logger.info("batch delete interface tmp success:"+batchUpdate3.length);
			}
		}else if("2".equals(methodType)){//全部导入
			
			List<Object[]> tmpList2=new ArrayList<Object[]>();
			List<Object[]> recordList2=new ArrayList<Object[]>();
			List<Object[]> delList2=new ArrayList<Object[]>();
			//单例模式获取在导入校验中存放在map缓存中的数据
			ExcelUtil obj = ExcelUtil.getInstance();
			Map<String, String> interfaceMap = obj.getInterfaceMap(dataSrcAbbr);
			List<DataInterface> list = new ArrayList<DataInterface>();
			
			DataInterfaceTmp condition = new DataInterfaceTmp();
			condition.setBatchNo(batchNo);
			List<DataInterfaceTmp> queryAllTmp = mapper.queryAllTmp(condition);
			for(DataInterfaceTmp tmp:queryAllTmp){
				String key = tmp.getDataSrcAbbr()+tmp.getDataInterfaceNo()+tmp.getDataInterfaceName();
				if(interfaceMap!=null&&interfaceMap.containsKey(key)){
					if(!tmp.toStr().equalsIgnoreCase(interfaceMap.get(key))){//修改
						recordList2.add(new Object[] {new Date(),tmp.getDataSrcAbbr(),tmp.getDataInterfaceNo(),tmp.getDataInterfaceName(),TimeUtil.getTw()});
						tmpList2.add(new Object[] {batchNo,dataSrcAbbr,tmp.getDataInterfaceNo(),tmp.getDataInterfaceName()});
					}
				}else{//新增
					DataInterface data = new DataInterface();
					data.setDataSrcAbbr(tmp.getDataSrcAbbr());
					data.setDataInterfaceNo(tmp.getDataInterfaceNo());
					data.setDataInterfaceName(tmp.getDataInterfaceName());
					data.setDataInterfaceDesc(tmp.getDataInterfaceDesc());
					data.setDataLoadFreq(tmp.getDataLoadFreq());
					data.setDataLoadMthd(tmp.getDataLoadMthd());
					data.setFiledDelim(tmp.getFiledDelim());
					data.setLineDelim(tmp.getLineDelim());
					data.setExtrnlDatabaseName(tmp.getExtrnlDatabaseName());
					data.setIntrnlDatabaseName(tmp.getIntrnlDatabaseName());
					data.setExtrnlTableName(tmp.getExtrnlTableName());
					data.setIntrnlTableName(tmp.getIntrnlTableName());
					data.setTableType(tmp.getTableType());
					data.setBucketNumber(tmp.getBucketNumber());
					data.setsDate(tmp.getsDate());
					data.seteDate(tmp.geteDate());
					list.add(data);
				}
			}
			
			if(recordList2.size()>0){
				int[] batchUpdate = jdbc.batchUpdate(updateSql, recordList2);
				logger.info("all batch update interface success:"+batchUpdate.length);
			}
			if(tmpList2.size()>0){
				int[] batchUpdate2 = jdbc.batchUpdate(sb.toString(),tmpList2);
				logger.info("all batch insert interface from tmp success:"+batchUpdate2.length);
			}
			if(list.size()>0){
				int batchInsertPro = mapper.batchInsertPro(list);
				logger.info("all batch insert interface success:"+batchInsertPro);
			}
			//删除临时表该批次下所有记录
			String delSql2 = "delete from data_interface_tmp where batch_no = ? ";
			delList2.add(new Object[]{batchNo});
			int[] batchUpdate3 = jdbc.batchUpdate(delSql2,delList2);
			logger.info("all batch delete interface tmp success:"+batchUpdate3.length);
		}
		long end = new Date().getTime();
		logger.info("导入结束,导入用时："+(end-start));
		return dataSrcAbbr;
	}

	/**
	 * 数据加载算法批量导入/全部导入
	 */
	@Override
	@Transactional
	public String batchImportProcFinal(ParamEntity param) throws Exception {
		// TODO Auto-generated method stub
		List<Object[]> tmpList=new ArrayList<Object[]>();
		List<Object[]> recordList=new ArrayList<Object[]>();
		List<Object[]> delList=new ArrayList<Object[]>();
		String [] tables = param.getTables();
		String dbType = param.getDbType();
		long time1 = new Date().getTime();
		String dataSrcAbbr = "";
		String batchNo = "";
		for(String table:tables) {
			if(!table.contains("-"))
				continue;
			String[] split = table.split("-");
			if(split.length!=4)
				continue;
			dataSrcAbbr = split[0];
			String dataInterfaceNo = split[1];
			String importType = split[2];
			batchNo = split[3];
			
			if("1".equals(importType)) {
				//导入类型是新增直接插入正式表
				tmpList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo});
				delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo});	
			}else if("2".equals(importType)) {
				//导入类型修改先将正式表原记录置为失效
				recordList.add(new Object[] {new Date(),dataSrcAbbr,dataInterfaceNo,TimeUtil.getTw()});
				tmpList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo});
				delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo});	
			}else if("3".equals(importType)){
				delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo});	
			}
		}
		logger.info("导入加载算法开始...dataSrcAbbr=["+dataSrcAbbr+"],batchNo=["+batchNo+"],type=["+param.getDbType()+"]");
		String updateSql = "update data_interface2proc set e_date = ? where data_src_abbr = ? and data_interface_no = ? and e_date >= ? ";
		StringBuffer sb = new StringBuffer();
		sb.append("insert into data_interface2proc (data_src_abbr, data_interface_no,");
		sb.append("proc_database_name,proc_name, ");
		sb.append("s_date, e_date) ");
		sb.append("SELECT tmp.data_src_abbr, tmp.data_interface_no, ");
		sb.append("tmp.proc_database_name,tmp.proc_name, ");
		sb.append("tmp.s_date, tmp.e_date ");
		sb.append("FROM data_interface2proc_tmp tmp  ");
		sb.append("WHERE tmp.batch_no = ? and tmp.data_src_abbr = ? and tmp.data_interface_no = ? ");
		String tmpToSaveSql = sb.toString();
		String delSql = "delete from data_interface2proc_tmp where 1=1 and batch_no = ? and data_src_abbr = ? and data_interface_no = ? ";
		if("1".equals(dbType)){
			if(recordList.size()>0){
				int[] batchUpdate = jdbc.batchUpdate(updateSql, recordList);
				logger.info("batch update proc success:"+batchUpdate.length);
			}
			if(tmpList.size()>0){
				int[] batchUpdate2 = jdbc.batchUpdate(tmpToSaveSql,tmpList);
				logger.info("batch insert proc from tmp success:"+batchUpdate2.length);
			}
			if(delList.size()>0){
				int[] batchUpdate3 = jdbc.batchUpdate(delSql,delList);
				logger.info("batch delete proc tmp success:"+batchUpdate3.length);
			}
		}else if("2".equals(dbType)){
			
			List<Object[]> tmpList2=new ArrayList<Object[]>();
			List<Object[]> recordList2=new ArrayList<Object[]>();
			List<Object[]> delList2=new ArrayList<Object[]>();
			
			ExcelUtil obj = ExcelUtil.getInstance();
			Map<String, String> procMap = obj.getProcMap(dataSrcAbbr);
			List<DataInterface2proc> list = new ArrayList<DataInterface2proc>();
			
			DataInterface2procTmp condition = new DataInterface2procTmp();
			condition.setBatchNo(batchNo);
			List<DataInterface2procTmp> queryAllTmp = procMapper.queryAllTmp(condition);
			for(DataInterface2procTmp tmp:queryAllTmp){
				if(procMap!=null&&procMap.containsKey(tmp.getDataSrcAbbr()+tmp.getDataInterfaceNo())){
					if(!tmp.toStr().equals(procMap.get(tmp.getDataSrcAbbr()+tmp.getDataInterfaceNo()))){
						recordList2.add(new Object[] {new Date(),tmp.getDataSrcAbbr(),tmp.getDataInterfaceNo(),TimeUtil.getTw()});
						tmpList2.add(new Object[] {batchNo,dataSrcAbbr,tmp.getDataInterfaceNo()});
					}
				}else{
					DataInterface2proc data = new DataInterface2proc();
					data.setDataSrcAbbr(tmp.getDataSrcAbbr());
					data.setDataInterfaceNo(tmp.getDataInterfaceNo());
					data.setProcDatabaseName(tmp.getProcDatabaseName());
					data.setProcName(tmp.getProcName());
					data.setsDate(tmp.getsDate());
					data.seteDate(tmp.geteDate());
					list.add(data);
				}
			}
			
			if(recordList2.size()>0){
				int[] batchUpdate = jdbc.batchUpdate(updateSql, recordList2);
				logger.info("all batch update proc success:"+batchUpdate.length);
			}
			if(tmpList2.size()>0){
				int[] batchUpdate2 = jdbc.batchUpdate(tmpToSaveSql,tmpList2);
				logger.info("all batch insert proc from tmp success:"+batchUpdate2.length);
			}
			if(list.size()>0){
				int batchInsertPro = procMapper.batchInsertPro(list);
				logger.info("all batch insert proc success:"+batchInsertPro);
			}
			String delSql2 = "delete from data_interface2proc_tmp where batch_no = ? ";
			delList2.add(new Object[]{batchNo});
			int[] batchUpdate3 = jdbc.batchUpdate(delSql2,delList2);
			logger.info("all batch delete proc tmp success:"+batchUpdate3.length);
		}
		long time2 = new Date().getTime();
		logger.info("导入结束,导入用时："+(time2-time1));
		return dataSrcAbbr;
	}

	/**
	 * 生成建表语句文件
	 */
	@Override
	public Map<String,String> createFile(List<String> list,ParamEntity param) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		List<String> sucList = new ArrayList<String>();
		List<String> failList = new ArrayList<String>();
		//创建使用单个线程的线程池
		ExecutorService es = Executors.newFixedThreadPool(10);
		// TODO Auto-generated method stub
		for(String table:list) {
			//使用lambda实现runnable接口
			Runnable task = ()->{
				long start = new Date().getTime();
				logger.info(Thread.currentThread().getName()+"创建了一个新的线程执行,创建表");
				try {
					Order order = testProduce.getSql(table,param.getDataSrcAbbr());
					sucList.add(order.getSql1()+"\n"+order.getSql2()+"\n");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					failList.add(table);
					e.printStackTrace();
				}
				long end = new Date().getTime();
				logger.info("存储过程用时:"+(end-start)+"毫秒");
			};
			es.submit(task);
			logger.info("创建表线程开始");
		}
		String filePath="";
		try {
			filePath = config.getFilePath()+param.getDataSrcAbbr()+FileUtil.formatDate(new Date())+".sql";
			logger.info("建表文件路径:"+filePath);
			int i =0;
			reWriteFile(list, sucList,failList,filePath, i);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			map.put("msgCode", "1111");
			map.put("msgData", "文件写入失败,文件路径:"+filePath+",文件编码"+config.getFileEncode());
			return map;
		}
		if(failList.size()>0){
			String str = "接口"+failList.toString()+"生成建表语句失败,失败接口数："+failList.size();
			map.put("msgCode", "1111");
			map.put("msgData", str);
			return map;
		}
		//成功
		String str = "建表语句文件生成成功\n路径:"+filePath+"\n共计:"+list.size()+"个接口:"+list.toString()+"\n";
		map.put("msgCode", "0000");
		map.put("idx", param.getDataSrcAbbr());
		map.put("msgData", str);
		map.put("filePath", filePath);
		return map;
	}
	//递归等待写文件，直到存储过程全部执行完，或者超过一定时间
	private void reWriteFile(List<String> list,List<String> sucList,List<String> failList,String filePath,int i) throws Exception{
		i++;
		Thread.sleep(500);
		if(list.size()==sucList.size()+failList.size()){
			String totalSql="";
			for(String sql :sucList){
				totalSql = totalSql+sql;
			}
			if(!"".equals(totalSql)){
				FileUtil.write(filePath, totalSql,config.getFileEncode());
				logger.info("write file success");
			}
		}else{
			if(i>120){
				//超过一分钟,跳出递归
			}else{
				logger.info("waiting for proc running finish"+i);
				reWriteFile(list, sucList,failList,filePath, i);
			}
		}
	}
	
	/**
	 * 物化建模
	 */
	@Override
	public Map<String, String> insertDb(List<String> list,List<String> hasList,ParamEntity param)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		List<String> sucTable = new ArrayList<String>();
		List<String> failTable = new ArrayList<String>();
		//创建使用单个线程的线程池
		ExecutorService es = Executors.newFixedThreadPool(10);
		//创建表
		for(String table:list) {
			//使用lambda实现runnable接口
			Runnable task = ()->{
				logger.info(Thread.currentThread().getName()+"创建了一个新的线程执行,创建表");
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
					logger.info(table+"建表成功");
					sucTable.add(table);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//logger.info(table+"建表失败,1.请查看该表是否有对应字段\n2.是否有分桶字段\n3.建表语句是否有中文符号\n4.建表语句关键字是否有不支持的关键字");
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
		String str = "建表入库执行完成,请关注建表创建状态!本次建表接口名:"+list.toString()+",共计:"+list.size()+"个接口\n";
		if(failTable.size()>0){
			str =str +"其中接口"+failTable.toString()+"创建失败,共计:"+failTable.size()+"\n1.请查看该表是否有重复字段\n2.是否有分桶字段\n3.建表语句是否有中文符号\n4.建表语句是否有不支持的关键字或字段类型\n";
		}
		if(hasList.size()>0){
			str = str +"接口"+hasList.toString()+"已存在,没有执行建表，如需变更，请先删除表";
		}
		map.put("msgData", str);
		map.put("msgCode", "0000");
		map.put("idx", param.getDataSrcAbbr());
		return map;
	}
	
	//递归等待入库建模
	private void complete(List<String> list,List<String> sucTable,List<String> failTable,int i) throws Exception{
		i++;
		Thread.sleep(500);
		if(list.size()==sucTable.size()+failTable.size()){
			//执行的存储过程数量=成功数量+失败数量，跳出递归，完成建模入库
		}else{
			if(i>120){
				//超过一分钟,跳出递归
			}else{
				logger.info("waiting for proc running finish"+i);
				complete(list,sucTable,failTable,i);
			}
		}
	}

}