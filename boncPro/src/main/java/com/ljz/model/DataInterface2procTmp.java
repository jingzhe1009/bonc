package com.ljz.model;

import java.io.Serializable;
import java.util.Date;

public class DataInterface2procTmp implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String dataSrcAbbr;

    private String dataInterfaceNo;
	
    private String procDatabaseName;
    
    private String procName;
    
    private Date sDate;

    private Date eDate;
    
    private String importType;
	 
	private String batchNo;

	public String getProcDatabaseName() {
		return procDatabaseName;
	}

	public void setProcDatabaseName(String procDatabaseName) {
		this.procDatabaseName = procDatabaseName;
	}

	public String getProcName() {
		return procName;
	}

	public void setProcName(String procName) {
		this.procName = procName;
	}

	public String getDataSrcAbbr() {
		return dataSrcAbbr;
	}

	public void setDataSrcAbbr(String dataSrcAbbr) {
		this.dataSrcAbbr = dataSrcAbbr;
	}

	public String getDataInterfaceNo() {
		return dataInterfaceNo;
	}

	public void setDataInterfaceNo(String dataInterfaceNo) {
		this.dataInterfaceNo = dataInterfaceNo;
	}

	public Date getsDate() {
		return sDate;
	}

	public void setsDate(Date sDate) {
		this.sDate = sDate;
	}

	public Date geteDate() {
		return eDate;
	}

	public void seteDate(Date eDate) {
		this.eDate = eDate;
	}
	
	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	@Override
	public String toString() {
		return "DataInterface2proc [dataSrcAbbr=" + dataSrcAbbr + ", dataInterfaceNo=" + dataInterfaceNo
				+ ", procDatabaseName=" + procDatabaseName + ", procName=" + procName + ", sDate=" + sDate + ", eDate="
				+ eDate + "]";
	}
	
	public String toStr() {
		return dataSrcAbbr + dataInterfaceNo + procDatabaseName  + procName;
	}

	

	
}