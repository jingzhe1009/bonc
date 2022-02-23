package com.ljz.service.factory;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ljz.service.ICreateSql;
import com.ljz.service.impl.MysqlCreateSqlImpl;
import com.ljz.service.impl.TDHCreateSqlImpl;
import com.ljz.service.impl.TeraDataCreateSqlImpl;

@Service
public class CreateSqlFactory {

	
	@Autowired
	MysqlCreateSqlImpl mysqlImpl;
	@Autowired
	TeraDataCreateSqlImpl teraDataImpl;
	@Autowired
	TDHCreateSqlImpl tdhImpl;


	public ICreateSql selectDb(String dbType) {
		// TODO Auto-generated method stub
		//创建sql
		switch(dbType) {
			case "mysql" : 
				return mysqlImpl;
			case "td" : 
				return teraDataImpl;
			case "tdh" : 
				return tdhImpl;
			default :
				return mysqlImpl;
		}
		
	}

}
