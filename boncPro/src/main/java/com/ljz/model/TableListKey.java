package com.ljz.model;

import java.io.Serializable;

public class TableListKey implements Serializable {
    private String tableEname;

    private String ename;

    private static final long serialVersionUID = 1L;

    public String getTableEname() {
        return tableEname;
    }

    public void setTableEname(String tableEname) {
        this.tableEname = tableEname == null ? null : tableEname.trim();
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename == null ? null : ename.trim();
    }

}