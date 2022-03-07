package com.ljz.entity;

import java.util.Arrays;

public class ParamEntity {
	
	String dbType;
	String [] tables;
	String dataSrcAbbr;
	String [] param;
	String [] desc;
	String [] funcType;
	String needVrsnNbr;
	String batchNo;
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String[] getTables() {
		return tables;
	}
	public void setTables(String[] tables) {
		this.tables = tables;
	}
	public String getDataSrcAbbr() {
		return dataSrcAbbr;
	}
	public void setDataSrcAbbr(String dataSrcAbbr) {
		this.dataSrcAbbr = dataSrcAbbr;
	}
	public String[] getParam() {
		return param;
	}
	public void setParam(String[] param) {
		this.param = param;
	}
	public String[] getDesc() {
		return desc;
	}
	public void setDesc(String[] desc) {
		this.desc = desc;
	}
	
	public String[] getFuncType() {
		return funcType;
	}
	public void setFuncType(String[] funcType) {
		this.funcType = funcType;
	}
	
	public String getNeedVrsnNbr() {
		return needVrsnNbr;
	}
	public void setNeedVrsnNbr(String needVrsnNbr) {
		this.needVrsnNbr = needVrsnNbr;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	@Override
	public String toString() {
		return "ParamEntity [dbType=" + dbType + ", tables=" + Arrays.toString(tables) + ", dataSrcAbbr=" + dataSrcAbbr
				+ ", param=" + Arrays.toString(param) + ", desc=" + Arrays.toString(desc) + "]";
	}
	
	
	
	
}
