package com.ljz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ljz.entity.PageRequest;
import com.ljz.entity.PageResult;
import com.ljz.mapper.TableListMapper;
import com.ljz.model.TableList;
import com.ljz.model.TableListKey;
import com.ljz.service.ITableListService;
import com.ljz.util.PageUtils;

@Service
public class TableListServiceImpl implements ITableListService{
	
	@Autowired
	TableListMapper mapper;


	@Override
	public PageResult findPage(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest));
	}
	
	private PageInfo<TableList> getPageInfo(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<TableList> sysMenus = mapper.selectPage(pageRequest.getCondition());
        return new PageInfo<TableList>(sysMenus);
	}

	@Override
	public PageResult findDetailPage(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return PageUtils.getPageResult(pageRequest, getPageDetailInfo(pageRequest));
	}
	
	private PageInfo<TableList> getPageDetailInfo(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<TableList> sysMenus = mapper.selectDetailPage(pageRequest.getCondition());
        return new PageInfo<TableList>(sysMenus);
	}

	@Override
	public void save(TableList tableList) {
		// TODO Auto-generated method stub
		mapper.updateByPrimaryKey(tableList);
	}

	@Override
	public TableList selectByPrimaryKey(TableListKey key) {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(key);
	}

	@Override
	public List<TableList> getTableList(String[] tableName) {
		// TODO Auto-generated method stub
		return mapper.queryTableList(tableName);
	}

}
