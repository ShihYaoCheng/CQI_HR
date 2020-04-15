package com.cqi.hr.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 一個有用的字串工具。
 * @author Tim Wang
 */
public class StringUtils {
	private StringUtils() {};
	private static final boolean _isGetterOrSetter(String methodName) {return methodName!=null && methodName.length()>3 && (methodName.charAt(0)=='g' || methodName.charAt(0)=='s') && methodName.charAt(1)=='e' && methodName.charAt(2)=='t';}
	private static final String _toGSetter(char firstChar, String property) {
		if(property!=null) {
			char[] str = new char[property.length()+3];
			str[0] = firstChar;
			str[1] = 'e';
			str[2] = 't';
			property.getChars(0, property.length(), str, 3);
			if(Character.isLowerCase(str[3])) {
				if(Character.isLowerCase(str[4])) {
					str[3] = Character.toUpperCase(str[3]);
				}
			}
			return new String(str);
		}
		return property;		
	}
	
	/**
	 * 判斷字串是否有值。
	 * @param s
	 * @return
	 * 如果字串不為null且非空字串則回傳true；否則false。
	 */
	public static final boolean hasText(String s) {return s!=null && s.length()>0;}
	
	/**
	 * 判斷字串是否有值。(忽略前後空白)
	 * @param s
	 * @return
	 * 如果字串不為null且非空字串則回傳true；否則false。
	 */
	public static final boolean hasTextIgnoreWhitSpaces(String s) {return s!=null && s.trim().length()>0;}
	
	/**
	 * 判斷字串陣列是否有直。
	 * @param s
	 * @return
	 * 如果字串陣列不為null且長度不為0則回傳true；否則false。
	 */
	public static final boolean hasText(String[] s) {return s!=null && s.length>0;}
	
	/**
	 * 判斷字串是否空。
	 * @param s
	 * @return
	 * 如果為空字串則回傳true；否則false。
	 */
	public static final boolean isEmpty(String s) {return !hasText(s);}
	
	/**
	 * 判斷字串是否空。(忽略前後空白)
	 * @param s
	 * @return
	 * 如果為空字串則回傳true；否則false。
	 */
	public static final boolean isEmptyIgnoreWhitSpaces(String s) {return !hasTextIgnoreWhitSpaces(s);}
	
	/**
	 * 將methodName轉換為property。
	 * @param methodName
	 * 符合getter、setter命名的方法名稱。
	 * @return
	 * 若方法名稱不符合getter、setter命名，則回傳原methodName。
	 */
	public static final String toProperty(String methodName) {
		if(_isGetterOrSetter(methodName)) {
			char[] str = new char[methodName.length()-3];
			methodName.getChars(3, methodName.length(), str, 0);
			str[0] = Character.toLowerCase(str[0]);
			return new String(str);
		}
		return methodName;
	}
	
	/**
	 * 將property轉換為getProperty。
	 * @param property
	 * @return
	 * 此property之getter方法。
	 */
	public static final String toGetter(String property) {return _toGSetter('g', property);}
	
	/**
	 * 將property轉換為setProperty。
	 * @param property
	 * @return
	 * 此property之setter方法。
	 */
	public static final String toSetter(String property) {return _toGSetter('s', property);}
	
	/**
	 * 將一字串s的第index個字變為小寫。
	 * @param s
	 * @param index
	 * @return
	 * 一個將指定index變為小寫的新字串。
	 */
	public static final String toLowerCase(String s, int index) {
		if(s==null || s.length()-1<index) return s;
		char[] str = s.toCharArray();
		str[index] = Character.toLowerCase(str[index]);
		return new String(str);
	}
	
	/**
	 * 將一字串s的第index個字變為大寫。
	 * @param s
	 * @param index
	 * @return
	 * 一個將指定index變為大寫的新字串。
	 */
	public static final String toUpperCase(String s, int index) {
		if(s==null || s.length()-1<index) return s;
		char[] str = s.toCharArray();
		str[index] = Character.toUpperCase(str[index]);
		return new String(str);
	}
	
