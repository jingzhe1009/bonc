package com.ljz.mapper;

import java.util.List;

import com.ljz.model.DataInterfaceHistory;
import com.ljz.model.DataInterfaceHistoryKey;

public interface DataInterfaceHistoryMapper {
    int deleteByPrimaryKey(DataInterfaceHistoryKey key);

    int insert(DataInterfaceHistory record);

    int insertSelective(DataInterfaceHistory record);

    DataInterfaceHistory selectByPrimaryKey(DataInterfaceHistoryKey key);

    int updateByPrimaryKeySelective(DataInterfaceHistory record);

    int updateByPrimaryKey(DataInterfaceHistory record);
    
    List<DataInterfaceHistory> queryAll(DataInterfaceHistory record);
    
    List<DataInterfaceHistory> queryFirst(DataInterfaceHistory record);
}