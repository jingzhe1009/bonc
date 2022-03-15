package com.ljz.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ljz.constant.BoncConstant;
import com.ljz.mapper.DirectoryMapper;
import com.ljz.model.DataInterface;
import com.ljz.model.DataInterface2proc;
import com.ljz.model.DataInterfaceColumns;
import com.ljz.model.Directory;
import com.ljz.model.attrC2e;
import com.ljz.model.entityC2e;

@Component
public class ExcelUtil {

	@Autowired
	DirectoryMapper mapper;

	private static volatile ExcelUtil instance;

	public static ExcelUtil getInstance() {
		if (null == instance) {
            synchronized (ExcelUtil.class) {
                if (null == instance) {
                	instance = new ExcelUtil();
                }
            }
        }
		return instance;
	}

	private Map<String,String> constantMap = new HashMap<String,String> ();

	private Map<String,String> tableMap = new HashMap<String,String> ();

	private Map<String,String> colMap = new HashMap<String,String> ();

	private Map<String,Map<String,String>> columnMap = new HashMap<String,Map<String,String>> ();

	private Map<String,Map<String,String>> interfaceMap = new HashMap<String,Map<String,String>> ();

	private Map<String,Map<String,String>> procMap = new HashMap<String,Map<String,String>> ();
	
	private Map<String,Object> entityMap = new HashMap<String,Object> ();

	public Map<String, String> getTableMap() {
		return tableMap;
	}

	public void setTableMap(Map<String, String> tableMap) {
		this.tableMap = tableMap;
	}

	public Map<String, String> getColMap() {
		return colMap;
	}

	public void setColMap(Map<String, String> colMap) {
		this.colMap = colMap;
	}

	public Map<String, String> getConstantMap() {
		return constantMap;
	}

	public void setConstantMap(Map<String, String> constantMap) {
		this.constantMap = constantMap;
	}

	public Map<String, String> getColumnMap(String dataSrcAbbr) {
		return columnMap.get(dataSrcAbbr);
	}

	public void setColumnMap(Map<String, Map<String, String>> columnMap) {
		this.columnMap = columnMap;
	}

	public Map<String,String> getInterfaceMap(String dataSrcAbbr) {
		return interfaceMap.get(dataSrcAbbr);
	}

	public void setInterfaceMap(Map<String, Map<String, String>> interfaceMap) {
		this.interfaceMap = interfaceMap;
	}


	public Map<String, String> getProcMap(String dataSrcAbbr) {
		return procMap.get(dataSrcAbbr);
	}

	public void setProcMap(Map<String, Map<String, String>> procMap) {
		this.procMap = procMap;
	}
	
	public Map<String, Object> getEntityMap() {
		return entityMap;
	}

	public void setEntityMap(Map<String, Object> entityMap) {
		this.entityMap = entityMap;
	}

	private ExcelUtil() {

	};

	public void init(List<Directory> directoryList) {
		//词根存入缓存map中
		for(Directory obj :directoryList) {
			constantMap.put(obj.getCname(), obj.getEname());
		}
		setConstantMap(constantMap);
	}

	public void initDTable(List<entityC2e> directoryList) {
		//词根表存入缓存map中
		for(entityC2e obj :directoryList) {
			tableMap.put(obj.getCname(), obj.getEname());
		}
		setTableMap(tableMap);
	}

	public void initDCol(List<attrC2e> directoryList) {
		//词根字段存入缓存map中
		for(attrC2e obj :directoryList) {
			colMap.put(obj.getCname(), obj.getEname());
		}
		setColMap(colMap);
	}

	public void initColumn(List<DataInterfaceColumns> list) {
		Map<String,String> map = new HashMap<String,String>();
		String dataSrcAbbr = "";
		for(DataInterfaceColumns obj :list) {
			dataSrcAbbr = obj.getDataSrcAbbr();
			map.put(obj.getDataInterfaceName()+obj.getColumnNo(), obj.toStr());
		}
		columnMap.put(dataSrcAbbr, map);
		//setColumnMap(columnMap);
	}

	public void initInterface(List<DataInterface> list) {
		Map<String,String> map = new HashMap<String,String>();
		String dataSrcAbbr = "";
		for(DataInterface obj :list) {
			dataSrcAbbr = obj.getDataSrcAbbr();
			map.put(obj.getDataInterfaceName(), obj.toStr());
		}
		interfaceMap.put(dataSrcAbbr, map);
		//setInterfaceMap(interfaceMap);
	}

	public void initProc(List<DataInterface2proc> list) {
		Map<String,String> map = new HashMap<String,String>();
		String dataSrcAbbr = "";
		for(DataInterface2proc obj :list) {
			dataSrcAbbr = obj.getDataSrcAbbr();
			map.put(obj.getDataSrcAbbr()+obj.getDataInterfaceNo(), obj.toStr());
		}
		procMap.put(dataSrcAbbr, map);
		//setProcMap(procMap);
	}

	
	public void put(String dataSrc,Object object) {
		entityMap.put(dataSrc, object);
	}
	
