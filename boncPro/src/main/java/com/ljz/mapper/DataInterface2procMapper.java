package com.ljz.mapper;

import java.util.List;

import com.ljz.model.DataInterface2proc;
import com.ljz.model.DataInterface2procKey;
import com.ljz.model.DataInterface2procTmp;

public interface DataInterface2procMapper {
    int deleteByPrimaryKey(DataInterface2procTmp key);

    int insert(DataInterface2proc record);

    int insertSelective(DataInterface2proc record);

    DataInterface2proc selectByPrimaryKey(DataInterface2procKey key);

    int updateByPrimaryKeySelective(DataInterface2proc record);

    int updateByPrimaryKey(DataInterface2proc record);
    
    List<DataInterface2proc> queryAll(DataInterface2proc record);
    
    int batchInsert(List<DataInterface2procTmp> list);
    
    int tmpToSave(DataInterface2procTmp record);
    
    List<DataInterface2proc> queryAllVersion(DataInterface2proc record);
    
    List<DataInterface2procTmp> queryAllTmp(DataInterface2procTmp record);
    
    int batchInsertPro(List<DataInterface2proc> list);
}