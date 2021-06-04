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
	public final static String DATA_DUPLICATED = "資料重複，請重新確認";
	public final static String NAME_DUPLICATED = "名稱重複";
	public final static String RECORD_NOT_EXIST = "紀錄不存在";
	public final static String EMERGENCE_ILLEGAL = "災害處理單規則不符，請參考說明。";
	
	//方便統計所以跨月分開請
	public final static String DIFFERENT_MONTH = "為了方便系統統計，如有跨月，請分開請";
	
	public final static String OVER_CROSS = "日期橫跨過其他資料，請重新確認。";
	
	public final static String LAST_MONTH_CLOSE = "上個月以前的出勤資料已結算，無法更動。";
	
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
	public final static String DEPARTMENT_5F = "5F";
	
	//ASANA
	public final static String ASANA_CLIENT_ID = "1199599852520253";
	public final static String ASANA_CLIENT_SECRET = "822216ac72ccc569b152ba3660e99dfb";
	
	// Developer
	// 需用ngrok把local8080連接到ngrok.io,並在asana app 設定Redirect URLs,此區也須跟著修改
	 public final static String ASANA_REDIRECT_URL = "https://fe6cfb353627.ngrok.io/hr-manage/asanaCallback";
	
	// Production
//	 public final static String ASANA_REDIRECT_URL = "https://hr.cqiserv.com/asanaCallback";
	
	/*
	 * Token Account
	 */
	public final static String CQI_GAMES_ASANA_TOKEN = "0/aa0cd0284bc273402c12b6cc9ad7700c";
	
	/**
	 * Line Bot Token Developer
	 */
	public final static String LINE_CHANNEL_ACCESS_TOKEN = "APTT0hgJxLyPdzRO//VXvmRrXm0MxPe68jwA4sv88tx1e3O+xpKsF9nx1vl47qwgdEZwC8zPoBSZbvCI9kh8of9fkr72JLmcr2Qi3DiMA1jlELhSAKeUWvcoL6cxhL/TbiRQxM1SGVqH7DiTS/FZHwdB04t89/1O/w1cDnyilFU=";
	public final static String LINE_CHANNEL_SECRET = "9db990d7beab261ed3dc6d0b19f82a81";
	
	/**
	 * Line Bot Token Production
	 */
//	public final static String LINE_CHANNEL_ACCESS_TOKEN = "uV0vhaGIS0iiTNYwxmMHR9Ck3hsHDlM/sdU75CYLhnd5G0WWJjYo/Bk3fNe8RRBMco+FBDE3+QThosZfhk9SbXMdkFvhRQyzeh/S294rl2NInI5vBQXrzlIyexdkJ21Zig5OgfSte2QS+3qS1ri1hQdB04t89/1O/w1cDnyilFU=";
//	public final static String LINE_CHANNEL_SECRET = "2599f73f1a402479584066720b7ae7dd";

	
	public final static String LINE_FLEX_MESSAGE_ALT_TEXT_EMERGENCE_REQUEST = "災害處理單申請";
	
	public final static String LINE_IMAGE_TYPE_PROJECT = "Project";
	public final static String LINE_IMAGE_TYPE_DEPARTMENT = "Department";
	public final static String LINE_IMAGE_TYPE_COMPANY = "Company";
	
	public final static String LINE_EMERGENCE_CONFIRM = "confirm";
	public final static String LINE_EMERGENCE_REJECT = "reject";
	
	public final static Integer LINE_EMERGENCE_REJECT_BY_PROJECT = -1;
	public final static Integer LINE_EMERGENCE_REJECT_BY_DEPARTMENT = -2;
	public final static Integer LINE_EMERGENCE_REJECT_BY_FINANCE = -3;
	public final static Integer LINE_EMERGENCE_REJECT_BY_ADMINISTRATION = -4;
	public final static Integer LINE_EMERGENCE_REJECT_BY_COMPANY = -5;
	
	public final static String LINE_EMERGENCE_LEVEL_PROJECT = "Project";
	public final static String LINE_EMERGENCE_LEVEL_DEPARTMENT = "Department";
	public final static String LINE_EMERGENCE_LEVEL_FINANCE = "Finance";
	public final static String LINE_EMERGENCE_LEVEL_ADMINISTRATION = "Administration";
	public final static String LINE_EMERGENCE_LEVEL_COMPANY = "Company";
	
	public final static Map<Integer, String> getEmergenceLevel(){
		Map<Integer, String> levelMap = new HashMap<Integer, String>();
		levelMap.put(3, "C1狼級");
		levelMap.put(6, "C2虎級");
		levelMap.put(9, "C3鬼級");
		levelMap.put(12, "C4龍級");
		levelMap.put(15, "C5神級");
		return levelMap;
	}

	public final static Map<String, String> getGenderMap(){
		Map<String, String> genderMap = new HashMap<String, String>();
		genderMap.put(GENDER_MALE, "男");
		genderMap.put(GENDER_FEMALE, "女");
		return genderMap;
	}
	
}
