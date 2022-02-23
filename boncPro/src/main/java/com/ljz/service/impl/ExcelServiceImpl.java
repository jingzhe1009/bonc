package com.ljz.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;






import com.ljz.mapper.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ljz.constant.BoncConstant;
import com.ljz.mapper.DataInterface2procMapper;
import com.ljz.mapper.DataInterfaceColumnsMapper;
import com.ljz.mapper.DataInterfaceMapper;
import com.ljz.mapper.attrC2eMapper;
import com.ljz.mapper.entityC2eMapper;
import com.ljz.model.DataInterface;
import com.ljz.model.DataInterface2proc;
import com.ljz.model.DataInterface2procTmp;
import com.ljz.model.DataInterfaceColumns;
import com.ljz.model.DataInterfaceColumnsTmp;
import com.ljz.model.DataInterfaceTmp;
import com.ljz.model.attrC2e;
import com.ljz.model.entityC2e;
import com.ljz.service.IExcelService;
import com.ljz.util.ExcelUtil;
import com.ljz.util.TimeUtil;
import com.ljz.util.TransUtil;

import javax.annotation.Resource;

@Service
public class ExcelServiceImpl implements IExcelService{

	private static final Logger logger = LoggerFactory.getLogger(ExcelServiceImpl.class);

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
		Map<String,String> dupTabMap = new HashMap<String,String>();
		List<entityC2e> eList = eMapper.queryAll(new entityC2e());
		List<attrC2e> aList = aMapper.queryAll(new attrC2e());
		ExcelUtil obj = ExcelUtil.getInstance();
		obj.initDTable(eList);
		obj.initDCol(aList);
		//excel数据添加到list中
		List<DataInterfaceTmp> list = new ArrayList<DataInterfaceTmp>();
		List list1 = new ArrayList<>();
		List list2 = new ArrayList<>();
		List listInfaceName = new ArrayList<>();
		StringBuffer stringBuffer = new StringBuffer();
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
				String startDate = obj.getCellValue(row.getCell(14));
				String endDate = obj.getCellValue(row.getCell(15));
				String intrnlTableName = obj.getCellValue(row.getCell(11));
				if("".equals(intrnlTableName)){  //内表表名为空，去词根表找
					intrnlTableName = TransUtil.transTable(dataInfaceCName,obj.getTableMap());
					if("".equals(intrnlTableName)){  //词根表查找为空，去词根字段查找
						intrnlTableName = obj.getCellValue(row.getCell(11),obj.getColMap(),dataInfaceCName);
                        if (intrnlTableName.equals("")){
                            intrnlTableName = "";
                        }else if (!intrnlTableName.startsWith("_")){
                            intrnlTableName = dataSrcAbbr + "_" + intrnlTableName;
                        }else {
                            intrnlTableName = dataSrcAbbr + intrnlTableName;
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
					if (stringBuffer.length() >= 300) {
						stringBuffer.append("......"+"\n"+"错误信息过多，请输入正确数据"+"\n");
						break;
					}
					continue;
				}
				DataInterfaceTmp model  = new DataInterfaceTmp(batchNo, dataInfaceName, dataInfaceCName, dataLoadFreq, dataLoadMthd,
						filedDelim, lineDelim, extrnlDatabaseName, intrnlDatabaseName, extrnlTableName, intrnlTableName,
						tableType, Integer.parseInt(bucketNumber),
						dataSrcAbbr, dataInfaceNo);
				/*model.setsDate(new java.sql.Date(new Date().getTime()));*/
				model.setsDate(TimeUtil.getTy());
				model.seteDate(TimeUtil.getE());
				//logger.info(model.toString());
				dupTabMap.put(dataInfaceName,intrnlTableName);
				list.add(model);
				list1.add(model.getIntrnlTableName());
				list2.add(dataSrcAbbr);
				listInfaceName.add(model.getDataInterfaceName());
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
			for (int j=0;j<list1.size();j++) {
				Matcher m = p.matcher(list1.get(j).toString());
				if (m.find()){
					stringBuffer.append("第"+(j+2)+"行"+"[内表表名]"+"「"+list1.get(j).toString()+"」"+"中文字符在词根找不到映射"+"\n");
				}
				if (!list1.get(j).toString().startsWith(list2.get(j).toString()) && !list1.get(j).toString().equals("")){
					stringBuffer.append("第"+(j+2)+"行"+"[内表表名]"+"「"+list1.get(j).toString()+"」"+"需前缀数据源"+"\n");
				}else if ("".equals(list1.get(j).toString())){
					stringBuffer.append("第"+(j+2)+"行"+"[内表表名]"+list1.get(j).toString()+"不能为空"+"\n");
				}
			}
			//重复性校验
			StringBuffer sb = new StringBuffer();
			List<String> dupInTabList = getDuplicateElements(list1);
			if (dupInTabList != null && !dupInTabList.isEmpty()) {
				sb.append("以下[接口名]对应的[内表表名]重复:\n");
				for (int k=0;k<dupInTabList.size();k++) {
					List dupInfaceName = getKeyList(dupTabMap, dupInTabList.get(k));
					sb.append("「"+dupInfaceName+"」-「" + dupInTabList.get(k) + "」" + "\n");
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
		}else if("".equals(dataSrcAbbr))
		stringBuffer.append("[数据源]不能为空"+"\n");
		
		//接口编号校验
		if("".equals(dataInterfaceNo)){
			stringBuffer.append("[接口编号]不能为空"+"\n");
		}
		if(dataInterfaceNo.length()!=5){
			stringBuffer.append("[接口编号]长度为5"+"\n");
		}
		
		//接口名校验
		//boolean iss = dataInterfaceName.matches(dataSrcAbbr+"_"+dataInterfaceNo+"[_(A-Z)|.\\\\n]*");
		boolean iss = dataInterfaceName.startsWith(dataSrcAbbr);
		if(!iss){
		stringBuffer.append("[接口名]应前缀数据源"+"\n");
		}else if("".equals(dataInterfaceName)) {
		stringBuffer.append("[接口名]不能为空" + "\n");
		}
		
		//接口中文名校验
		if("".equals(dataInfaceChineseName)){
		stringBuffer.append("[接口中文名]称不能为空"+"\n");
		}
		
		//数据加载频度
		if("".equals(dataLoadFreq)) {
		stringBuffer.append("[数据加载频度]不能为空" + "\n");
		}
		
		//数据加载方式校验
		String regex = "[Z|Q]";
		boolean is = dataLoadMthd.matches(regex);
		if (is==false){
		stringBuffer.append("[数据加载方式]应为Z/Q"+"\n");
		}else if("".equals(dataLoadMthd)){
		stringBuffer.append("[数据加载方式]不能为空"+"\n");
		}
		
		//字段分隔符
		if ("".equals(filedDelim)){
		stringBuffer.append("字段分隔符不能为空"+"\n");
		}
		
		//行分隔符
		if ("".equals(lineDelim)){
		stringBuffer.append("行分隔符不能为空"+"\n");
		}
		
		//外表数据库
		if("".equals(externlDatabase))
		stringBuffer.append("[外表数据库]不能为空"+"\n");
		
		//外表表名校验
		if (!extrnlTableName.equals(dataInterfaceName)){
			stringBuffer.append("[外表表名]与[接口名]应一致"+"\n");
		}else if ("".equals(extrnlTableName)){
			stringBuffer.append("[外表表名]不能为空"+"\n");
		}
		
		//内表数据库校验
		if ("".equals(intrnlDataBase)){
		stringBuffer.append("[内表数据库]不能为空"+"\n");
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
		String regex2 = "[0-9]+";
		boolean is3 = bucketNumber.matches(regex2);
		if(is3==false || bucketNumber.equals("") ){
		stringBuffer.append("[分桶数]应非空且为全数字"+"\n");
		}
		
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
				ds = dataSrcAbbr;
				String dataType = obj.getCellValue(row.getCell(5));
				String format = obj.getCellValue(row.getCell(6));
				String nullable = obj.getCellValue(row.getCell(7));
				String replacenull = obj.getCellValue(row.getCell(8));
				String comma = obj.getCellValue(row.getCell(9));
				String fieldDesc = obj.getCellValue(row.getCell(10));
//				if("".equals(fieldName)){
//					fieldName = obj.getCellValue(row.getCell(4),obj.getColMap(),fieldDesc);
//					if("".equals(fieldName)) {
//						return "[" + fieldDesc + "]在词根找不到对应的接口英文名!请检查第" + (i + 1) + "行";
//					}else {
//						fieldName = dataSrcAbbr +fieldName;
//					}
//				}
				String fieldName = obj.getCellValue(row.getCell(4));
				if("".equals(fieldName)){  //内表表名为空，去词根表找
						fieldName = obj.getCellValue(row.getCell(4),obj.getColMap(),fieldDesc);
						if (fieldName.startsWith("_")){
							fieldName = fieldName.substring(1);
						}
				}
				TransUtil.sb=new StringBuffer();
				TransUtil.sb = new StringBuffer();
				String bucketField = obj.getCellValue(row.getCell(11));
				String startDate = obj.getCellValue(row.getCell(12));
				String endDate = obj.getCellValue(row.getCell(13));
				StringBuffer temp = new StringBuffer();
				temp.append(verifyFieldInfo(dataSrcAbbr,infaceNo,infaceName,orderNumber,fieldName, dataType,
						format,comma,fieldDesc,bucketField));
				string = temp.toString().trim();
				if (!string.equals("") && !string.isEmpty() && string!=null){
					stringBuffer.append("第"+(i+1)+"行:"+"\n");
					stringBuffer.append(temp.toString());
					if (stringBuffer.length() >= 300) {
						stringBuffer.append("......"+"\n"+"错误信息过多，请输入正确数据"+"\n");
//						break;
						msgMap.put("msgData", "导入失败\r\n" + stringBuffer);
						msgMap.put("dataSrcAbbr", ds);
						return msgMap;
					}
//					continue;
				}
				DataInterfaceColumnsTmp model = new DataInterfaceColumnsTmp(dataSrcAbbr, infaceNo,
						Integer.parseInt(orderNumber), infaceName, fieldName, dataType, format,
						 comma, fieldDesc, bucketField);
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
//										 String nullable,String replacenull,
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
		String regex1 = "[0-9]+";
		boolean is = orderNumber.matches(regex1);
		if (!is || orderNumber.equals("")){
			stringBuffer.append("[序号]应非空且为数字"+"\n");
		}

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
		if("".equals(dataType))
			stringBuffer.append("[数据类型]不能为空"+"\n");

		//是否非空校验
//		if(!nullable.isEmpty() && nullable!=null && !nullable.equals("0") && !nullable.equals("1") ){
//			stringBuffer.append("[是否非空]应为0/1");
//		}

		//字段说明校验
		if("".equals(columnComment))
			stringBuffer.append("[字段说明]不能为空"+"\n");

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


			for(int i=0;i<=sheet.getLastRowNum();i++) {
				if(i==0)
					continue;

				Row row = sheet.getRow(i);
				String dataSrcAbbr = obj.getCellValue(row.getCell(0));
				if("".equals(dataSrcAbbr)){
					msgMap.put("msgData","第"+(i+1)+"行,第1列数据源不能为空");
					msgMap.put("dataSrcAbbr", ds);
					return msgMap;
				}
				String dataInterfaceNo = obj.getCellValue(row.getCell(1));
				if("".equals(dataInterfaceNo)){
					msgMap.put("msgData","第"+(i+1)+"行,第2列数据源不能为空");
					msgMap.put("dataSrcAbbr", ds);
					return msgMap;
				}

				ds = dataSrcAbbr;
				String dbName = obj.getCellValue(row.getCell(2));
				String procName = obj.getCellValue(row.getCell(3));
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
			/*for(DataInterface2proc record :list) {
				pMapper.insertSelective(record);
			}*/

			/*DataInterface2proc key = new DataInterface2proc();
			key.setDataSrcAbbr(ds);
			pMapper.deleteByPrimaryKey(key);*/
			int batchInsert = pMapper.batchInsert(list);

			obj.clearProc(ds);
			DataInterface2proc data = new DataInterface2proc();
			data.seteDate(TimeUtil.getTw());
			data.setDataSrcAbbr(ds);
			obj.initProc(pMapper.queryAll(data));
			msgMap.put("msgData","校验成功!记录条数:"+batchInsert);
			msgMap.put("dataSrcAbbr", ds);
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
	 * 筛选重复值
	 * @param list
	 * @return
	 */
	public List<String> getDuplicateElements(List<String> list) {
		// list 对应的 Stream
		// 获得元素出现频率的 Map，键为元素，值为元素出现的次数
		// 所有 entry 对应的 Stream
		// 过滤出元素出现次数大于 1 (重复元素)的 entry
		// 获得 entry 的键(重复元素)对应的 Stream
		// 转化为 List
		return list.stream().collect(Collectors.toMap(e -> e, e -> 1, Integer::sum)).entrySet().stream().filter(e -> e.getValue() > 1).map(Map.Entry::getKey).collect(Collectors.toList());
	}
	

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
				List<String> tmpList = new ArrayList<>();
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
					tmpList.add(cname);
				}
				List<String> duplicateElements = getDuplicateElements(tmpList);
				/*if(duplicateElements.size()>0)
					return "中文名"+duplicateElements.toString()+"重复";*/
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
				List<String> tmpList = new ArrayList<>();
				for(int i=0;i<=sheet2.getLastRowNum();i++) {
					if(i==0)
						continue;
					Row row = sheet2.getRow(i);
					String cname = obj.getCellValue(row.getCell(0));
					if("".equals(cname))
						continue;
					/*return "第"+(i+1)+"行,第1列不能为空";*/
					String ename = obj.getCellValue(row.getCell(1));
					/*if("".equals(ename))
						continue;*/
					/*if(obj.StringFilter(cname))
						continue;*/
					tmpList.add(cname);
				}
				List<String> duplicateElements = getDuplicateElements(tmpList);
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
					//return "第"+(i+1)+"行,第1列不能为空";
					String ename = obj.getCellValue(row.getCell(1));
					/*if("".equals(ename))
						continue;*/
					//return "第"+(i+1)+"行,第2列不能为空";
					/*if(duplicateElements.toString().contains(cname))
						continue;*/
					
					/*if(obj.StringFilter(cname))
						continue;*/
					String lenb = obj.getCellValue(row.getCell(2));
					int int_lenb =0;
					int int_len =0;
					if(!"".equals(lenb))
						int_lenb = Integer.parseInt(lenb);
					//return "第"+(i+1)+"行,第3列不能为空";
					String len = obj.getCellValue(row.getCell(3));
					if(!"".equals(len))
						int_len = Integer.parseInt(len);
					//return "第"+(i+1)+"行,第4列不能为空";
					//String type = "table";
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
					//model.setVersion("1");
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
