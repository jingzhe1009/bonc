package com.ljz.model;

import java.io.Serializable;

public class entityC2e extends entityC2eKey implements Serializable {
    private String ename;

    private Integer tLenb;

    private Integer tLen;

    private String createDate;

    private static final long serialVersionUID = 1L;
    
    private String cname;

    private String version;


    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname == null ? null : cname.trim();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename == null ? null : ename.trim();
    }

    public Integer gettLenb() {
        return tLenb;
    }

    public void settLenb(Integer tLenb) {
        this.tLenb = tLenb;
    }

    public Integer gettLen() {
        return tLen;
    }

    public void settLen(Integer tLen) {
        this.tLen = tLen;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate == null ? null : createDate.trim();
    }

	@Override
	public String toString() {
		return "ename=" + ename + ", tLenb=" + tLenb + ", tLen=" + tLen + ", createDate=" + createDate
				+ ", cname=" + cname + ", version=" + version;
	}

	
    
    
    
}