	/**
	 * 將一字串中所有正、負(如果isIgnoreSign為false)整數列出。
	 * @param s
	 * @param ignoreSign
	 * 是否忽略正負號
	 * @return
	 * 字串中的正、負整數。
	 */
	public static List<Integer> retrieveDigits(String s, boolean ignoreSign) {
		List<Integer> l = new ArrayList<Integer>();
		if(s!=null) {
			int tmp = 0;
			boolean hasNum = false;
			boolean isPlus = true;
			char c;
			for(int i=0,len=s.length(),val;i<len;i++) {
				c = s.charAt(i);
				switch (c) {
					case '0':
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9':
						val = c-'0';
						break;
					case '０':
					case '１':
					case '２':
					case '３':
					case '４':
					case '５':
					case '６':
					case '７':
					case '８':
					case '９':
						val = c-'０';
						break;
					default:
						val = -1;
				}
				if(val!=-1) {
					tmp = tmp*10+val;
					if(i==len-1) {
						if(!ignoreSign && !isPlus) tmp = -tmp;
						l.add(Integer.valueOf(tmp));
					} else {
						hasNum = true;
					}
				} else {
					if(hasNum) {
						if(!ignoreSign && !isPlus) tmp = -tmp;
						l.add(Integer.valueOf(tmp));
						tmp = 0;
						hasNum = false;
					}
					isPlus = '-'!=c;
				}
			}
		}
		return l;
	}
	
	/**
	 * 將一字串中所有整數列出。
	 * @param s
	 * @return
	 * 字串中的整數。
	 * @see #retrieveDigits(String, boolean)
	 */
	public static List<Integer> retrieveDigits(String s) {return retrieveDigits(s, false);}
	
	/**
	 * 將字串串接。
	 * @param appendable
	 * @param messages
	 * 若傳入的物件為null，則會忽略
	 * @return
	 */
	public static Appendable appendAll(Appendable appendable, Object...messages) {
		if(appendable==null) appendable = new StringBuilder();
		if(messages!=null) {
			for(Object o:messages) {
				if(o==null) continue;
				try {
					appendable.append(o.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return appendable;
	}
	
	/**
	 * 清空StringBuilder並且添加messages。
	 * @param sb
	 * @param strings
	 * @return
	 */
	public static String appendAllWithClean(StringBuilder stringBuilder, Object...messages) {
		if(stringBuilder!=null) stringBuilder.setLength(0);
		return appendAll(stringBuilder, messages).toString();
	}
	
	/**
	 * @param s
	 * @return
	 * null如果s為空值或trim完後長度為0；否則回傳trim完的結果。
	 */
	public static String trim(String s) {
		return s==null?null:((s=s.trim()).length()==0?null:s);
	}
	
	/**
	 * 取得子字串。
	 * @param s 來源字串
	 * @param position 第幾個位置的字(從1開始)
	 * @param length 從position開始算length個字
	 * @return null或子字串
	 */
	public static String substring(String s, int position, int length) {
		if(s==null) return null;
		
		int startIndex = position-1;
		int endIndex   = startIndex+length;
		int slen       = s.length();
		
		if(startIndex<0)   startIndex = 0;
		if(endIndex>slen)  endIndex = slen;
		
		if(endIndex<startIndex) return null;
		
		return s.substring(startIndex, endIndex);
	}
	
	public static String[] chineseName = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十"};
	
	/**
	 * 正整數的正規表示式
	 * @param value
	 * @return
	 */
	public static boolean isIntegerStartWithOne(String value) {
	    Pattern pattern = Pattern.compile("^\\+?[1-9][0-9]*.0$");
	    return pattern.matcher(value).matches();
	}
	
	/*
	 * 若為null顯示空字串
	 */
	public static String showText(String value) {
		if(!hasText(value)) {
			return "";
		}
		return value;
	}
}
