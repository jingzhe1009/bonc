package com.ljz.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.ljz.mapper.*;

import com.ljz.model.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ljz.constant.BoncConstant;
import com.ljz.mapper.DataInterface2procMapper;
import com.ljz.mapper.DataInterfaceColumnsMapper;
import com.ljz.mapper.DataInterfaceMapper;
import com.ljz.mapper.attrC2eMapper;
import com.ljz.mapper.entityC2eMapper;
import com.ljz.service.IExcelService;
import com.ljz.util.ExcelUtil;
import com.ljz.util.TimeUtil;
import com.ljz.util.TransUtil;


import static com.ljz.util.TimeUtil.*;

@Service
public class ExcelServiceImpl implements IExcelService{

	private static final Logger logger = LoggerFactory.getLogger(ExcelServiceImpl.class);

	@Resource
	DataRvsdRecordMapper recordMapper;
	@Resource
	DataInterfaceMapper interMapper;
	@Resource
	DataInterfaceColumnsMapper colMapper;
	@Resource
	attrC2eMapper aMapper;
	@Resource
	entityC2eMapper eMapper;
	@Resource
	VersionServiceImpl versionService;
	@Resource
	DataInterface2procMapper pMapper;
	@Resource
	DataSrcMapper dataSrcMapper;

	public  List<String> queryDataSrc() {
		return dataSrcMapper.queryDataSrc();
	}

	/**
	 * 接口导入校验
	 */
	@Override
	@Transactional
	public Map<String,String> importTable(MultipartFile file,String batchNo){
		// TODO Auto-generated method stub
		String ds = "";
		Map<String,String> map = new HashMap<String,String>();
		Map<String,String> dupTabMap = new HashMap();
		Map<String,String> dupCName = new HashMap();
		List<entityC2e> eList = eMapper.queryAll(new entityC2e());
		List<attrC2e> aList = aMapper.queryAll(new attrC2e());
		ExcelUtil obj = ExcelUtil.getInstance();
		obj.initDTable(eList);
		obj.initDCol(aList);
		//excel数据添加到list中
		List<DataInterfaceTmp> list = new ArrayList<DataInterfaceTmp>();
		List listDupInTabName = new ArrayList<>();
		List list2 = new ArrayList<>();
		List listInfaceName = new ArrayList<>();
		List listDupInfaceCName = new ArrayList();
		StringBuffer stringBuffer = new StringBuffer();
		StringBuffer sbBukNum = new StringBuffer();
		String string = null;
		try {
			Workbook wb = getWorkbook(file);
			if(wb==null){
				map.put("msgData", "读取文件失败");
				map.put("dataSrcAbbr", ds);
				return map;
			}

			Sheet sheet = wb.getSheetAt(0);
			if(sheet == null){
				map.put("msgData", "读取文件失败");
				map.put("dataSrcAbbr", ds);
				return map;
			}

			int i;
			for(i=0;i<=sheet.getLastRowNum();i++) {
				if(i==0)
					continue;
				Row row = sheet.getRow(i);
				String dataSrcAbbr = obj.getCellValue(row.getCell(0));
				ds = dataSrcAbbr;
				String dataInfaceNo = obj.getCellValue(row.getCell(1));
				String dataInfaceName = obj.getCellValue(row.getCell(2));
				String dataInfaceCName = obj.getCellValue(row.getCell(3));
				String dataLoadFreq = obj.getCellValue(row.getCell(4));
				String dataLoadMthd = obj.getCellValue(row.getCell(5));
				String filedDelim = obj.getCellValue(row.getCell(6));
				String lineDelim = obj.getCellValue(row.getCell(7));
				String extrnlDatabaseName = obj.getCellValue(row.getCell(8));
				String intrnlDatabaseName = obj.getCellValue(row.getCell(9));
				String extrnlTableName = obj.getCellValue(row.getCell(10));
				String tableType = obj.getCellValue(row.getCell(12));
				String bucketNumber = obj.getCellValue(row.getCell(13));
				String regex2 = "[0-9]+";
				boolean is3 = bucketNumber.matches(regex2);
				if(is3==false || bucketNumber.equals("") ){
					sbBukNum.append("第"+(i+1)+"行[分桶数]应非空且为全数字"+"\n");
					map.put("msgData", "导入失败\r\n" + sbBukNum);
					map.put("dataSrcAbbr", ds);
					return map;
				}
				String startDate = obj.getCellValue(row.getCell(14));
				String endDate = obj.getCellValue(row.getCell(15));
				String intrnlTableName = obj.getCellValue(row.getCell(11));
				if("".equals(intrnlTableName)){  //内表表名为空，去词根表找
					intrnlTableName = TransUtil.transTable(dataInfaceCName,obj.getTableMap());
					if("".equals(intrnlTableName)){  //词根表查找为空，去词根字段查找
//						intrnlTableName = obj.getCellValue(row.getCell(11),obj.getColMap(),dataInfaceCName);
						intrnlTableName = TransUtil.translateField(obj.getColMap(),dataInfaceCName);
						if (intrnlTableName.equals("")){
                            intrnlTableName = "";
                        }else if (!intrnlTableName.startsWith("_")){
                            intrnlTableName = dataSrcAbbr + "_" + intrnlTableName.toUpperCase();
                        }else {
                            intrnlTableName = dataSrcAbbr + intrnlTableName.toUpperCase();
                        }
					}
				}
				TransUtil.sb=new StringBuffer();
				StringBuffer temp = new StringBuffer();
				temp.append(verifyInfaceInfo(dataSrcAbbr,dataInfaceNo,dataInfaceName,dataInfaceCName,
						dataLoadFreq,dataLoadMthd,filedDelim,lineDelim,extrnlDatabaseName,extrnlTableName,
						intrnlDatabaseName,intrnlTableName,tableType,bucketNumber));
				string = temp.toString().trim();
				if (!string.equals("") && !string.isEmpty() && string!=null){
					stringBuffer.append("第"+(i+1)+"行:"+"\n");
					stringBuffer.append(temp.toString());
//					if (stringBuffer.length() >= 300) {
//						stringBuffer.append("......"+"\n"+"错误信息过多，请输入正确数据"+"\n");
//						break;
//					}
//					continue;
				}
				DataInterfaceTmp model  = new DataInterfaceTmp(batchNo, dataInfaceName, dataInfaceCName, dataLoadFreq,
						dataLoadMthd, filedDelim, lineDelim, extrnlDatabaseName, intrnlDatabaseName, extrnlTableName,
						intrnlTableName, tableType, Integer.parseInt(bucketNumber), dataSrcAbbr, dataInfaceNo);
				model.setsDate(TimeUtil.getTy());
				model.seteDate(TimeUtil.getE());
				//logger.info(model.toString());
				dupTabMap.put(dataInfaceName,intrnlTableName);
				dupCName.put(dataInfaceName,dataInfaceCName);
				list.add(model);
				listDupInTabName.add(model.getIntrnlTableName());
				list2.add(dataSrcAbbr);
				listInfaceName.add(model.getDataInterfaceName());
				listDupInfaceCName.add(model.getDataInterfaceDesc());
			}

			//接口名重复性校验
			List<String> infaceName = getDuplicateElements(listInfaceName);
			for (int j=0;j<infaceName.size();j++) {
				if (infaceName != null && !infaceName.isEmpty()) {
					stringBuffer.append("\n"+"[接口名]" + infaceName.get(j) + "有重复");
				}
			}
			//内表表名校验
			String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正则
			Pattern p = Pattern.compile(REGEX_CHINESE);
			for (int j=0;j<listDupInTabName.size();j++) {
				Matcher m = p.matcher(listDupInTabName.get(j).toString());
				if (m.find()){
					stringBuffer.append("第"+(j+2)+"行"+"[内表表名]"+"「"+listDupInTabName.get(j).toString()+"」"+"中文字符在词根找不到映射"+"\n");
				}
				if (!listDupInTabName.get(j).toString().startsWith(list2.get(j).toString()) && !listDupInTabName.get(j).toString().equals("")){
					stringBuffer.append("第"+(j+2)+"行"+"[内表表名]"+"「"+listDupInTabName.get(j).toString()+"」"+"需前缀数据源"+"\n");
				}else if (!listDupInTabName.get(j).toString().equals("") && listDupInTabName.get(j).toString().endsWith("_TB")  ){
					stringBuffer.append("第"+(j+2)+"行"+"[内表表名]"+"「"+listDupInTabName.get(j).toString()+"」"+"需前删除后缀'_TB'"+"\n");
				}else if ("".equals(listDupInTabName.get(j).toString())){
					stringBuffer.append("第"+(j+2)+"行"+"[内表表名]"+listDupInTabName.get(j).toString()+"不能为空"+"\n");
				}
			}
			//内表表名重复性校验
			StringBuffer sb = new StringBuffer();
			List<String> dupInTabList = getDuplicateElements(listDupInTabName);
			if (dupInTabList != null && !dupInTabList.isEmpty()) {
				sb.append("以下[接口名]对应的[内表表名]重复:\n");
				for (int k=0;k<dupInTabList.size();k++) {
					List dupInfaceName = getKeyList(dupTabMap, dupInTabList.get(k));
					sb.append("「"+dupInfaceName+"」-「" + dupInTabList.get(k) + "」" + "\n");
				}
			}
			//接口中文名重复性校验
			List<String> dupInfaceNameL = getDuplicateElements(listDupInfaceCName);
			if (dupInfaceNameL != null && !dupInfaceNameL.isEmpty()) {
				sb.append("以下[接口名]对应的[接口中文描述]重复:\n");
				for (int k=0;k<dupInfaceNameL.size();k++) {
					List dupInfaceName = getKeyList(dupCName, dupInfaceNameL.get(k));
//				if (dupInTabList != null && !dupInTabList.isEmpty()) {
//					sb.append("以下[接口名]对应的[内表表名]重复:\n");
					sb.append("「"+dupInfaceName+"」-「" + dupInfaceNameL.get(k) + "」" + "\n");
//				}
				}
			}

			if (sb.length()>0) {
				stringBuffer.append(sb);
			}

			string = stringBuffer.toString().trim();
			if (!string.equals("") && !string.isEmpty() && string!=null) {
				map.put("msgData", "导入失败\r\n"+stringBuffer);
				map.put("dataSrcAbbr", ds);
				return map;
			}
			int batchInsert = interMapper.batchInsert(list);

			ExcelUtil util = ExcelUtil.getInstance();
			util.clearInterface(ds);
			DataInterface data = new DataInterface();
			data.seteDate(TimeUtil.getTw());
			data.setDataSrcAbbr(ds);
			List<DataInterface> listQueryAll = interMapper.queryAll(data);
			logger.info("listQueryAll:::"+listQueryAll.toString());
			util.initInterface(interMapper.queryAll(data));
			map.put("msgData", "校验成功!记录条数:"+batchInsert);
			map.put("dataSrcAbbr", ds);
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("msgData", "校验失败");
			map.put("dataSrcAbbr", ds);
			return map;
		}
	}


