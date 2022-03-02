package com.ljz.model;

import java.io.Serializable;

public class DataInterfaceVersion extends DataInterfaceVersionKey implements Serializable {
    private String dataInterfaceDesc;

    private String intfTot;

    private String intfDscr;

    private String createDate;

    private static final long serialVersionUID = 1L;

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
}