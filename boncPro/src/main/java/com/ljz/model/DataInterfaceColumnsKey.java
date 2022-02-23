package com.ljz.model;

import java.io.Serializable;

public class DataInterfaceColumnsKey implements Serializable {
    private String dataSrcAbbr;

    private String dataInterfaceNo;

    private Integer columnNo;

    private static final long serialVersionUID = 1L;

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
}