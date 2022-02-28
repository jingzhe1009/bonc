package com.ljz.service.impl;

import com.ljz.mapper.DataRvsdRecordMapper;
import com.ljz.model.DataRvsdRecord;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DataRvsdRecordServiceImpl {

    @Autowired
    DataRvsdRecordMapper dataRvsdRecordMapper;

    public List<DataRvsdRecord> queryExptSeqNbr(String dataSrcAbbr){
        return dataRvsdRecordMapper.queryExptSeqNbr(dataSrcAbbr);
    }



}
