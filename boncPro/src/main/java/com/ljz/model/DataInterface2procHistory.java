package com.ljz.model;

import java.io.Serializable;
import java.util.Date;

public class DataInterface2procHistory implements Serializable {
    private String needVrsnNbr;

    private String exptSeqNbr;

    private String dataSrcAbbr;

    private String dataInterfaceNo;

    private String procDatabaseName;

    private String procName;

    private Date sDate;

    private Date eDate;

    private static final long serialVersionUID = 1L;

    public String getNeedVrsnNbr() {
        return needVrsnNbr;
    }

    public void setNeedVrsnNbr(String needVrsnNbr) {
        this.needVrsnNbr = needVrsnNbr == null ? null : needVrsnNbr.trim();
    }

    public String getExptSeqNbr() {
        return exptSeqNbr;
    }

    public void setExptSeqNbr(String exptSeqNbr) {
        this.exptSeqNbr = exptSeqNbr == null ? null : exptSeqNbr.trim();
    }

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

    public String getProcDatabaseName() {
        return procDatabaseName;
    }

    public void setProcDatabaseName(String procDatabaseName) {
        this.procDatabaseName = procDatabaseName == null ? null : procDatabaseName.trim();
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName == null ? null : procName.trim();
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