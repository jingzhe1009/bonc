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
public class TDHCreateSqlImpl extends CreateSqlImpl implements ICreateSql{

	@Autowired
	TableListMapper mapper;
	@Autowired
	DbInfoMapper dbInfo;
	@Autowired
	JdbcDao jdbc;
	
	/*@Override
	public String create(String [] tables){
		// TODO Auto-generated method stub
		
		StringBuffer sb = new StringBuffer();
		DbInfo info = dbInfo.selectByPrimaryKey("thd");
		
		for(String tableName : tables) {
			String sql = common(tableName,info);
			sb.append(sql);
		}
		
		System.out.println(sb.toString());
		return sb.toString();
	}*/
	
	
	/**
	 *  ORC事务表相对与Inceptor中的其他表支持更多CRUD（增删改）语法，ORC事务表的建表则需要几个
		额外的重点步骤：
		为表分桶：为了保证增删改过程中的性能，我们要求ORC事务表必须是部分排序或者全局排序
		的，但是全局排序又过于耗费计算资源，因此我们要求ORC表必须是分桶表。
		在 TBLPROPERTIES 里需要加上 "transactional"="true"，以标识这是一个要用作事务操作的
		表。
		如果表的数据量特别大，建议在分桶的基础上再分区，ORC事务表支持单值分区和范围分区。
	 * @param tableName
	 * @param info
	 * @return
	 */
	String common(String tableName,DbInfo info) {
		String tableCName = "";
		StringBuffer sb = new StringBuffer();
		Map<String,String> condition = new HashMap<String,String>();
		condition.put("table_ename", tableName);
		List<TableList> list = mapper.selectDetailPage(condition);
		sb.append("CREATE TABLE "+info.getDbName().toUpperCase()+"."+tableName.toUpperCase()+"(\n");
		for(int i=0;i<list.size();i++) {
			TableList t = list.get(i);
			tableCName = t.getTableCname();
			String ename = SqlUtil.dealEName(t.getEname());
			//循环字段+类型+是否为空+注释
			sb.append(ename+SqlUtil.dealType(t.getColumnType())+SqlUtil.dealRequired(t.getNullFlag())+" COMMENT '"+t.getCname()+"'");
			if(i==list.size()-1) {
				sb.append("\n");
			}else {
				sb.append(",\n");
			}
		}
		//表注释
		sb.append(") COMMENT '"+tableCName+"';\n");
		sb.append("STORED AS ORC;\n");
		return sb.toString();
	}

	/*@Override
	@Transactional
	public String insetDb(String [] tables) {
		// TODO Auto-generated method stub
		//创建表结构
		DbInfo info = dbInfo.selectByPrimaryKey("tdh");
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
		return "建表入库成功!表名:"+Arrays.toString(tables)+",入库tdh数据库,库名:"+info.getDbName()+",共计:"+tables.length;
		
	}

	@Override
	@Transactional
	public String createSqlAndFile(String [] tables) {
		// TODO Auto-generated method stub
		String filePath = "C:\\Users\\byan\\Desktop\\dest\\"+FileUtil.formatDate(new Date())+"mysql.txt";
		//创建表结构
		String sql = create(tables);
		FileUtil.write(filePath, sql);
		return sql;
	}*/

	

}
