package com.ljz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ljz.entity.PageRequest;
import com.ljz.entity.PageResult;
import com.ljz.mapper.DataLogMapper;
import com.ljz.model.DataLog;
import com.ljz.service.ILogService;

@Service
public class LogServiceImpl implements ILogService{
	
	@Autowired
	DataLogMapper mapper;

	
	
	@Override
	public PageResult findPage(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		/*return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest));*/
		return null;
	}



	@Override
	public List<DataLog> queryAll(DataLog record) {
		// TODO Auto-generated method stub
		return mapper.queryAll(record);
	}
	
	/*private PageInfo<tbLog> getPageInfo(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<tbLog> sysMenus = mapper.selectPage(pageRequest.getCondition());
        return new PageInfo<tbLog>(sysMenus);
	}

	@Override
	public List<DataLog> queryAll(DataLog record) {
		// TODO Auto-generated method stub
		return null;
	}*/


	

}
