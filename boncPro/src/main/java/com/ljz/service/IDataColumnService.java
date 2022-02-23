package com.ljz.service;

import java.util.List;

import com.ljz.entity.ParamEntity;
import com.ljz.model.DataInterfaceColumns;
import com.ljz.model.DataInterfaceColumnsTmp;

public interface IDataColumnService {
	
	List<DataInterfaceColumns> queryAll(DataInterfaceColumns record);
	
	int insert(DataInterfaceColumns record);
	
	int update(DataInterfaceColumns record);
	
	int delete(DataInterfaceColumnsTmp record);
	
	List<DataInterfaceColumns> queryAllVersion(DataInterfaceColumns record);
	
	List<DataInterfaceColumnsTmp> queryAllTmp(DataInterfaceColumnsTmp record);
	
	int tmpToSave(DataInterfaceColumnsTmp record);
	
	int tmpToSaveBatch(List<DataInterfaceColumnsTmp> list);
	
	int deleteBatch(List<DataInterfaceColumnsTmp> list);
    
    int updateBatch(List<DataInterfaceColumns> list);
    
    void batchImport(ParamEntity param)  throws Exception;
    
    void batchImportNew(ParamEntity param)  throws Exception;
    
    String batchImportFinal(ParamEntity param)  throws Exception;
	
}
