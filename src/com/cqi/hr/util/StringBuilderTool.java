package com.cqi.hr.util;

/**
 * StringBuilder replace工具:彷String replace用法
 * #模組類別:請勿依某些專案需求直接修改本物件。僅限修改BUG/擴充功用方法時修改<br/>
 * #version:1.0
 * */
public class StringBuilderTool {
	public static StringBuilder replace(StringBuilder sb,String regex,String replace){
		int index = sb.indexOf(regex);
		if(index != -1){
			return sb.replace(index,index + regex.length(), replace);
		}else{
			return sb;
		}
		
	}
	public static StringBuilder replaceAll(StringBuilder sb,String regex,String replace){
		while(sb.indexOf(regex) != -1){
			sb = replace(sb, regex, replace);
		}
		return sb;
	}
}
