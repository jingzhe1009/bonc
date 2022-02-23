package com.ljz.service;

import java.util.List;

import com.ljz.model.attrC2e;
import com.ljz.model.entityC2e;


public interface IDictionaryService {
	
	List<attrC2e> queryColumn(attrC2e key);
	
	List<entityC2e> queryTable(entityC2e key);
	
	void insertColumn(attrC2e record);
	
	void updateColumn(attrC2e record);
	
	void deleteColumn(attrC2e record);
	
	void insertTable(entityC2e record);
	
	void updateTable(entityC2e record);
	
	void deleteTable(entityC2e record);

}
