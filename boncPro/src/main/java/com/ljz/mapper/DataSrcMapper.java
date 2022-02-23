package com.ljz.mapper;

import java.util.List;

import com.ljz.model.DataSrc;

public interface DataSrcMapper {
    int deleteByPrimaryKey(String dataSrcAbbr);

    int insert(DataSrc record);

    int insertSelective(DataSrc record);

    DataSrc selectByPrimaryKey(String dataSrcAbbr);

    int updateByPrimaryKeySelective(DataSrc record);

    int updateByPrimaryKey(DataSrc record);

    List<DataSrc> queryAll(DataSrc record);

    List<String> queryDataSrc();
}