package com.ljz.service;


import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface IExcelService {
	
	Map<String,String> importTable(MultipartFile file,String batchNo);
	
	String importDictionary(MultipartFile file);
	
	Map<String,String> importColumn(MultipartFile file,String batchNo);
	
	Map<String,String> importProc(MultipartFile file,String batchNo);
	

}
