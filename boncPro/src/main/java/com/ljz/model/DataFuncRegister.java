package com.ljz.model;

import java.io.Serializable;

public class DataFuncRegister implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int funcId;
	
	private String funcName;

    private String useType;

    private String funcParam;

    private String funcDesc;

    private String createUser;

    private String createTime;
    
    private String funcParamDesc;
    

	public int getFuncId() {
		return funcId;
	}

	public void setFuncId(int funcId) {
		this.funcId = funcId;
	}

	public String getFuncName() {
		return funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	public String getUseType() {
		return useType;
	}

	public void setUseType(String useType) {
		this.useType = useType;
	}

	public String getFuncParam() {
        return funcParam;
    }

    public void setFuncParam(String funcParam) {
        this.funcParam = funcParam == null ? null : funcParam.trim();
    }

    public String getFuncDesc() {
        return funcDesc;
    }

    public void setFuncDesc(String funcDesc) {
        this.funcDesc = funcDesc == null ? null : funcDesc.trim();
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

	public String getFuncParamDesc() {
		return funcParamDesc;
	}

	public void setFuncParamDesc(String funcParamDesc) {
		this.funcParamDesc = funcParamDesc;
	}
    
    
}