package com.ljz.mapper;

import java.util.List;

import com.ljz.model.DataInterfaceColumns;
import com.ljz.model.DataInterfaceColumnsHistory;
import com.ljz.model.DataInterfaceColumnsTmp;


public interface DataInterfaceColumnsMapper {
    int deleteByPrimaryKey(DataInterfaceColumnsTmp key);

    int insert(DataInterfaceColumns record);

    int insertSelective(DataInterfaceColumns record);

    DataInterfaceColumns selectByPrimaryKey(DataInterfaceColumns key);

    int updateByPrimaryKeySelective(DataInterfaceColumns record);

    int updateByPrimaryKey(DataInterfaceColumns record);

    List<DataInterfaceColumns> queryAll(DataInterfaceColumns record);

    int batchInsert(List<DataInterfaceColumnsTmp> list);

    int batchInsertPro(List<DataInterfaceColumns> list);

    int batchInsertHis(List<DataInterfaceColumnsHistory> list);
    
    List<DataInterfaceColumns> queryAllVersion(DataInterfaceColumns record);

    List<DataInterfaceColumnsTmp> queryAllTmp(DataInterfaceColumnsTmp record);

    int tmpToSave(DataInterfaceColumnsTmp record);

    int tmpToSaveBatch(List<DataInterfaceColumnsTmp> list);

    int deleteBatch(List<DataInterfaceColumnsTmp> list);

    int updateBatch(List<DataInterfaceColumns> list);

    List<DataInterfaceColumns> queryDup(String dataInterfaceName);
}