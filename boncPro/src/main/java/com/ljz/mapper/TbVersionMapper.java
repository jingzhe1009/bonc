package com.ljz.mapper;

import java.util.List;

import com.ljz.model.TbVersion;

public interface TbVersionMapper {


    int insertSelective(TbVersion record);
    
    List<TbVersion> queryAll(TbVersion record);
}