	/**
	 *接口信息校验
	 */
	public  StringBuffer verifyInfaceInfo(String dataSrcAbbr,String dataInterfaceNo,String dataInterfaceName,String dataInfaceChineseName,
			  String dataLoadFreq,String dataLoadMthd,String filedDelim,String lineDelim,String externlDatabase,String extrnlTableName,
			  String intrnlDataBase,String intrnlTableName,String tableType,String bucketNumber){
		StringBuffer stringBuffer = new StringBuffer();
		//数据源校验
		String s = null;
		List<String> list = queryDataSrc();
		for (int i=0;i<list.size();i++) {
		//System.out.println("在这呢："+list.get(i));
			if (list.get(i)	.equals(dataSrcAbbr)){
				s = dataSrcAbbr;
			}
		}
		if (s==null){
			stringBuffer.append("[数据源]"+dataSrcAbbr+"不存在，请登记该数据源"+"\n");
		}else if("".equals(dataSrcAbbr)) {
			stringBuffer.append("[数据源]不能为空" + "\n");
		}

		//接口编号校验
		if("".equals(dataInterfaceNo)){
			stringBuffer.append("[接口编号]不能为空"+"\n");
		}else if(dataInterfaceNo.length()!=5){
			stringBuffer.append("[接口编号]长度为5"+"\n");
		}

		//接口名校验
		//boolean iss = dataInterfaceName.matches(dataSrcAbbr+"_"+dataInterfaceNo+"[_(A-Z)|.\\\\n]*");
		boolean iss = dataInterfaceName.startsWith(dataSrcAbbr);
		if("".equals(dataInterfaceName)) {
			stringBuffer.append("[接口名]不能为空" + "\n");
		}else if(!iss){
		stringBuffer.append("[接口名]应前缀数据源"+"\n");
		}

		//接口中文名校验
		if("".equals(dataInfaceChineseName)){
		stringBuffer.append("[接口中文名]称不能为空"+"\n");
		}

		//数据加载频度 年：Y   半年：S   季：Q    M：月 	W：周   D2：日（自然日）  D1：日（交易日）
		if("".equals(dataLoadFreq)) {
			stringBuffer.append("[数据加载频度]不能为空" + "\n");
		}else if (!dataLoadFreq.equals("Y") && !dataLoadFreq.equals("S") && !dataLoadFreq.equals("Q") && !dataLoadFreq.equals("M") && !dataLoadFreq.equals("W") && !dataLoadFreq.equals("D1") && !dataLoadFreq.equals("D2")){
			stringBuffer.append("[数据加载频度]应为Y/S/Q/M/W/D2/D1" + "\n");
		}

		//数据加载方式校验
		String regex = "[Z|Q]";
		boolean is = dataLoadMthd.matches(regex);
		if("".equals(dataLoadMthd)){
			stringBuffer.append("[数据加载方式]不能为空"+"\n");
		}else if (is==false){
		stringBuffer.append("[数据加载方式]应为Z/Q(大写)"+"\n");
		}

		if ("".equals(filedDelim)){
			filedDelim="|";
		}
		if ("".equals(lineDelim)){
			lineDelim="\012";
		}
		//字段分隔符
//		if ("".equals(filedDelim)){
//	//		stringBuffer.append("字段分隔符不能为空"+"\n");
//		filedDelim="|";
//		}

		//行分隔符
//		if ("".equals(lineDelim)){
//		stringBuffer.append("行分隔符不能为空"+"\n");
//		}

		//外表库sdata_oltp
		if("".equals(externlDatabase)) {
			stringBuffer.append("[外表数据库]不能为空" + "\n");
		}else if (!externlDatabase.equals("sdata_oltp")){
			stringBuffer.append("[外表数据库]应为sdata_oltp"+"\n");
		}

		//外表表名校验
		if ("".equals(extrnlTableName)){
			stringBuffer.append("[外表表名]不能为空"+"\n");
		}else if (!extrnlTableName.startsWith(dataSrcAbbr)){
			stringBuffer.append("[外表表名]需前缀数据源"+"\n");
		}

		//内表库odata
//		if (!externlDatabase.equals("odata")){
//			stringBuffer.append("[内表数据库]应为odata"+"\n");
//		}else
		if ("".equals(intrnlDataBase)){
			stringBuffer.append("[内表数据库]不能为空"+"\n");
		}else if (!intrnlDataBase.equals("odata")) {
			stringBuffer.append("[内表数据库]应为odata" + "\n");
		}
		/*
		内表表名唯一性校验
		*/
		//内表表名校验
		//String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正则
		//Pattern p = Pattern.compile(REGEX_CHINESE);
		//Matcher m = p.matcher(intrnlTableName);
		//if (m.find()){
		//stringBuffer.append("[内表表名]不能含有中文字符"+"\n");
		//}
		//if ("".equals(intrnlTableName)){
		//stringBuffer.append("[内表表名]不能为空"+"\n");
		//}
		//if (!intrnlTableName.startsWith(dataSrcAbbr)){
		//stringBuffer.append("[内表表名]需前缀数据源"+"\n");
		//}

		//表类型校验
		String regex1 = "[Z|S|z|s]";
		boolean is1 = tableType.matches(regex1);
		if (is1==false || "".equals(tableType)){
		stringBuffer.append("[表类型]应为Z/S"+"\n");
		}
		////表大小校验
		//String regex2 = "[0-9]+";
		//boolean is4 = bucketNumber.matches(regex2);
		//if(is4==false && tableSize.equals("") ){
		//stringBuffer.append("表大小应非空且为全数字"+"\n");
		//}
		//
		//字段分隔符校验
		if ("".equals(filedDelim)) {
		stringBuffer.append("字段分隔符不能为空" + "\n");
		}

		//行分隔符校验
		if ("".equals(lineDelim)) {
		stringBuffer.append("行分隔符不能为空" + "\n");
		}

		//分桶数校验
//		String regex2 = "[0-9]+";
//		boolean is3 = bucketNumber.matches(regex2);
//		if(is3==false || bucketNumber.equals("") ){
//		stringBuffer.append("[分桶数]应非空且为全数字"+"\n");
//		}

		//日期格式校验
//		String regex3 = "^([1-9]\\d{3}-)(([0]{0,1}[1-9]-)|([1][0-2]-))(([0-3]{0,1}[0-9]))$";
//		boolean sdate = Pattern.matches(regex3,startDate);
//		boolean edate = Pattern.matches(regex3,endDate);
//		if (sdate == false || edate == false){
//		stringBuffer.append("[日期格式]应为yyyy-MM-dd"+"\n");
//		}else if ("".equals(sdate)){
//		stringBuffer.append("[生效日期]不能为空"+"\n");
//		}else if ("".equals(edate)){
//		stringBuffer.append("[失效日期]不能为空"+"\n");
//		}
		return stringBuffer;
	}

