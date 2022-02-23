package com.ljz.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransUtil {

    public static StringBuffer sb = new StringBuffer();

    /**
     * 翻译
     * @param constantMap 词根表/字段 map
     * @param key 中文名
     * @return
     */
    public static String translateField(Map<String,String> constantMap, String key) {

//        char[] charArray = key.toCharArray();
//        char c;
//        String oldStr="";
        String finalStr = "";
//        finalStr = transField(oldStr, constantMap);
        finalStr = transField(key, constantMap);
        return finalStr;
    }
    public static String transField(String str,Map<String,String>  constantMap) {
        for(int i=str.length();i>0;i--) {
            String leftStr = str.substring(0,i);
            boolean isContainKey = constantMap.containsKey(leftStr);
            if(isContainKey==true) {
                if (constantMap.get(leftStr).trim()==null){
                    sb.append("");
                }
                sb.append(constantMap.get(leftStr).trim());
                String rightStr = str.substring(i,str.length());
                if (rightStr != null && !rightStr.equals("")  && !rightStr.isEmpty()){
                    transField(rightStr, constantMap);
                    return sb.toString();
                }else {
                    return sb.toString();
                }
            }else if (leftStr.trim().length()==1) {
            	
//            	String REGEX_CHINESE = "[\u4e00-\u9fa5]";
//                Pattern p2 = Pattern.compile(REGEX_CHINESE);
//                Matcher m2 = p2.matcher(leftStr);
//                if(m2.find()) {
//                	
//                }else {
                	
                	 String REGEX_toUpperCaseENG = "^[a-z]+$";
                     Pattern p = Pattern.compile(REGEX_toUpperCaseENG);
                     Matcher m = p.matcher(leftStr);
                     if(m.find()) {
                         sb.append(leftStr.toUpperCase());
                     }else {
                         sb.append(leftStr);
                     }
//                }
                
                String rightStr = str.substring(i, str.length());
                if (rightStr.trim() != null && !rightStr.trim().equals("") && !rightStr.trim().isEmpty()) {
                    transField(rightStr, constantMap);
                }else{
                    return sb.toString();
                }
            }else {
                continue;
            }
        }
        return sb.toString();
    }

//    public static String translateTable(Map<String,String> constantMap, String key) {
//        char[] charArray = key.toCharArray();
//        String oldStr="";
//        for(char c:charArray) {
//            String REGEX_CHINESE = "[\u4e00-\u9fa5]";
//            Pattern p = Pattern.compile(REGEX_CHINESE);
//            Matcher m = p.matcher(Character.toString(c));
//            if(!m.find()) {
//                continue;
//            }else{
//                oldStr += c;
//            }
//        }
//        String finalStr = "";
//        finalStr += transTable(oldStr, constantMap);
//        return finalStr;
//    }
    public static String transTable(String str,Map<String,String>  constantMap) {
        boolean isContainKey = constantMap.containsKey(str);
        if(isContainKey==true) {
            sb.append(constantMap.get(str).trim());
        }
        else{
            sb.append("");
        }
        return sb.toString();
    }
}
