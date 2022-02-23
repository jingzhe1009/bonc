package com.ljz.mapper;

import java.util.List;

import com.ljz.model.DataLog;

public interface DataLogMapper {
    int deleteByPrimaryKey(String actionId);

    int insert(DataLog record);

    int insertSelective(DataLog record);

    DataLog selectByPrimaryKey(String actionId);

    int updateByPrimaryKeySelective(DataLog record);

    int updateByPrimaryKey(DataLog record);
    
    List<DataLog> queryAll(DataLog record);
}