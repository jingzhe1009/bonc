package com.ljz.service;

import java.util.List;
import java.util.Map;

import com.ljz.model.DataFuncConfig;
import com.ljz.model.DataFuncRegister;

public interface IFuncRegisterService {

	
	List<DataFuncRegister> queryAll(DataFuncRegister record);
	
	void insert(DataFuncRegister record);
	
	void update(DataFuncRegister record);
	
	void delete(DataFuncRegister record);
	
	List<Map<String,String>> queryAllConfig(DataFuncConfig record);
	
	void insertConfig(DataFuncConfig record);
	
	void updateConfig(DataFuncConfig record);
	
	void deleteConfig(DataFuncConfig record);
	
	String getWord(String dataSrcAbbr,String type);
	
	void batchInsertConfig(List<DataFuncConfig> list);
}
