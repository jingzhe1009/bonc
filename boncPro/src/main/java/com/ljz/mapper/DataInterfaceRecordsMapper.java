package com.ljz.mapper;

import java.util.List;

import com.ljz.model.DataInterfaceRecords;
import com.ljz.model.DataInterfaceRecordsKey;

public interface DataInterfaceRecordsMapper {
    int deleteByPrimaryKey(DataInterfaceRecordsKey key);

    int insert(DataInterfaceRecords record);

    int insertSelective(DataInterfaceRecords record);

    DataInterfaceRecords selectByPrimaryKey(DataInterfaceRecordsKey key);

    int updateByPrimaryKeySelective(DataInterfaceRecords record);

    int updateByPrimaryKey(DataInterfaceRecords record);
    
    List<DataInterfaceRecords> queryAll(DataInterfaceRecords record);
}