	/**
	 * 接口字段导入校验
	 */
	@Override
	@Transactional
	public Map<String,String> importColumn(MultipartFile file,String batchNo) {

		String ds = "";
		Map<String,String> msgMap = new HashMap<String,String>();
		List<attrC2e> aList = aMapper.queryAll(new attrC2e());
		ExcelUtil obj = ExcelUtil.getInstance();
		obj.initDCol(aList);
		//excel数据添加到list中
		List<DataInterfaceColumnsTmp> list = new ArrayList<DataInterfaceColumnsTmp>();
		StringBuffer stringBuffer = new StringBuffer();
		String string = null;
		List<String> listDupFieldName = new ArrayList<>();
		List list1 = new ArrayList<>();
		Map<String,List> map = new HashMap<>();
//		Map<String,String> dupFieldMap = new HashMap();
		try {
			Workbook wb = getWorkbook(file);
			if(wb==null){
				msgMap.put("msgData", "读取文件失败");
				msgMap.put("dataSrcAbbr", ds);
				return msgMap;
			}
			Sheet sheet = wb.getSheetAt(0);
			if(sheet == null){
				msgMap.put("msgData", "读取文件失败");
				msgMap.put("dataSrcAbbr", ds);
				return msgMap;
			}
			StringBuffer sbOrderNum = new StringBuffer();
			for(int i=0;i<=sheet.getLastRowNum();i++) {
				if(i==0)
					continue;
				Row row = sheet.getRow(i);
				if(row.getCell(0)==null)
					continue;
				String dataSrcAbbr = obj.getCellValue(row.getCell(0));
				String infaceNo = obj.getCellValue(row.getCell(1));
				String infaceName = obj.getCellValue(row.getCell(2));
				String orderNumber = obj.getCellValue(row.getCell(3));
				String regex1 = "[0-9]+";
				boolean is = orderNumber.matches(regex1);
				if (!is || orderNumber.equals("")){
					sbOrderNum.append("第"+(i+1)+"行:[序号]应非空且为数字"+"\n");
					msgMap.put("msgData", "导入失败\r\n" + sbOrderNum);
					msgMap.put("dataSrcAbbr", ds);
					return msgMap;
				}
				ds = dataSrcAbbr;
				String dataType = obj.getCellValue(row.getCell(5));
				String format = obj.getCellValue(row.getCell(6));
				String nullable = obj.getCellValue(row.getCell(7));
				String replacenull = obj.getCellValue(row.getCell(8));
				String comma = obj.getCellValue(row.getCell(9));
				String fieldDesc = obj.getCellValue(row.getCell(10));
				String fieldName = obj.getCellValue(row.getCell(4));
				if("".equals(fieldName)){  //字段名为空，去词根表找
						fieldName = obj.getCellValue(row.getCell(4),obj.getColMap(),fieldDesc).toUpperCase();
						if (fieldName.startsWith("_")){
							fieldName = fieldName.substring(1).toUpperCase();
						}
				}
				TransUtil.sb = new StringBuffer();
				String bucketField = obj.getCellValue(row.getCell(11));
				String iskey = obj.getCellValue(row.getCell(12));
				String isvalid = obj.getCellValue(row.getCell(13));
				String incrementfield = obj.getCellValue(row.getCell(14));
				String startDate = obj.getCellValue(row.getCell(15));
				String endDate = obj.getCellValue(row.getCell(16));
				StringBuffer temp = new StringBuffer();
				temp.append(verifyFieldInfo(dataSrcAbbr,infaceNo,infaceName,orderNumber,fieldName, dataType,
						format,nullable,comma,fieldDesc,bucketField));
				string = temp.toString().trim();
				if (!string.equals("") && !string.isEmpty() && string!=null){
					stringBuffer.append("第"+(i+1)+"行:"+"\n");
					stringBuffer.append(temp.toString());
//					if (stringBuffer.length() >= 300) {
//						stringBuffer.append("......"+"\n"+"错误信息过多，请输入正确数据"+"\n");
////						break;
//						msgMap.put("msgData", "导入失败\r\n" + stringBuffer);
//						msgMap.put("dataSrcAbbr", ds);
//						return msgMap;
//					}
//					continue;
				}
				DataInterfaceColumnsTmp model = new DataInterfaceColumnsTmp(dataSrcAbbr, infaceNo,
						Integer.parseInt(orderNumber), infaceName, fieldName, dataType, format,
						 comma, fieldDesc, bucketField, iskey, isvalid, incrementfield);
				if(nullable!=null && !"".equals(nullable)){
					model.setNullable(Integer.parseInt(nullable));
				}else{
					model.setNullable(0);
				}
				if( replacenull!=null && !"".equals(replacenull)){
					model.setReplacenull(Integer.parseInt(replacenull));
				}else{
					model.setReplacenull(0);
				}
				/*model.setsDate(new java.sql.Date(new Date().getTime()));*/
				model.setsDate(TimeUtil.getTy());
				model.seteDate(TimeUtil.getE());
				model.setBatchNo(batchNo);
				list.add(model);
				list1.add(fieldName);
				listDupFieldName.add(infaceName+"」-「"+fieldName);
//				dupFieldMap.put(infaceName+"+"+fieldDesc,fieldName);
			}
			//字段名校验
			String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正则
			Pattern p = Pattern.compile(REGEX_CHINESE);
				for (int j=0;j<list1.size();j++) {
					String str = list1.get(j).toString();
					Matcher m = p.matcher(str);
				if (m.find()){
					stringBuffer.append("第"+(j+2)+"行"+"[字段名]"+"「"+list1.get(j).toString()+"」"+"中文字符在词根找不到映射"+"\n");
				}else if("".equals(list1.get(j).toString())){
					stringBuffer.append("第"+(j+2)+"行"+"[字段名]不能为空"+"\n");
				}
			}
			//重复性校验
			StringBuffer sb = new StringBuffer();
			List<String> dupFieldNameList = getDuplicateElements(listDupFieldName);
			if (dupFieldNameList != null && !dupFieldNameList.isEmpty()) {
				sb.append("\n以下[接口名]对应的[字段名]重复:" + "\n");
				for (int k=0;k<dupFieldNameList.size();k++) {
					sb.append("「" + dupFieldNameList.get(k) + "」" + "\n");
				}
			}

			if (sb.length()>0) {
				stringBuffer.append(sb);
//				return "导入失败\r\n" + stringBuffer;
			}
			string = stringBuffer.toString().trim();
			if (!string.equals("") && !string.isEmpty() && string!=null) {
				msgMap.put("msgData", "导入失败\r\n" + stringBuffer);
				msgMap.put("dataSrcAbbr", ds);
				return msgMap;
			}
			//批量入库
			/*for(DataInterfaceColumns record :list) {
				colMapper.insertSelective(record);
				insertVersion(record, "2");
			}*/
			//DataInterfaceColumns key = new DataInterfaceColumns();
			//key.setDataSrcAbbr(ds);
			//colMapper.deleteByPrimaryKey(key);
			int batchInsert = colMapper.batchInsert(list);
			ExcelUtil util = ExcelUtil.getInstance();
			util.clearColumn(ds);
			DataInterfaceColumns data = new DataInterfaceColumns();
			data.seteDate(TimeUtil.getTw());
			data.setDataSrcAbbr(ds);
			util.initColumn(colMapper.queryAll(data));
			msgMap.put("msgData", "校验成功!记录条数:"+batchInsert);
			msgMap.put("dataSrcAbbr", ds);
			return msgMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgMap.put("msgData", "导入失败");
			msgMap.put("dataSrcAbbr", ds);
			return msgMap;
		}
		//return "导入成功!记录条数:"+list.size();
	}

