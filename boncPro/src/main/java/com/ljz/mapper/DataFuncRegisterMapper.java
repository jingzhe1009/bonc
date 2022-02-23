package com.ljz.mapper;

import java.util.List;

import com.ljz.model.DataFuncRegister;
import com.ljz.model.DataFuncRegisterKey;

public interface DataFuncRegisterMapper {
    int deleteByPrimaryKey(DataFuncRegister key);

    int insert(DataFuncRegister record);

    int insertSelective(DataFuncRegister record);

    DataFuncRegister selectByPrimaryKey(DataFuncRegisterKey key);

    int updateByPrimaryKeySelective(DataFuncRegister record);

    int updateByPrimaryKey(DataFuncRegister record);
    
    List<DataFuncRegister> queryAll(DataFuncRegister record);

    int existFunc(String funcName);
}