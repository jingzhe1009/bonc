package com.ljz.service;

import java.util.List;

import com.ljz.model.TbVersion;

public interface IVersionService {
	
	 List<TbVersion> queryAll(TbVersion record);
	 
	 void insert(TbVersion record);

}
