package com.ljz.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ljz.dao.JdbcDao;
import com.ljz.mapper.DbInfoMapper;
import com.ljz.mapper.TableListMapper;
import com.ljz.model.DbInfo;
import com.ljz.model.TableList;
import com.ljz.service.ICreateSql;
import com.ljz.util.SqlUtil;

@Service
public class TeraDataCreateSqlImpl extends CreateSqlImpl implements ICreateSql{

	@Autowired
	TableListMapper mapper;
	@Autowired
	DbInfoMapper dbInfo;
	@Autowired
	JdbcDao jdbc;
	
	/*@Override
	public String create(String [] tables) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		DbInfo info = dbInfo.selectByPrimaryKey("td");
		
		for(String tableName : tables) {
			String sql = common(tableName, info);
			sb.append(sql);
		}
		
		System.out.println(sb.toString());
		return sb.toString();
	}*/
	
	String common(String tableName,DbInfo info) {
		StringBuffer sb = new StringBuffer();
		StringBuffer pk = new StringBuffer();
		Map<String,String> condition = new HashMap<String,String>();
		condition.put("table_ename", tableName);
		List<TableList> list = mapper.selectDetailPage(condition);
		sb.append("CREATE MULTISET TABLE "+info.getDbName().toUpperCase()+"."+tableName.toUpperCase()+",\n");
		sb.append("NO FALLBACK,\n");
		sb.append("NO BEFORE JOURNAL,\n");
		sb.append("NO AFTER JOURNAL,\n");
		sb.append("CHECKSUM = DEFAULT,\n");
		sb.append("DEFAULT MERGEBLOCKRATIO(\n");
		//循环字段+类型+是否为空+注释
		for(int i =0;i<list.size();i++) {
			TableList t = list.get(i);
			String ename = SqlUtil.dealEName(t.getEname());
			String type = SqlUtil.dealTdType(t.getColumnType());
			String cname = t.getCname();
			if(i==list.size()-1) {
				sb.append(ename+" "+type+" TITLE '"+cname+"')\n");
			}else {
				sb.append(ename+" "+type+" TITLE '"+cname+"',\n");
			}
			//主键处理
			if("Y".equals(t.getPkFlag())) {
				pk.append(ename+",");
			}
		}
		//主键
		if(pk==null||"".equals(pk.toString())) {
			sb.append(";\n");
		}else {
			String index =pk.toString();
			if(",".equals(index.substring(index.length()-1))) {
				index = index.substring(0,index.length()-1);
			}
			sb.append(" PRIMARY INDEX ("+index+");\n");
		}
		//表注释
		//sb.append(") TITLE '"+tableCName+"';\n");
		return sb.toString();
	}


	/*@Override
	@Transactional
	public String insetDb(String [] tables) {
		// TODO Auto-generated method stub
		//创建表结构
		DbInfo info = dbInfo.selectByPrimaryKey("td");
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
		return "建表入库成功!表名:"+Arrays.toString(tables)+",入库td数据库,库名:"+info.getDbName()+",共计:"+tables.length;
		
	}

	@Override
	@Transactional
	public String createSqlAndFile(String [] tables) {
		// TODO Auto-generated method stub
		String sql = create(tables);
		String filePath = "C:\\Users\\byan\\Desktop\\dest\\"+FileUtil.formatDate(new Date())+"teraDate.txt";
		FileUtil.write(filePath, sql);
		return null;
	}*/


	

}
