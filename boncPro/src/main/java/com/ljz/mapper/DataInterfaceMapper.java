package com.ljz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ljz.model.DataInterface;
import com.ljz.model.DataInterfaceHistory;
import com.ljz.model.DataInterfaceTmp;

public interface DataInterfaceMapper {
    int deleteByPrimaryKey(DataInterfaceTmp key);

    int insert(DataInterface record);

    int insertSelective(DataInterface record);

    DataInterface selectByPrimaryKey(DataInterface key);

    int updateByPrimaryKeySelective(DataInterface record);

    List<DataInterface> queryAll(DataInterface record);
    
    List<DataInterface> queryAllVersion(DataInterface record);
    
    List<DataInterfaceTmp> queryAllTmp(DataInterfaceTmp record);
    
    List<DataInterface> queryByList(@Param("dataSrcAbbr") String ds,@Param("array") String[] array);
    
    int batchInsert(List<DataInterfaceTmp> list);
    
    int batchInsertPro(List<DataInterface> list);
    
    int batchInsertHis(List<DataInterfaceHistory> list);
    
    int tmpToSave(DataInterfaceTmp record);
    
    int tmpToSaveBatch(List<DataInterfaceTmp> list);
    
    int deleteBatch(List<DataInterfaceTmp> list);
    
    int updateBatch(List<DataInterface> list);
    
    List<DataInterface> queryIntNum();
    
    List<DataInterface> queryDsAndInfaceName(String dataSrc);
    
    List<String> queryDbName();
}