	/**
	 *接口字段信息校验
	 */
	public  StringBuffer verifyFieldInfo(String dataSrcAbbr,String dataInterfaceNo,String dataInterfaceName,String orderNumber,String fieldName,
										 String dataType,String dataFormat,
										 String nullable,
//										 String replacenull,
										 String comma,String columnComment,String bucketField){
		StringBuffer stringBuffer = new StringBuffer();
		//数据源校验
		String s = null;
		List<String> list = queryDataSrc();
		for (int i=0;i<list.size();i++) {
//			System.out.println("在这呢："+list.get(i));
			if (list.get(i)	.equals(dataSrcAbbr)){
				s = dataSrcAbbr;
			}
		}
		if (s==null){
			stringBuffer.append("[数据源]"+dataSrcAbbr+"不存在，请登记该数据源"+"\n");
		}else if("".equals(dataSrcAbbr))
			stringBuffer.append("[数据源]不能为空"+"\n");

		//接口编号校验
		if("".equals(dataInterfaceNo))
			stringBuffer.append("[接口编号]不能为空"+"\n");
		if(dataInterfaceNo.length()!=5){
			stringBuffer.append("[接口编号]长度应为5"+"\n");
		}

		//接口名校验
//		boolean iss = dataInterfaceName.matches(dataSrcAbbr+"_"+dataInterfaceNo+"[_(A-Z)|.\\\\n]*");
		boolean iss = dataInterfaceName.startsWith(dataSrcAbbr);
		if(!iss){
			stringBuffer.append("[接口名]应前缀数据源"+"\n");
		}else if("".equals(dataInterfaceName))
			stringBuffer.append("[接口名]不能为空"+"\n");

		//序号
//		String regex1 = "[0-9]+";
//		boolean is = orderNumber.matches(regex1);
//		if (!is || orderNumber.equals("")){
//			stringBuffer.append("[序号]应非空且为数字"+"\n");
//		}

		//字段名校验
		/*String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正则
		Pattern p = Pattern.compile(REGEX_CHINESE);
		Matcher m = p.matcher(fieldName);
		if (m.find()){
			stringBuffer.append("[字段名]不能含有中文"+"\n");
		}else if("".equals(fieldName)){
			stringBuffer.append("[字段名]不能为空"+"\n");
		}*/


		/*
		数据类型校验
		 */
		//数据类型
		if("".equals(dataType)) {
			stringBuffer.append("[数据类型]不能为空" + "\n");
		}

		if(!dataType.toUpperCase().startsWith("CHAR")  && !dataType.toUpperCase().startsWith("INT") && !dataType.toUpperCase().startsWith("VARCHAR")
				&& !dataType.toUpperCase().startsWith("BIGINT") && !dataType.toUpperCase().startsWith("STRING") && !dataType.toUpperCase().startsWith("NUMBER")
				&& !dataType.toUpperCase().startsWith("DECIMAL") && !dataType.toUpperCase().startsWith("DATE") && !dataType.toUpperCase().startsWith("TIMESTAMP")){
			stringBuffer.append("[数据类型]'"+dataType + "'不存在\n");
		}

		//是否非空校验
		if(!nullable.isEmpty() && nullable!=null && !nullable.equals("0") && !nullable.equals("1") ){
			stringBuffer.append("[是否非空]应为0/1,不填默认为0");
		}

		//逗号分隔符
		if (comma!=null && !comma.isEmpty() && !comma.equals("") && !comma.equals(",")){
			stringBuffer.append("[逗号分隔符]应为英文逗号\n");
		}

		//字段说明校验
		if("".equals(columnComment)) {
			stringBuffer.append("[字段说明]不能为空" + "\n");
		}
		//日期格式校验
		/*String regex3 = "^([1-9]\\d{3}-)(([0]{0,1}[1-9]-)|([1][0-2]-))(([0-3]{0,1}[0-9]))$";
		boolean sdate = Pattern.matches(regex3,startDate);
		boolean edate = Pattern.matches(regex3,endDate);
		if (sdate == false || edate == false){
			stringBuffer.append("[日期格式]应为yyyy-MM-dd"+"\n");
		}else if ("".equals(sdate)){
			stringBuffer.append("[生效日期]不能为空"+"\n");
		}else if ("".equals(edate)){
			stringBuffer.append("[失效日期]不能为空"+"\n");
		}*/

		return stringBuffer;
	}

	/**
	 * 导入数据算法
	 * @param file
	 * @param batchNo
	 * @return
	 */
	@Override
	@Transactional
	public Map<String,String> importProc(MultipartFile file,String batchNo){
		// TODO Auto-generated method stub
		String ds = "";
		Map<String,String> msgMap = new HashMap<String,String>();
		ExcelUtil obj = ExcelUtil.getInstance();
		//excel数据添加到list中
		List<DataInterface2procTmp> list = new ArrayList<DataInterface2procTmp>();
		try {
			Workbook wb = getWorkbook(file);
			if(wb==null){
				msgMap.put("msgData","读取文件失败");
				msgMap.put("dataSrcAbbr", ds);
				return msgMap;
			}
			Sheet sheet = wb.getSheetAt(0);
			if(sheet == null){
				msgMap.put("msgData","读取文件失败");
				msgMap.put("dataSrcAbbr", ds);
				return msgMap;
			}
			StringBuffer stringBuffer = new StringBuffer();
			String string = null;
			for(int i=0;i<=sheet.getLastRowNum();i++) {
				if(i==0)
					continue;
				Row row = sheet.getRow(i);
				String dataSrcAbbr = obj.getCellValue(row.getCell(0));
				String s = null;
				String dataInterfaceNo = obj.getCellValue(row.getCell(1));
				ds = dataSrcAbbr;
				String dbName = obj.getCellValue(row.getCell(2));
				String procName = obj.getCellValue(row.getCell(3));
				StringBuffer temp = new StringBuffer();
				temp.append(verifyLoadProcInfo(dataSrcAbbr,dataInterfaceNo,dbName,procName));
				string = temp.toString().trim();
				if (!string.equals("") && !string.isEmpty() && string!=null){
					stringBuffer.append("第"+(i+1)+"行:"+"\n");
					stringBuffer.append(temp.toString());
				}
				DataInterface2procTmp model = new DataInterface2procTmp();
				model.setDataSrcAbbr(dataSrcAbbr);
				model.setDataInterfaceNo(dataInterfaceNo);
				model.setProcDatabaseName(dbName);
				model.setProcName(procName);
				model.setsDate(new java.sql.Date(new Date().getTime()));
				model.seteDate(ExcelUtil.getInstance().StringToDate(BoncConstant.CON_E_DATE));
				model.setBatchNo(batchNo);
				list.add(model);
			}
			//批量入库
			int batchInsert = pMapper.batchInsert(list);

			obj.clearProc(ds);
			DataInterface2proc data = new DataInterface2proc();
			data.seteDate(TimeUtil.getTw());
			data.setDataSrcAbbr(ds);
			obj.initProc(pMapper.queryAll(data));

			string = stringBuffer.toString().trim();
			if (!string.equals("") && !string.isEmpty() && string!=null) {
				msgMap.put("msgData", "校验失败:\n"+stringBuffer);
			}else {
				msgMap.put("msgData","校验成功!记录条数:"+batchInsert);
				msgMap.put("dataSrcAbbr", ds);
			}
			return msgMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgMap.put("msgData","导入失败");
			msgMap.put("dataSrcAbbr", ds);
			return msgMap;
		}
	}


	/**
	 * 数据加载算法导入校验
	 * @param dataSrcAbbr
	 * @param dataInterfaceNo
	 * @param dbName
	 * @param procName
	 * @return
	 */
	public  StringBuffer verifyLoadProcInfo(String dataSrcAbbr,String dataInterfaceNo,String dbName,String procName){
		StringBuffer stringBuffer = new StringBuffer();
		List<String> dataSrcList = queryDataSrc();
		List dataLoadProcList = interMapper.queryDbName();
		if (!dataSrcList.contains(dataSrcAbbr.trim())){
			stringBuffer.append("[数据源]"+dataSrcAbbr+"不存在，请登记该数据源"+"\n");
		}else if("".equals(dataSrcAbbr)) {
			stringBuffer.append("[数据源]不能为空" + "\n");
		}

		if("".equals(dataInterfaceNo.trim())){
			stringBuffer.append("[接口编号]不能为空\n");
		}else if (dataInterfaceNo.length()!=5){
			stringBuffer.append("[接口编号]长度应为5\n");
		}

		if (dbName == null || "".equals(dbName.trim()) || dbName.isEmpty()){
			stringBuffer.append("[存储过程数据库名]不能为空\n");
		}else if (!dataLoadProcList.contains(dbName)){
			stringBuffer.append("[存储过程数据库名]不存在\n");
		}

		if(procName==null || "".equals(procName.trim()) || procName.isEmpty() ){
			stringBuffer.append("[存储过程名]不能为空\n");
		}

		return stringBuffer;
	}

