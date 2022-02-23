package com.ljz.mapper;

import java.util.List;
import java.util.Map;

import com.ljz.model.tbWarn;

public interface tbWarnMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(tbWarn record);

    int insertSelective(tbWarn record);

    tbWarn selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(tbWarn record);

    int updateByPrimaryKey(tbWarn record);
    
    List<tbWarn> selectAll();
    
    List<tbWarn> selectPage(Map<String,String> condition);
}