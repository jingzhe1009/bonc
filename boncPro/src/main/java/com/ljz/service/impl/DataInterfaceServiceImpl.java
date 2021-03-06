package com.ljz.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ljz.config.InfoConfig;
import com.ljz.constant.BoncConstant;
import com.ljz.entity.ParamEntity;
import com.ljz.mapper.DataInterface2procMapper;
import com.ljz.mapper.DataInterfaceColumnsMapper;
import com.ljz.mapper.DataInterfaceHistoryMapper;
import com.ljz.mapper.DataInterfaceMapper;
import com.ljz.mapper.DataInterfaceRecordsMapper;
import com.ljz.model.DataInterface;
import com.ljz.model.DataInterface2proc;
import com.ljz.model.DataInterface2procHistory;
import com.ljz.model.DataInterface2procTmp;
import com.ljz.model.DataInterfaceColumns;
import com.ljz.model.DataInterfaceColumnsHistory;
import com.ljz.model.DataInterfaceColumnsTmp;
import com.ljz.model.DataInterfaceHistory;
import com.ljz.model.DataInterfaceRecords;
import com.ljz.model.DataInterfaceRecordsDetail;
import com.ljz.model.DataInterfaceTmp;
import com.ljz.model.DataRvsdRecordTmp;
import com.ljz.model.Order;
import com.ljz.service.IDataInterfaceService;
import com.ljz.util.ExcelUtil;
import com.ljz.util.FileUtil;
import com.ljz.util.InsertDbProduceReturn;
import com.ljz.util.TestProduceReturn;
import com.ljz.util.TimeUtil;

@Service
public class DataInterfaceServiceImpl implements IDataInterfaceService{

	@Autowired
	DataInterfaceMapper mapper;

	@Autowired
	DataInterface2procMapper procMapper;

	@Autowired
	DataInterfaceHistoryMapper hisMapper;
	
	@Autowired
	DataInterfaceRecordsMapper recordsMapper;
	
	@Autowired
	DataInterfaceColumnsMapper colMapper;

	@Autowired
	JdbcTemplate jdbc;

	@Autowired
	TestProduceReturn testProduce;

	@Autowired
	InsertDbProduceReturn dbProduce;

	@Autowired
	InfoConfig config;
	
	@Autowired
    InfoImportService infoImportService;

	private static final Logger logger = LoggerFactory.getLogger(DataInterfaceServiceImpl.class);

	public String date = TimeUtil.getDateToString(TimeUtil.getTy());
	public String time = FileUtil.formatDate(new Date());
	public StringBuffer DMLDeclare = new StringBuffer();
	public StringBuffer DMLInsert = new StringBuffer();

    @Override
    public List<DataInterface> queryDsAndInfaceName(String dataSrc) {
        return  mapper.queryDsAndInfaceName(dataSrc);
    }

	@Override
	public List<DataInterface> queryAll(DataInterface record) {
		// TODO Auto-generated method stub
		return mapper.queryAll(record);
	}

	@Override
	public List<DataInterface> queryByList(String ds, String [] array) {
		// TODO Auto-generated method stub
		return mapper.queryByList(ds, array);
	}

	@Override
	public int insert(DataInterface record) {
		// TODO Auto-generated method stub
		return mapper.insertSelective(record);
	}

