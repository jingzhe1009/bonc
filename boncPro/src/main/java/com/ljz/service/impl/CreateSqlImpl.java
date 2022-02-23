package com.ljz.service.impl;

import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ljz.config.InfoConfig;
import com.ljz.dao.JdbcDao;
import com.ljz.mapper.DbInfoMapper;
import com.ljz.model.DbInfo;
import com.ljz.model.Order;
import com.ljz.service.ICreateSql;
import com.ljz.util.FileUtil;
import com.ljz.util.TestProduceReturn;

@Service
public abstract  class CreateSqlImpl implements ICreateSql{
	
	@Autowired
	DbInfoMapper dbInfo;
	@Autowired
	JdbcDao jdbc;
	@Autowired
	InfoConfig config;
	@Autowired
	TestProduceReturn testProduce;
	
	abstract String common(String tableName,DbInfo info);

	@Override
	public String create(String [] tables,String dbType){
		// TODO Auto-generated method stub
		
		StringBuffer sb = new StringBuffer();
		DbInfo info = dbInfo.selectByPrimaryKey(dbType);
		
		for(String tableName : tables) {
			String sql = common(tableName,info);
			sb.append(sql);
		}
		
//		System.out.println(sb.toString());
		return sb.toString();
	}

	@Override
	@Transactional
	public String insetDb(String [] tables,String dataSrcAbbr) {
		String filePath = config.getFilePath()+FileUtil.formatDate(new Date())+dataSrcAbbr+".txt";
		System.out.println("filepath:"+filePath);
		DbInfo info = dbInfo.selectByPrimaryKey("tdh");
		//创建表结构
		String sql = "";
		/*if("tdh".equals(dbType)) {*/
		String addSql1 = "";
		for(String table:tables) {
			Order order = testProduce.getSql(table,dataSrcAbbr);
			addSql1 =  order.getSql1() +"\n"+addSql1;
		}
		sql = addSql1;
		return "建表入库成功!表名:"+Arrays.toString(tables)+",入库thd数据库,库名:"+info.getDbName()+",共计:"+tables.length+"个表";
		// TODO Auto-generated method stub
		/*if("tdh".equals(dbType)) {
			DbInfo info = dbInfo.selectByPrimaryKey("tdh");
			String addSql = "";
			for(String table:tables) {
				Order order = testProduce.getSql(table);
				addSql =  order.getName() +"\n"+addSql;
			}
			return "建表入库成功!表名:"+Arrays.toString(tables)+",入库"+dbType+"数据库,库名:"+info.getDbName()+",共计:"+tables.length+"个表";
		}else {
			//创建表结构
			DbInfo info = dbInfo.selectByPrimaryKey("mysql");
			//创建使用单个线程的线程池
	        ExecutorService es = Executors.newFixedThreadPool(10);
			
			for(String tableName : tables) {
				try {
					//使用lambda实现runnable接口
				    Runnable task = ()->{
				    	String sql = common(tableName,info);
				    	System.out.println(Thread.currentThread().getName()+"创建了一个新的线程执行"+tableName);
				    	//入库
						jdbc.insertDb(sql, info.getDriver(), info.getUrl(), 
								info.getUsername(), info.getPassword());
						//调用submit传递线程任务，开启线程
				    };
				    es.submit(task);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					es.shutdown();
					return tableName+"建表入库失败";
				}
			}
			
			return "建表入库成功!表名:"+Arrays.toString(tables)+",入库"+dbType+"数据库,库名:"+info.getDbName()+",共计:"+tables.length+"个表";
		}*/
		
		
	}

	@Override
	@Transactional
	public String createSqlAndFile(String [] tables,String dataSrcAbbr) {
		// TODO Auto-generated method stub
		String filePath = config.getFilePath()+FileUtil.formatDate(new Date())+dataSrcAbbr+".txt";
		/*System.out.println("filepath:"+filePath);
		//创建表结构
		String sql = "";
		if("tdh".equals(dbType)) {
		String addSql1 = "";
		for(String table:tables) {
			Order order = testProduce.getSql(table,dataSrcAbbr);
			addSql1 =  order.getSql1() +"\n"+addSql1;
		}
		sql = addSql1;
		}else {
			sql = create(tables,dbType);
		}
		  
		FileUtil.write(filePath, sql);*/
		return "文件生成路径:"+filePath;
	}

}
