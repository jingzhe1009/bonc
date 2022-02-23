package com.ljz.service;


public interface ICreateSql{
	
	public String create(String [] tables,String dbType);
	
	public String createSqlAndFile(String [] tables,String dbType);
	
	public String insetDb(String [] tables,String dbType);

}
