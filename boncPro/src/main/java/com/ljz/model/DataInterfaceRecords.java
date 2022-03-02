package com.ljz.model;

import java.io.Serializable;

public class DataInterfaceRecords extends DataInterfaceRecordsKey implements Serializable {
    private String exptFileName;

    private String dataInterfaceDesc;

    private String intfTot;

    private String exctPsn;

    private String exptDate;

    private static final long serialVersionUID = 1L;

    public String getExptFileName() {
        return exptFileName;
    }

    public void setExptFileName(String exptFileName) {
        this.exptFileName = exptFileName == null ? null : exptFileName.trim();
    }

    public String getDataInterfaceDesc() {
        return dataInterfaceDesc;
    }

    public void setDataInterfaceDesc(String dataInterfaceDesc) {
        this.dataInterfaceDesc = dataInterfaceDesc == null ? null : dataInterfaceDesc.trim();
    }

    public String getIntfTot() {
        return intfTot;
    }

    public void setIntfTot(String intfTot) {
        this.intfTot = intfTot == null ? null : intfTot.trim();
    }

    public String getExctPsn() {
        return exctPsn;
    }

    public void setExctPsn(String exctPsn) {
        this.exctPsn = exctPsn == null ? null : exctPsn.trim();
    }

    public String getExptDate() {
        return exptDate;
    }

    public void setExptDate(String exptDate) {
        this.exptDate = exptDate == null ? null : exptDate.trim();
    }
}