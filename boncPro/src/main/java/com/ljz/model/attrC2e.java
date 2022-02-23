package com.ljz.model;

import java.io.Serializable;

public class attrC2e implements Serializable {
    private String ename;

    private String fullEname;

    private Integer wordNum;

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

    public String getFullEname() {
        return fullEname;
    }

    public void setFullEname(String fullEname) {
        this.fullEname = fullEname == null ? null : fullEname.trim();
    }

    public Integer getWordNum() {
        return wordNum;
    }

    public void setWordNum(Integer wordNum) {
        this.wordNum = wordNum;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate == null ? null : createDate.trim();
    }

	@Override
	public String toString() {
		return "ename=" + ename + ", fullEname=" + fullEname + ", wordNum=" + wordNum + ", createDate="
				+ createDate + ", cname=" + cname + ", version=" + version ;
	}

	
    
    
}