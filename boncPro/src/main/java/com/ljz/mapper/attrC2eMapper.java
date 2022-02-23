package com.ljz.mapper;

import java.util.List;

import com.ljz.model.attrC2e;

public interface attrC2eMapper {
    int deleteByPrimaryKey(attrC2e key);

    int insert(attrC2e record);

    int insertSelective(attrC2e record);

    attrC2e selectByPrimaryKey(attrC2e key);

    int updateByPrimaryKeySelective(attrC2e record);

    int updateByPrimaryKey(attrC2e record);
    
    List<attrC2e> queryAll(attrC2e key);
    
    void batchInsert(List<attrC2e> list);
}