package com.ljz.model;

import java.io.Serializable;
import java.util.Date;

public class DataInterface  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String needVrsnNbr;
	
	private String expt_seq_nbr;
	 
	private String dataSrcAbbr;

	private String dataInterfaceNo;
	
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
    
    private String num;
    
    private String condition;



    public String getDataSrcAbbr() {
        return dataSrcAbbr;
    }

    public void setDataSrcAbbr(String dataSrcAbbr) {
        this.dataSrcAbbr = dataSrcAbbr == null ? null : dataSrcAbbr.trim();
    }

    public String getDataInterfaceNo() {
        return dataInterfaceNo;
    }

    public void setDataInterfaceNo(String dataInterfaceNo) {
        this.dataInterfaceNo = dataInterfaceNo == null ? null : dataInterfaceNo.trim();
    }

    public String getDataInterfaceName() {
        return dataInterfaceName;
    }

    public void setDataInterfaceName(String dataInterfaceName) {
        this.dataInterfaceName = dataInterfaceName == null ? null : dataInterfaceName.trim();
    }

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
	
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}
	
	

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		return "DataInterface [dataInterfaceName=" + dataInterfaceName + ", dataInterfaceDesc=" + dataInterfaceDesc
				+ ", dataLoadFreq=" + dataLoadFreq + ", dataLoadMthd=" + dataLoadMthd + ", filedDelim=" + filedDelim
				+ ", lineDelim=" + lineDelim + ", extrnlDatabaseName=" + extrnlDatabaseName + ", intrnlDatabaseName="
				+ intrnlDatabaseName + ", extrnlTableName=" + extrnlTableName + ", intrnlTableName=" + intrnlTableName
				+ ", tableType=" + tableType + ", bucketNumber=" + bucketNumber + ", sDate=" + sDate + ", eDate="
				+ eDate + ", dataSrcAbbr=" + dataSrcAbbr + ", dataInterfaceNo=" + dataInterfaceNo + "]";
	}
	
	public String toStr() {
		return dataSrcAbbr +  dataInterfaceNo +dataInterfaceName + dataInterfaceDesc + dataLoadFreq + dataLoadMthd + filedDelim+
				lineDelim + extrnlDatabaseName + intrnlDatabaseName + extrnlTableName + intrnlTableName+ tableType + bucketNumber;
	}

	public DataInterface(String dataLoadFreq, String dataLoadMthd, String filedDelim, String lineDelim,
			String extrnlDatabaseName, String intrnlDatabaseName, String extrnlTableName, String intrnlTableName,
			String tableType) {
		this.dataLoadFreq = dataLoadFreq;
		this.dataLoadMthd = dataLoadMthd;
		this.filedDelim = filedDelim;
		this.lineDelim = lineDelim;
		this.extrnlDatabaseName = extrnlDatabaseName;
		this.intrnlDatabaseName = intrnlDatabaseName;
		this.extrnlTableName = extrnlTableName;
		this.intrnlTableName = intrnlTableName;
		this.tableType = tableType;
	}
	
	

	public DataInterface(String dataInterfaceName, String dataInterfaceDesc, String dataLoadFreq, String dataLoadMthd,
			String filedDelim, String lineDelim, String extrnlDatabaseName, String intrnlDatabaseName,
			String extrnlTableName, String intrnlTableName, String tableType, Integer bucketNumber, Date sDate,
			Date eDate, String dataSrcAbbr, String dataInterfaceNo) {
		super();
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
		this.sDate = sDate;
		this.eDate = eDate;
		this.dataSrcAbbr = dataSrcAbbr;
		this.dataInterfaceNo = dataInterfaceNo;
	}

	public DataInterface() {
		super();
	}
	
	

	
    
}