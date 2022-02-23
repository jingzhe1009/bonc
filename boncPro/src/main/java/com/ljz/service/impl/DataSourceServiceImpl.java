package com.ljz.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ljz.mapper.DataSrcMapper;
import com.ljz.model.DataSrc;
import com.ljz.service.IDataSourceService;

import javax.annotation.Resource;

@Service
public class DataSourceServiceImpl implements IDataSourceService{

	@Resource
	DataSrcMapper mapper;

	public List<String> queryDataSrc(){
		return mapper.queryDataSrc();
	}

	@Override
	public List<DataSrc> queryAll(DataSrc record) {
		// TODO Auto-generated method stub
		return mapper.queryAll(record);
	}

	@Override
	public DataSrc queryById(String dataSrcAbbr) {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(dataSrcAbbr);
	}

	@Override
	public void insert(DataSrc record) {
		// TODO Auto-generated method stub
		mapper.insertSelective(record);
	}

	@Override
	public void update(DataSrc record) {
		// TODO Auto-generated method stub
		mapper.updateByPrimaryKey(record);
	}

	@Override
	public void delete(DataSrc record) {
		// TODO Auto-generated method stub
		mapper.deleteByPrimaryKey(record.getDataSrcAbbr());
	}

}
