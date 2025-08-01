package com.cqi.hr.service;


import static org.hamcrest.CoreMatchers.nullValue;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.CompanyLeaveDAO;
import com.cqi.hr.dao.SpecialDateAboutWorkDAO;
import com.cqi.hr.dao.SysUserDAO;
import com.cqi.hr.dao.UserAskForLeaveDAO;
import com.cqi.hr.dao.UserAskForOvertimeDAO;
import com.cqi.hr.dao.UserLeaveDAO;
import com.cqi.hr.dao.UserLeaveHistoryDAO;
import com.cqi.hr.entity.CompanyLeave;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SpecialDateAboutWork;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.entity.UserAskForOvertime;
import com.cqi.hr.entity.UserLeaveHistory;
import com.cqi.hr.util.StringUtils;
import com.cqi.hr.util.DateUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class UserAskForLeaveService extends AbstractService<UserAskForLeave>{
	@Resource UserLeaveDAO userLeaveDAO;
	@Resource CompanyLeaveDAO companyLeaveDAO;
	@Resource UserAskForLeaveDAO userAskForLeaveDAO;
	@Resource UserAskForOvertimeDAO userAskForOvertimeDAO;
	@Resource SysUserDAO sysUserDAO;
	@Resource UserLeaveHistoryDAO userLeaveHistoryDAO;
	@Resource SpecialDateAboutWorkDAO specialDateAboutWorkDAO;
	
	@Override
	protected AbstractDAO<UserAskForLeave> getDAO() {
		return userAskForLeaveDAO;
	}
	
	@Transactional
	public PagingList<UserAskForLeave> getListByPage(Integer page, String userId) throws Exception {
		return userAskForLeaveDAO.getListByPage(page, userId);
	}
	
	@Transactional
	public List<UserAskForLeave> getListByUserId(String sysUserId) {
		return userAskForLeaveDAO.getListByUserId(sysUserId);
	}
	
	@Transactional
	public JSONArray getCalendarData(Date start, Date end, String userId) throws Exception{
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		JSONArray dataArray = new JSONArray();
		Map<String, SysUser> mappingUser = new HashMap<>();
		List<SysUser> listUser = sysUserDAO.get();
		for(SysUser user : listUser){
			mappingUser.put(user.getSysUserId(), user);
		}
		Map<Long, CompanyLeave> mappingLeave = new HashMap<Long, CompanyLeave>();
		List<CompanyLeave> listLeave = companyLeaveDAO.get();
		for(CompanyLeave leave : listLeave){
			mappingLeave.put(leave.getLeaveId(), leave);
		}
		List<UserAskForLeave> userAskForLeaveList = userAskForLeaveDAO.getMonthlyData(start, end, userId);
		logger.info("userAskForLeaveList size : " + userAskForLeaveList.size());
		for(UserAskForLeave data:userAskForLeaveList) {
			JSONObject jsonObject = new JSONObject();
			//For Test
			//jsonObject.put("title", getLeaveCalendarTitleForTest(mappingUser.get(data.getSysUserId()), data, mappingLeave));
			//Production
			jsonObject.put("title", getLeaveCalendarTitle(mappingUser.get(data.getSysUserId()), data, mappingLeave, false));
			jsonObject.put("start", datetimeFormat.format(data.getStartTime()));
			jsonObject.put("end", datetimeFormat.format(data.getEndTime()));
			dataArray.add(jsonObject);
		}
		List<UserAskForOvertime> userAskForOvertimeList = userAskForOvertimeDAO.getMonthlyData(start, end, userId);
		logger.info("userAskForOvertimeList size : " + userAskForOvertimeList.size());
		for(UserAskForOvertime data:userAskForOvertimeList) {
			JSONObject jsonObject = new JSONObject();
			//For Test
			//jsonObject.put("title", getOvertimeCalendarTitleForTest(mappingUser.get(data.getSysUserId()), data, mappingLeave));
			//Production
			jsonObject.put("title", getOvertimeCalendarTitle(mappingUser.get(data.getSysUserId()), data, mappingLeave, false));
			jsonObject.put("start", datetimeFormat.format(data.getStartTime()));
			jsonObject.put("end", datetimeFormat.format(data.getEndTime()));
			dataArray.add(jsonObject);
		}
		List<SpecialDateAboutWork> specialDateList = specialDateAboutWorkDAO.getVacationBetweenDate(start, end);
		SimpleDateFormat allDayFormat = new SimpleDateFormat("yyyy-MM-dd");
		for(SpecialDateAboutWork data:specialDateList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rendering", "background");
			jsonObject.put("start", allDayFormat.format(data.getTheDay()));
			jsonObject.put("end", allDayFormat.format(data.getTheDay()));
			dataArray.add(jsonObject);
		}
		return dataArray;
	}
	
	@Transactional
	public JSONArray getCalendarSimpleData(Date start, Date end, String userId) throws Exception{
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		JSONArray dataArray = new JSONArray();
		Map<String, SysUser> mappingUser = new HashMap<>();
		List<SysUser> listUser = sysUserDAO.get();
		for(SysUser user : listUser){
			mappingUser.put(user.getSysUserId(), user);
		}
		Map<Long, CompanyLeave> mappingLeave = new HashMap<Long, CompanyLeave>();
		List<CompanyLeave> listLeave = companyLeaveDAO.get();
		for(CompanyLeave leave : listLeave){
			mappingLeave.put(leave.getLeaveId(), leave);
		}
		List<UserAskForLeave> userAskForLeaveList = userAskForLeaveDAO.getMonthlyData(start, end, userId);
		logger.info("userAskForLeaveList size : " + userAskForLeaveList.size());
		for(UserAskForLeave data:userAskForLeaveList) {
			JSONObject jsonObject = new JSONObject();
			//For Test
			//jsonObject.put("title", getLeaveCalendarTitleForTest(mappingUser.get(data.getSysUserId()), data, mappingLeave));
			//Production
			jsonObject.put("title", getLeaveCalendarTitle(mappingUser.get(data.getSysUserId()), data, mappingLeave, true));
			jsonObject.put("start", datetimeFormat.format(data.getStartTime()));
			jsonObject.put("end", datetimeFormat.format(data.getEndTime()));
			dataArray.add(jsonObject);
		}
		List<UserAskForOvertime> userAskForOvertimeList = userAskForOvertimeDAO.getMonthlyData(start, end, userId);
		logger.info("userAskForOvertimeList size : " + userAskForOvertimeList.size());
		for(UserAskForOvertime data:userAskForOvertimeList) {
			JSONObject jsonObject = new JSONObject();
			//For Test
			//jsonObject.put("title", getOvertimeCalendarTitleForTest(mappingUser.get(data.getSysUserId()), data, mappingLeave));
			//Production
			jsonObject.put("title", getOvertimeCalendarTitle(mappingUser.get(data.getSysUserId()), data, mappingLeave, true));
			jsonObject.put("start", datetimeFormat.format(data.getStartTime()));
			jsonObject.put("end", datetimeFormat.format(data.getEndTime()));
			dataArray.add(jsonObject);
		}
		return dataArray;
	}
	
	/**
	 * 本月初總結上個月的請假資料
	 * @throws Exception
	 */
	@Transactional
	public void getLastMonthlySummary() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		List<SysUser> userList = new ArrayList<SysUser>();
		userList = sysUserDAO.getEnableUserOrGraduationInMonth(calendar.getTime());
		
		//for test 
		//userList.add(sysUserDAO.get("1198842813042872"));
		
		for(SysUser sysUser:userList) {
			List<Object[]> userAskForLeaveList = userAskForLeaveDAO.getMonthlyRowdata(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), sysUser.getSysUserId(), null);
			logger.info("sysUser : " + sysUser.getUserName() + ", size : " + userAskForLeaveList.size());
			if(userAskForLeaveList.size()>0) {
				for(Object[] dataArray:userAskForLeaveList){
					UserLeaveHistory userLeaveHistory = new UserLeaveHistory();
					userLeaveHistory.setDateOfYear(calendar.get(Calendar.YEAR));
					userLeaveHistory.setDateOfMonth(calendar.get(Calendar.MONTH) + 1); //人看得懂的月份
					userLeaveHistory.setLeaveId(Long.parseLong(dataArray[2].toString()));
					userLeaveHistory.setStatus(1);
					userLeaveHistory.setSysUserId(sysUser.getSysUserId());
					userLeaveHistory.setCount(Double.parseDouble(dataArray[0].toString()));
					userLeaveHistory.setCreateDate(new Date());
					userLeaveHistoryDAO.persist(userLeaveHistory);
				}
			}
			List<Object[]> userAskForOvertimeList = userAskForOvertimeDAO.getMonthlyRowdata(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), sysUser.getSysUserId(), null);
			if(userAskForOvertimeList.size()>0) {
				for(Object[] dataArray:userAskForOvertimeList){
					UserLeaveHistory userLeaveHistory = new UserLeaveHistory();
					userLeaveHistory.setDateOfYear(calendar.get(Calendar.YEAR));
					userLeaveHistory.setDateOfMonth(calendar.get(Calendar.MONTH) + 1); //人看得懂的月份
					userLeaveHistory.setLeaveId(Long.parseLong(dataArray[2].toString()));
					userLeaveHistory.setStatus(1);
					userLeaveHistory.setSysUserId(sysUser.getSysUserId());
					userLeaveHistory.setCount(Double.parseDouble(dataArray[0].toString()));
					userLeaveHistory.setCreateDate(new Date());
					userLeaveHistoryDAO.persist(userLeaveHistory);
				}
			}
		}
	}
	
	@Transactional
	public Map<String, List<UserAskForLeave>> getTodayLeave(){
		Map<String, List<UserAskForLeave>> dataMap = new HashMap<>();
		List<UserAskForLeave> todayLeave = userAskForLeaveDAO.getTodayLeave();
		logger.debug("Test : " + todayLeave.size());
		for(UserAskForLeave userAskForLeave:todayLeave) {
			if(null == dataMap.get(userAskForLeave.getSysUserId())) {
				List<UserAskForLeave> userLeave = new ArrayList<UserAskForLeave>();
				userLeave.add(userAskForLeave);
				dataMap.put(userAskForLeave.getSysUserId(), userLeave);
			}else {
				dataMap.get(userAskForLeave.getSysUserId()).add(userAskForLeave);
			}
		}
		return dataMap;
	}
	
	private static String[] testName = {"艾波", "鳥伯", "莎莎"}; 
	public static String getLeaveCalendarTitleForTest(SysUser sysUser, UserAskForLeave leave, Map<Long, CompanyLeave> leaveMapping) {
		SimpleDateFormat dayMonthFormat = new SimpleDateFormat("MM/dd");
		SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
		StringBuilder asanaName = new StringBuilder();
        asanaName.append("[" + leaveMapping.get(leave.getLeaveId()).getLeaveName() + "]");
        asanaName.append(" " + testName[ThreadLocalRandom.current().nextInt(0, 3)]);
        asanaName.append(" ");
        if(leave.getSpendTime()>20) {
        	asanaName.append(new DecimalFormat("#").format(leave.getSpendTime()));
        }else {
        	asanaName.append(StringUtils.chineseName[leave.getSpendTime().intValue()]);
        }
        if(leaveMapping.get(leave.getLeaveId()).getUnitType().equals(1)) {
        	asanaName.append("天");
        }else {
        	asanaName.append("小時");
        }
        if(DateUtils.isSameDay(leave.getStartTime(), leave.getEndTime())) {
        	asanaName.append(" " + hourFormat.format(leave.getStartTime()) + "~" + hourFormat.format(leave.getEndTime()));	        	
        }else {
        	asanaName.append(" " + dayMonthFormat.format(leave.getStartTime()) + " " + hourFormat.format(leave.getStartTime()) + " ~ " + dayMonthFormat.format(leave.getEndTime()) + " " + hourFormat.format(leave.getEndTime()) );
        }
        return asanaName.toString();
	}
	
	public static String getLeaveCalendarTitle(SysUser sysUser, UserAskForLeave leave, Map<Long, CompanyLeave> leaveMapping, boolean isSimple) {
		SimpleDateFormat dayMonthFormat = new SimpleDateFormat("MM/dd");
		SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
		StringBuilder asanaName = new StringBuilder();
        asanaName.append("[" + leaveMapping.get(leave.getLeaveId()).getLeaveName() + "]");
        if(!isSimple) {
        	asanaName.append(" " + sysUser.getOriginalName());
        }
        if(!isSimple) {
        	asanaName.append(" ");
        	if(leave.getSpendTime()>20) {
            	asanaName.append(new DecimalFormat("#").format(leave.getSpendTime()));
            }else {
            	asanaName.append(StringUtils.chineseName[leave.getSpendTime().intValue()]);
            }
	        if(leaveMapping.get(leave.getLeaveId()).getUnitType().equals(1)) {
	        	asanaName.append("天");
	        }else {
	        	asanaName.append("小時");
	        }
        }
        if(DateUtils.isSameDay(leave.getStartTime(), leave.getEndTime())) {
        	asanaName.append(" " + hourFormat.format(leave.getStartTime()) + "~" + hourFormat.format(leave.getEndTime()));	        	
        }else {
        	asanaName.append(" " + dayMonthFormat.format(leave.getStartTime()) + " " + hourFormat.format(leave.getStartTime()) + " ~ " + dayMonthFormat.format(leave.getEndTime()) + " " + hourFormat.format(leave.getEndTime()) );
        }
        return asanaName.toString();
	}
	
	public static String getOvertimeCalendarTitleForTest(SysUser sysUser, UserAskForOvertime overtime, Map<Long, CompanyLeave> leaveMapping) {
		SimpleDateFormat dayMonthFormat = new SimpleDateFormat("MM/dd");
		SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
		StringBuilder asanaName = new StringBuilder();
        asanaName.append("[" + leaveMapping.get(overtime.getOvertimeId()).getLeaveName() + "]");
        asanaName.append(" " + testName[ThreadLocalRandom.current().nextInt(0, 3)]);
        asanaName.append(" ");
        if(overtime.getSpendTime()>20) {
        	asanaName.append(new DecimalFormat("#").format(overtime.getSpendTime()));
        }else {
        	asanaName.append(StringUtils.chineseName[overtime.getSpendTime().intValue()]);
        }
        if(leaveMapping.get(overtime.getOvertimeId()).getUnitType().equals(1)) {
        	asanaName.append("天");
        }else {
        	asanaName.append("小時");
        }
        
        if(DateUtils.isSameDay(overtime.getStartTime(), overtime.getEndTime())) {
        	asanaName.append(" " + hourFormat.format(overtime.getStartTime()) + "~" + hourFormat.format(overtime.getEndTime()));	        	
        }else {
        	asanaName.append(" " + dayMonthFormat.format(overtime.getStartTime()) + " " + hourFormat.format(overtime.getStartTime()) + " ~ " + dayMonthFormat.format(overtime.getEndTime()) + " " + hourFormat.format(overtime.getEndTime()) );
        }
        return asanaName.toString();
	}
	
	public static String getOvertimeCalendarTitle(SysUser sysUser, UserAskForOvertime overtime, Map<Long, CompanyLeave> leaveMapping, boolean isSimple) {
		SimpleDateFormat dayMonthFormat = new SimpleDateFormat("MM/dd");
		SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
		StringBuilder asanaName = new StringBuilder();
        asanaName.append("[" + leaveMapping.get(overtime.getOvertimeId()).getLeaveName() + "]");
        if(!isSimple) {
        	asanaName.append(" " + sysUser.getOriginalName());
            asanaName.append(" ");
            if(overtime.getSpendTime()>20) {
            	asanaName.append(new DecimalFormat("#").format(overtime.getSpendTime()));
            }else {
            	asanaName.append(StringUtils.chineseName[overtime.getSpendTime().intValue()]);
            }
            if(leaveMapping.get(overtime.getOvertimeId()).getUnitType().equals(1)) {
            	asanaName.append("天");
            }else {
            	asanaName.append("小時");
            }
        }
        if(DateUtils.isSameDay(overtime.getStartTime(), overtime.getEndTime())) {
        	asanaName.append(" " + hourFormat.format(overtime.getStartTime()) + "~" + hourFormat.format(overtime.getEndTime()));	        	
        }else {
        	asanaName.append(" " + dayMonthFormat.format(overtime.getStartTime()) + " " + hourFormat.format(overtime.getStartTime()) + " ~ " + dayMonthFormat.format(overtime.getEndTime()) + " " + hourFormat.format(overtime.getEndTime()) );
        }
        return asanaName.toString();
	}
	
	@Transactional
	public Map<String, List<UserAskForLeave>> getRestTodayLeave() throws Exception{
		Map<String, SysUser> mappingUser = new HashMap<>();
		List<SysUser> listUser = sysUserDAO.get();
		for(SysUser user : listUser){
			mappingUser.put(user.getSysUserId(), user);
		}
		Map<String, List<UserAskForLeave>> dataMap = new HashMap<>();
		List<UserAskForLeave> todayLeave = userAskForLeaveDAO.getTodayLeave();
		logger.debug("Test : " + todayLeave.size());
		for(UserAskForLeave userAskForLeave:todayLeave) {
			if(null == dataMap.get(mappingUser.get(userAskForLeave.getSysUserId()).getUserName())) {
				List<UserAskForLeave> userLeave = new ArrayList<UserAskForLeave>();
				userLeave.add(userAskForLeave);
				dataMap.put(mappingUser.get(userAskForLeave.getSysUserId()).getUserName(), userLeave);
			}else {
				dataMap.get(mappingUser.get(userAskForLeave.getSysUserId()).getUserName()).add(userAskForLeave);
			}
		}
		return dataMap;
	}
	
	@Transactional
	public String checkRule(UserAskForLeave data) {
		//確認是否為上個月以前不可請的時間  
		/*
		Calendar today = Calendar.getInstance();
		Calendar dataStartTime = Calendar.getInstance();
		dataStartTime.setTime(data.getStartTime());
		if(today.get(Calendar.MONTH)>dataStartTime.get(Calendar.MONTH)) {
			if(today.get(Calendar.DAY_OF_MONTH)>=4) {
				return Constant.LAST_MONTH_CLOSE;
			}
		}
		*/ //modify by sam 20201229 ask for leave next year
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 3);
		
		Date todayDate = new Date(),leaveDate = data.getStartTime() ;
		Date thirdDateOfThisMonth = cal.getTime();
		if(leaveDate.before(DateUtils.getFirstDateOfLastMonth())) {
			return Constant.LAST_MONTH_CLOSE;
		}else if (leaveDate.before(DateUtils.getFirstDateOfThisMonth())) {
			if(todayDate.after(thirdDateOfThisMonth)) {
				return Constant.LAST_MONTH_CLOSE;
			}
		}
		
		
		//確認時間有無重疊
		List<UserAskForLeave> dataList = userAskForLeaveDAO.checkTimeOverCross(data);
		if(dataList.size()>0) {
			for(UserAskForLeave askLeave: dataList) {
				if(!askLeave.getAskForLeaveId().equals(data.getAskForLeaveId())) {
					return Constant.OVER_CROSS;
				}
			}
		}
		if(!DateUtils.isTheSameMonth(data.getStartTime(), data.getEndTime())) {
			return Constant.DIFFERENT_MONTH;
		}
		return "";
	}

	@Transactional
	public UserAskForLeave getMenstruationLeaveByUserIdAndMonth(String sysUserId,int monthDiff ) {
		Date firstDateOfMonth ;
		if (monthDiff == 0) {
			firstDateOfMonth = DateUtils.getFirstDateOfThisMonth();
		}else if (monthDiff == -1) {
			firstDateOfMonth = DateUtils.getFirstDateOfLastMonth();
		}else {
			return null;
		}
		
		UserAskForLeave userAskForLeave = userAskForLeaveDAO.getMenstruationLeaveByUserIdAndMonth( sysUserId,firstDateOfMonth);
		return userAskForLeave;
	}

	
	
}
