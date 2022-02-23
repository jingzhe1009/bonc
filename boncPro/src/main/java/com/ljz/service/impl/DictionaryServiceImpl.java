package com.ljz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ljz.mapper.attrC2eMapper;
import com.ljz.mapper.entityC2eMapper;
import com.ljz.model.attrC2e;
import com.ljz.model.entityC2e;
import com.ljz.service.IDictionaryService;

@Service
public class DictionaryServiceImpl implements IDictionaryService{
	
	@Autowired
	attrC2eMapper aMapper;
	
	@Autowired
	entityC2eMapper eMapper;

	@Override
	public List<attrC2e> queryColumn(attrC2e key) {
		// TODO Auto-generated method stub
		return aMapper.queryAll(key);
	}

	@Override
	public List<entityC2e> queryTable(entityC2e key) {
		// TODO Auto-generated method stub
		return eMapper.queryAll(key);
	}

	@Override
	public void insertColumn(attrC2e record) {
		// TODO Auto-generated method stub
		aMapper.insertSelective(record);
	}

	@Override
	public void updateColumn(attrC2e record) {
		// TODO Auto-generated method stub
		System.out.println(record.toString());
		aMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public void deleteColumn(attrC2e record) {
		// TODO Auto-generated method stub
		aMapper.deleteByPrimaryKey(record);
	}

	@Override
	public void insertTable(entityC2e record) {
		// TODO Auto-generated method stub
		eMapper.insertSelective(record);
	}

	@Override
	public void updateTable(entityC2e record) {
		// TODO Auto-generated method stub
		System.out.println(record.toString());
		eMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public void deleteTable(entityC2e record) {
		// TODO Auto-generated method stub
		eMapper.deleteByPrimaryKey(record);
	}

	

}
