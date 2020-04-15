package com.cqi.hr.util;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;


/***/
public class ObjectUtils {
	/**取得方法名稱
	 * @param fieldName 屬性名稱<br/>
	 * @param isGetter  是否為getter方法<br/>
	 * example1:getMethodName("user",false);return value is setUser;<br/>
	 * example2:getMethodName("user",true); return value is getUser;<br/>
	 * */
	public static  String getMethodName(String fieldName, boolean isGetter) {
		if(fieldName==null) return "";

		char[] str = new char[fieldName.length()+3];
		str[0] = isGetter?'g':'s';
		str[1] = 'e';
		str[2] = 't';
		fieldName.getChars(0, fieldName.length(), str, 3);
		str[3] = Character.toUpperCase(str[3]);
		
		return new String(str);
	}

	/**取得物件的屬性值
	 * @param t         物件實例<br/>
	 * @param fieldName 屬性名稱<br/>
	 * #須有對應的getter方法
	 * */
	public static  <T> Object getValue(T t,String fieldName)throws Exception{
		Method m = t.getClass().getDeclaredMethod(getMethodName(fieldName,true));
		return m.invoke(t);
	}
	
	/**設定物件的屬性值
	 * @param t         物件實例<br/>
	 * @param fieldName 屬性名稱<br/>
	 * #須有對應的setter方法
	 * */
	public static <T> Object setValue(T t,String fieldName,Object val)throws Exception{
		Method m = t.getClass().getDeclaredMethod(getMethodName(fieldName,false),val.getClass());
		return m.invoke(t,val);
	}
	
	/**重設結束時間*/
	public static   Date resetEndDate(Date src){
		Calendar c = Calendar.getInstance();
		c.setTime(src);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}
	
	/**重設開始時間*/
	public static   Date resetBeginDate(Date src){
		Calendar c = Calendar.getInstance();
		c.setTime(src);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	
}
