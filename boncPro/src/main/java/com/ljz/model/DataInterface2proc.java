package com.ljz.model;

import java.io.Serializable;
import java.util.Date;

public class DataInterface2proc  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String dataSrcAbbr;

    private String dataInterfaceNo;
	
    private String procDatabaseName;
    
    private String procName;
    
    private Date sDate;

    private Date eDate;
    
    private String num;

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
	

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
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