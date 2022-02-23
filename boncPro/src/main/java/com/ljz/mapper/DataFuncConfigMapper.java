package com.ljz.mapper;

import java.util.List;
import java.util.Map;

import com.ljz.model.DataFuncConfig;

public interface DataFuncConfigMapper {
    int deleteByPrimaryKey(DataFuncConfig record);

    int insert(DataFuncConfig record);

    int insertSelective(DataFuncConfig record);

    DataFuncConfig selectByPrimaryKey(String dataSrcAbbr);

    int updateByPrimaryKeySelective(DataFuncConfig record);

    int updateByPrimaryKey(DataFuncConfig record);
    
    List<Map<String,String>> queryAll(DataFuncConfig record);
    
    List<String> queryGroup(DataFuncConfig record);
    
    List<String> queryType(DataFuncConfig record);
    
    void batchInsertConfig(List<DataFuncConfig> list);
    
}