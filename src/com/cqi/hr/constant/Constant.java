package com.cqi.hr.constant;

import java.util.HashMap;
import java.util.Map;


public class Constant {
	/* Session Keys */
	public final static String LOGIN_INFO 					= "loginInfo";
	public final static String ADMIN_LOGIN_INFO 			= "adminLoginInfo";
	public final static String MENU 						= "menu";
	public final static String ROLE_PRIVILEGE 				= "rolePrivilege";
	/* Function Ids */
	public final static String SYS_PRIVILEGE 				= "01";
	public final static String SYS_USER 					= "02";
	public final static String COURSE_DESIGN				= "04";
	public final static String PORTAL_ACCOUNT				= "05";
	public final static String PORTAL_SIGNUP				= "06";
	public final static String VERIFY						= "07";
	public final static String ADMIN_ACCOUNT				= "08";
	
	public final static String VISIBLE = "Y";
	public final static String HIDDEN = "N";
	
	public final static String BOOK="book";
	public final static String CHECK_LIST="checkList";
	public final static String SET="set";
	public final static String CD="cd";
	public final static String DVD="dvd";
	
	public final static String COURSE = "course";
	public final static String Material = "material";
	public final static String PAY_TYPE = "pay_type";
	
	public final static String ITEM_STATUS_OPEN = "opened";
	public final static String ITEM_STATUS_UPOPEN = "unopen";
	public final static String ITEM_STATUS_TRANSFER = "transfer";
	public final static String ITEM_STATUS_TAKEN = "taken";
	public final static String ITEM_STATUS_NOPAY = "nopay";
	
	public final static Integer RECORD_TYPE_BUY = 1;
	public final static Integer RECORD_TYPE_TRANS = 2;
	public final static Integer RECORD_TYPE_OPEN = 3;
	public final static Integer RECORD_TYPE_FSM = 4;
	
	public final static Integer FSM_CREDIT = 3;
	/*
	 * 3:cancal
	 * 1:join
	 * 2:attend
	 * 4:absent
	 * */
	public final static Integer ACTIVITY_STATUS_CANCEL = 4;
	public final static Integer ACTIVITY_STATUS_JOIN = 1;
	public final static Integer ACTIVITY_STATUS_ATTEND = 2;
	public final static Integer ACTIVITY_STATUS_ABSENT = 3;
	
	public final static int    PAGE_SIZE 					= 10;
	public final static String SUCCESS = "成功";//
	public final static String NETWORK_BUSY = "系統或網路繁忙，請稍候在試。";//network.busy
	public final static String NAME_DUPLICATED = "名稱重複";
	public final static String RECORD_NOT_EXIST = "紀錄不存在";
	
	//方便統計所以跨月分開請
	public final static String DIFFERENT_MONTH = "為了方便系統統計，如有跨月，請分開請";//network.busy
	
	public final static String SYSUSER_ENABLE = "y";
	public final static String SYSUSER_DISABLE = "n";
	public final static String MANGA_ROLE_NAME_AND_NICKNAME_DUPLICATED = "role name and nickname are dulicated";
	public final static String MANGA_ROLE_NAME_DUPLICATED = "role name is dulicated";
	public final static String PASSWORD_INCORRECT = "password incorrect";
	public final static String EMOTION_NAME_DUPLICATED = "emotion name is dulicated";
	public final static String EMOTION_INNER_AND_OUTER_DUPLICATED = "emotion inner and outer are dulicated";
	
	public final static Integer CATEGORY_ROLE = 0;
	public final static Integer CATEGORY_BACKGROUND = 1;
	public final static Integer CATEGORY_DIALOG = 2;
	
	public final static String ADDR_HOME = "H";
	public final static String ADDR_COMPANY = "C";
	
	public final static String GENDER_MALE = "M";
	public final static String GENDER_FEMALE = "F";
	
	public final static String MARRIAGE_MARRIED = "Y";
	public final static String MARRIAGE_NON_MARRIED = "N";
	
