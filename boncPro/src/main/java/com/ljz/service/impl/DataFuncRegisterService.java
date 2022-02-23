package com.ljz.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ljz.mapper.DataFuncConfigMapper;
import com.ljz.mapper.DataFuncRegisterMapper;
import com.ljz.model.DataFuncConfig;
import com.ljz.model.DataFuncRegister;
import com.ljz.service.IFuncRegisterService;

import javax.annotation.Resource;

@Service
public class DataFuncRegisterService implements IFuncRegisterService{

	@Resource
	DataFuncRegisterMapper mapper;
	
	@Resource
	DataFuncConfigMapper configMapper;

	public int existFunc(String funcName){
		return mapper.existFunc(funcName);
	}

	@Override
	public List<DataFuncRegister> queryAll(DataFuncRegister record) {
		// TODO Auto-generated method stub
		return mapper.queryAll(record);
	}

	@Override
	public void insert(DataFuncRegister record) {
		// TODO Auto-generated method stub
		mapper.insertSelective(record);
	}

	@Override
	public void update(DataFuncRegister record) {
		// TODO Auto-generated method stub
		mapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public void delete(DataFuncRegister record) {
		// TODO Auto-generated method stub
		mapper.deleteByPrimaryKey(record);
	}
	
	@Override
	public List<Map<String,String>> queryAllConfig(DataFuncConfig record) {
		// TODO Auto-generated method stub
		return configMapper.queryAll(record);
	}

	@Override
	public void insertConfig(DataFuncConfig record) {
		// TODO Auto-generated method stub
		configMapper.insertSelective(record);
	}

	@Override
	public void updateConfig(DataFuncConfig record) {
		// TODO Auto-generated method stub
		configMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public void deleteConfig(DataFuncConfig record) {
		// TODO Auto-generated method stub
		configMapper.deleteByPrimaryKey(record);
	}
	
	public String getWord(String dataSrcAbbr,String type) {
		StringBuffer sb = new StringBuffer();
		//sb.append(BoncConstant.getStr());
		/*DataFuncConfig config = new DataFuncConfig();*/
		//config.setDataSrcAbbr(dataSrcAbbr);
		/*List<String> list = configMapper.queryGroup(config);
		if(list.size()>0) {*/
			/*for(String ds:list) {
				sb.append("["+ds+"]<br>");*/
				//DataFuncConfig record = new DataFuncConfig();
				//record.setDataSrcAbbr(ds);
				//List<String> queryType = configMapper.queryType(record);
				/*for(String t:queryType) {*/
						sb.append("["+dataSrcAbbr+"]<br>");
						DataFuncConfig data = new DataFuncConfig();
						data.setDataSrcAbbr(dataSrcAbbr);
						data.setUseType(type);
						List<Map<String,String>> dataList = configMapper.queryAll(data);
						if("1".equals(type)) {
							sb.append("MONITOR_FUNCTION=");
						}else if("2".equals(type)) {
							sb.append("TRANS_FUNCTION=");
						}
						for(int i=0;i<dataList.size();i++) {
							Map<String,String> map = dataList.get(i);
							if(i==dataList.size()-1) {
								sb.append(map.get("funcName")+map.get("funcParam"));
							}else {
								sb.append(map.get("funcName")+map.get("funcParam")+"#<br>");
							}
						}
						sb.append("<br>");
				/*}*/
				
			/*}*/
		/*}*/
		return sb.toString();
	}

	@Override
	public void batchInsertConfig(List<DataFuncConfig> list) {
		// TODO Auto-generated method stub
		configMapper.batchInsertConfig(list);
		
	}

}
