package com.ljz.service.impl;

import com.ljz.entity.ParamEntity;
import com.ljz.mapper.DataInterface2procMapper;
import com.ljz.mapper.DataInterfaceColumnsMapper;
import com.ljz.mapper.DataInterfaceMapper;
import com.ljz.mapper.DataRvsdRecordMapper;
import com.ljz.model.*;
import com.ljz.util.ExcelUtil;
import com.ljz.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class InfoImportService {

    private static final Logger logger = LoggerFactory.getLogger(InfoImportService.class);

    @Resource
    DataRvsdRecordMapper recordMapper;

    @Resource
    DataInterfaceMapper infacemapper;

    @Resource
    DataInterfaceColumnsMapper columnmapper;

    @Resource
    DataInterface2procMapper procMapper;

    @Autowired
    JdbcTemplate jdbc;

    @Transactional
    public String batchImportRecord(ParamEntity param) throws Exception {
        // TODO Auto-generated method stub
        long start = new Date().getTime();

        long end = new Date().getTime();
        logger.info("导入结束,导入用时："+(end-start));
//        return dataSrcAbbr;
        return "";
    }

    /**
     * 接口批量导入/全部导入
     */
    @Transactional
    public String batchImportInfaceFinal(ParamEntity param) throws Exception {
        // TODO Auto-generated method stub
        long start = new Date().getTime();
        String dataSrcAbbr = "";
        //导入批次号
        String batchNo = "";
        List<Object[]> tmpList=new ArrayList<Object[]>();
        List<Object[]> recordList=new ArrayList<Object[]>();
        List<Object[]> delList=new ArrayList<Object[]>();
        String [] tables = param.getTables();
        //methodType 导入方法 1：导入 2：全部导入
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

            if("1".equals(importType)) {//导入类型是1.新增直接插入正式表，最后删除临时表记录
                tmpList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo,dataInterfaceName});
                delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo,dataInterfaceName});
            }else if("2".equals(importType)) {
                //导入类型2.修改先将正式表原记录置为失效，在将临时表数据导入正式表，最后删除临时表记录
                recordList.add(new Object[] {new Date(),dataSrcAbbr,dataInterfaceNo,dataInterfaceName, TimeUtil.getTw()});
                tmpList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo,dataInterfaceName});
                delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo,dataInterfaceName});
            }else if("3".equals(importType)){//导入类型3.无变化不进行操作，直接删除临时表记录
                delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo,dataInterfaceName});
            }
        }
        logger.info("导入接口开始...数据源=["+dataSrcAbbr+"],批次号=["+batchNo+"],导入方法=["+methodType+"]");
        //修改sql
        String updateSql = "update data_interface set e_date = ? "
                + "where data_src_abbr = ? and data_interface_no = ? and data_interface_name = ? and e_date >= ? ";
        //临时表insert到正式表sql
        StringBuffer sb = new StringBuffer();
        sb.append("insert into data_interface (data_src_abbr, data_interface_no, data_interface_name,data_interface_desc, ");
        sb.append("data_load_freq, data_load_mthd,filed_delim, line_delim, extrnl_database_name,intrnl_database_name, ");
        sb.append("extrnl_table_name,intrnl_table_name,table_type, bucket_number, s_date, e_date) ");
        sb.append("select tmp.data_src_abbr, tmp.data_interface_no, tmp.data_interface_name,tmp.data_interface_desc, ");
        sb.append("tmp.data_load_freq,tmp.data_load_mthd,tmp.filed_delim, tmp.line_delim, tmp.extrnl_database_name, tmp.intrnl_database_name, ");
        sb.append("tmp.extrnl_table_name,tmp.intrnl_table_name,tmp.table_type, tmp.bucket_number, tmp.s_date, tmp.e_date ");
        sb.append("from data_interface_tmp tmp  ");
        sb.append("where tmp.batch_no = ? and tmp.data_src_abbr = ? and tmp.data_interface_no = ? and data_interface_name = ? ");
        //删除sql
        String delSql = "delete from data_interface_tmp "
                + "where batch_no = ? and data_src_abbr = ? and data_interface_no = ? and data_interface_name = ? ";
        if("1".equals(methodType)){//导入
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
        }else if("2".equals(methodType)){//全部导入

            List<Object[]> tmpList2=new ArrayList<Object[]>();
            List<Object[]> recordList2=new ArrayList<Object[]>();
            List<Object[]> delList2=new ArrayList<Object[]>();
            //单例模式获取在导入校验中存放在map缓存中的数据
            ExcelUtil obj = ExcelUtil.getInstance();
            Map<String, String> interfaceMap = obj.getInterfaceMap(dataSrcAbbr);
            List<DataInterface> list = new ArrayList<DataInterface>();

            DataInterfaceTmp condition = new DataInterfaceTmp();
            condition.setBatchNo(batchNo);
            List<DataInterfaceTmp> queryAllTmp = infacemapper.queryAllTmp(condition);
            for(DataInterfaceTmp tmp:queryAllTmp){
                String key = tmp.getDataSrcAbbr()+tmp.getDataInterfaceNo()+tmp.getDataInterfaceName();
                if(interfaceMap!=null&&interfaceMap.containsKey(key)){
                    if(!tmp.toStr().equalsIgnoreCase(interfaceMap.get(key))){//修改
                        recordList2.add(new Object[] {new Date(),tmp.getDataSrcAbbr(),tmp.getDataInterfaceNo(),tmp.getDataInterfaceName(),TimeUtil.getTw()});
                        tmpList2.add(new Object[] {batchNo,dataSrcAbbr,tmp.getDataInterfaceNo(),tmp.getDataInterfaceName()});
                    }
                }else{//新增
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
                int batchInsertPro = infacemapper.batchInsertPro(list);
                logger.info("all batch insert interface success:"+batchInsertPro);
            }
            //删除临时表该批次下所有记录
            String delSql2 = "delete from data_interface_tmp where batch_no = ? ";
            delList2.add(new Object[]{batchNo});
            int[] batchUpdate3 = jdbc.batchUpdate(delSql2,delList2);
            logger.info("all batch delete interface tmp success:"+batchUpdate3.length);
        }
        long end = new Date().getTime();
        logger.info("导入结束,导入用时："+(end-start));
        return dataSrcAbbr;
    }


    @Transactional
    public String batchImportColumnFinal(ParamEntity param) throws Exception {
        // TODO Auto-generated method stub
        List<Object[]> tmpList=new ArrayList<Object[]>();
        List<Object[]> recordList=new ArrayList<Object[]>();
        List<Object[]> delList=new ArrayList<Object[]>();
        String [] tables = param.getTables();
        String dbType = param.getDbType();
//		logger.info("param.getDbType():::"+param.getDbType());

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
            logger.info("importType:::"+importType);

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
        sb.append("column_name, data_type,data_format, nullable, replacenull,comma, column_comment, isbucket, iskey, isvalid, increment_field,s_date, e_date) ");
        sb.append("select tmp.data_src_abbr, tmp.data_interface_no, tmp.data_interface_name,tmp.column_no, ");
        sb.append("tmp.column_name, tmp.data_type,data_format, tmp.nullable, tmp.replacenull,tmp.comma, tmp.column_comment, tmp.isbucket, tmp.iskey, tmp.isvalid, tmp.increment_field,tmp.s_date, tmp.e_date ");
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
            List<DataInterfaceColumnsTmp> queryAllTmp = columnmapper.queryAllTmp(condition);
            for(DataInterfaceColumnsTmp tmp:queryAllTmp){
                String key = tmp.getDataSrcAbbr()+tmp.getDataInterfaceNo()+tmp.getDataInterfaceName()+tmp.getColumnNo();
//				logger.info("\n"+"columnMap:::"+columnMap.get(key)+"\n"+"tmp:::"+tmp);
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
                    data.setNullable(tmp.getNullable());
                    data.setReplacenull(tmp.getReplacenull());
                    data.setIsbucket(tmp.getIsbucket());
                    data.setIskey(tmp.getIskey());
                    data.setIsvalid(tmp.getIsvalid());
                    data.setIncrementfield(tmp.getIncrementfield());
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
                int batchInsertPro = columnmapper.batchInsertPro(list);
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

    /**
     * 数据加载算法批量导入/全部导入
     */
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
                //导入类型是新增直接插入正式表
                tmpList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo});
                delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo});
            }else if("2".equals(importType)) {
                //导入类型修改先将正式表原记录置为失效
                recordList.add(new Object[] {new Date(),dataSrcAbbr,dataInterfaceNo,TimeUtil.getTw()});
                tmpList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo});
                delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo});
            }else if("3".equals(importType)){
                delList.add(new Object[] {batchNo,dataSrcAbbr,dataInterfaceNo});
            }
        }
        logger.info("导入加载算法开始...dataSrcAbbr=["+dataSrcAbbr+"],batchNo=["+batchNo+"],type=["+param.getDbType()+"]");
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
        logger.info("导入结束,导入用时："+(time2-time1));
        return dataSrcAbbr;
    }


}