	@Override
	public int update(DataInterface record) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int delete(DataInterfaceTmp record) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(record);
	}

	@Override
	public List<DataInterface2proc> queryProc(DataInterface2proc record) {
		// TODO Auto-generated method stub
		return procMapper.queryAll(record);
	}

	@Override
	public int insertProc(DataInterface2proc record) {
		// TODO Auto-generated method stub
		return procMapper.insertSelective(record);
	}

	@Override
	public int updateProc(DataInterface2proc record) {
		// TODO Auto-generated method stub
		return procMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int deleteProc(DataInterface2procTmp record) {
		// TODO Auto-generated method stub
		return procMapper.deleteByPrimaryKey(record);
	}

	@Override
	public List<DataInterface> queryAllVersion(DataInterface record) {
		// TODO Auto-generated method stub
		List<DataInterface> list = mapper.queryAllVersion(record);
		List<DataInterface> returnList = new ArrayList<DataInterface>();
		for(int i=0;i<list.size();i++) {
			DataInterface data = list.get(i);
			data.setNum("v1."+(i));
			returnList.add(data);
		}
		return returnList;
	}

	/**
	 * ?????????????????????
	 */
	@Override
	public List<DataInterfaceTmp> queryAllTmp(DataInterfaceTmp record) {
		// TODO Auto-generated method stub
		int intUpdateNum=0;
		int intInsertNum=0;
		int intAllNum=0;
		ExcelUtil obj = ExcelUtil.getInstance();
		List<DataInterfaceTmp> resultList = new ArrayList<DataInterfaceTmp>();
		String dataSrcAbbr = record.getDataSrcAbbr();
		if(dataSrcAbbr!=null&&!"".equals(dataSrcAbbr)){
			logger.info("?????????????????????????????????"+record.getDataSrcAbbr());
			List<DataInterfaceTmp> queryAllTmp = mapper.queryAllTmp(record);
			Map<String,String> interfaceMap =obj.getInterfaceMap(record.getDataSrcAbbr());
			for(DataInterfaceTmp tmp:queryAllTmp){
				String key = tmp.getDataInterfaceName();
				if(interfaceMap!=null&&interfaceMap.containsKey(key)){
					if(tmp.toStr().equalsIgnoreCase(interfaceMap.get(key))){//?????????
						tmp.setImportType("3");
					}else{//??????
						tmp.setImportType("2");
						intUpdateNum++;
					}
				}else{//??????
					tmp.setImportType("1");
					intInsertNum++;
				}
				intAllNum++;
				resultList.add(tmp);
			}
			obj.put(dataSrcAbbr+"intUpdateNum", intUpdateNum);
			obj.put(dataSrcAbbr+"intInsertNum", intInsertNum);
			obj.put(dataSrcAbbr+"intAllNum", intAllNum);
		}
		return resultList;
	}

	@Override
	public int tmpToSave(DataInterfaceTmp record) {
		// TODO Auto-generated method stub
		return mapper.tmpToSave(record);
	}

	@Override
	public List<DataInterface2proc> queryAllVersionProc(DataInterface2proc record) {
		// TODO Auto-generated method stub
		List<DataInterface2proc> list = procMapper.queryAllVersion(record);
		List<DataInterface2proc> returnList = new ArrayList<DataInterface2proc>();
		for(int i=0;i<list.size();i++) {
			DataInterface2proc data = list.get(i);
			data.setNum("v1."+(i));
			returnList.add(data);
		}
		return returnList;
	}

	/**
	 * ?????????????????????????????????
	 */
	@Override
	public List<DataInterface2procTmp> queryAllTmpProc(DataInterface2procTmp record) {
		// TODO Auto-generated method stub
		int procUpdateNum=0;
		int procInsertNum=0;
		int procAllNum=0;
		List<DataInterface2procTmp> resultList = new ArrayList<DataInterface2procTmp>();
		String dataSrcAbbr = record.getDataSrcAbbr();
		if(dataSrcAbbr!=null&&!"".equals(dataSrcAbbr)){
			logger.info("?????????????????????????????????"+record.getDataSrcAbbr());
			ExcelUtil obj = ExcelUtil.getInstance();
			List<DataInterface2procTmp> queryAllTmp = procMapper.queryAllTmp(record);
			Map<String,String> procMap =obj.getProcMap(record.getDataSrcAbbr());
			for(DataInterface2procTmp tmp:queryAllTmp){
				if(procMap!=null&&procMap.containsKey(tmp.getDataSrcAbbr()+tmp.getDataInterfaceNo())){
					if(tmp.toStr().equals(procMap.get(tmp.getDataSrcAbbr()+tmp.getDataInterfaceNo()))){//?????????
						tmp.setImportType("3");
					}else{//??????
						tmp.setImportType("2");
						procUpdateNum++;
					}
				}else{//??????
					tmp.setImportType("1");
					procInsertNum++;
				}
				resultList.add(tmp);
				procAllNum++;
			}
			obj.put(dataSrcAbbr+"procUpdateNum", procUpdateNum);
			obj.put(dataSrcAbbr+"procInsertNum", procInsertNum);
			obj.put(dataSrcAbbr+"procAllNum", procAllNum);
		}
		return resultList;
	}

	@Override
	public int tmpToSaveProc(DataInterface2procTmp record) {
		// TODO Auto-generated method stub
		return procMapper.tmpToSave(record);
	}

	@Override
	public int tmpToSaveBatch(List<DataInterfaceTmp> list) {
		// TODO Auto-generated method stub
		return mapper.tmpToSaveBatch(list);
	}

	@Override
	public int deleteBatch(List<DataInterfaceTmp> list) {
		// TODO Auto-generated method stub
		return mapper.deleteBatch(list);
	}

	@Override
	public int updateBatch(List<DataInterface> list) {
		// TODO Auto-generated method stub
		return mapper.updateBatch(list);
	}

	/**
	 * ??????????????????
	 */
	@Override
	public List<DataInterface> queryModel(DataInterface record) {

		// TODO Auto-generated method stub
		List<DataInterface> queryAll = mapper.queryAll(record);
		//?????????????????????????????????

//		List<Map<String, Object>> queryIntNum = jdbc.queryForList("select data_interface_name,count(*) as bucket_number from data_interface_columns "
//				+"where data_src_abbr='"+record.getDataSrcAbbr()+"' and e_date='"+BoncConstant.CON_E_DATE+"'  group by data_interface_name");
//		//????????????sdata????????????
//		List<Map<String, Object>> sdataList = jdbc.queryForList("SELECT table_name FROM SYSTEM.tables_v WHERE database_name='"+BoncConstant.SDATA+"'");
//		//????????????odata????????????
//		List<Map<String, Object>> odataList= jdbc.queryForList("SELECT table_name FROM SYSTEM.tables_v WHERE database_name='"+BoncConstant.ODATA+"'");
//		List<DataInterface> resultList = new ArrayList<DataInterface>();
//		for(DataInterface data:queryAll){
//			for(Map<String,Object> map:queryIntNum){
//				String data_interface_name = (String)map.get("data_interface_name");
//				long bucket_number = (long)map.get("bucket_number");
//				if(data_interface_name.equalsIgnoreCase(data.getDataInterfaceName())){
//				data.setNum(bucket_number+"");
//				}
//			}
//			//????????????0????????????1?????????
//			String flag1 = "0";
//			for(int i=0;i<sdataList.size();i++){
//				Map<String, Object> map = sdataList.get(i);
//				String tb = (String) map.get("table_name");
//				if(tb.equalsIgnoreCase(data.getExtrnlTableName())){
//					data.setCondition("???????????????");
//					flag1 = "1";
//					break;
//				}else{
//					if(i==sdataList.size()-1&&"0".equals(flag1)){
//						data.setCondition("???????????????");
//					}
//				}
//			}
//			String flag2 = "0";
//			for(int i=0;i<odataList.size();i++){
//				Map<String,Object> map = odataList.get(i);
//				String tb = (String) map.get("table_name");
//				if(tb.equalsIgnoreCase(data.getIntrnlTableName())){
//					data.setCondition(data.getCondition()+",???????????????");
//					flag2 = "1";
//					break;
//				}else{
//					if(i==odataList.size()-1&&"0".equals(flag2)){
//						data.setCondition(data.getCondition()+",???????????????");
//					}
//				}
//			}
//			resultList.add(data);
//
//		}
		return queryAll;
	}
	
	public String createRollBackFile(String dataSrc) {

        DataInterface record = new DataInterface();
		String rollBackFilePath = config.getFilePath()+record.getDataSrcAbbr()+"_ROLLBACK_"+infoImportService.date+".sql";
		StringBuffer rollBackSb = null;

		//record??????????????????????????????????????????
        record.setDataSrcAbbr(dataSrc);
        record.seteDate(TimeUtil.getTw());

        // TODO Auto-generated method stub
        List<DataInterface> queryAll = mapper.queryAll(record);
        //?????????????????????????????????
//        List<Map<String, Object>> queryIntNum = jdbc.queryForList("select data_interface_name,count(*) as bucket_number from data_interface_columns "
//                +"where data_src_abbr='"+record.getDataSrcAbbr()+"' and e_date='"+BoncConstant.CON_E_DATE+"'  group by data_interface_name");
//        //????????????sdata????????????
//        List<Map<String, Object>> sdataList = jdbc.queryForList("SELECT table_name FROM SYSTEM.tables_v WHERE database_name='" +BoncConstant.SDATA+"'");
//        //????????????odata????????????
//        List<Map<String, Object>> odataList = jdbc.queryForList("SELECT table_name FROM SYSTEM.tables_v WHERE database_name='" +BoncConstant.ODATA+"'");
//        List<DataInterface> resultList = new ArrayList<DataInterface>();
//
////        String rollBackFilePath = config.getFilePath()+record.getDataSrcAbbr()+"_ROLLBACK_"+infoImportService.date+".sql";
////        List<String> rollBackList = new ArrayList<String>();
////        StringBuffer rollBackSb = null;
//
//        for(DataInterface data:queryAll){
//            for(Map<String,Object> map:queryIntNum){
//                String data_interface_name = (String)map.get("data_interface_name");
//                long bucket_number = (long)map.get("bucket_number");
//                if(data_interface_name.equalsIgnoreCase(data.getDataInterfaceName())){
//                    data.setNum(bucket_number+"");
//                }
//            }
//            //????????????0????????????1?????????
//            String flag1 = "0";
//            for(int i=0;i<sdataList.size();i++){
//                Map<String, Object> map = sdataList.get(i);
//                String tb = (String) map.get("table_name");
//                if(tb.equalsIgnoreCase(data.getExtrnlTableName())){
//                    //?????????????????????
//
//                    data.setCondition("???????????????");
//                    flag1 = "1";
//                    String rollBackExternalSql = "--"+tb+"--\n"
//                            +"CREATE EXTERNAL TABLE SDATA_OLTP."+tb+"_"+"_"+infoImportService.date+" LIKE SDATA_OLTP."+tb+";\n"
//                            +"INSERT INTO SDATA_OLTP."+tb+"_"+"_"+infoImportService.date+ " SELECT * FROM SDATA_OLTP."+tb+";\n"
//                            +"DROP TABLE IF EXISTS SDATA_OLTP."+tb+";\n"
//                            +"CREATE EXTERNAL TABLE SDATA_OLTP."+tb+"\n"
//                            +//?????????
//
//                            ";\n"
//                            +"INSERT INTO SDATA_OLTP."+tb+" SELECT * FROM SDATA_OLTP."+tb+"_"+"_"+infoImportService.date+";\n\n"
//                            ;
////                    rollBackList.add(rollBackExternalSql);
//                    rollBackSb.append(rollBackExternalSql);
////                    try {
////                        FileUtil.write(rollBackFilePath, rollBackExternalSql,config.getFileEncode());
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
//                    break;
//                }else{
//                    if(i==sdataList.size()-1&&"0".equals(flag1)){
//                        data.setCondition("???????????????");
//                    }
//                }
//            }
//            String flag2 = "0";
//            for(int i=0;i<odataList.size();i++){
//                Map<String,Object> map = odataList.get(i);
//                String tb = (String) map.get("table_name");
//                if(tb.equalsIgnoreCase(data.getIntrnlTableName())){
//                    data.setCondition(data.getCondition()+",???????????????");
//                    flag2 = "1";
//                    String rollBackSql = "--"+data.getDataInterfaceName()+"--\n"
//                            +"CREATE TABLE IF NOT EXISTS ODATA."+tb+"_"+"_"+infoImportService.date+" LIKE ODATA."+tb+";\n"
//                            +"INSERT INTO ODATA."+tb+"_"+"_"+infoImportService.date+ " SELECT * FROM ODATA."+tb+";\n"
//                            +"DROP TABLE IF EXISTS ODATA."+tb+";\n"
//                            +"CREATE TABLE ODATA."+tb+"\n"
//                            //?????????
//
//                            +";\n"
//                            +"INSERT INTO ODATA."+tb+" SELECT * FROM ODATA."+tb+"_"+"_"+infoImportService.date+";\n\n"
//                            ;
//                    rollBackSb.append(rollBackSql);
////                    try {
////                        FileUtil.write(rollBackFilePath, rollBackSql,config.getFileEncode());
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
//                    break;
//                }else{
//                    if(i==odataList.size()-1 && "0".equals(flag2)){
//                        data.setCondition(data.getCondition()+",???????????????");
//                    }
//                }
//            }
//            resultList.add(data);
//        }
        if (rollBackSb != null){
            try {
                FileUtil.write(rollBackFilePath, rollBackSb.toString(),config.getFileEncode());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return rollBackFilePath;
    }

	@Override
	public void batchImport(ParamEntity param) throws Exception{
		// TODO Auto-generated method stub
	}

	/**
	 * ??????????????????/????????????
	 */
	@Override
	@Transactional
	public String batchImportFinal(ParamEntity param) throws Exception {
		// TODO Auto-generated method stub
		long start = new Date().getTime();
		String dataSrcAbbr = "";
		//???????????????
		String batchNo = "";
		List<Object[]> tmpList=new ArrayList<Object[]>();
		List<Object[]> recordList=new ArrayList<Object[]>();
		List<Object[]> delList=new ArrayList<Object[]>();
		String [] tables = param.getTables();
		//methodType ???????????? 1????????? 2???????????????
		String methodType = param.getDbType();
		for(String table:tables) {
			if(!table.contains("-"))
				continue;
			String[] split = table.split("-");
			if(split.length!=5)
				continue;
			dataSrcAbbr = split[0];
			String dataInterfaceNo = split[1];
			String importType = split[2];
			batchNo = split[3];
			String dataInterfaceName = split[4];

			if("1".equals(importType)) {//???????????????1.?????????????????????????????????????????????????????????
				tmpList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo,dataInterfaceName});
				delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo,dataInterfaceName});
			}else if("2".equals(importType)) {
				//????????????2.???????????????????????????????????????????????????????????????????????????????????????????????????????????????
				recordList.add(new Object[] {new Date(),dataSrcAbbr,dataInterfaceNo,dataInterfaceName,TimeUtil.getTw()});
				tmpList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo,dataInterfaceName});
				delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo,dataInterfaceName});
			}else if("3".equals(importType)){//????????????3.??????????????????????????????????????????????????????
				delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo,dataInterfaceName});
			}
		}
		logger.info("??????????????????...?????????=["+dataSrcAbbr+"],?????????=["+batchNo+"],????????????=["+methodType+"]");
		//??????sql
		String updateSql = "update data_interface set e_date = ? "
				+ "where data_src_abbr = ? and data_interface_no = ? and data_interface_name = ? and e_date >= ? ";
		//?????????insert????????????sql
		StringBuffer sb = new StringBuffer();
		sb.append("insert into data_interface (data_src_abbr, data_interface_no, data_interface_name,data_interface_desc, ");
		sb.append("data_load_freq, data_load_mthd,filed_delim, line_delim, extrnl_database_name,intrnl_database_name, ");
		sb.append("extrnl_table_name,intrnl_table_name,table_type, bucket_number, s_date, e_date) ");
		sb.append("select tmp.data_src_abbr, tmp.data_interface_no, tmp.data_interface_name,tmp.data_interface_desc, ");
		sb.append("tmp.data_load_freq,tmp.data_load_mthd,tmp.filed_delim, tmp.line_delim, tmp.extrnl_database_name, tmp.intrnl_database_name, ");
		sb.append("tmp.extrnl_table_name,tmp.intrnl_table_name,tmp.table_type, tmp.bucket_number, tmp.s_date, tmp.e_date ");
		sb.append("from data_interface_tmp tmp  ");
		sb.append("where tmp.batch_no = ? and tmp.data_src_abbr = ? and tmp.data_interface_no = ? and data_interface_name = ? ");
		//??????sql
		String delSql = "delete from data_interface_tmp "
				+ "where batch_no = ? and data_src_abbr = ? and data_interface_no = ? and data_interface_name = ? ";
		if("1".equals(methodType)){//??????
			if(recordList.size()>0){
				int[] batchUpdate = jdbc.batchUpdate(updateSql, recordList);
				logger.info("batch update interface success:"+batchUpdate.length);
			}
			if(tmpList.size()>0){
				int[] batchUpdate2 = jdbc.batchUpdate(sb.toString(),tmpList);
				logger.info("batch insert interface from tmp success:"+batchUpdate2.length);
			}
			if(delList.size()>0){
				int[] batchUpdate3 = jdbc.batchUpdate(delSql,delList);
				logger.info("batch delete interface tmp success:"+batchUpdate3.length);
			}
		}else if("2".equals(methodType)){//????????????

			List<Object[]> tmpList2=new ArrayList<Object[]>();
			List<Object[]> recordList2=new ArrayList<Object[]>();
			List<Object[]> delList2=new ArrayList<Object[]>();
			//?????????????????????????????????????????????map??????????????????
			ExcelUtil obj = ExcelUtil.getInstance();
			Map<String, String> interfaceMap = obj.getInterfaceMap(dataSrcAbbr);
			List<DataInterface> list = new ArrayList<DataInterface>();

			DataInterfaceTmp condition = new DataInterfaceTmp();
			condition.setBatchNo(batchNo);
			List<DataInterfaceTmp> queryAllTmp = mapper.queryAllTmp(condition);
			for(DataInterfaceTmp tmp:queryAllTmp){
				String key = tmp.getDataSrcAbbr()+tmp.getDataInterfaceNo()+tmp.getDataInterfaceName();
				if(interfaceMap!=null&&interfaceMap.containsKey(key)){
					if(!tmp.toStr().equalsIgnoreCase(interfaceMap.get(key))){//??????
						recordList2.add(new Object[] {new Date(),tmp.getDataSrcAbbr(),tmp.getDataInterfaceNo(),tmp.getDataInterfaceName(),TimeUtil.getTw()});
						tmpList2.add(new Object[] {batchNo,dataSrcAbbr,tmp.getDataInterfaceNo(),tmp.getDataInterfaceName()});
					}
				}else{//??????
					DataInterface data = new DataInterface();
					data.setDataSrcAbbr(tmp.getDataSrcAbbr());
					data.setDataInterfaceNo(tmp.getDataInterfaceNo());
					data.setDataInterfaceName(tmp.getDataInterfaceName());
					data.setDataInterfaceDesc(tmp.getDataInterfaceDesc());
					data.setDataLoadFreq(tmp.getDataLoadFreq());
					data.setDataLoadMthd(tmp.getDataLoadMthd());
					data.setFiledDelim(tmp.getFiledDelim());
					data.setLineDelim(tmp.getLineDelim());
					data.setExtrnlDatabaseName(tmp.getExtrnlDatabaseName());
					data.setIntrnlDatabaseName(tmp.getIntrnlDatabaseName());
					data.setExtrnlTableName(tmp.getExtrnlTableName());
					data.setIntrnlTableName(tmp.getIntrnlTableName());
					data.setTableType(tmp.getTableType());
					data.setBucketNumber(tmp.getBucketNumber());
					data.setsDate(tmp.getsDate());
					data.seteDate(tmp.geteDate());
					list.add(data);
				}
			}

			if(recordList2.size()>0){
				int[] batchUpdate = jdbc.batchUpdate(updateSql, recordList2);
				logger.info("all batch update interface success:"+batchUpdate.length);
			}
			if(tmpList2.size()>0){
				int[] batchUpdate2 = jdbc.batchUpdate(sb.toString(),tmpList2);
				logger.info("all batch insert interface from tmp success:"+batchUpdate2.length);
			}
			if(list.size()>0){
				int batchInsertPro = mapper.batchInsertPro(list);
				logger.info("all batch insert interface success:"+batchInsertPro);
			}
			//???????????????????????????????????????
			String delSql2 = "delete from data_interface_tmp where batch_no = ? ";
			delList2.add(new Object[]{batchNo});
			int[] batchUpdate3 = jdbc.batchUpdate(delSql2,delList2);
			logger.info("all batch delete interface tmp success:"+batchUpdate3.length);
		}
		long end = new Date().getTime();
		logger.info("????????????,???????????????"+(end-start));
		return dataSrcAbbr;
	}

	/**
	 * ??????????????????????????????/????????????
	 */
	@Override
	@Transactional
	public String batchImportProcFinal(ParamEntity param) throws Exception {
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
			if(split.length!=4)
				continue;
			dataSrcAbbr = split[0];
			String dataInterfaceNo = split[1];
			String importType = split[2];
			batchNo = split[3];

			if("1".equals(importType)) {
				//??????????????????????????????????????????
				tmpList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo});
				delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo});
			}else if("2".equals(importType)) {
				//??????????????????????????????????????????????????????
				recordList.add(new Object[] {new Date(),dataSrcAbbr,dataInterfaceNo,TimeUtil.getTw()});
				tmpList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo});
				delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo});
			}else if("3".equals(importType)){
				delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo});
			}
		}
		logger.info("????????????????????????...dataSrcAbbr=["+dataSrcAbbr+"],batchNo=["+batchNo+"],type=["+param.getDbType()+"]");
		String updateSql = "update data_interface2proc set e_date = ? where data_src_abbr = ? and data_interface_no = ? and e_date >= ? ";
		StringBuffer sb = new StringBuffer();
		sb.append("insert into data_interface2proc (data_src_abbr, data_interface_no,");
		sb.append("proc_database_name,proc_name, ");
		sb.append("s_date, e_date) ");
		sb.append("SELECT tmp.data_src_abbr, tmp.data_interface_no, ");
		sb.append("tmp.proc_database_name,tmp.proc_name, ");
		sb.append("tmp.s_date, tmp.e_date ");
		sb.append("FROM data_interface2proc_tmp tmp  ");
		sb.append("WHERE tmp.batch_no = ? and tmp.data_src_abbr = ? and tmp.data_interface_no = ? ");
		String tmpToSaveSql = sb.toString();
		String delSql = "delete from data_interface2proc_tmp where 1=1 and batch_no = ? and data_src_abbr = ? and data_interface_no = ? ";
		if("1".equals(dbType)){
			if(recordList.size()>0){
				int[] batchUpdate = jdbc.batchUpdate(updateSql, recordList);
				logger.info("batch update proc success:"+batchUpdate.length);
			}
			if(tmpList.size()>0){
				int[] batchUpdate2 = jdbc.batchUpdate(tmpToSaveSql,tmpList);
				logger.info("batch insert proc from tmp success:"+batchUpdate2.length);
			}
			if(delList.size()>0){
				int[] batchUpdate3 = jdbc.batchUpdate(delSql,delList);
				logger.info("batch delete proc tmp success:"+batchUpdate3.length);
			}
		}else if("2".equals(dbType)){

			List<Object[]> tmpList2=new ArrayList<Object[]>();
			List<Object[]> recordList2=new ArrayList<Object[]>();
			List<Object[]> delList2=new ArrayList<Object[]>();

			ExcelUtil obj = ExcelUtil.getInstance();
			Map<String, String> procMap = obj.getProcMap(dataSrcAbbr);
			List<DataInterface2proc> list = new ArrayList<DataInterface2proc>();

			DataInterface2procTmp condition = new DataInterface2procTmp();
			condition.setBatchNo(batchNo);
			List<DataInterface2procTmp> queryAllTmp = procMapper.queryAllTmp(condition);
			for(DataInterface2procTmp tmp:queryAllTmp){
				if(procMap!=null&&procMap.containsKey(tmp.getDataSrcAbbr()+tmp.getDataInterfaceNo())){
					if(!tmp.toStr().equals(procMap.get(tmp.getDataSrcAbbr()+tmp.getDataInterfaceNo()))){
						recordList2.add(new Object[] {new Date(),tmp.getDataSrcAbbr(),tmp.getDataInterfaceNo(),TimeUtil.getTw()});
						tmpList2.add(new Object[] {batchNo,dataSrcAbbr,tmp.getDataInterfaceNo()});
					}
				}else{
					DataInterface2proc data = new DataInterface2proc();
					data.setDataSrcAbbr(tmp.getDataSrcAbbr());
					data.setDataInterfaceNo(tmp.getDataInterfaceNo());
					data.setProcDatabaseName(tmp.getProcDatabaseName());
					data.setProcName(tmp.getProcName());
					data.setsDate(tmp.getsDate());
					data.seteDate(tmp.geteDate());
					list.add(data);
				}
			}

			if(recordList2.size()>0){
				int[] batchUpdate = jdbc.batchUpdate(updateSql, recordList2);
				logger.info("all batch update proc success:"+batchUpdate.length);
			}
			if(tmpList2.size()>0){
				int[] batchUpdate2 = jdbc.batchUpdate(tmpToSaveSql,tmpList2);
				logger.info("all batch insert proc from tmp success:"+batchUpdate2.length);
			}
			if(list.size()>0){
				int batchInsertPro = procMapper.batchInsertPro(list);
				logger.info("all batch insert proc success:"+batchInsertPro);
			}
			String delSql2 = "delete from data_interface2proc_tmp where batch_no = ? ";
			delList2.add(new Object[]{batchNo});
			int[] batchUpdate3 = jdbc.batchUpdate(delSql2,delList2);
			logger.info("all batch delete proc tmp success:"+batchUpdate3.length);
		}
		long time2 = new Date().getTime();
		logger.info("????????????,???????????????"+(time2-time1));
		return dataSrcAbbr;
	}

	/**
	 * ????????????????????????
	 */
	@Override
	public Map<String,String> createFile(List<String> list,ParamEntity param) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		List<String> sucList = new ArrayList<String>();
		List<String> failList = new ArrayList<String>();
		//????????????????????????????????????
		ExecutorService es = Executors.newFixedThreadPool(100);
		// TODO Auto-generated method stub
		for(String table:list) {
			//??????lambda??????runnable??????
			Runnable task = ()->{
				long start = new Date().getTime();
				logger.info(Thread.currentThread().getName()+"?????????????????????????????????,?????????");
				try {
					Order order = testProduce.getSql(table,param.getDataSrcAbbr());
					sucList.add(order.getSql1()+"\n"+order.getSql2()+"\n");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					failList.add(table);
					e.printStackTrace();
				}
				long end = new Date().getTime();
				logger.info("??????????????????:"+(end-start)+"??????");
			};
			es.submit(task);
			logger.info("?????????????????????");
		}
		String filePath="";
		String DMLFilePath="";

		try {
//			filePath = config.getFilePath()+"test"+time+"/"+param.getDataSrcAbbr()+"_DDL_"+date+".sql";
//			DMLFilePath = config.getFilePath()+"test"+time+"/"+param.getDataSrcAbbr()+"_DML_"+date+".sql";
			filePath = config.getFilePath()+param.getDataSrcAbbr()+"_DDL_"+date+".sql";
			DMLFilePath = config.getFilePath()+param.getDataSrcAbbr()+"_DML_"+date+".sql";

//			logger.info("??????????????????:"+filePath);
			logger.info("??????????????????:"+DMLFilePath);

			int i =0;
//			reWriteFile(list, sucList,failList,filePath, i);
			FileUtil.write(filePath, DMLInsert.toString(),config.getFileEncode());
			FileUtil.write(DMLFilePath, DMLDeclare.toString(),config.getFileEncode());

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			map.put("msgCode", "1111");
			map.put("msgData", "??????????????????,????????????:\n"/*+filePath+"\n"*/+DMLFilePath+"\n????????????:"+config.getFileEncode());
			return map;
		} finally {
			es.shutdown();
		}
		if(failList.size()>0){
			String str = "??????"+failList.toString()+"????????????????????????,??????????????????"+failList.size();
			map.put("msgCode", "1111");
			map.put("msgData", str);
			return map;
		}
		//??????
		String str = "??????????????????????????????\n??????:"+filePath+"\n??????:"+list.size()+"?????????:"+list.toString()+"\n";
		map.put("msgCode", "0000");
		map.put("idx", param.getDataSrcAbbr());
		map.put("msgData", str);
		map.put("filePath", filePath);
		map.put("DMLFilePath",DMLFilePath);
		return map;
	}
	//????????????????????????????????????????????????????????????????????????????????????
	private void reWriteFile(List<String> list,List<String> sucList,List<String> failList,String filePath,int i) throws Exception{
		i++;
		Thread.sleep(500);
		if(list.size()==sucList.size()+failList.size()){
			String totalSql="";
			for(String sql :sucList){
				totalSql = totalSql+sql;
			}
			if(!"".equals(totalSql)){
				FileUtil.write(filePath, totalSql,config.getFileEncode());
				logger.info("write file success");
			}
		}else{
			if(i>120){
				//???????????????,????????????
			}else{
				logger.info("waiting for proc running finish"+i);
				reWriteFile(list, sucList,failList,filePath, i);
			}
		}
	}

	/**
	 * ????????????
	 */
	@Override
	public Map<String, String> insertDb(List<String> list,List<String> hasList,ParamEntity param)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		List<String> sucTable = new ArrayList<String>();
		List<String> failTable = new ArrayList<String>();
		//????????????????????????????????????
		ExecutorService es = Executors.newFixedThreadPool(100);
		//?????????
		for(String table:list) {
			//??????lambda??????runnable??????
			Runnable task = ()->{
				logger.info(Thread.currentThread().getName()+"?????????????????????????????????,?????????");
				try {
					long start2 = new Date().getTime();
					Order order = dbProduce.getSql(table,param.getDataSrcAbbr());
					if(order.getSql1()==null||"".equals(order.getSql1())) {
						System.out.println(order.getSql1());
					}
					if(order.getSql2()==null||"".equals(order.getSql2())) {
						System.out.println(order.getSql2());
					}
					long end2 = new Date().getTime();
					logger.info("??????????????????:"+(end2-start2)+"??????");
					logger.info(table+"????????????");
					sucTable.add(table);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					//logger.info(table+"????????????,1.????????????????????????????????????\n2.?????????????????????\n3.?????????????????????????????????\n4.???????????????????????????????????????????????????");
					logger.info(table+"????????????");
					failTable.add(table);
				}
				//??????submit?????????????????????????????????
			};
			es.submit(task);
			logger.info("?????????????????????");
		}
		//System.out.println("cacheMap="+cacheMap.keySet());
		int i =0;
		complete(list,sucTable,failTable,i);
		String str = "????????????????????????,???????????????????????????!?????????????????????:"+list.toString()+",??????:"+list.size()+"?????????\n";
		if(failTable.size()>0){
			str =str +"????????????"+failTable.toString()+"????????????,??????:"+failTable.size()+"\n1.????????????????????????????????????\n2.?????????????????????\n3.?????????????????????????????????\n4.?????????????????????????????????????????????????????????\n";
		}
		if(hasList.size()>0){
			str = str +"??????"+hasList.toString()+"?????????,???????????????????????????????????????????????????";
		}
		es.shutdown();
		map.put("msgData", str);
		map.put("msgCode", "0000");
		map.put("idx", param.getDataSrcAbbr());
		return map;
	}

	//????????????????????????
	private void complete(List<String> list,List<String> sucTable,List<String> failTable,int i) throws Exception{
		i++;
		Thread.sleep(500);
		if(list.size()==sucTable.size()+failTable.size()){
			//???????????????????????????=????????????+????????????????????????????????????????????????
		}else{
			if(i>120){
				//???????????????,????????????
			}else{
				logger.info("waiting for proc running finish"+i);
				complete(list,sucTable,failTable,i);
			}
		}
	}
	
	@Override
	public List<DataInterfaceHistory> queryInterfaceCompare(DataInterfaceHistory record) {
		List<Map<String, Object>> intHistoryList = jdbc.queryForList("select * from  data_interface where e_date = '3000-12-31' and data_src_abbr='"+record.getDataSrcAbbr()+"' ");
		if(intHistoryList.size()<1) {//????????????????????????
			return hisMapper.queryFirst(record);
		}
		List<DataInterfaceHistory> historyList = hisMapper.queryAll(record);
		List<DataInterfaceHistory> resultList = new ArrayList<DataInterfaceHistory>();
		ExcelUtil obj = ExcelUtil.getInstance();
		try {
			Map<String,Object> msgMap = new HashMap<String,Object>();
			DataInterfaceHistory tmp = null;
			for(DataInterfaceHistory data:historyList) {
				String red = "";
				if("0".equals(data.getFlag())) {//?????????
					tmp = data;
				}else if("1".equals(data.getFlag())) {//?????????
					if(!data.getDataInterfaceName().equals(tmp.getDataInterfaceName())) {
						tmp.setFlag("0");
						resultList.add(tmp);
						DataInterfaceHistory del =new DataInterfaceHistory();
						del.setFlag("4");
						resultList.add(del);//??????
					}
					List msgList = new ArrayList();
					DataInterfaceTmp intRecord = new DataInterfaceTmp();
					intRecord.setBatchNo(record.getExptSeqNbr());
					intRecord.setDataInterfaceName(data.getDataInterfaceName());
					Map<String,String> interfaceMap =obj.getInterfaceMap(record.getDataSrcAbbr());
					String key = data.getDataInterfaceName();
					if(interfaceMap!=null&&interfaceMap.containsKey(key)){
						if(data.toStr().equalsIgnoreCase(interfaceMap.get(key))){//?????????
							//????????????
					        if("1".equals(data.getFlag())) {
					        	DataInterfaceColumnsTmp columnRecord = new DataInterfaceColumnsTmp();
					        	columnRecord.setDataSrcAbbr(data.getDataSrcAbbr());
					        	columnRecord.setDataInterfaceNo(data.getDataInterfaceNo());
					        	columnRecord.setBatchNo(record.getExptSeqNbr());
								List<DataInterfaceColumnsTmp> queryAllTmpCol = colMapper.queryAllTmp(columnRecord);
								Map<String,String> columnMap =obj.getColumnMap(record.getDataSrcAbbr());
								for(DataInterfaceColumnsTmp colTmp:queryAllTmpCol){
									String colKey = colTmp.getDataInterfaceName()+colTmp.getColumnNo();
									if(columnMap!=null&&columnMap.containsKey(colKey)){
										if(colTmp.toStr().equalsIgnoreCase(columnMap.get(colKey))){//?????????
										}else{//??????
					                		data.setFlag("2");//??????
					                		msgList.add("????????????["+colTmp.getColumnComment()+"]");
					                		//break;
										}
									}else{//??????
				                		data.setFlag("2");//??????
				                		msgList.add("????????????["+colTmp.getColumnComment()+"]");
				                		//break;
									}
								}
					        }
						}else{//??????
					        Field[] fields = data.getClass().getDeclaredFields();
							for(int i=0; i<fields.length; i++){  
					            Field f = fields[i];  
					            f.setAccessible(true);  
					            System.out.println("?????????:" + f.getName() + " ?????????:" + f.get(data));  
					            Field[] fields2 = tmp.getClass().getDeclaredFields();
					            if("red".equals(f.getName())||"flag".equals(f.getName())||"serialVersionUID".equals(f.getName()))
				                	continue;
					            for(int j=0; j<fields2.length; j++){  
					                Field f2 = fields2[j];  
					                f2.setAccessible(true);  
					                System.out.println("tmp?????????:" + f2.getName() + " tmp?????????:" + f2.get(tmp)); 
					                if(f.getName()==null||f2.getName()==null||f.get(data)==null||f2.get(tmp)==null)
					                	continue;
					                if(f.getName().equals(f2.getName())) {
					                	if(f.get(data)!=f2.get(tmp)&&!f.get(data).equals(f2.get(tmp))) {
					                		red +="'"+f.get(data)+"',";
					                		msgList.add("??????????????????["+f.getName()+"]???"+f.get(data));
					                	}
					                }
					            }
					        }
							data.setFlag("2");
						}

						//??????????????????
						if(tmp!=null&&tmp.getProcName()!=null) {
							if(data==null||tmp.getProcName()==null||!data.getProcName().equals(tmp.getProcName())) {
								data.setFlag("2");
								red +="'"+data.getProcName()+"',";
							}
						}
					}else{//??????
						DataInterfaceHistory add = new DataInterfaceHistory();
						add.setFlag("0");
						resultList.add(add);
						data.setFlag("3");
						resultList.add(data);//??????
						msgList.add("????????????"+data.getDataInterfaceDesc());
					}
					if("2".equals(data.getFlag())) {//??????
						data.setRed(red);
						resultList.add(tmp);
						resultList.add(data);
					}
					msgMap.put(data.getDataInterfaceName(), msgList);
					//tmp = null;
				}
			}
			obj.put(record.getDataSrcAbbr()+"msgMap", msgMap);
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		//record.setExptSeqNbr("1.0.0");
		//List<DataInterfaceHistory> historyList = hisMapper.queryAll(record);
		return resultList;
	}
//					if(tmp==null) {
//						data.setFlag("3");//??????
//					}else {
						//????????????
//						Class cls = data.getClass();  
//				        Field[] fields = cls.getDeclaredFields();  
//				        for(int i=0; i<fields.length; i++){  
//				            Field f = fields[i];  
//				            f.setAccessible(true);  
//				            System.out.println("?????????:" + f.getName() + " ?????????:" + f.get(data));  
//				            Class cls2 = tmp.getClass();  
//				            Field[] fields2 = cls2.getDeclaredFields(); 
//				            if("flag".equals(f.getName())||"serialVersionUID".equals(f.getName()))
//			                	continue;
//				            for(int j=0; j<fields2.length; j++){  
//				                Field f2 = fields2[j];  
//				                f2.setAccessible(true);  
//				                System.out.println("tmp?????????:" + f2.getName() + " tmp?????????:" + f2.get(tmp)); 
//				                if(f.getName()==null||f2.getName()==null||f.get(data)==null||f2.get(tmp)==null)
//				                	continue;
//				                if(f.getName().equals(f2.getName())) {
//				                	if(f.get(data)!=f2.get(tmp)&&!f.get(data).equals(f2.get(tmp))) {
//				                		red +=f.get(data)+",";
//				                		data.setFlag("2");//??????
//				                	}
//				                }
//				            }
//				        }
				        //????????????
//				        if("1".equals(data.getFlag())) {
//				        	DataInterfaceColumnsTmp columnRecord = new DataInterfaceColumnsTmp();
//				        	columnRecord.setDataSrcAbbr(data.getDataSrcAbbr());
//				        	columnRecord.setDataInterfaceNo(data.getDataInterfaceNo());
//				        	columnRecord.setBatchNo(record.getExptSeqNbr());
//							List<DataInterfaceColumnsTmp> queryAllTmpCol = colMapper.queryAllTmp(columnRecord);
//							Map<String,String> columnMap =obj.getColumnMap(record.getDataSrcAbbr());
//							for(DataInterfaceColumnsTmp colTmp:queryAllTmpCol){
//								String colKey = colTmp.getDataInterfaceName()+colTmp.getColumnNo();
//								if(columnMap!=null&&columnMap.containsKey(colKey)){
//									if(colTmp.toStr().equalsIgnoreCase(columnMap.get(colKey))){//?????????
//									}else{//??????
//				                		data.setFlag("2");//??????
//				                		break;
//									}
//								}else{//??????
//			                		data.setFlag("2");//??????
//			                		break;
//								}
//							}
//				        	String sql = "SELECT a.nullable as A1,b.nullable B1,a.replacenull A2,b.replacenull B2 FROM data_interface_columns_tmp a \r\n"
//					        		+ "inner join data_interface_columns b "
//					        		+ "on a.data_interface_name=b.data_interface_name\r\n "
//					        		+ "and a.column_no=b.column_no\r\n"
//					        		+ "and a.column_name=b.column_name\r\n"
//					        		+ "and a.data_type=b.data_type\r\n"
//					        		+ "and a.data_format=b.data_format\r\n"
//					        		+ "and a.comma=b.comma\r\n"
//					        		+ "and a.nullable=b.nullable\r\n"
//					        		+ "and a.replacenull=b.replacenull\r\n"
//					        		+ "and a.column_comment=b.column_comment\r\n"
//					        		+ "and a.isbucket=b.isbucket\r\n"
//					        		+ "and a.e_date=b.e_date "
//					        		+ "where a.data_interface_name = '"+data.getDataInterfaceName()+"' and a.e_date='3000-12-31' and a.batch_no='"+record.getExptSeqNbr()+"'";
//				        	List<Map<String, Object>> list =jdbc.queryForList(sql);
//				        	System.out.println(list.size());
//				        	if(list.size()<1) {
//				        		data.setFlag("2");//??????
//				        	}else {
////				        		for(Map<String, Object> map:list) {
////				        			int A1 =(int) map.get("A1");
////				        			int B1 =(int) map.get("B1");
////				        			int A2 =(int) map.get("A2");
////				        			int B2 =(int) map.get("B2");
////				        			if(A1==B1&&A2==B2) {
////				        				
////				        			}else {
////				        				data.setFlag("2");//??????
////				        			}
////				        		}
//				        	}
							/*
							 * for(Map<String, Object> map:list) { int count = (int) map.get("count");
							 * if(count!=0) { data.setFlag("2");//?????? } }
							 */
					        	
//				        }

//				        tmp = null;
//					}
				
	private String getNewId(String id) {
		String newId = "";
		if(id!=null&&!"".equals(id)) {
			String pre=id.substring(0,id.length()-1);
			String nex=id.substring(id.length()-1,id.length());
			int newNex = Integer.parseInt(nex)+1;
			newId = pre+newNex;
		}
		return newId;
	}
	
	/**
	 * ????????????
	 */
	@Override
	@Transactional
	public String saveAll(ParamEntity param) throws Exception {
		// TODO Auto-generated method stub
		Map<String,String> mapResult = new HashMap<>();
		String dataSrcAbbr = param.getDataSrcAbbr();
		String batchNo = param.getBatchNo();//???????????????
		String needVrsnNbr = "";//?????????
//        String needVrsnNbr = param.getNeedVrsnNbr();//?????????
        String exptSeqNbr = ""; //?????????
		String fileName = "";//?????????
		String createDate ="";
		String altDate ="";
		String exctPsn ="";
		String exptDate ="";
		String intfDscr = "";

		//?????????????????????????????????????????????map??????????????????
		ExcelUtil obj = ExcelUtil.getInstance();
		Map<String, String> interfaceMap = obj.getInterfaceMap(dataSrcAbbr);
		Map<String, String> columnMap = obj.getColumnMap(dataSrcAbbr);
		Map<String, String> procMap = obj.getProcMap(dataSrcAbbr);

		//???????????????????????????
		DataRvsdRecordTmp dataRvsdRecordTmp = (DataRvsdRecordTmp)obj.getEntityMap().get(dataSrcAbbr+"DataRvsdRecordTmp");
		needVrsnNbr = dataRvsdRecordTmp.getNeedVrsnNbr();
		if(dataRvsdRecordTmp.getNeedVrsnNbr().equals(needVrsnNbr)) {
			List<Map<String,Object>> dirList =jdbc.queryForList(" select expt_seq_nbr from data_interface_records order by expt_date desc ");
			for(int i=0;i<dirList.size();i++) {
				if(i==0) {
					Map<String,Object> m =dirList.get(i);
					exptSeqNbr = getNewId((String) m.get("expt_seq_nbr"));
				}
			}
		}else {
			exptSeqNbr=needVrsnNbr+".1";
		}
		
		//??????????????????
		initHistory(needVrsnNbr,exptSeqNbr);
		

		
		
		
		
		/**
		 * ??????
		 */
		long intTime = new Date().getTime();
		logger.info("??????????????????...?????????=["+dataSrcAbbr+"],?????????=["+batchNo+"],?????????=["+needVrsnNbr+"],?????????=["+exptSeqNbr+"]");
		//sql
		List<Object[]> updateList=new ArrayList<Object[]>();
		List<Object[]> insertList=new ArrayList<Object[]>();
		List<DataInterface> newAddList = new ArrayList<DataInterface>();
		List<DataInterfaceHistory> newAddListHis = new ArrayList<DataInterfaceHistory>();
		//????????????
		DataInterfaceTmp intCondition = new DataInterfaceTmp();
		intCondition.setBatchNo(batchNo);
		//???????????????????????????
		List<DataInterfaceTmp> queryAllTmpInt = mapper.queryAllTmp(intCondition);
		for(DataInterfaceTmp tmp:queryAllTmpInt){
			String key = tmp.getDataInterfaceName();
			//?????????
			if(interfaceMap!=null&&interfaceMap.containsKey(key)){
				if(!tmp.toStr().equalsIgnoreCase(interfaceMap.get(key))){//??????
					//??????
					updateList.add(new Object[] {new Date(),tmp.getDataInterfaceName()});
					//??????sql
					insertList.add(new Object[] {batchNo,tmp.getDataInterfaceName()});

                    DMLInsert.append("UPDATE SDATA_OLTP_CFG.DATA_INTERFACE SET e_date='"+TimeUtil.getDateToString(TimeUtil.getToday())
                            +"' where data_interface_name = '"+tmp.getDataInterfaceName()+"' and e_date ='3000-12-31';\n"
                            +"INSERT INTO SDATA_OLTP_CFG.DATA_INTERFACE VALUES('"+tmp.getDataSrcAbbr()+"','"+tmp.getDataInterfaceNo()+"','"+tmp.getDataInterfaceName()
                            +"','"+tmp.getDataInterfaceDesc()+"','"+tmp.getDataLoadFreq()+"','"+tmp.getDataLoadMthd()+"','"+tmp.getFiledDelim()
                            +"','"+tmp.getLineDelim()+"','"+tmp.getExtrnlDatabaseName()+"','"+tmp.getIntrnlDatabaseName()+"','"+tmp.getExtrnlTableName()
                            +"','"+tmp.getIntrnlTableName()+"','"+tmp.getTableType()+"',"+tmp.getBucketNumber()+",'"+TimeUtil.getDate(tmp.getsDate())
                            +"','"+TimeUtil.getDate(tmp.geteDate())+"');"
                            +"\n\n");
                    DMLDeclare.append("DECLARE \n"
                            +"o_extrnl_table_ddl STRING\n"
                            +"o_intrnl_table_ddl STRING\n"
                            +"BEGIN\n"
                            +"pkg_ruku_ddl.pro_sp_ddl('"+tmp.getDataSrcAbbr()+"','"+tmp.getDataInterfaceName()+"',o_extrnl_table_ddl,o_intrnl_table_ddl)\n"
                            +"DBMS_OUTPUT.PUT_LINE(o_extrnl_table_ddl)\n"
                            +"DBMS_OUTPUT.PUT_LINE(o_intrnl_table_ddl)\n"
                            +"END"
                            +"\n\n");
				}
			}else{//??????
				DataInterface data = new DataInterface();
				data.setDataSrcAbbr(tmp.getDataSrcAbbr());
				data.setDataInterfaceNo(tmp.getDataInterfaceNo());
				data.setDataInterfaceName(tmp.getDataInterfaceName());
				data.setDataInterfaceDesc(tmp.getDataInterfaceDesc());
				data.setDataLoadFreq(tmp.getDataLoadFreq());
				data.setDataLoadMthd(tmp.getDataLoadMthd());
				data.setFiledDelim(tmp.getFiledDelim());
				data.setLineDelim(tmp.getLineDelim());
				data.setExtrnlDatabaseName(tmp.getExtrnlDatabaseName());
				data.setIntrnlDatabaseName(tmp.getIntrnlDatabaseName());
				data.setExtrnlTableName(tmp.getExtrnlTableName());
				data.setIntrnlTableName(tmp.getIntrnlTableName());
				data.setTableType(tmp.getTableType());
				data.setBucketNumber(tmp.getBucketNumber());
				data.setsDate(tmp.getsDate());
				data.seteDate(tmp.geteDate());
				newAddList.add(data);

				DMLInsert.append("INSERT INTO SDATA_OLTP_CFG.DATA_INTERFACE VALUES('"+tmp.getDataSrcAbbr()+"','"+tmp.getDataInterfaceNo()+"','"+tmp.getDataInterfaceName()
						+"','"+tmp.getDataInterfaceDesc()+"','"+tmp.getDataLoadFreq()+"','"+tmp.getDataLoadMthd()+"','"+tmp.getFiledDelim()
						+"','"+tmp.getLineDelim()+"','"+tmp.getExtrnlDatabaseName()+"','"+tmp.getIntrnlDatabaseName()+"','"+tmp.getExtrnlTableName()
						+"','"+tmp.getIntrnlTableName()+"','"+tmp.getTableType()+"',"+tmp.getBucketNumber()+",'"+TimeUtil.getDate(tmp.getsDate())
						+"','"+TimeUtil.getDate(tmp.geteDate())+"');"
						+"\n\n");
				DMLDeclare.append("DECLARE \n"
						+"o_extrnl_table_ddl STRING\n"
						+"o_intrnl_table_ddl STRING\n"
						+"BEGIN\n"
						+"pkg_ruku_ddl.pro_sp_ddl('"+tmp.getDataSrcAbbr()+"','"+tmp.getDataInterfaceName()+"',o_extrnl_table_ddl,o_intrnl_table_ddl)\n"
						+"DBMS_OUTPUT.PUT_LINE(o_extrnl_table_ddl)\n"
						+"DBMS_OUTPUT.PUT_LINE(o_intrnl_table_ddl)\n"
						+"END"
						+"\n\n");
//
//
//				FileUtil.write(DMLFilePath, DMLSql, config.getFileEncode());
				
				DataInterfaceHistory dataHis = new DataInterfaceHistory();
				dataHis.setNeedVrsnNbr(needVrsnNbr);
				dataHis.setExptSeqNbr(exptSeqNbr);
				dataHis.setDataSrcAbbr(tmp.getDataSrcAbbr());
				dataHis.setDataInterfaceNo(tmp.getDataInterfaceNo());
				dataHis.setDataInterfaceName(tmp.getDataInterfaceName());
				dataHis.setDataInterfaceDesc(tmp.getDataInterfaceDesc());
				dataHis.setDataLoadFreq(tmp.getDataLoadFreq());
				dataHis.setDataLoadMthd(tmp.getDataLoadMthd());
				dataHis.setFiledDelim(tmp.getFiledDelim());
				dataHis.setLineDelim(tmp.getLineDelim());
				dataHis.setExtrnlDatabaseName(tmp.getExtrnlDatabaseName());
				dataHis.setIntrnlDatabaseName(tmp.getIntrnlDatabaseName());
				dataHis.setExtrnlTableName(tmp.getExtrnlTableName());
				dataHis.setIntrnlTableName(tmp.getIntrnlTableName());
				dataHis.setTableType(tmp.getTableType());
				dataHis.setBucketNumber(tmp.getBucketNumber());
				dataHis.setsDate(tmp.getsDate());
				dataHis.seteDate(tmp.geteDate());
				newAddListHis.add(dataHis);
			}
		}
		logger.info("??????????????????,???????????????"+(new Date().getTime()-intTime));
		
		
		
		
		/**
		 * ??????
		 */
		long colTime = new Date().getTime();
		logger.info("??????????????????...?????????=["+dataSrcAbbr+"],?????????=["+batchNo+"],?????????=["+needVrsnNbr+"],?????????=["+exptSeqNbr+"]");
		
		//sql
		List<Object []> colUpdateList=new ArrayList<Object []>();
		List<Object []> colInsertList=new ArrayList<Object []>();
		List<DataInterfaceColumns> colNewAddList = new ArrayList<DataInterfaceColumns>();
		List<DataInterfaceColumnsHistory> colNewAddListHis = new ArrayList<DataInterfaceColumnsHistory>();
		
		//???????????????????????????
		DataInterfaceColumnsTmp colCondition = new DataInterfaceColumnsTmp();
		colCondition.setBatchNo(batchNo);
		List<DataInterfaceColumnsTmp> queryAllTmpCol = colMapper.queryAllTmp(colCondition);
		for(DataInterfaceColumnsTmp tmp:queryAllTmpCol){
			String key = tmp.getDataInterfaceName()+tmp.getColumnNo();
			if(columnMap!=null&&columnMap.containsKey(key)){
				if(!tmp.toStr().equalsIgnoreCase(columnMap.get(key))){//??????
					//??????
					colUpdateList.add(new Object[] {new Date(),tmp.getDataInterfaceName(),tmp.getColumnNo()});
					colInsertList.add(new Object[] {batchNo,tmp.getDataInterfaceName(),tmp.getColumnNo()});

                    DMLInsert.append("UPDATE SDATA_OLTP_CFG.DATA_INTERFACE_COLUMNS SET e_date='"+TimeUtil.getDateToString(TimeUtil.getToday())
                            +"' where data_interface_name = '"+tmp.getDataInterfaceName()+"' and e_date ='3000-12-31';\n"
                            +"INSERT INTO SDATA_OLTP_CFG.DATA_INTERFACE_COLUMNS VALUES('"+tmp.getDataSrcAbbr()+"','"+tmp.getDataInterfaceNo()
                            +"','"+tmp.getDataInterfaceName()+"','"+tmp.getColumnNo()+"','"+tmp.getColumnName()+"','"+tmp.getDataType()
                            +"','"+tmp.getDataFormat()+"','"+tmp.getNullable()+"','"+tmp.getReplacenull()+"','"+tmp.getComma()
                            +"','"+tmp.getColumnComment()+"','"+tmp.getIsbucket()+"','"+tmp.getIskey()+"','"+tmp.getIsvalid()
                            +"','"+tmp.getIncrementfield()+"','"+TimeUtil.getDate(tmp.getsDate())+"','"+TimeUtil.getDate(tmp.geteDate())+"');"
                            +"\n\n");
				}
			}else{//??????
				//??????
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
				data.setIskey(tmp.getIskey());
				data.setIsvalid(tmp.getIsvalid());
				data.setIncrementfield(tmp.getIncrementfield());
				colNewAddList.add(data);

				DMLInsert.append("INSERT INTO SDATA_OLTP_CFG.DATA_INTERFACE_COLUMNS VALUES('"+tmp.getDataSrcAbbr()+"','"+tmp.getDataInterfaceNo()
						+"','"+tmp.getDataInterfaceName()+"','"+tmp.getColumnNo()+"','"+tmp.getColumnName()+"','"+tmp.getDataType()
						+"','"+tmp.getDataFormat()+"','"+tmp.getNullable()+"','"+tmp.getReplacenull()+"','"+tmp.getComma()
						+"','"+tmp.getColumnComment()+"','"+tmp.getIsbucket()+"','"+tmp.getIskey()+"','"+tmp.getIsvalid()
						+"','"+tmp.getIncrementfield()+"','"+TimeUtil.getDate(tmp.getsDate())+"','"+TimeUtil.getDate(tmp.geteDate())+"');"
						+"\n\n");

				//??????
				DataInterfaceColumnsHistory dataHis = new DataInterfaceColumnsHistory();
				dataHis.setNeedVrsnNbr(needVrsnNbr);
				dataHis.setExptSeqNbr(exptSeqNbr);
				dataHis.setDataSrcAbbr(tmp.getDataSrcAbbr());
				dataHis.setDataInterfaceNo(tmp.getDataInterfaceNo());
				dataHis.setDataInterfaceName(tmp.getDataInterfaceName());
				dataHis.setColumnNo(tmp.getColumnNo());
				dataHis.setColumnName(tmp.getColumnName());
				dataHis.setColumnComment(tmp.getColumnComment());
				dataHis.setComma(tmp.getComma());
				dataHis.setDataType(tmp.getDataType());
				dataHis.setDataFormat(tmp.getDataFormat());
				dataHis.setIsbucket(tmp.getIsbucket());
				dataHis.setNullable(tmp.getNullable());
				dataHis.setReplacenull(tmp.getReplacenull());
				dataHis.setsDate(tmp.getsDate());
				dataHis.seteDate(tmp.geteDate());
				dataHis.setIskey(tmp.getIskey());
				dataHis.setIsvalid(tmp.getIsvalid());
				dataHis.setIncrementfield(tmp.getIncrementfield());
				colNewAddListHis.add(dataHis);
			}
		}
		logger.info("??????????????????,???????????????"+(new Date().getTime()-colTime));
		
		
		
		/**
		 * ????????????
		 */
		
		long procTime = new Date().getTime();
		logger.info("????????????????????????...?????????=["+dataSrcAbbr+"],?????????=["+batchNo+"],?????????=["+needVrsnNbr+"],?????????=["+exptSeqNbr+"]");
		
			
		List<Object []> procUpdateList=new ArrayList<Object []>();
		List<Object []> procInsertList=new ArrayList<Object []>();
		List<DataInterface2proc> procNewAddList = new ArrayList<DataInterface2proc>();
		List<DataInterface2procHistory> procNewAddListHis = new ArrayList<DataInterface2procHistory>();
		
		DataInterface2procTmp procCondition = new DataInterface2procTmp();
		procCondition.setBatchNo(batchNo);
		List<DataInterface2procTmp> queryAllTmp = procMapper.queryAllTmp(procCondition);
		for(DataInterface2procTmp tmp:queryAllTmp){
			if(procMap!=null&&procMap.containsKey(tmp.getDataSrcAbbr()+tmp.getDataInterfaceNo())){
				//??????
				if(!tmp.toStr().equals(procMap.get(tmp.getDataSrcAbbr()+tmp.getDataInterfaceNo()))){
					procUpdateList.add(new Object[] {new Date(),tmp.getDataSrcAbbr(),tmp.getDataInterfaceNo()});
					procInsertList.add(new Object[] {batchNo,tmp.getDataSrcAbbr(),tmp.getDataInterfaceNo()});

					DMLInsert.append("UPDATE SDATA_OLTP_CFG.data_interface2proc SET e_date='"+TimeUtil.getDateToString(TimeUtil.getToday())
							+"' where data_src_abbr = "+tmp.getDataSrcAbbr()+"and data_interface_no = '"+tmp.getDataInterfaceNo()+"' and e_date ='3000-12-31';\n"
							+"INSERT INTO SDATA_OLTP_CFG.data_interface2proc VALUES('"+tmp.getDataSrcAbbr()+"','"+tmp.getDataInterfaceNo()
							+"','"+tmp.getProcDatabaseName()+"','"+tmp.getProcName()+"','"+TimeUtil.getDate(tmp.getsDate())
							+"','"+TimeUtil.getDate(tmp.geteDate())+"');"
							+"\n\n");

				}
			//??????
			}else{
				DataInterface2proc data = new DataInterface2proc();
				data.setDataSrcAbbr(tmp.getDataSrcAbbr());
				data.setDataInterfaceNo(tmp.getDataInterfaceNo());
				data.setProcDatabaseName(tmp.getProcDatabaseName());
				data.setProcName(tmp.getProcName());
				data.setsDate(tmp.getsDate());
				data.seteDate(tmp.geteDate());
				procNewAddList.add(data);
				
				DataInterface2procHistory dataHis = new DataInterface2procHistory();
				dataHis.setNeedVrsnNbr(needVrsnNbr);
				dataHis.setExptSeqNbr(exptSeqNbr);
				dataHis.setDataSrcAbbr(tmp.getDataSrcAbbr());
				dataHis.setDataInterfaceNo(tmp.getDataInterfaceNo());
				dataHis.setProcDatabaseName(tmp.getProcDatabaseName());
				dataHis.setProcName(tmp.getProcName());
				dataHis.setsDate(tmp.getsDate());
				dataHis.seteDate(tmp.geteDate());
				procNewAddListHis.add(dataHis);

				DMLInsert.append("INSERT INTO SDATA_OLTP_CFG.data_interface2proc VALUES('"+tmp.getDataSrcAbbr()+"','"+tmp.getDataInterfaceNo()
						+"','"+tmp.getProcDatabaseName()+"','"+tmp.getProcName()+"','"+TimeUtil.getDate(tmp.getsDate())
						+"','"+TimeUtil.getDate(tmp.geteDate())+"');"
						+"\n\n");
			}
		}
		logger.info("????????????????????????,???????????????"+(new Date().getTime()-procTime));
		
		
//		cache.put("interface","update-edit",updateList);
//		cache.put("interface","update-add",insertList);
//		cache.put("interface","insert",newAddList);
//		cache.put("interface","insertHis",newAddListHis);
//		
//		cache.put("column","update-edit",updateList);
//		cache.put("column","update-add",colInsertList);
//		cache.put("column","insert",newAddList);
//		cache.put("column","insertHis",colNewAddListHis);
//		
//		cache.put("proc","update-edit",updateList);
//		cache.put("proc","update-add",procInsertList);
//		cache.put("proc","insert",newAddList);
//		cache.put("proc","insertHis",newAddListHis);
		
//		List<String> delList=new ArrayList<String>();
//		delList.add("delete from data_interface_tmp where batch_no = '"+batchNo+"'; ");
//		delList.add("delete from data_interface_columns_tmp where batch_no = '"+batchNo+"'; ");
//		delList.add("delete from data_interface2proc_tmp where batch_no = '"+batchNo+"'; ");
//		
//		cache.put("all","delete",delList);
		
		
		
		
		/**
		 * ?????????
		 */
		try {
            //?????? ?????????insert
            if(newAddList.size()>0){
				logger.info("all batch insert interface success:"+mapper.batchInsertPro(newAddList));
			}
			//?????? ?????????update???insert
			if(updateList.size()>0){
				logger.info("all batch update interface success:"+jdbc.batchUpdate(getSql("intPro", "update", needVrsnNbr, exptSeqNbr), updateList).length);
			}
			if(insertList.size()>0){
				logger.info("all batch edit&insert interface from tmp success:"+jdbc.batchUpdate(getSql("intPro", "insert", needVrsnNbr, exptSeqNbr),insertList).length);
			}
			
			
			
			if(colNewAddList.size()>0){
				logger.info("all batch insert column success:"+colMapper.batchInsertPro(colNewAddList));
			}
			if(colUpdateList.size()>0){
				logger.info("all batch update column success:"+jdbc.batchUpdate(getSql("colPro", "update", needVrsnNbr, exptSeqNbr), colUpdateList).length);
			}
			if(colInsertList.size()>0){
				logger.info("all batch insert column from tmp success:"+jdbc.batchUpdate(getSql("colPro", "insert", needVrsnNbr, exptSeqNbr),colInsertList).length);
			}
			
			
			
			if(procNewAddList.size()>0){
				logger.info("all batch insert proc success:"+procMapper.batchInsertPro(procNewAddList));
			}
			if(procUpdateList.size()>0){
				logger.info("all batch update proc success:"+jdbc.batchUpdate(getSql("procPro", "update", needVrsnNbr, exptSeqNbr), procUpdateList).length);
			}
			if(procInsertList.size()>0){
				logger.info("all batch insert proc from tmp success:"+jdbc.batchUpdate(getSql("procPro", "insert", needVrsnNbr, exptSeqNbr),procInsertList).length);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
//			lautch.countDown();
		}
		
		/**
		 * ?????????
		 */
		
		try {
			//??????????????????
			List<Map<String, Object>> rvsdList = jdbc.queryForList("select file_name,s_date,intf_dscr,exct_psn from data_rvsd_record_tmp where batch_no='"+batchNo+"'");
			if(rvsdList!=null&&rvsdList.size()>0) {
				Map<String, Object> recordMap=rvsdList.get(0);
				fileName = recordMap.get("file_name")+"";
				createDate = recordMap.get("s_date")+"";
				altDate = recordMap.get("s_date")+"";
				exptDate = recordMap.get("s_date")+"";
				intfDscr = recordMap.get("intf_dscr")+"";
				exctPsn = recordMap.get("exct_psn")+"";
			}
//			lautch.await();
			DataInterfaceRecords records = new DataInterfaceRecords();
			records.setNeedVrsnNbr(needVrsnNbr);
			records.setExptSeqNbr(exptSeqNbr);
			records.setDataSrcAbbr(dataSrcAbbr);
			records.setExptFileName(fileName);
			records.setIntfTot(queryAllTmpInt.size()+"");
			records.setIntfNew(newAddList.size()+"");
			records.setIntfAlt(updateList.size()+"");
			records.setCreateDate(createDate);
			records.setAltDate(altDate);
			records.setExctPsn(exctPsn);
			records.setExptDate(exptDate);
			records.setIntfDscr(intfDscr);
			recordsMapper.insertSelective(records);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		//???????????????
		Map<String,Object> map =(Map<String, Object>) obj.getEntityMap().get(dataSrcAbbr+"msgMap");
		for(Map.Entry<String, Object> entry:map.entrySet()) {
			String key = entry.getKey();
			List list = (List) entry.getValue();
			for(Object o:list) {
				String msg = (String) o;
				jdbc.update(" insert into data_interface_records_detail (need_vrsn_nbr,expt_seq_nbr,data_src_abbr,data_interface_name,data_change,expt_date) values ('"+needVrsnNbr+"','"+exptSeqNbr+"','"+dataSrcAbbr+"'"+key+"','"+msg+"','"+exptDate+"')");
			}
		}
		/**
		 * ???????????????
		 */
//		try {
//			jdbc.update(" insert into data_rvsd_record (need_vrsn_nbr,expt_seq_nbr,data_src_abbr,chg_psn,exct_psn,corr_intf_std_vrsn,intf_dscr,s_date,e_date) "
//					+ " select b.need_vrsn_nbr,'"+exptSeqNbr+"',b.data_src_abbr,b.chg_psn,exct_psn,b.corr_intf_std_vrsn,b.intf_dscr,b.s_date,b.e_date "
//					+ "from data_rvsd_record_tmp b where b.batch_no='"+batchNo+"'");
//		} catch (DataAccessException e1) {
//			e1.printStackTrace();
//		}
		/**
		 * ?????????
		 */
		try {

			if(newAddListHis.size()>0){
				logger.info("all batch insert interface his success:"+mapper.batchInsertHis(newAddListHis));
			}
			if(updateList.size()>0){
				logger.info("all batch update interface his success:"+jdbc.batchUpdate(getSql("intHis", "update", needVrsnNbr, exptSeqNbr), updateList).length);
			}
			if(insertList.size()>0){
				logger.info("all batch edit&insert interface his from tmp success:"+jdbc.batchUpdate(getSql("intHis", "insert", needVrsnNbr, exptSeqNbr),insertList).length);
			}
//
//
//
			if(colNewAddListHis.size()>0){
				logger.info("all batch insert column his success:"+colMapper.batchInsertHis(colNewAddListHis));
			}
			if(colUpdateList.size()>0){
				logger.info("all batch update column his success:"+jdbc.batchUpdate(getSql("colHis", "update", needVrsnNbr, exptSeqNbr), colUpdateList).length);
			}
			if(colInsertList.size()>0){
				logger.info("all batch insert column his from tmp success:"+jdbc.batchUpdate(getSql("colHis", "insert", needVrsnNbr, exptSeqNbr),colInsertList).length);
			}
//
//
//
			if(procNewAddListHis.size()>0){
				logger.info("all batch insert proc his success:"+procMapper.batchInsertHis(procNewAddListHis));
			}
			if(procUpdateList.size()>0){
				logger.info("all batch update proc his success:"+jdbc.batchUpdate(getSql("procHis", "update", needVrsnNbr, exptSeqNbr), procUpdateList).length);
			}
			if(procInsertList.size()>0){
				logger.info("all batch insert proc his from tmp success:"+jdbc.batchUpdate(getSql("procHis", "insert", needVrsnNbr, exptSeqNbr),procInsertList).length);
			}
			
//			if(delList.size()>0){
//				jdbc.batchUpdate((String[]) delList.toArray());
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//????????????????????????????????????
		ExecutorService es = Executors.newFixedThreadPool(3);
//				CountDownLatch lautch = new CountDownLatch(3);
		//??????lambda??????runnable??????
		Runnable intTask = ()->{
			logger.info(Thread.currentThread().getName()+"????????????");
			
			//??????submit?????????????????????????????????
		};
		es.submit(intTask);
		
		ExecutorService hisEs = Executors.newFixedThreadPool(1);
		Runnable hisTask = ()->{
			logger.info(Thread.currentThread().getName()+"????????????");
			
			//??????submit?????????????????????????????????
		};
		hisEs.submit(hisTask);


		obj.clearColumn(dataSrcAbbr);
		obj.clearInterface(dataSrcAbbr);
		obj.clearProc(dataSrcAbbr);

		return "success";
	}
	
	private void initHistory(String needVrsnNbr,String exptSeqNbr) {
		/**
		 * ?????????????????????
		 */
		List<Map<String, Object>> intHistoryList = jdbc.queryForList("select * from  data_interface_history where e_date = '3000-12-31' ");
		if(intHistoryList.size()<1) {//????????????????????????
			String sql = "insert into data_interface_history  (need_vrsn_nbr, expt_seq_nbr, data_src_abbr, \r\n"
					+ "      data_interface_no, data_interface_name, data_interface_desc, \r\n"
					+ "      data_load_freq, data_load_mthd, filed_delim, \r\n"
					+ "      line_delim, extrnl_database_name, intrnl_database_name, \r\n"
					+ "      extrnl_table_name, intrnl_table_name, table_type, \r\n"
					+ "      bucket_number, s_date, e_date\r\n"
					+ "      ) select '1.0','1.0.0', b.data_src_abbr, b.data_interface_no, b.data_interface_name, \r\n"
					+ "    b.data_interface_desc, b.data_load_freq, b.data_load_mthd, b.filed_delim, b.line_delim, b.extrnl_database_name, \r\n"
					+ "    b.intrnl_database_name, b.extrnl_table_name, b.intrnl_table_name, b.table_type, b.bucket_number, \r\n"
					+ "    b.s_date, b.e_date from data_interface b where b.e_date='3000-12-31'";
			jdbc.batchUpdate(sql);
		}else {
			List<Map<String, Object>> intHistoryList2 = jdbc.queryForList("select * from  data_interface_history where e_date = '3000-12-31' and need_vrsn_nbr='"+needVrsnNbr+"' and expt_seq_nbr='"+exptSeqNbr+"' ");
			if(intHistoryList2.size()<1) {//????????????????????????
				String sql = "insert into data_interface_history  (need_vrsn_nbr, expt_seq_nbr, data_src_abbr, \r\n"
						+ "      data_interface_no, data_interface_name, data_interface_desc, \r\n"
						+ "      data_load_freq, data_load_mthd, filed_delim, \r\n"
						+ "      line_delim, extrnl_database_name, intrnl_database_name, \r\n"
						+ "      extrnl_table_name, intrnl_table_name, table_type, \r\n"
						+ "      bucket_number, s_date, e_date\r\n"
						+ "      ) select '"+needVrsnNbr+"','"+exptSeqNbr+"', b.data_src_abbr, b.data_interface_no, b.data_interface_name, \r\n"
						+ "    b.data_interface_desc, b.data_load_freq, b.data_load_mthd, b.filed_delim, b.line_delim, b.extrnl_database_name, \r\n"
						+ "    b.intrnl_database_name, b.extrnl_table_name, b.intrnl_table_name, b.table_type, b.bucket_number, \r\n"
						+ "    b.s_date, b.e_date from data_interface b where b.e_date='3000-12-31' ";
				jdbc.batchUpdate(sql);
			}
		}
		List<Map<String, Object>> colHistoryList = jdbc.queryForList("select *  from data_interface_columns_history where e_date = '3000-12-31' ");
		if(colHistoryList.size()<1) {
			String sql = "insert into data_interface_columns_history  (need_vrsn_nbr, expt_seq_nbr, data_src_abbr, data_interface_no, data_interface_name, \r\n"
					+ "    column_no, column_name, data_type, data_format, nullable, replacenull, comma, column_comment, \r\n"
					+ "    isbucket, s_date, e_date"
					+ "      ) select '1.0','1.0.0', b.data_src_abbr,b.data_interface_no,b.data_interface_name,b.\r\n"
					+ "    column_no,b.column_name,b.data_type,b.data_format,b.nullable,b.replacenull,b.comma,b.column_comment,b.\r\n"
					+ "    isbucket,b.s_date,b.e_date from data_interface_columns b where b.e_date='3000-12-31'";
			jdbc.batchUpdate(sql);
		}else {
			List<Map<String, Object>> colHistoryList2 = jdbc.queryForList("select *  from data_interface_columns_history where e_date = '3000-12-31' and need_vrsn_nbr='"+needVrsnNbr+"' and expt_seq_nbr='"+exptSeqNbr+"' ");
			if(colHistoryList2.size()<1) {
				String sql = "insert into data_interface_columns_history  (need_vrsn_nbr, expt_seq_nbr, data_src_abbr, data_interface_no, data_interface_name, \r\n"
						+ "    column_no, column_name, data_type, data_format, nullable, replacenull, comma, column_comment, \r\n"
						+ "    isbucket, s_date, e_date"
						+ "      ) select '"+needVrsnNbr+"','"+exptSeqNbr+"', b.data_src_abbr,b.data_interface_no,b.data_interface_name,b.\r\n"
						+ "    column_no,b.column_name,b.data_type,b.data_format,b.nullable,b.replacenull,b.comma,b.column_comment,b.\r\n"
						+ "    isbucket,b.s_date,b.e_date from data_interface_columns b where b.e_date='3000-12-31'";
				jdbc.batchUpdate(sql);
			}
		}
		List<Map<String, Object>> procHistoryList = jdbc.queryForList("select *  from data_interface2proc_history where e_date = '3000-12-31' ");
		if(procHistoryList.size()<1) {
			String sql = "insert into data_interface2proc_history  (need_vrsn_nbr, expt_seq_nbr, data_src_abbr, data_interface_no,proc_database_name,proc_name,s_date,e_date) "
					+ "  select '1.0','1.0.0', b.data_src_abbr,b.data_interface_no,b.proc_database_name,b.proc_name,b.s_date,b.e_date from data_interface2proc b where b.e_date='3000-12-31'";
			jdbc.batchUpdate(sql);
		}else {
			List<Map<String, Object>> procHistoryList2 = jdbc.queryForList("select *  from data_interface2proc_history where e_date = '3000-12-31' and need_vrsn_nbr='"+needVrsnNbr+"' and expt_seq_nbr='"+exptSeqNbr+"' ");
			if(procHistoryList2.size()<1) {
				String sql = "insert into data_interface2proc_history  (need_vrsn_nbr, expt_seq_nbr, data_src_abbr, data_interface_no,proc_database_name,proc_name,s_date,e_date) "
						+ "  select '"+needVrsnNbr+"','"+exptSeqNbr+"', b.data_src_abbr,b.data_interface_no,b.proc_database_name,b.proc_name,b.s_date,b.e_date from data_interface2proc b where b.e_date='3000-12-31'";
				jdbc.batchUpdate(sql);
			}
		}
	}
	
	private String getSql(String type,String method,String needVrsnNbr,String exptSeqNbr) {
		if("intHis".equals(type)) {
			if("update".equals(method)) {
				String updateSqlHis = "update data_interface_history set e_date = ? where data_interface_name = ? and e_date ='3000-12-31' ";
				return updateSqlHis;
			}else if("insert".equals(method)) {
				StringBuffer sbHis = new StringBuffer();
				sbHis.append("insert into data_interface_history (need_vrsn_nbr,expt_seq_nbr,data_src_abbr, data_interface_no, data_interface_name,data_interface_desc, ");
				sbHis.append("data_load_freq, data_load_mthd,filed_delim, line_delim, extrnl_database_name,intrnl_database_name, ");
				sbHis.append("extrnl_table_name,intrnl_table_name,table_type, bucket_number, s_date, e_date) ");
				sbHis.append("select '"+needVrsnNbr+"','"+exptSeqNbr+"', tmp.data_src_abbr, tmp.data_interface_no, tmp.data_interface_name,tmp.data_interface_desc, ");
				sbHis.append("tmp.data_load_freq,tmp.data_load_mthd,tmp.filed_delim, tmp.line_delim, tmp.extrnl_database_name, tmp.intrnl_database_name, ");
				sbHis.append("tmp.extrnl_table_name,tmp.intrnl_table_name,tmp.table_type, tmp.bucket_number, tmp.s_date, tmp.e_date ");
				sbHis.append("from data_interface_tmp tmp  ");
				sbHis.append("where tmp.batch_no = ? and data_interface_name = ? ");
				return sbHis.toString();
			}
		}else if("intPro".equals(type)){
			if("update".equals(method)) {
				String updateSql = "update data_interface set e_date = ? where data_interface_name = ? and e_date ='3000-12-31' ";
				return updateSql;
			}else if("insert".equals(method)) {
				StringBuffer sb = new StringBuffer();
				sb.append("insert into data_interface (data_src_abbr, data_interface_no, data_interface_name,data_interface_desc, ");
				sb.append("data_load_freq, data_load_mthd,filed_delim, line_delim, extrnl_database_name,intrnl_database_name, ");
				sb.append("extrnl_table_name,intrnl_table_name,table_type, bucket_number, s_date, e_date) ");
				sb.append("select tmp.data_src_abbr, tmp.data_interface_no, tmp.data_interface_name,tmp.data_interface_desc, ");
				sb.append("tmp.data_load_freq,tmp.data_load_mthd,tmp.filed_delim, tmp.line_delim, tmp.extrnl_database_name, tmp.intrnl_database_name, ");
				sb.append("tmp.extrnl_table_name,tmp.intrnl_table_name,tmp.table_type, tmp.bucket_number, tmp.s_date, tmp.e_date ");
				sb.append("from data_interface_tmp tmp  ");
				sb.append("where tmp.batch_no = ? and data_interface_name = ? ");
				return sb.toString();
			}
		}else if("colHis".equals(type)) {
			if("update".equals(method)) {
				String colUpdateSqlHis = "update data_interface_columns_history set e_date = ? where data_interface_name = ? and column_no = ? and e_date='3000-12-31' ";
				return colUpdateSqlHis;
			}else if("insert".equals(method)) {
				StringBuffer colSbHis = new StringBuffer();
				colSbHis.append("insert into data_interface_columns_history (need_vrsn_nbr,expt_seq_nbr,data_src_abbr, data_interface_no, data_interface_name,column_no, ");
				colSbHis.append("column_name, data_type,data_format, nullable, replacenull,comma, column_comment, isbucket,s_date, e_date,iskey,isvalid,increment_field) ");
				colSbHis.append("select '"+needVrsnNbr+"','"+exptSeqNbr+"',tmp.data_src_abbr, tmp.data_interface_no, tmp.data_interface_name,tmp.column_no, ");
				colSbHis.append("tmp.column_name, tmp.data_type,data_format, tmp.nullable, tmp.replacenull,tmp.comma, tmp.column_comment, tmp.isbucket,tmp.s_date, tmp.e_date,tmp.iskey,tmp.isvalid,tmp.increment_field ");
				colSbHis.append("from data_interface_columns_tmp tmp  ");
				colSbHis.append("where tmp.batch_no = ? and data_interface_name = ? and tmp.column_no = ? ");
				return colSbHis.toString();
			}
		}else if("colPro".equals(type)) {
			if("update".equals(method)) {
				String colUpdateSql = "update data_interface_columns set e_date = ? where data_interface_name = ? and column_no = ? and e_date='3000-12-31' ";
				return colUpdateSql;
			}else if("insert".equals(method)) {
				StringBuffer colSb = new StringBuffer();
				colSb.append("insert into data_interface_columns (data_src_abbr, data_interface_no, data_interface_name,column_no, ");
				colSb.append("column_name, data_type,data_format, nullable, replacenull,comma, column_comment, isbucket,s_date, e_date,iskey,isvalid,increment_field) ");
				colSb.append("select tmp.data_src_abbr, tmp.data_interface_no, tmp.data_interface_name,tmp.column_no, ");
				colSb.append("tmp.column_name, tmp.data_type,data_format, tmp.nullable, tmp.replacenull,tmp.comma, tmp.column_comment, tmp.isbucket,tmp.s_date, tmp.e_date,tmp.iskey,tmp.isvalid,tmp.increment_field ");
				colSb.append("from data_interface_columns_tmp tmp  ");
				colSb.append("where tmp.batch_no = ? and data_interface_name = ? and tmp.column_no = ? ");
				return colSb.toString();
			}
		}else if("procHis".equals(type)) {
			if("update".equals(method)) {
				String procUpdateSqlHis = "update data_interface2proc_history set e_date = ? where data_src_abbr = ? and data_interface_no = ? and e_date ='3000-12-31' ";
				return procUpdateSqlHis;
			}else if("insert".equals(method)) {
				StringBuffer procSbHis = new StringBuffer();
				procSbHis.append("insert into data_interface2proc_history (need_vrsn_nbr,expt_seq_nbr,data_src_abbr, data_interface_no,");
				procSbHis.append("proc_database_name,proc_name, ");
				procSbHis.append("s_date, e_date) ");
				procSbHis.append("SELECT '"+needVrsnNbr+"','"+exptSeqNbr+"',tmp.data_src_abbr, tmp.data_interface_no, ");
				procSbHis.append("tmp.proc_database_name,tmp.proc_name, ");
				procSbHis.append("tmp.s_date, tmp.e_date ");
				procSbHis.append("FROM data_interface2proc_tmp tmp  ");
				procSbHis.append("WHERE tmp.batch_no = ? and tmp.data_src_abbr = ? and tmp.data_interface_no = ? ");
				return procSbHis.toString();
			}
		}else if("procPro".equals(type)) {
			if("update".equals(method)) {
				String procUpdateSql = "update data_interface2proc set e_date = ? where data_src_abbr = ? and data_interface_no = ? and e_date ='3000-12-31' ";
				return procUpdateSql;
			}else if("insert".equals(method)) {
				StringBuffer procSb = new StringBuffer();
				procSb.append("insert into data_interface2proc (data_src_abbr, data_interface_no,");
				procSb.append("proc_database_name,proc_name, ");
				procSb.append("s_date, e_date) ");
				procSb.append("SELECT tmp.data_src_abbr, tmp.data_interface_no, ");
				procSb.append("tmp.proc_database_name,tmp.proc_name, ");
				procSb.append("tmp.s_date, tmp.e_date ");
				procSb.append("FROM data_interface2proc_tmp tmp  ");
				procSb.append("WHERE tmp.batch_no = ? and tmp.data_src_abbr = ? and tmp.data_interface_no = ? ");
				return procSb.toString();
			}
		}
		return null;
	}
	
	@Override
	public List<DataInterfaceRecords> queryRecord(DataInterfaceRecords record) {
		return recordsMapper.queryAll(record);
	}

	@Override
	public List<DataInterfaceRecordsDetail> queryLastFive(DataInterfaceRecordsDetail record) {
		return recordsMapper.queryLastFive(record);
	}


}
