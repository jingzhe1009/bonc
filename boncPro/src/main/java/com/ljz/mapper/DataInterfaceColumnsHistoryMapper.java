package com.ljz.mapper;

import java.util.List;

import com.ljz.model.DataInterfaceColumnsHistory;
import com.ljz.model.DataInterfaceColumnsHistoryKey;

public interface DataInterfaceColumnsHistoryMapper {
    int deleteByPrimaryKey(DataInterfaceColumnsHistoryKey key);

    int insert(DataInterfaceColumnsHistory record);

    int insertSelective(DataInterfaceColumnsHistory record);

    DataInterfaceColumnsHistory selectByPrimaryKey(DataInterfaceColumnsHistoryKey key);

    int updateByPrimaryKeySelective(DataInterfaceColumnsHistory record);

    int updateByPrimaryKey(DataInterfaceColumnsHistory record);
    
    List<DataInterfaceColumnsHistory> queryAll(DataInterfaceColumnsHistory record);
    
    List<DataInterfaceColumnsHistory> queryFirst(DataInterfaceColumnsHistory record);
}