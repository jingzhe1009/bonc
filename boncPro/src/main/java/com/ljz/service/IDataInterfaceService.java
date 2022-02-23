package com.ljz.service;

import java.util.List;
import java.util.Map;

import com.ljz.entity.ParamEntity;
import com.ljz.model.DataInterface;
import com.ljz.model.DataInterface2proc;
import com.ljz.model.DataInterface2procTmp;
import com.ljz.model.DataInterfaceTmp;

public interface IDataInterfaceService {
	
	//接口
	
	List<DataInterface> queryAll(DataInterface record);
	
	List<DataInterface> queryByList(String ds,String [] array);
	
	int insert(DataInterface record);
	
	int update(DataInterface record);
	
	int delete(DataInterfaceTmp record);
	
	List<DataInterface> queryAllVersion(DataInterface record);
	
	List<DataInterfaceTmp> queryAllTmp(DataInterfaceTmp record);
	
	int tmpToSave(DataInterfaceTmp record);
	
	int tmpToSaveBatch(List<DataInterfaceTmp> list);
    
    int deleteBatch(List<DataInterfaceTmp> list);
    
    int updateBatch(List<DataInterface> list);
    
    List<DataInterface> queryModel(DataInterface record);
    
    Map<String,String> createFile(List<String> list,ParamEntity param) throws Exception;
    
    Map<String,String> insertDb(List<String> list,List<String> hasList,ParamEntity param) throws Exception;
    
    void batchImport(ParamEntity param)  throws Exception;
    
    String batchImportFinal(ParamEntity param)  throws Exception;
	
	//数据算法加载
	
	List<DataInterface2proc> queryProc(DataInterface2proc record);
	
	int insertProc(DataInterface2proc record);
	
	int updateProc(DataInterface2proc record);
	
	int deleteProc(DataInterface2procTmp record);
	
	List<DataInterface2proc> queryAllVersionProc(DataInterface2proc record);
	
	List<DataInterface2procTmp> queryAllTmpProc(DataInterface2procTmp record);
	
	int tmpToSaveProc(DataInterface2procTmp list);
	
	String batchImportProcFinal(ParamEntity param)  throws Exception;
	
}