	public void clearEntity(String dataSrcAbbr){
		entityMap.remove(dataSrcAbbr);
	}
	
	public void clearInterface(String dataSrcAbbr){
		interfaceMap.remove(dataSrcAbbr);
	}

	public void clearProc(String dataSrcAbbr){
		procMap.remove(dataSrcAbbr);
	}

	public void clearColumn(String dataSrcAbbr){
		columnMap.remove(dataSrcAbbr);
	}


	//检查cell非空
	public String getCellValue(Cell cell) throws Exception{

		if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
			String column = "";
			if(cell.getCellType()==Cell.CELL_TYPE_STRING) {
				column = cell.getStringCellValue();
//				System.out.println("string:"+column);
			}else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC) {
				column = String.valueOf(cell.getNumericCellValue());
				column = column.substring(0,column.indexOf("."));
//				System.out.println("int:"+column);
			}else {
				//column = String.valueOf(cell.getNumericCellValue());
			}
			return column.trim();
		}else {
			return "";
		}
	}

	/*public String getValue(Row row,DataInterface di) throws Exception{
		Field[] f1=DataInterface model.class.getFields();
		di.get
		row.getCell();
		if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
			String column = "";
			if(cell.getCellType()==Cell.CELL_TYPE_STRING) {
				column = cell.getStringCellValue();
			}else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC) {
				column = String.valueOf(cell.getNumericCellValue());
			}
			return column;
		}else {
			return "";
		}
	}*/
	// 过滤特殊字符
	public static boolean StringFilter(String str) throws PatternSyntaxException {
		// 只允许字母和数字 // String regEx ="[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}_【】‘；：”“’。，、？％＞≥]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		boolean b= m.matches();
		return b;
	}

	//检查cell非空
	public String getCellValue(Cell cell,Map<String,String> constantMap,String key) throws Exception{

		if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
			String column = "";
			if(cell.getCellType()==Cell.CELL_TYPE_STRING) {
				column = cell.getStringCellValue();
			}else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC) {
				column = String.valueOf(cell.getNumericCellValue());
			}
			if("".equals(column)) {
				return TransUtil.translateField(constantMap,key);
			}else {
				//关键字判断
				if(BoncConstant.COLUMN.contains(column)) {
					column = "BONC_"+column;
				}
			}
			return column;
		}else {
			return TransUtil.translateField(constantMap,key);
			//词根判断
			/*boolean flag = constantMap.containsKey(key);
			if(flag) {
				return constantMap.get(key);
			}
			return "";*/
		}
	}
//	/**
//	 * 翻译
//	 * @param constantMap 词根表/字段 map
//	 * @param key 中文名
//	 * @return
//	 */
//	public static String translate(Map<String,String> constantMap,String key) {
//
//		char[] charArray = key.toCharArray();
//		String oldStr="";
//		for(char c:charArray) {
//			String REGEX_CHINESE = "[\u4e00-\u9fa5]";
//			Pattern p = Pattern.compile(REGEX_CHINESE);
//			Matcher m = p.matcher(Character.toString(c));
//			if(!m.find()) {
//				continue;
//			}else{
//				oldStr += c;
//			}
//		}
//		String finalStr = "";
//		finalStr += trans(oldStr, constantMap);
//		return finalStr;
//	}
//	public static StringBuffer sb = new StringBuffer();
//	public static String trans(String str,Map<String,String>  constantMap) {
//		String rightStr = "";
//		String finalStr=str;
//		for(int i=str.length();i>0;i--) {
//			str = finalStr.substring(0,i);
//			boolean isContainKey = constantMap.containsKey(str);
//			if(isContainKey==true) {
//				rightStr = finalStr.substring(i,finalStr.length());
//				sb.append(constantMap.get(str).trim());
//			}
//		}
//		//递归结束条件
//		if("".equals(rightStr.trim())) {
////			System.out.println("sb="+sb.toString());
//			return sb.toString();
//		}
//		return trans(rightStr, constantMap);
//	}

//	public static void main(String[] args) {
//		Map<String,String>  constantMap = new HashMap<String, String>();
//		constantMap.put("客户", "	_CUSTOMER");
//		constantMap.put("关系", "	_RLTN");
//		translate(constantMap,"客户关系");
//	}

	public String getTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}

	public String getDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	public java.sql.Date StringToDate(String str) {
		str = str.replaceAll("//", "-");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if(str.indexOf("-")==-1) {
				return new java.sql.Date(Long.parseLong(str));
			}else {
				Date date = simpleDateFormat.parse(str);
				return new java.sql.Date(date.getTime());
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new java.sql.Date(new Date().getTime());
	}
	/**
	 * 删除重复值
	 * @param list
	 * @return
	 */
	public static List removeDuplicate(List list){
		//去重
		/*List list=(List) a.stream().distinct().collect(Collectors.toList());
        System.out.println(list);*/
        List listTemp = new ArrayList();
        for(int i=0;i<list.size();i++){
            if(!listTemp.contains(list.get(i))){
                listTemp.add(list.get(i));
            }
        }
        return listTemp;
    }


}
