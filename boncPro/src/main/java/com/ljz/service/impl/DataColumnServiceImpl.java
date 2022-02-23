package com.ljz.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ljz.entity.ParamEntity;
import com.ljz.mapper.DataInterfaceColumnsMapper;
import com.ljz.model.DataInterfaceColumns;
import com.ljz.model.DataInterfaceColumnsTmp;
import com.ljz.service.IDataColumnService;
import com.ljz.util.ExcelUtil;
import com.ljz.util.TimeUtil;

@Service
public class DataColumnServiceImpl implements IDataColumnService{
	
	@Autowired
	DataInterfaceColumnsMapper mapper;
	
	private static final Logger logger = LoggerFactory.getLogger(DataColumnServiceImpl.class);

	@Autowired
	JdbcTemplate jdbc;

	@Override
	public List<DataInterfaceColumns> queryAll(DataInterfaceColumns record) {
		// TODO Auto-generated method stub
		return mapper.queryAll(record);
	}
	
	@Override
	public int insert(DataInterfaceColumns record) {
		// TODO Auto-generated method stub
		return mapper.insertSelective(record);
	}

	@Override
	public int update(DataInterfaceColumns record) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int delete(DataInterfaceColumnsTmp record) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(record);
	}

	@Override
	public List<DataInterfaceColumns> queryAllVersion(DataInterfaceColumns record) {
		// TODO Auto-generated method stub
		List<DataInterfaceColumns> list = mapper.queryAllVersion(record);
		List<DataInterfaceColumns> returnList = new ArrayList<DataInterfaceColumns>();
		for(int i=0;i<list.size();i++) {
			DataInterfaceColumns data = list.get(i);
			data.setNum("v1."+(i));
			returnList.add(data);
		}
		return returnList;
	}

	@Override
	public List<DataInterfaceColumnsTmp> queryAllTmp(DataInterfaceColumnsTmp record) {
		// TODO Auto-generated method stub
		List<DataInterfaceColumnsTmp> resultList = new ArrayList<DataInterfaceColumnsTmp>();
		String dataSrcAbbr = record.getDataSrcAbbr();
		if(dataSrcAbbr!=null&&!"".equals(dataSrcAbbr)){
			logger.info("导入临时表时，数据源是"+record.getDataSrcAbbr());
			ExcelUtil obj = ExcelUtil.getInstance();
			List<DataInterfaceColumnsTmp> queryAllTmp = mapper.queryAllTmp(record);
			Map<String,String> columnMap =obj.getColumnMap(record.getDataSrcAbbr());
			for(DataInterfaceColumnsTmp tmp:queryAllTmp){
				String key = tmp.getDataSrcAbbr()+tmp.getDataInterfaceNo()+tmp.getDataInterfaceName()+tmp.getColumnNo();
				if(columnMap!=null&&columnMap.containsKey(key)){
					if(tmp.toStr().equalsIgnoreCase(columnMap.get(key))){//无变化
						tmp.setImportType("3");
					}else{//修改
						tmp.setImportType("2");
					}
				}else{//新增
					tmp.setImportType("1");
				}
				resultList.add(tmp);
			}
		}
		return resultList;
	}

	@Override
	public int tmpToSave(DataInterfaceColumnsTmp record) {
		// TODO Auto-generated method stub
		return mapper.tmpToSave(record);
	}

	@Override
	public int tmpToSaveBatch(List<DataInterfaceColumnsTmp> list) {
		// TODO Auto-generated method stub
		return mapper.tmpToSaveBatch(list);
	}

	@Override
	public int deleteBatch(List<DataInterfaceColumnsTmp> list) {
		// TODO Auto-generated method stub
		return mapper.deleteBatch(list);
	}

	@Override
	public int updateBatch(List<DataInterfaceColumns> list) {
		// TODO Auto-generated method stub
		return mapper.updateBatch(list);
	}

	@Override
	@Transactional
	public void batchImport(ParamEntity param) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void batchImportNew(ParamEntity param) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	@Transactional
	public String batchImportFinal(ParamEntity param) throws Exception {
		// TODO Auto-generated method stub
		List<Object[]> tmpList=new ArrayList<Object[]>();
		List<Object[]> recordList=new ArrayList<Object[]>();
		List<Object[]> delList=new ArrayList<Object[]>();
		String [] tables = param.getTables();
		String dbType = param.getDbType();
		
		long time1 = new Date().getTime();
		String dataSrcAbbr = "";
		String batchNo = "";
		for(String table:tables) {
			if(!table.contains("-"))
				continue;
			String[] split = table.split("-");
			if(split.length!=6)
				continue;
			dataSrcAbbr = split[0];
			String dataInterfaceNo = split[1];
			String importType = split[2];
			batchNo = split[3];
			String columnNo = split[4];
			String dataInterfaceName = split[5];
			
			if("1".equals(importType)) {
				//导入类型是新增直接插入正式表
				tmpList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo,columnNo,dataInterfaceName});
				delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo,columnNo,dataInterfaceName});
			}else if("2".equals(importType)) {
				//导入类型修改先将正式表原记录置为失效
				recordList.add(new Object[] {new Date(),dataSrcAbbr,dataInterfaceNo,columnNo,dataInterfaceName,TimeUtil.getE()});
				tmpList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo,columnNo,dataInterfaceName});
				delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo,columnNo,dataInterfaceName});
			}else if("3".equals(importType)){
				delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo,columnNo,dataInterfaceName});
			}
		}
		logger.info("导入字段开始,batchNo=["+batchNo+"],dataSrcAbbr=["+dataSrcAbbr+"],type=["+dbType+"]");
		String updateSql = "update data_interface_columns set e_date = ? "
				+ "where data_src_abbr = ? and data_interface_no = ? and column_no = ? and data_interface_name = ? and e_date >= ? ";
		StringBuffer sb = new StringBuffer();
		sb.append("insert into data_interface_columns (data_src_abbr, data_interface_no, data_interface_name,column_no, ");
		sb.append("column_name, data_type,data_format, nullable, replacenull,comma, column_comment, isbucket,s_date, e_date) ");
		sb.append("select tmp.data_src_abbr, tmp.data_interface_no, tmp.data_interface_name,tmp.column_no, ");
		sb.append("tmp.column_name, tmp.data_type,data_format, tmp.nullable, tmp.replacenull,tmp.comma, tmp.column_comment, tmp.isbucket,tmp.s_date, tmp.e_date ");
		sb.append("from data_interface_columns_tmp tmp  ");
		sb.append("where tmp.batch_no = ? and tmp.data_src_abbr = ? and tmp.data_interface_no = ? and tmp.column_no = ? and data_interface_name = ? ");
		String tmpToSaveSql = sb.toString();
		String delSql = "delete from data_interface_columns_tmp "
				+ "where 1=1 and batch_no = ? and data_src_abbr = ? and data_interface_no = ? and column_no = ? and data_interface_name = ? ";
		if("1".equals(dbType)){
			if(recordList.size()>0){
				int[] batchUpdate = jdbc.batchUpdate(updateSql, recordList);
				logger.info("batch update column success:"+batchUpdate.length);
			}
			if(tmpList.size()>0){
				int[] batchUpdate2 = jdbc.batchUpdate(tmpToSaveSql,tmpList);
				logger.info("batch insert column from tmp success:"+batchUpdate2.length);
			}
			if(delList.size()>0){
				int[] batchUpdate3 = jdbc.batchUpdate(delSql,delList);
				logger.info("batch delete column tmp success:"+batchUpdate3.length);
			}
		}else if("2".equals(dbType)){
			
			List<Object[]> tmpList2=new ArrayList<Object[]>();
			List<Object[]> recordList2=new ArrayList<Object[]>();
			List<Object[]> delList2=new ArrayList<Object[]>();
			
			ExcelUtil obj = ExcelUtil.getInstance();
			Map<String, String> columnMap = obj.getColumnMap(dataSrcAbbr);
			List<DataInterfaceColumns> list = new ArrayList<DataInterfaceColumns>();
			
			
			DataInterfaceColumnsTmp condition = new DataInterfaceColumnsTmp();
			condition.setBatchNo(batchNo);
			List<DataInterfaceColumnsTmp> queryAllTmp = mapper.queryAllTmp(condition);
			for(DataInterfaceColumnsTmp tmp:queryAllTmp){
				String key = tmp.getDataSrcAbbr()+tmp.getDataInterfaceNo()+tmp.getColumnNo()+tmp.getDataInterfaceName();
				if(columnMap!=null&&columnMap.containsKey(key)){
					if(!tmp.toStr().equalsIgnoreCase(columnMap.get(key))){//修改
						recordList2.add(new Object[] {new Date(),dataSrcAbbr,tmp.getDataInterfaceNo(),tmp.getColumnNo(),tmp.getDataInterfaceName(),TimeUtil.getE()});
						tmpList2.add(new Object[] {batchNo,dataSrcAbbr,tmp.getDataInterfaceNo(),tmp.getColumnNo(),tmp.getDataInterfaceName()});
					}
				}else{//新增
					DataInterfaceColumns data = new DataInterfaceColumns();
					data.setDataSrcAbbr(tmp.getDataSrcAbbr());
					data.setDataInterfaceNo(tmp.getDataInterfaceNo());
					data.setDataInterfaceName(tmp.getDataInterfaceName());
					data.setColumnNo(tmp.getColumnNo());
					data.setColumnName(tmp.getColumnName());
					data.setColumnComment(tmp.getColumnComment());
					data.setComma(tmp.getComma());
					data.setDataType(tmp.getDataType());
					data.setDataFormat(tmp.getDataFormat());
					data.setIsbucket(tmp.getIsbucket());
					data.setNullable(tmp.getNullable());
					data.setReplacenull(tmp.getReplacenull());
					data.setsDate(tmp.getsDate());
					data.seteDate(tmp.geteDate());
					list.add(data);
				}
			}
			//修改 原纪录update
			if(recordList2.size()>0){
				int[] batchUpdate = jdbc.batchUpdate(updateSql, recordList2);
				logger.info("all batch update column success:"+batchUpdate.length);
			}
			//修改 新纪录insert
			if(tmpList2.size()>0){
				int[] batchUpdate2 = jdbc.batchUpdate(tmpToSaveSql,tmpList2);
				logger.info("all batch insert column from tmp success:"+batchUpdate2.length);
			}
			//新增 新纪录insert
			if(list.size()>0){
				int batchInsertPro = mapper.batchInsertPro(list);
				logger.info("all batch insert column success:"+batchInsertPro);
			}
			//删除临时表delete
			String delSql2 = "delete from data_interface_columns_tmp where batch_no = ? ";
			delList2.add(new Object[]{batchNo});
			int[] batchUpdate3 = jdbc.batchUpdate(delSql2,delList2);
			logger.info("all batch delete column tmp success:"+batchUpdate3.length);
		}
		long time2 = new Date().getTime();
		logger.info("导入字段结束,导入用时："+(time2-time1));
		return dataSrcAbbr;
	}

}
