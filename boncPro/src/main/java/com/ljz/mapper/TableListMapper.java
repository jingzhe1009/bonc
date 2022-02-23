package com.ljz.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ljz.model.TableList;
import com.ljz.model.TableListKey;

@Repository
public interface TableListMapper {
	
    int deleteByPrimaryKey(TableListKey key);

    int insert(TableList record);

    int insertSelective(TableList record);

    TableList selectByPrimaryKey(TableListKey key);

    int updateByPrimaryKeySelective(TableList record);

    int updateByPrimaryKey(TableList record);
    
    /**
     * 新增
     */
    List<TableList> selectPage(Map<String,String> condition);
	
	List<TableList> selectDetailPage(Map<String,String> condition);
	
	void batchInsert(List<TableList> list);
	
	List<TableList> queryTableList(String [] tableNames);
}