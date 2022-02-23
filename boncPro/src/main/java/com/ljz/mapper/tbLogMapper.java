package com.ljz.mapper;

import java.util.List;
import java.util.Map;

import com.ljz.model.tbLog;

public interface tbLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(tbLog record);

    int insertSelective(tbLog record);

    tbLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(tbLog record);

    int updateByPrimaryKey(tbLog record);
    
    List<tbLog> selectPage(Map<String,String> condition);
}