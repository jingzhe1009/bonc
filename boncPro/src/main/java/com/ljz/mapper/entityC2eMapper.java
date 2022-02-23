package com.ljz.mapper;

import java.util.List;

import com.ljz.model.entityC2e;

public interface entityC2eMapper {
    int deleteByPrimaryKey(entityC2e key);

    int insert(entityC2e record);

    int insertSelective(entityC2e record);

    entityC2e selectByPrimaryKey(entityC2e key);

    int updateByPrimaryKeySelective(entityC2e record);

    int updateByPrimaryKey(entityC2e record);
    
    List<entityC2e> queryAll(entityC2e key);
    
    void batchInsert(List<entityC2e> list);
}