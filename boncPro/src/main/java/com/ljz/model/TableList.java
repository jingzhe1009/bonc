package com.ljz.model;

import java.io.Serializable;

public class TableList implements Serializable {
	
	private String tableEname;

    private String tableCname;
    
    private String ename;

    private String cname;

    private String pkFlag;

    private String columnType;

    private String nullFlag;

    private String createTime;

    private String createUser;

    private String batchNo;

    private static final long serialVersionUID = 1L;

    public String getTableCname() {
        return tableCname;
    }

    public void setTableCname(String tableCname) {
        this.tableCname = tableCname == null ? null : tableCname.trim();
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname == null ? null : cname.trim();
    }

    public String getPkFlag() {
        return pkFlag;
    }

    public void setPkFlag(String pkFlag) {
        this.pkFlag = pkFlag == null ? null : pkFlag.trim();
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType == null ? null : columnType.trim();
    }

    public String getNullFlag() {
        return nullFlag;
    }

    public void setNullFlag(String nullFlag) {
        this.nullFlag = nullFlag == null ? null : nullFlag.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo == null ? null : batchNo.trim();
    }

	public String getTableEname() {
		return tableEname;
	}

	public void setTableEname(String tableEname) {
		this.tableEname = tableEname;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public TableList(String tableEname, String tableCname, String ename, String cname, String pkFlag, String columnType,
			String nullFlag, String createTime, String createUser, String batchNo) {
		super();
		this.tableEname = tableEname;
		this.tableCname = tableCname;
		this.ename = ename;
		this.cname = cname;
		this.pkFlag = pkFlag;
		this.columnType = columnType;
		this.nullFlag = nullFlag;
		this.createTime = createTime;
		this.createUser = createUser;
		this.batchNo = batchNo;
	}

	@Override
	public String toString() {
		return "TableList [tableEname=" + tableEname + ", tableCname=" + tableCname + ", ename=" + ename + ", cname="
				+ cname + ", pkFlag=" + pkFlag + ", columnType=" + columnType + ", nullFlag=" + nullFlag
				+ ", createTime=" + createTime + ", createUser=" + createUser + ", batchNo=" + batchNo + "]";
	}

	
	
	
    
    
}