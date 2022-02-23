package com.ljz.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ljz.model.Directory;

@Repository
public interface DirectoryMapper {
	
    int deleteByPrimaryKey(String cname);

    int insert(Directory record);

    int insertSelective(Directory record);

    Directory selectByPrimaryKey(String cname);

    int updateByPrimaryKeySelective(Directory record);

    int updateByPrimaryKey(Directory record);
    
    /**
     * 新增
     * @param list
     */
    void batchInsertDirectory(List<Directory> list);
    
    List<Directory> queryAll();
}