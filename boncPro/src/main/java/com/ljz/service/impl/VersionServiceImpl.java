package com.ljz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ljz.mapper.TbVersionMapper;
import com.ljz.model.TbVersion;
import com.ljz.service.IVersionService;

@Service
public class VersionServiceImpl implements IVersionService{
	
	@Autowired
	TbVersionMapper mapper;

	@Override
	public List<TbVersion> queryAll(TbVersion record) {
		// TODO Auto-generated method stub
		return mapper.queryAll(record);
	}

	@Override
	public void insert(TbVersion record) {
		// TODO Auto-generated method stub
		mapper.insertSelective(record);
	}

}
