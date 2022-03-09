package com.ljz.model;

import java.io.Serializable;

public class DataInterfaceRecords extends DataInterfaceRecordsKey implements Serializable {
    private String exptFileName;

    private String intfTot;

    private String intfNew;

    private String intfAlt;

    private String intfDscr;

    private String createDate;

    private String altDate;

    private String exctPsn;

    private String exptDate;

    private static final long serialVersionUID = 1L;

    public String getExptFileName() {
        return exptFileName;
    }

    public void setExptFileName(String exptFileName) {
        this.exptFileName = exptFileName == null ? null : exptFileName.trim();
    }

    public String getIntfTot() {
        return intfTot;
    }

    public void setIntfTot(String intfTot) {
        this.intfTot = intfTot == null ? null : intfTot.trim();
    }

    public String getIntfNew() {
        return intfNew;
    }

    public void setIntfNew(String intfNew) {
        this.intfNew = intfNew == null ? null : intfNew.trim();
    }

    public String getIntfAlt() {
        return intfAlt;
    }

    public void setIntfAlt(String intfAlt) {
        this.intfAlt = intfAlt == null ? null : intfAlt.trim();
    }

    public String getIntfDscr() {
        return intfDscr;
    }

    public void setIntfDscr(String intfDscr) {
        this.intfDscr = intfDscr == null ? null : intfDscr.trim();
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate == null ? null : createDate.trim();
    }

    public String getAltDate() {
        return altDate;
    }

    public void setAltDate(String altDate) {
        this.altDate = altDate == null ? null : altDate.trim();
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