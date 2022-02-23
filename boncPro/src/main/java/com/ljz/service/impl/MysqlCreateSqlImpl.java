package com.ljz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ljz.mapper.DataInterfaceColumnsMapper;
import com.ljz.model.DataInterfaceColumns;
import com.ljz.model.DbInfo;
import com.ljz.service.ICreateSql;
import com.ljz.util.SqlUtil;

@Service
public class MysqlCreateSqlImpl extends CreateSqlImpl implements ICreateSql{

	@Autowired
	DataInterfaceColumnsMapper mapper;
	
	String common(String tableName,DbInfo info) {
		String tableCName = "";
		StringBuffer sb = new StringBuffer();
		StringBuffer pk = new StringBuffer();
		DataInterfaceColumns record = new DataInterfaceColumns();
		record.setDataInterfaceNo(tableName);
		List<DataInterfaceColumns> list = mapper.queryAll(record);
		sb.append("CREATE TABLE IF NOT EXISTS "+info.getDbName().toUpperCase()+"."+tableName.toUpperCase()+"(\n");
		for(int i=0;i<list.size();i++) {
			DataInterfaceColumns t = list.get(i);
			tableCName = t.getColumnComment();
			String ename = SqlUtil.dealEName(t.getColumnName());
			//循环字段+类型+是否为空+注释
			sb.append(ename+SqlUtil.dealType(t.getDataType())+SqlUtil.dealRequired(t.getNullable()+"")+" COMMENT '"+t.getColumnComment()+"'");
			if(i==list.size()-1) {
				sb.append("\n");
			}else {
				sb.append(",\n");
			}
			//主键拼接
			/*if("Y".equals(t.getPkFlag())) {
				if(i==list.size()-1) {
					pk.append(ename);
				}else {
					pk.append(ename+",");
				}
			}*/
		}
		//主键
		if(pk!=null&&!"".equals(pk.toString())) {
			String index = pk.toString();
			if(index.trim().substring(index.length()-1).equals(","))
				index = index.substring(0,index.length()-1);
			sb.append(",PRIMARY KEY("+index+")\n");
		}
		//表注释
		sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '"+tableCName+"';\n");
		return sb.toString();
	}

}
