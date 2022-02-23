package com.ljz.model;

import java.io.Serializable;

public class DataFuncConfig implements Serializable {
    private String dataSrcAbbr;

    private String funcName;

    private String funcType;

    private String useType;
    
    private String funcParam;
    
    private String funcParamDesc;
    
    private int func_order;

    private static final long serialVersionUID = 1L;

    public String getDataSrcAbbr() {
        return dataSrcAbbr;
    }

    public void setDataSrcAbbr(String dataSrcAbbr) {
        this.dataSrcAbbr = dataSrcAbbr == null ? null : dataSrcAbbr.trim();
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName == null ? null : funcName.trim();
    }

    public String getFuncType() {
        return funcType;
    }

    public void setFuncType(String funcType) {
        this.funcType = funcType == null ? null : funcType.trim();
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType == null ? null : useType.trim();
    }

	public String getFuncParam() {
		return funcParam;
	}

	public void setFuncParam(String funcParam) {
		this.funcParam = funcParam;
	}

	public String getFuncParamDesc() {
		return funcParamDesc;
	}

	public void setFuncParamDesc(String funcParamDesc) {
		this.funcParamDesc = funcParamDesc;
	}

	public int getFunc_order() {
		return func_order;
	}

	public void setFunc_order(int func_order) {
		this.func_order = func_order;
	}

	
    
    
}