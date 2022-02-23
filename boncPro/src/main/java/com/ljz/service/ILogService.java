package com.ljz.service;


import java.util.List;

import com.ljz.entity.PageRequest;
import com.ljz.entity.PageResult;
import com.ljz.model.DataLog;

public interface ILogService {
	
	
	PageResult findPage(PageRequest pageRequest);
    

	List<DataLog> queryAll(DataLog record);
}
