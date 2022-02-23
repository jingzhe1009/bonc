package com.ljz.entity;

import java.util.Map;

public class PageRequest {
	
	 /**
     * 当前页码
     */
    private int pageNum;
    /**
     * 每页数量
     */
    private int pageSize;
    
    private String table_ename;
    
    private String ename;
    
    private Map<String,String> condition;
    
    public int getPageNum() {
        return pageNum;
    }
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
	public String getTable_ename() {
		return table_ename;
	}
	public void setTable_ename(String table_ename) {
		this.table_ename = table_ename;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	
	public Map<String, String> getCondition() {
		return condition;
	}
	public void setCondition(Map<String, String> condition) {
		this.condition = condition;
	}
	@Override
	public String toString() {
		return "PageRequest [pageNum=" + pageNum + ", pageSize=" + pageSize + "]";
	}
    

}
