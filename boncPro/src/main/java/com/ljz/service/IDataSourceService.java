package com.ljz.service;

import java.util.List;

import com.ljz.model.DataSrc;

public interface IDataSourceService {
	
	List<DataSrc> queryAll(DataSrc record);
	
	DataSrc queryById(String dataSrcAbbr);
	
	void insert(DataSrc record);
	
	void update(DataSrc record);
	
	void delete(DataSrc record);

}
