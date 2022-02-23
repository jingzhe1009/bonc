package com.ljz.util;


public class SqlUtil {

	public static String dealEName(String ename) {
		return ename.toUpperCase();
	}
	
	public static String dealRequired(String required) {
		if("".equals(required)||"Y".equals(required)) {
			required = "";
		}
		if("N".equals(required))
			required = " not null ";
		return " "+required.toUpperCase();
	}
	
	public static String dealType(String type) {
		
		return " "+type.toUpperCase();
	}
	
	public static String dealTdType(String type) {
		if(type.toUpperCase().contains("VARCHAR")||type.toUpperCase().contains("CHAR")) {
			type = type + " CHARACTER SET LAIIN CASESPECIFIC ";
		}
		return type.toUpperCase();
	}
	
	public static String dealTdhType(String type) {
		if(type.toUpperCase().contains("VARCHAR")||type.toUpperCase().contains("CHAR")) {
			type = " string DEFAULT ";
		}
		return type.toUpperCase();
	}
	
	
}