	public final static String HAS_SERVICE_BOOK = "B";
	public final static String HAS_SERVICE_COURSE = "C";
	
	public final static String TRANSACTION_TYPE_SAVE = "1";
	public final static String TRANSACTION_TYPE_USE = "2";
	public final static String TRANSACTION_TYPE_TRANS = "3";
	
	public final static Integer STATUS_ENABLE = 1;
	public final static Integer STATUS_DISABLE = 0;
	
	public final static String DEPARTMENT_26F = "26F";
	public final static String DEPARTMENT_25F = "25F";
	
	/**
	 * Developer
	 */
//	public final static String ASANA_CLIENT_ID = "904212848320509";
//	public final static String ASANA_CLIENT_SECRET = "22b136c8e709c7cdf50a3c0e1ac1dd98";
//	public final static String ASANA_REDIRECT_URL = "https://36.230.171.36/CQIHRManage/asanaCallback";
	
	/**
	 * Production
	 */
	public final static String ASANA_CLIENT_ID = "923603382034989";
	public final static String ASANA_CLIENT_SECRET = "749e5c15900b039263f59d93f9c44450";
	public final static String ASANA_REDIRECT_URL = "https://hr.cqiserv.com/asanaCallback";
	
	/*
	 * Token Account
	 */
	public final static String CQI_GAMES_ASANA_TOKEN = "0/aa0cd0284bc273402c12b6cc9ad7700c";
	
	public final static Map<Integer, String> getCategoryMap(){
		Map<Integer, String> categoryMap = new HashMap<Integer, String>();
		categoryMap.put(CATEGORY_ROLE, "角色");
		categoryMap.put(CATEGORY_BACKGROUND, "背景");
		categoryMap.put(CATEGORY_DIALOG, "對話框");
		return categoryMap;
	}

	public final static Map<String, String> getGenderMap(){
		Map<String, String> genderMap = new HashMap<String, String>();
		genderMap.put(GENDER_MALE, "男");
		genderMap.put(GENDER_FEMALE, "女");
		return genderMap;
	}
	
	public final static Map<String, String> getMarriageMap(){
		Map<String, String> marriageMap = new HashMap<String, String>();
		marriageMap.put(MARRIAGE_MARRIED, "已婚");
		marriageMap.put(MARRIAGE_NON_MARRIED, "未婚");
		return marriageMap;
	}
	
	public final static Map<String, String> getServiceMap(){
		Map<String, String> map = new HashMap<String, String>();
		map.put(HAS_SERVICE_BOOK, "購買書籍");
		map.put(HAS_SERVICE_COURSE, "接受服務");
		return map;
	}
	
	public final static Map<String, String> getTransactionMap(){
		Map<String, String> map = new HashMap<String, String>();
		map.put(TRANSACTION_TYPE_SAVE, "存入");
		map.put(TRANSACTION_TYPE_TRANS, "轉讓");
		map.put(TRANSACTION_TYPE_USE, "使用");
		return map;
	}

	public static Map<String, String> getItemStatus(){
		Map<String, String> itemStatusMap = new HashMap<String, String>();
		itemStatusMap.put(ITEM_STATUS_OPEN, "已開課");
		itemStatusMap.put(ITEM_STATUS_UPOPEN, "未開課");
		itemStatusMap.put(ITEM_STATUS_TRANSFER, "已轉讓");
		itemStatusMap.put(ITEM_STATUS_TAKEN, "已領取");
		itemStatusMap.put(ITEM_STATUS_NOPAY, "未付款");
		return itemStatusMap;
	}

	public static Map<String, String> getCourseStatus(){
		Map<String, String> itemStatusMap = new HashMap<String, String>();
		itemStatusMap.put(ITEM_STATUS_OPEN, "開課");
		itemStatusMap.put(ITEM_STATUS_TRANSFER, "轉讓");
		itemStatusMap.put(ITEM_STATUS_TAKEN, "結業");
		return itemStatusMap;
	}
}
