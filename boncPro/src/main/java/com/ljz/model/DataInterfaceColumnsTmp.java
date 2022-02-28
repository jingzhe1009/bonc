package com.ljz.model;

import java.io.Serializable;
import java.util.Date;

public class DataInterfaceColumnsTmp implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String dataSrcAbbr;

    private String dataInterfaceNo;

    private Integer columnNo;
    
    private String dataInterfaceName;

    private String columnName;

    private String dataType;

    private String dataFormat;

    private Integer nullable;

    private Integer replacenull;

    private String comma;

    private String columnComment;

    private String isbucket;

    private Date sDate;

    private Date eDate;
    
    private String batchNo;
    
    private String importType;
    
    private String condition;

    private String iskey;

    private String isvalid;

    private String incrementfield;

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

    public Integer getColumnNo() {
        return columnNo;
    }

    public void setColumnNo(Integer columnNo) {
        this.columnNo = columnNo;
    }

    public String getDataInterfaceName() {
        return dataInterfaceName;
    }

    public void setDataInterfaceName(String dataInterfaceName) {
        this.dataInterfaceName = dataInterfaceName == null ? null : dataInterfaceName.trim();
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName == null ? null : columnName.trim();
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType == null ? null : dataType.trim();
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat == null ? null : dataFormat.trim();
    }

    public Integer getNullable() {
        return nullable;
    }

    public void setNullable(Integer nullable) {
        this.nullable = nullable;
    }

    public Integer getReplacenull() {
        return replacenull;
    }

    public void setReplacenull(Integer replacenull) {
        this.replacenull = replacenull;
    }

    public String getComma() {
        return comma;
    }

    public void setComma(String comma) {
        this.comma = comma == null ? null : comma.trim();
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment == null ? null : columnComment.trim();
    }

    public String getIsbucket() {
        return isbucket;
    }

    public void setIsbucket(String isbucket) {
        this.isbucket = isbucket == null ? null : isbucket.trim();
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

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

    public String getIskey() {
        return iskey;
    }

    public void setIskey(String iskey) {
        this.iskey = iskey;
    }

    public String getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(String isvalid) {
        this.isvalid = isvalid;
    }

    public String getIncrementfield() {
        return incrementfield;
    }

    public void setIncrementfield(String incrementfield) {
        this.incrementfield = incrementfield;
    }

    @Override
	public String toString() {
		return "DataInterfaceColumnsTmp [dataSrcAbbr=" + dataSrcAbbr + ", dataInterfaceNo=" + dataInterfaceNo
				+ ", columnNo=" + columnNo + ", dataInterfaceName=" + dataInterfaceName + ", columnName=" + columnName
				+ ", dataType=" + dataType + ", dataFormat=" + dataFormat + ", nullable=" + nullable + ", replacenull="
				+ replacenull + ", comma=" + comma + ", columnComment=" + columnComment + ", isbucket=" + isbucket
				+ ", sDate=" + sDate + ", eDate=" + eDate + ", batchNo=" + batchNo + ", importType=" + importType + "]";
	}

	public DataInterfaceColumnsTmp(String dataSrcAbbr, String dataInterfaceNo, Integer columnNo,
			String dataInterfaceName, String columnName, String dataType, String dataFormat, Integer nullable,
			Integer replacenull, String comma, String columnComment, String isbucket, Date sDate, Date eDate) {
		super();
		this.dataSrcAbbr = dataSrcAbbr;
		this.dataInterfaceNo = dataInterfaceNo;
		this.columnNo = columnNo;
		this.dataInterfaceName = dataInterfaceName;
		this.columnName = columnName;
		this.dataType = dataType;
		this.dataFormat = dataFormat;
		this.nullable = nullable;
		this.replacenull = replacenull;
		this.comma = comma;
		this.columnComment = columnComment;
		this.isbucket = isbucket;
		this.sDate = sDate;
		this.eDate = eDate;
	}
	
	public String toStr() {
//		return dataSrcAbbr + dataInterfaceNo + columnNo + dataInterfaceName + columnName + dataType + dataFormat + nullable + replacenull + comma + columnComment + isbucket;
        return dataSrcAbbr + dataInterfaceNo + columnNo + dataInterfaceName + columnName + dataType + dataFormat + nullable +
                replacenull + comma + columnComment + isbucket + iskey + isvalid + incrementfield;
	}
	public DataInterfaceColumnsTmp(String dataSrcAbbr, String dataInterfaceNo, Integer columnNo,
			String dataInterfaceName, String columnName, String dataType, String dataFormat, String comma, String columnComment, String isbucket,
                                   String iskey,String isvalid,String incrementfield) {
		super();
		this.dataSrcAbbr = dataSrcAbbr;
		this.dataInterfaceNo = dataInterfaceNo;
		this.columnNo = columnNo;
		this.dataInterfaceName = dataInterfaceName;
		this.columnName = columnName;
		this.dataType = dataType;
		this.dataFormat = dataFormat;
		this.comma = comma;
		this.columnComment = columnComment;
		this.isbucket = isbucket;
		this.iskey = iskey;
		this.isvalid = isvalid;
		this.incrementfield = incrementfield;
	}

	public DataInterfaceColumnsTmp() {
		super();
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}

	
	
	
	
	
    
    
}