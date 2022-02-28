package com.ljz.mapper;

import com.ljz.model.DataInterface;
import com.ljz.model.DataInterfaceTmp;
import com.ljz.model.DataRvsdRecord;
import com.ljz.model.DataRvsdRecordTmp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DataRvsdRecordMapper {

    int batchInsert(List<DataRvsdRecordTmp> list);

    List<DataRvsdRecord> queryExptSeqNbr(String dataSrcAbbr);

}