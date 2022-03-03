package com.ljz.model;

import java.io.Serializable;
import java.util.Date;

public class DataInterfaceHistory extends DataInterfaceHistoryKey implements Serializable {
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
    
    private String flag;
    
    private String red;

    private static final long serialVersionUID = 1L;

    public String getDataInterfaceDesc() {
        return dataInterfaceDesc;
    }

    public void setDataInterfaceDesc(String dataInterfaceDesc) {
        this.dataInterfaceDesc = dataInterfaceDesc == null ? null : dataInterfaceDesc.trim();
    }

    public String getDataLoadFreq() {
        return dataLoadFreq;
    }

    public void setDataLoadFreq(String dataLoadFreq) {
        this.dataLoadFreq = dataLoadFreq == null ? null : dataLoadFreq.trim();
    }

    public String getDataLoadMthd() {
        return dataLoadMthd;
    }

    public void setDataLoadMthd(String dataLoadMthd) {
        this.dataLoadMthd = dataLoadMthd == null ? null : dataLoadMthd.trim();
    }

    public String getFiledDelim() {
        return filedDelim;
    }

    public void setFiledDelim(String filedDelim) {
        this.filedDelim = filedDelim == null ? null : filedDelim.trim();
    }

    public String getLineDelim() {
        return lineDelim;
    }

    public void setLineDelim(String lineDelim) {
        this.lineDelim = lineDelim == null ? null : lineDelim.trim();
    }

    public String getExtrnlDatabaseName() {
        return extrnlDatabaseName;
    }

    public void setExtrnlDatabaseName(String extrnlDatabaseName) {
        this.extrnlDatabaseName = extrnlDatabaseName == null ? null : extrnlDatabaseName.trim();
    }

    public String getIntrnlDatabaseName() {
        return intrnlDatabaseName;
    }

    public void setIntrnlDatabaseName(String intrnlDatabaseName) {
        this.intrnlDatabaseName = intrnlDatabaseName == null ? null : intrnlDatabaseName.trim();
    }

    public String getExtrnlTableName() {
        return extrnlTableName;
    }

    public void setExtrnlTableName(String extrnlTableName) {
        this.extrnlTableName = extrnlTableName == null ? null : extrnlTableName.trim();
    }

    public String getIntrnlTableName() {
        return intrnlTableName;
    }

    public void setIntrnlTableName(String intrnlTableName) {
        this.intrnlTableName = intrnlTableName == null ? null : intrnlTableName.trim();
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType == null ? null : tableType.trim();
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

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getRed() {
		return red;
	}

	public void setRed(String red) {
		this.red = red;
	}
	
	
    
    
}