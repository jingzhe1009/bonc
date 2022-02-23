package com.ljz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ljz.entity.PageRequest;
import com.ljz.entity.PageResult;
import com.ljz.mapper.tbWarnMapper;
import com.ljz.model.tbWarn;
import com.ljz.service.IWarnService;
import com.ljz.util.PageUtils;

@Service
public class TbWarnServiceImpl implements IWarnService{
	
	@Autowired
	tbWarnMapper mapper;

	@Override
	public List<tbWarn> selectAll() {
		// TODO Auto-generated method stub
		return mapper.selectAll();
	}
	
	@Override
	public PageResult findPage(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest));
	}
	
	private PageInfo<tbWarn> getPageInfo(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<tbWarn> sysMenus = mapper.selectPage(pageRequest.getCondition());
        return new PageInfo<tbWarn>(sysMenus);
	}


	

}
