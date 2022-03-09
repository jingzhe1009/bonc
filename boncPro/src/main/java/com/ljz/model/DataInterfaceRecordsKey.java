package com.ljz.model;

import java.io.Serializable;

public class DataInterfaceRecordsKey implements Serializable {
    private String needVrsnNbr;

    private String exptSeqNbr;

    private String dataSrcAbbr;

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
}