package com.ljz.model;

import java.io.Serializable;

public class Directory implements Serializable {
    private String cname;

    private String ename;

    private String type;

    private static final long serialVersionUID = 1L;

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname == null ? null : cname.trim();
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename == null ? null : ename.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

	public Directory(String cname, String ename, String type) {
		super();
		this.cname = cname;
		this.ename = ename;
		this.type = type;
	}
    
    
}