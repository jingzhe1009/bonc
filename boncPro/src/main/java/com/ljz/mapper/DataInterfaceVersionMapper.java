package com.ljz.mapper;

import com.ljz.model.DataInterfaceVersion;
import com.ljz.model.DataInterfaceVersionKey;

public interface DataInterfaceVersionMapper {
    int deleteByPrimaryKey(DataInterfaceVersionKey key);

    int insert(DataInterfaceVersion record);

    int insertSelective(DataInterfaceVersion record);

    DataInterfaceVersion selectByPrimaryKey(DataInterfaceVersionKey key);

    int updateByPrimaryKeySelective(DataInterfaceVersion record);

    int updateByPrimaryKey(DataInterfaceVersion record);
}