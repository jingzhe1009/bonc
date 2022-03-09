package com.ljz.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class DataInterfaceColumnsHistory extends DataInterfaceColumnsHistoryKey implements Serializable {
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
    
    private String flag;
    
    private String red;
    
    private String iskey;

    private String isvalid;

    private String incrementfield;

    private static final long serialVersionUID = 1L;

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
    
    public String toStr(){
        return 	 columnName + dataType + dataFormat + nullable +
                replacenull + comma + columnComment + isbucket + iskey + isvalid + incrementfield;
    }
    
    
}