package com.ljz.model;

import java.io.Serializable;
import java.sql.Date;

public class DataSrc implements Serializable {
    private String dataSrcAbbr;

    private String dataSrcDesc;

    private Date sDate;

    private Date eDate;

    private static final long serialVersionUID = 1L;

    public String getDataSrcAbbr() {
        return dataSrcAbbr;
    }

    public void setDataSrcAbbr(String dataSrcAbbr) {
        this.dataSrcAbbr = dataSrcAbbr == null ? null : dataSrcAbbr.trim();
    }

    public String getDataSrcDesc() {
        return dataSrcDesc;
    }

    public void setDataSrcDesc(String dataSrcDesc) {
        this.dataSrcDesc = dataSrcDesc == null ? null : dataSrcDesc.trim();
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
}