	/**
	 * 筛选重复值
	 * @param list1
	 * @return
	 */
	public List<String> getDuplicateElements(List<String> list1) {
		// list 对应的 Stream
		// 获得元素出现频率的 Map，键为元素，值为元素出现的次数
		// 所有 entry 对应的 Stream
		// 过滤出元素出现次数大于 1 (重复元素)的 entry
		// 获得 entry 的键(重复元素)对应的 Stream
		// 转化为 List
		List<String> list = list1.stream().map(String::toUpperCase).collect(Collectors.toList());
//		System.out.println("list:::"+list);
		return list.stream().collect(Collectors.toMap(e -> e, e -> 1, Integer::sum)).entrySet().stream().filter(e -> e.getValue() > 1).map(Map.Entry::getKey).collect(Collectors.toList());
	}







	@Override
	@Transactional
	public Map<String,String>  importInfo(MultipartFile file,String batchNo) {
		//String batch_no = UUID.randomUUID().toString();
		String ds = "";
		int batchInsertRecord = 0;
		int batchInsertInface = 0;
		int batchInsertColumn = 0;
		int batchInsertProc = 0;
		Map<String, String> msgMap = new HashMap<String, String>();
		Map<String, String> mapRecord = new HashMap<String, String>();
		Map<String, String> mapInface = new HashMap<String, String>();
		Map<String,String> mapColumn = new HashMap<String,String>();
		Map<String,String> mapProc = new HashMap<String,String>();
		List<DataRvsdRecordTmp> listRecord = new ArrayList<DataRvsdRecordTmp>();
		List<DataInterfaceTmp> listInface = new ArrayList<DataInterfaceTmp>();
		List<DataInterfaceColumnsTmp> listColumn = new ArrayList<DataInterfaceColumnsTmp>();
		List<DataInterface2procTmp> listProc = new ArrayList<DataInterface2procTmp>();
		ExcelUtil objInface = ExcelUtil.getInstance();
		try {
			Workbook wb = getWorkbook(file);
			if (wb == null) {
				msgMap.put("msgData", "读取文件失败");
				msgMap.put("dataSrcAbbr", ds);
				return msgMap;
			}

			//sheet2接口信息
			Sheet sheet2 = wb.getSheetAt(1);
			logger.info("sheet2.getLastRowNum():::"+sheet2.getLastRowNum());
			if (sheet2 != null && sheet2.getLastRowNum() != 0) {
	//			String ds = "";
	//			Map<String, String> mapInface = new HashMap<String, String>();
				Map<String, String> dupTabMap = new HashMap();
				Map<String, String> dupCName = new HashMap();
				List<entityC2e> eList = eMapper.queryAll(new entityC2e());
				List<attrC2e> aList = aMapper.queryAll(new attrC2e());
				objInface.initDTable(eList);
				objInface.initDCol(aList);
				//excel数据添加到list中
//				List<DataInterfaceTmp> list = new ArrayList<DataInterfaceTmp>();
				List listDupInTabName = new ArrayList<>();
				List list2 = new ArrayList<>();
				List listInfaceName = new ArrayList<>();
				List listDupInfaceCName = new ArrayList();
				StringBuffer bufferInface = new StringBuffer();
				StringBuffer sbBukNum = new StringBuffer();
				String string = null;
				int i;
				for (i = 0; i <= sheet2.getLastRowNum(); i++) {
					if (i == 0)
						continue;
					Row row = sheet2.getRow(i);
					String dataSrcAbbr = objInface.getCellValue(row.getCell(0));
					String a = null;
					if (!ds.equals("") && !ds.equals(dataSrcAbbr)){
						a = "[数据源]不一致,请校验\n";
					}
					ds = dataSrcAbbr;
					String dataInfaceNo = objInface.getCellValue(row.getCell(1));
					String dataInfaceName = objInface.getCellValue(row.getCell(2));
					String dataInfaceCName = objInface.getCellValue(row.getCell(3));
					String dataLoadFreq = objInface.getCellValue(row.getCell(4));
					String dataLoadMthd = objInface.getCellValue(row.getCell(5));
					String filedDelim = objInface.getCellValue(row.getCell(6));
					String lineDelim = objInface.getCellValue(row.getCell(7));
					String extrnlDatabaseName = objInface.getCellValue(row.getCell(8));
					String intrnlDatabaseName = objInface.getCellValue(row.getCell(9));
					String extrnlTableName = objInface.getCellValue(row.getCell(10));
					String tableType = objInface.getCellValue(row.getCell(12));
					String bucketNumber = objInface.getCellValue(row.getCell(13));
					String regex2 = "[0-9]+";
					boolean is3 = bucketNumber.matches(regex2);
					if (is3 == false || bucketNumber.equals("")) {
						sbBukNum.append("第" + (i + 1) + "行[分桶数]"+bucketNumber+"应非空且为全数字" + "\n");
						mapInface.put("msgData", "接口信息导入失败:\r\n" + sbBukNum);
						mapInface.put("dataSrcAbbr", ds);
						return mapInface;
					}
//					String startDate = objInface.getCellValue(row.getCell(14));
//					String endDate = objInface.getCellValue(row.getCell(15));
					String intrnlTableName = objInface.getCellValue(row.getCell(11));
					if ("".equals(intrnlTableName)) {  //内表表名为空，去词根表找
						intrnlTableName = TransUtil.transTable(dataInfaceCName, objInface.getTableMap());
						if ("".equals(intrnlTableName)) {  //词根表查找为空，去词根字段查找
//						intrnlTableName = obj.getCellValue(row.getCell(11),obj.getColMap(),dataInfaceCName);
							intrnlTableName = TransUtil.translateField(objInface.getColMap(), dataInfaceCName);
							if (intrnlTableName.equals("")) {
								intrnlTableName = "";
							} else if (!intrnlTableName.startsWith("_")) {
								intrnlTableName = dataSrcAbbr + "_" + intrnlTableName.toUpperCase();
							} else {
								intrnlTableName = dataSrcAbbr + intrnlTableName.toUpperCase();
							}
						}
					}
					TransUtil.sb = new StringBuffer();
					StringBuffer temp = new StringBuffer();
//					System.out.println("a:::"+a);
					if (a!=null) {
						temp.append(a+verifyInfaceInfo(dataSrcAbbr, dataInfaceNo, dataInfaceName, dataInfaceCName,
								dataLoadFreq, dataLoadMthd, filedDelim, lineDelim, extrnlDatabaseName, extrnlTableName,
								intrnlDatabaseName, intrnlTableName, tableType, bucketNumber));
					}else {
						temp.append(verifyInfaceInfo(dataSrcAbbr, dataInfaceNo, dataInfaceName, dataInfaceCName,
								dataLoadFreq, dataLoadMthd, filedDelim, lineDelim, extrnlDatabaseName, extrnlTableName,
								intrnlDatabaseName, intrnlTableName, tableType, bucketNumber));
					}
					string = temp.toString().trim();
					if (!string.equals("") && !string.isEmpty() && string != null) {
						bufferInface.append("第" + (i + 1) + "行:" + "\n");
						bufferInface.append(temp.toString());
					}
					DataInterfaceTmp model = new DataInterfaceTmp(batchNo, dataInfaceName, dataInfaceCName, dataLoadFreq, dataLoadMthd,
							filedDelim, lineDelim, extrnlDatabaseName, intrnlDatabaseName, extrnlTableName, intrnlTableName,
							tableType, Integer.parseInt(bucketNumber),
							dataSrcAbbr, dataInfaceNo);
					/*model.setsDate(new java.sql.Date(new Date().getTime()));*/
					model.setsDate(TimeUtil.getTy());
					model.seteDate(getE());
					//logger.info(model.toString());
					dupTabMap.put(dataInfaceName, intrnlTableName);
					dupCName.put(dataInfaceName, dataInfaceCName);
					listInface.add(model);
					listDupInTabName.add(model.getIntrnlTableName());
					list2.add(dataSrcAbbr);
					listInfaceName.add(model.getDataInterfaceName());
					listDupInfaceCName.add(model.getDataInterfaceDesc());
				}

				//接口名重复性校验
				List<String> infaceName = getDuplicateElements(listInfaceName);
				for (int j = 0; j < infaceName.size(); j++) {
					if (infaceName != null && !infaceName.isEmpty()) {
						bufferInface.append("\n" + "[接口名]" + infaceName.get(j) + "有重复");
					}
				}
				//内表表名校验
				String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正则
				Pattern p = Pattern.compile(REGEX_CHINESE);
				for (int j = 0; j < listDupInTabName.size(); j++) {
					Matcher m = p.matcher(listDupInTabName.get(j).toString());
					if (m.find()) {
						bufferInface.append("第" + (j + 2) + "行" + "[内表表名]" + "「" + listDupInTabName.get(j).toString() + "」" + "中文字符在词根找不到映射" + "\n");
					}
					if (!listDupInTabName.get(j).toString().startsWith(list2.get(j).toString()) && !listDupInTabName.get(j).toString().equals("")) {
						bufferInface.append("第" + (j + 2) + "行" + "[内表表名]" + "「" + listDupInTabName.get(j).toString() + "」" + "需前缀数据源" + "\n");
					} else if (!listDupInTabName.get(j).toString().equals("") && listDupInTabName.get(j).toString().endsWith("_TB")) {
						bufferInface.append("第" + (j + 2) + "行" + "[内表表名]" + "「" + listDupInTabName.get(j).toString() + "」" + "需前删除后缀'_TB'" + "\n");
					} else if ("".equals(listDupInTabName.get(j).toString())) {
						bufferInface.append("第" + (j + 2) + "行" + "[内表表名]" + listDupInTabName.get(j).toString() + "不能为空" + "\n");
					}
				}
				//内表表名重复性校验
				StringBuffer sb = new StringBuffer();
				List<String> dupInTabList = getDuplicateElements(listDupInTabName);
				if (dupInTabList != null && !dupInTabList.isEmpty()) {
					sb.append("以下[接口名]对应的[内表表名]重复:\n");
					for (int k = 0; k < dupInTabList.size(); k++) {
						List dupInfaceName = getKeyList(dupTabMap, dupInTabList.get(k));
						sb.append("「" + dupInfaceName + "」-「" + dupInTabList.get(k) + "」" + "\n");
					}
				}
				//接口中文名重复性校验
				List<String> dupInfaceNameL = getDuplicateElements(listDupInfaceCName);
				if (dupInfaceNameL != null && !dupInfaceNameL.isEmpty()) {
					sb.append("以下[接口名]对应的[接口中文描述]重复:\n");
					for (int k = 0; k < dupInfaceNameL.size(); k++) {
						List dupInfaceName = getKeyList(dupCName, dupInfaceNameL.get(k));
						sb.append("「" + dupInfaceName + "」-「" + dupInfaceNameL.get(k) + "」" + "\n");
					}
				}

				if (sb.length() > 0) {
					bufferInface.append(sb);
				}

				string = bufferInface.toString().trim();
				if (!string.equals("") && !string.isEmpty() && string != null) {
					mapInface.put("msgData", "接口信息导入失败\r\n" + bufferInface);
					mapInface.put("dataSrcAbbr", ds);
//					return mapInface;
				}
//				batchInsertInface = interMapper.batchInsert(list);
//				ExcelUtil util = ExcelUtil.getInstance();
//				util.clearInterface(ds);
//				DataInterface data = new DataInterface();
//				data.seteDate(TimeUtil.getTw());
//				data.setDataSrcAbbr(ds);
//				util.initInterface(interMapper.queryAll(data));
//				mapInface.put("msgData", "接口信息校验成功!记录条数:" + batchInsert+"\n");
//				mapInface.put("dataSrcAbbr", ds);
			}else {
				mapInface.put("msgData", "数据接口信息表不能为空\r\n");
			}

			//sheet1版本信息
			Sheet sheet1 = wb.getSheetAt(0);
			logger.info("sheet1.getLastRowNum():::"+sheet1.getLastRowNum());
			if (sheet1 != null && sheet1.getLastRowNum() != 0) {
				StringBuffer recordBuffer = new StringBuffer();
				for(int i=0;i<=sheet1.getLastRowNum();i++) {
					if (i == 0)
						continue;
					Row row = sheet1.getRow(i);
					String needVrsnNbr = objInface.getCellValue(row.getCell(0));
					String chgPsn = objInface.getCellValue(row.getCell(1));
					String exctPsn = objInface.getCellValue(row.getCell(2));
					String corrIntfStdVrsn = objInface.getCellValue(row.getCell(3));
					String intfDscr = objInface.getCellValue(row.getCell(4));

					if (needVrsnNbr.trim() == null || needVrsnNbr.trim().isEmpty() || needVrsnNbr.trim().equals("")) {
						recordBuffer.append("需求版本号第"+(i+1)+"行不能为空");
					}
					if (chgPsn.trim() == null || chgPsn.trim().isEmpty() || chgPsn.trim().equals("")) {
						recordBuffer.append("变更人第"+(i+1)+"行不能为空");
					}
					if (exctPsn.trim() == null || exctPsn.trim().isEmpty() || exctPsn.trim().equals("")) {
						recordBuffer.append("执行人第"+(i+1)+"行不能为空");
					}
					if (corrIntfStdVrsn.trim() == null || corrIntfStdVrsn.trim().isEmpty() || corrIntfStdVrsn.trim().equals("")) {
						recordBuffer.append("对应接口规范版本第"+(i+1)+"行不能为空");
					}
					if (intfDscr.trim() == null || intfDscr.trim().isEmpty() || intfDscr.trim().equals("")) {
						recordBuffer.append("接口说明第"+(i+1)+"行不能为空");
					}
					if (recordBuffer.length() != 0) {
						mapInface.put("msgData", "接口修订记录导入失败:\r\n" + recordBuffer);
						mapInface.put("dataSrcAbbr", ds);
						return mapInface;
					}

					DataRvsdRecordTmp model = new DataRvsdRecordTmp();
					model.setNeedVrsnNbr(needVrsnNbr);
					model.setDataDrcAbbr(ds);
					model.setChgPsn(chgPsn);
					model.setExctPsn(exctPsn);
					model.setCorrIntfStdVrsn(corrIntfStdVrsn);
					model.setIntfDscr(intfDscr);
					model.setsDate(getDate(getToday()));
					model.seteDate(getDate(getE()));
					model.setBatchNo(batchNo);
					listRecord.add(model);
					logger.info("listRecord:::" + listRecord.toString());
				}
			}else {
				mapRecord.put("msgData","修订记录表不能为空");
			}

			//sheet3接口字段信息
			Sheet sheet3 = wb.getSheetAt(2);
			logger.info("sheet3.getLastRowNum():::"+sheet3.getLastRowNum());
			if(sheet3 != null && sheet3.getLastRowNum() !=0){
	//			Map<String,String> mapColumn = new HashMap<String,String>();
				List<attrC2e> aList1 = aMapper.queryAll(new attrC2e());
				ExcelUtil objColumn = ExcelUtil.getInstance();
				objColumn.initDCol(aList1);
				//excel数据添加到list中
//				List<DataInterfaceColumnsTmp> listColumn = new ArrayList<DataInterfaceColumnsTmp>();
				String stringColumn = null;
				List<String> listDupFieldName = new ArrayList<>();
				List list1 = new ArrayList<>();
				StringBuffer bufferColumn = new StringBuffer();
				StringBuffer sbOrderNum = new StringBuffer();
				for(int i=0;i<=sheet3.getLastRowNum();i++) {
					if(i==0)
						continue;
					Row row = sheet3.getRow(i);
					if(row.getCell(0)==null)
						continue;
					String dataSrcAbbr = objColumn.getCellValue(row.getCell(0));
					String infaceNo = objColumn.getCellValue(row.getCell(1));
					String infaceName = objColumn.getCellValue(row.getCell(2));
					String orderNumber = objColumn.getCellValue(row.getCell(3));
					String regex1 = "[0-9]+";
					boolean is = orderNumber.matches(regex1);
					if (!is || orderNumber.equals("")){
						sbOrderNum.append("第"+(i+1)+"行:[序号]应非空且为数字"+"\n");
						mapColumn.put("msgData", "接口字段信息导入失败\r\n" + sbOrderNum);
						mapColumn.put("dataSrcAbbr", ds);
						return mapColumn;
					}
					ds = dataSrcAbbr;
					String dataType = objColumn.getCellValue(row.getCell(5));
					String format = objColumn.getCellValue(row.getCell(6));
					String nullable = objColumn.getCellValue(row.getCell(7));
					String replacenull = objColumn.getCellValue(row.getCell(8));
					String comma = objColumn.getCellValue(row.getCell(9));
					String fieldDesc = objColumn.getCellValue(row.getCell(10));
					String fieldName = objColumn.getCellValue(row.getCell(4));
					if("".equals(fieldName)){  //字段名为空，去词根表找
						fieldName = objColumn.getCellValue(row.getCell(4),objColumn.getColMap(),fieldDesc).toUpperCase();
						if (fieldName.startsWith("_")){
							fieldName = fieldName.substring(1).toUpperCase();
						}
					}
					TransUtil.sb = new StringBuffer();
					String bucketField = objColumn.getCellValue(row.getCell(11));
					String iskey = objColumn.getCellValue(row.getCell(12));
					String isvalid = objColumn.getCellValue(row.getCell(13));
					String incrementfield = objColumn.getCellValue(row.getCell(14));
//					String startDate = objColumn.getCellValue(row.getCell(15));
//					String endDate = objColumn.getCellValue(row.getCell(16));
					StringBuffer temp = new StringBuffer();
					temp.append(verifyFieldInfo(dataSrcAbbr,infaceNo,infaceName,orderNumber,fieldName, dataType,
							format,nullable,comma,fieldDesc,bucketField));
					stringColumn = temp.toString().trim();
					if (!stringColumn.equals("") && !stringColumn.isEmpty() && stringColumn!=null){
						bufferColumn.append("第"+(i+1)+"行:"+"\n");
						bufferColumn.append(temp.toString());
					}
					DataInterfaceColumnsTmp model = new DataInterfaceColumnsTmp(dataSrcAbbr, infaceNo,
							Integer.parseInt(orderNumber), infaceName, fieldName, dataType, format,
							comma, fieldDesc, bucketField, iskey, isvalid, incrementfield);
					if(nullable!=null && !"".equals(nullable)){
						model.setNullable(Integer.parseInt(nullable));
					}else{
						model.setNullable(0);
					}
					if( replacenull!=null && !"".equals(replacenull)){
						model.setReplacenull(Integer.parseInt(replacenull));
					}else{
						model.setReplacenull(0);
					}
					/*model.setsDate(new java.sql.Date(new Date().getTime()));*/
					model.setsDate(TimeUtil.getTy());
					model.seteDate(getE());
					model.setBatchNo(batchNo);
					listColumn.add(model);
					list1.add(fieldName);
					listDupFieldName.add(infaceName+"」-「"+fieldName);
				}
				//字段名校验
				String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正则
				Pattern p = Pattern.compile(REGEX_CHINESE);
				for (int j=0;j<list1.size();j++) {
					String str = list1.get(j).toString();
					Matcher m = p.matcher(str);
					if (m.find()){
						bufferColumn.append("第"+(j+2)+"行"+"[字段名]"+"「"+list1.get(j).toString()+"」"+"中文字符在词根找不到映射"+"\n");
					}else if("".equals(list1.get(j).toString())){
						bufferColumn.append("第"+(j+2)+"行"+"[字段名]不能为空"+"\n");
					}
				}
				//重复性校验
				StringBuffer sb = new StringBuffer();
				List<String> dupFieldNameList = getDuplicateElements(listDupFieldName);
				if (dupFieldNameList != null && !dupFieldNameList.isEmpty()) {
					sb.append("以下[接口名]对应的[字段名]重复:" + "\n");
					for (int k=0;k<dupFieldNameList.size();k++) {
						sb.append("「" + dupFieldNameList.get(k) + "」" + "\n");
					}
				}

				if (sb.length()>0) {
					bufferColumn.append(sb);
				}
				stringColumn = bufferColumn.toString().trim();
				if (!stringColumn.equals("") && !stringColumn.isEmpty() && stringColumn!=null) {
					mapColumn.put("msgData", "接口字段信息导入失败:\r\n" + bufferColumn+"\n");
					mapColumn.put("dataSrcAbbr", ds);
//					return mapColumn;
				}
					//批量入库
//					batchInsertColumn = colMapper.batchInsert(listColumn);
//					ExcelUtil util = ExcelUtil.getInstance();
//					util.clearColumn(ds);
//					DataInterfaceColumns data = new DataInterfaceColumns();
//					data.seteDate(TimeUtil.getTw());
//					data.setDataSrcAbbr(ds);
//					util.initColumn(colMapper.queryAll(data));
//					mapColumn.put("msgData", "接口字段信息校验成功!记录条数:" + batchInsertColumn + "\n");
//					mapColumn.put("dataSrcAbbr", ds);
//				return mapColumn;

			}else {
				mapColumn.put("msgData", "数据接口字段表不能为空\r\n");
			}

			//sheet4数据加载算法导入
			Sheet sheet4 = wb.getSheetAt(3);
//			Map<String,String> mapProc = new HashMap<String,String>();
			logger.info("sheet4.getLastRowNum():::"+sheet4.getLastRowNum());
			if(sheet4 != null && sheet4.getLastRowNum() != 0) {
//				 ds = "";
				ExcelUtil objProc = ExcelUtil.getInstance();
				//excel数据添加到list中
//				List<DataInterface2procTmp> list = new ArrayList<DataInterface2procTmp>();
				StringBuffer bufferProc = new StringBuffer();
				String string = null;
				for(int i=0;i<=sheet4.getLastRowNum();i++) {
					if(i==0)
						continue;
					Row row = sheet4.getRow(i);
					String dataSrcAbbr = objProc.getCellValue(row.getCell(0));
					String s = null;
					String dataInterfaceNo = objProc.getCellValue(row.getCell(1));
					ds = dataSrcAbbr;
					String dbName = objProc.getCellValue(row.getCell(2));
					String procName = objProc.getCellValue(row.getCell(3));
					StringBuffer temp = new StringBuffer();
					temp.append(verifyLoadProcInfo(dataSrcAbbr,dataInterfaceNo,dbName,procName));
					string = temp.toString().trim();
					if (!string.equals("") && !string.isEmpty() && string!=null){
						bufferProc.append("数据加载算法第"+(i+1)+"行:"+"\n");
						bufferProc.append(temp.toString());
					}
					DataInterface2procTmp model = new DataInterface2procTmp();
					model.setDataSrcAbbr(dataSrcAbbr);
					model.setDataInterfaceNo(dataInterfaceNo);
					model.setProcDatabaseName(dbName);
					model.setProcName(procName);
					model.setsDate(new java.sql.Date(new Date().getTime()));
					model.seteDate(ExcelUtil.getInstance().StringToDate(BoncConstant.CON_E_DATE));
					model.setBatchNo(batchNo);
					listProc.add(model);
				}
				string = bufferProc.toString().trim();
				if (!string.equals("") && !string.isEmpty() && string!=null) {
					mapProc.put("msgData", "数据加载算法校验失败:\n"+bufferProc);
				}
				//批量入库
//				batchInsertProc = pMapper.batchInsert(list);
//				objProc.clearProc(ds);
//				DataInterface2proc data = new DataInterface2proc();
//				data.seteDate(TimeUtil.getTw());
//				data.setDataSrcAbbr(ds);
//				objProc.initProc(pMapper.queryAll(data));
//				mapProc.put("msgData","数据加载算法校验成功!记录条数:"+batchInsert);
//				mapProc.put("dataSrcAbbr", ds);
//				return mapProc;
			}else {
				mapColumn.put("msgData", "数据加载算法表不能为空\r\n");
			}
//			System.out.println("msg:::"+(mapInface.get("msgData")+mapColumn.get("msgData")+mapProc.get("msgData")));

			if (mapInface.get("msgData") == null && mapColumn.get("msgData") == null && mapProc.get("msgData") == null ){
				try {
					//sheet1批量入库临时表
//					batchInsertRecord = recordMapper.batchInsert(listRecord);
//					mapRecord.put("msgData", "接口修订记录校验成功!记录条数:" + batchInsertRecord + "\n");
//					mapRecord.put("dataSrcAbbr", ds);

					//sheet2批量入库临时表
					batchInsertInface = interMapper.batchInsert(listInface);
					ExcelUtil util = ExcelUtil.getInstance();
					util.clearInterface(ds);
					DataInterface data = new DataInterface();
					data.seteDate(TimeUtil.getTw());
					data.setDataSrcAbbr(ds);
					util.initInterface(interMapper.queryAll(data));
					mapInface.put("msgData", "接口信息校验成功!记录条数:" + batchInsertInface + "\n");
					mapInface.put("dataSrcAbbr", ds);

					//sheet3批量入库
					batchInsertColumn = colMapper.batchInsert(listColumn);
					util.clearColumn(ds);
					DataInterfaceColumns dataColumn = new DataInterfaceColumns();
					dataColumn.seteDate(TimeUtil.getTw());
					dataColumn.setDataSrcAbbr(ds);
					util.initColumn(colMapper.queryAll(dataColumn));
					mapColumn.put("msgData", "接口字段信息校验成功!记录条数:" + batchInsertColumn + "\n");
					mapColumn.put("dataSrcAbbr", ds);

					//sheet4批量入库
					batchInsertProc = pMapper.batchInsert(listProc);
					util.clearProc(ds);
					DataInterface2proc dataProc = new DataInterface2proc();
					dataProc.seteDate(TimeUtil.getTw());
					dataProc.setDataSrcAbbr(ds);
					util.initProc(pMapper.queryAll(dataProc));
					mapProc.put("msgData", "数据加载算法校验成功!记录条数:" + batchInsertProc);
					mapProc.put("dataSrcAbbr", ds);
				}catch (Exception e){
					e.printStackTrace();
					msgMap.put("msgData", "校验完成，导入异常");
					msgMap.put("dataSrcAbbr", ds);
					return msgMap;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msgMap.put("msgData", "导入异常");
			msgMap.put("dataSrcAbbr", ds);
			return msgMap;
		}

		msgMap.put("msgData",mapRecord.get("msgdata")+mapInface.get("msgData")+mapColumn.get("msgData")+mapProc.get("msgData"));
		msgMap.put("dataSrcAbbr", ds);
		return msgMap;
	}












	/**
	 * 词根导入
	 * @param file
	 * @return
	 */
	@Override
	@Transactional
	public String importDictionary(MultipartFile file) {
		//String batch_no = UUID.randomUUID().toString();
		ExcelUtil obj = ExcelUtil.getInstance();
		try {
			Workbook wb = getWorkbook(file);
			if(wb==null)
				return "读取文件失败";
			//sheet第一页是字段映射
			Sheet sheet = wb.getSheetAt(0);
			List<attrC2e> aList = new ArrayList<attrC2e>();
			if(null != sheet){
				//校验
//				List<String> tmpList = new ArrayList<>();
				List<String> enameList = new ArrayList<>();
				StringBuffer stringBuffer = new StringBuffer();
				for(int i=0;i<=sheet.getLastRowNum();i++) {
					if(i==0)
						continue;
					Row row = sheet.getRow(i);
					String cname = obj.getCellValue(row.getCell(0));
					if("".equals(cname))
						continue;
					/*return "第"+(i+1)+"行,第1列不能为空";*/
					String ename = obj.getCellValue(row.getCell(1));
					/*if("".equals(ename))
						continue;*/
					/*if(obj.StringFilter(cname))
						continue;*/
//					tmpList.add(cname);
					String regex = "^\\w+$";
					boolean is = ename.matches(regex);
					if (ename.trim() != null || !ename.trim().equals("") || !ename.trim().isEmpty()){
						if (is == false){
							stringBuffer.append("第"+(i+1)+"行["+ename+"][英文缩写]只能由数字、字母和下划线组成");
						}
					}
//					enameList.add(ename);
				}
//				List<String> duplicateElements = getDuplicateElements(tmpList);
				/*if(duplicateElements.size()>0)
					return "中文名"+duplicateElements.toString()+"重复";*/
				if (stringBuffer.length()!=0){
					return "校验失败\n"+stringBuffer;
				}

				String str = "";
				//取值
				for(int i=0;i<=sheet.getLastRowNum();i++) {
					if(i==0)
						continue;
					Row row = sheet.getRow(i);
					String cname = obj.getCellValue(row.getCell(0));
					if("".equals(cname))
						continue;
					/*return "第"+(i+1)+"行,第1列不能为空";*/
					String ename = obj.getCellValue(row.getCell(1));
					/*if("".equals(ename))
						continue;*/
					//return "第"+(i+1)+"行,第2列不能为空";
					/*if(duplicateElements.toString().contains(cname))
						continue;*/
					/*if(obj.StringFilter(cname))
						continue;*/
					int int_wordNum=0;
					String wordNum = obj.getCellValue(row.getCell(3));
					if(!"".equals(wordNum))
						int_wordNum = 0;
					//return "第"+(i+1)+"行,第3列不能为空";
					//String type = "column";
					attrC2e model = new attrC2e();
					if(str.contains(","+cname+",")){
						model.setCname(cname);
						continue;
					}
					model.setCname(cname);
					model.setEname(ename);
					model.setFullEname(obj.getCellValue(row.getCell(2)));
					model.setWordNum(int_wordNum);
					model.setCreateDate(obj.getDate(new Date()));
					//model.setVersion("1");
					aList.add(model);
					str = str +","+cname;
				}
			}

			//sheet第二页是表映射
			Sheet sheet2 = wb.getSheetAt(1);
			List<entityC2e> eList = new ArrayList<entityC2e>();
			if(null != sheet2){
				String str = "";
				//校验
//				List<String> tmpList = new ArrayList<>();
				StringBuffer stringBuffer = new StringBuffer();
				for(int i=0;i<=sheet2.getLastRowNum();i++) {
					if(i==0)
						continue;
					Row row = sheet2.getRow(i);
					String cname = obj.getCellValue(row.getCell(0));
					if("".equals(cname))
						continue;
					/*return "第"+(i+1)+"行,第1列不能为空";*/
					String ename = obj.getCellValue(row.getCell(1));
					String regex = "^[a-zA-Z_]+$";
					boolean is = ename.matches(regex);
					if (ename.trim() != null || !ename.trim().equals("") || !ename.trim().isEmpty()){
						if (is == false){
							stringBuffer.append("第"+(i+1)+"行["+ename+"][表名]只能由字母和下划线组成");
						}
					}
				}
				if (stringBuffer.length()!=0){
					return "校验失败\n"+stringBuffer;
				}

//				List<String> duplicateElements = getDuplicateElements(tmpList);
				/*if(duplicateElements.size()>0)
					return "中文名"+duplicateElements.toString()+"重复";*/
				//取值
				for(int i=0;i<=sheet2.getLastRowNum();i++) {
					if(i==0)
						continue;
					Row row = sheet2.getRow(i);
					String cname = obj.getCellValue(row.getCell(0));
					if("".equals(cname))
						continue;
					String ename = obj.getCellValue(row.getCell(1));
					String lenb = obj.getCellValue(row.getCell(2));
					int int_lenb =0;
					int int_len =0;
					if(!"".equals(lenb))
						int_lenb = Integer.parseInt(lenb);
					String len = obj.getCellValue(row.getCell(3));
					if(!"".equals(len))
						int_len = Integer.parseInt(len);
					entityC2e model = new entityC2e();
					if(str.contains(","+cname+",")){
						model.setCname(cname);
						continue;
					}
					model.setCname(cname);
					model.setEname(ename);
					model.settLenb(int_lenb);
					model.settLen(int_len);
					model.setCreateDate(obj.getDate(new Date()));
					eList.add(model);
					str = str +","+cname;
				}
			}

			//创建使用单个线程的线程池
			ExecutorService es = Executors.newFixedThreadPool(10);
			//词根字段
			try {
				//使用lambda实现runnable接口
				Runnable task = ()->{
					aMapper.deleteByPrimaryKey(new attrC2e());
					logger.info(Thread.currentThread().getName()+"创建了一个新的线程执行,词根字段");
			    	/*for(attrC2e record:aList) {
						aMapper.insertSelective(record);
					}*/
					aMapper.batchInsert(aList);
					//调用submit传递线程任务，开启线程
				};
				es.submit(task);
				logger.info("词根字段线程开始");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				es.shutdown();
				throw new Exception("词根字段导入失败");
			}
			//词根表
			try {
				//使用lambda实现runnable接口
				Runnable task = ()->{
					eMapper.deleteByPrimaryKey(new entityC2e());
					logger.info(Thread.currentThread().getName()+"创建了一个新的线程执行,词根表");
			    	/*for(entityC2e record:eList) {
						eMapper.insertSelective(record);
					}*/
					eMapper.batchInsert(eList);
					//调用submit传递线程任务，开启线程
				};
				es.submit(task);
				logger.info("词根表线程开始");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				es.shutdown();
				throw new Exception("词根表导入失败");
			}

			//批量入库
//			directory.batchInsertDirectory(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "导入词根失败";
		}
		return "导入词根成功!";
	}

	private Workbook getWorkbook(MultipartFile file) throws Exception{
		Workbook workbook = null;
		String fileName = file.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
		if (("xls").equals(suffix)) {
			workbook = new HSSFWorkbook(file.getInputStream());
		} else if (("xlsx").equals(suffix)) {
			workbook = new XSSFWorkbook(file.getInputStream());
		}
		return workbook;
	}

	public static List<String> getKeyList(Map<String,String> map,String value){
		List keyList = new ArrayList();
		for(String getKey : map.keySet()){
			if (map.get(getKey).equals(value)){
				keyList.add(getKey);
			}
		}
		return keyList;
	}

}
