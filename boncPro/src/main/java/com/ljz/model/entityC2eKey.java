package com.ljz.model;

import java.io.Serializable;

public class entityC2eKey implements Serializable {
    private String cname;

    private String version;

    private static final long serialVersionUID = 1L;

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
}