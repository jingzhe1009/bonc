package com.ljz.model;

import java.io.Serializable;
import java.util.Date;

public class DataInterfaceTmp implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String importType;
	 
	private String batchNo;
	
	private String dataInterfaceName;

    private String dataInterfaceDesc;

    private String dataLoadFreq;

    private String dataLoadMthd;

    private String filedDelim;

    private String lineDelim;

    private String extrnlDatabaseName;

    private String intrnlDatabaseName;

    private String extrnlTableName;

    private String intrnlTableName;

    private String tableType;

    private Integer bucketNumber;

    private Date sDate;

    private Date eDate;

    private String dataSrcAbbr;

    private String dataInterfaceNo;
    
    private String condition;
	 
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
	

	public String getDataInterfaceName() {
		return dataInterfaceName;
	}

	public void setDataInterfaceName(String dataInterfaceName) {
		this.dataInterfaceName = dataInterfaceName;
	}

	public String getDataInterfaceDesc() {
		return dataInterfaceDesc;
	}

	public void setDataInterfaceDesc(String dataInterfaceDesc) {
		this.dataInterfaceDesc = dataInterfaceDesc;
	}

	public String getDataLoadFreq() {
		return dataLoadFreq;
	}

	public void setDataLoadFreq(String dataLoadFreq) {
		this.dataLoadFreq = dataLoadFreq;
	}

	public String getDataLoadMthd() {
		return dataLoadMthd;
	}

	public void setDataLoadMthd(String dataLoadMthd) {
		this.dataLoadMthd = dataLoadMthd;
	}

	public String getFiledDelim() {
		return filedDelim;
	}

	public void setFiledDelim(String filedDelim) {
		this.filedDelim = filedDelim;
	}

	public String getLineDelim() {
		return lineDelim;
	}

	public void setLineDelim(String lineDelim) {
		this.lineDelim = lineDelim;
	}

	public String getExtrnlDatabaseName() {
		return extrnlDatabaseName;
	}

	public void setExtrnlDatabaseName(String extrnlDatabaseName) {
		this.extrnlDatabaseName = extrnlDatabaseName;
	}

	public String getIntrnlDatabaseName() {
		return intrnlDatabaseName;
	}

	public void setIntrnlDatabaseName(String intrnlDatabaseName) {
		this.intrnlDatabaseName = intrnlDatabaseName;
	}

	public String getExtrnlTableName() {
		return extrnlTableName;
	}

	public void setExtrnlTableName(String extrnlTableName) {
		this.extrnlTableName = extrnlTableName;
	}

	public String getIntrnlTableName() {
		return intrnlTableName;
	}

	public void setIntrnlTableName(String intrnlTableName) {
		this.intrnlTableName = intrnlTableName;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public Integer getBucketNumber() {
		return bucketNumber;
	}

	public void setBucketNumber(Integer bucketNumber) {
		this.bucketNumber = bucketNumber;
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
	
	

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public DataInterfaceTmp(String batchNo, String dataInterfaceName, String dataInterfaceDesc, String dataLoadFreq,
			String dataLoadMthd, String filedDelim, String lineDelim, String extrnlDatabaseName,
			String intrnlDatabaseName, String extrnlTableName, String intrnlTableName, String tableType,
			Integer bucketNumber,String dataSrcAbbr, String dataInterfaceNo) {
		super();
		this.batchNo = batchNo;
		this.dataInterfaceName = dataInterfaceName;
		this.dataInterfaceDesc = dataInterfaceDesc;
		this.dataLoadFreq = dataLoadFreq;
		this.dataLoadMthd = dataLoadMthd;
		this.filedDelim = filedDelim;
		this.lineDelim = lineDelim;
		this.extrnlDatabaseName = extrnlDatabaseName;
		this.intrnlDatabaseName = intrnlDatabaseName;
		this.extrnlTableName = extrnlTableName;
		this.intrnlTableName = intrnlTableName;
		this.tableType = tableType;
		this.bucketNumber = bucketNumber;
		this.dataSrcAbbr = dataSrcAbbr;
		this.dataInterfaceNo = dataInterfaceNo;
	}

	public DataInterfaceTmp() {
		super();
	}

	@Override
	public String toString() {
		return "DataInterfaceTmp [importType=" + importType + ", batchNo=" + batchNo + ", dataInterfaceName="
				+ dataInterfaceName + ", dataInterfaceDesc=" + dataInterfaceDesc + ", dataLoadFreq=" + dataLoadFreq
				+ ", dataLoadMthd=" + dataLoadMthd + ", filedDelim=" + filedDelim + ", lineDelim=" + lineDelim
				+ ", extrnlDatabaseName=" + extrnlDatabaseName + ", intrnlDatabaseName=" + intrnlDatabaseName
				+ ", extrnlTableName=" + extrnlTableName + ", intrnlTableName=" + intrnlTableName + ", tableType="
				+ tableType + ", bucketNumber=" + bucketNumber + ", sDate=" + sDate + ", eDate=" + eDate
				+ ", dataSrcAbbr=" + dataSrcAbbr + ", dataInterfaceNo=" + dataInterfaceNo + "]";
	}

	public String toStr() {
		return dataInterfaceDesc + dataLoadFreq + dataLoadMthd + filedDelim+
				lineDelim + extrnlDatabaseName + intrnlDatabaseName + extrnlTableName + intrnlTableName+ tableType + bucketNumber;
	}
	
    
}