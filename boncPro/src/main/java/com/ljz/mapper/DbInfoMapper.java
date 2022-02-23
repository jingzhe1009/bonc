package com.ljz.mapper;

import org.springframework.stereotype.Repository;

import com.ljz.model.DbInfo;

@Repository
public interface DbInfoMapper {
    int deleteByPrimaryKey(String dbType);

    int insert(DbInfo record);

    int insertSelective(DbInfo record);

    DbInfo selectByPrimaryKey(String dbType);

    int updateByPrimaryKeySelective(DbInfo record);

    int updateByPrimaryKey(DbInfo record);
    
}