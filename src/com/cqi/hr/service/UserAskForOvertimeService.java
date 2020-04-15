package com.cqi.hr.service;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.CompanyLeaveDAO;
import com.cqi.hr.dao.UserAskForOvertimeDAO;
import com.cqi.hr.dao.UserLeaveDAO;
import com.cqi.hr.entity.CompanyLeave;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserAskForOvertime;
import com.cqi.hr.entity.UserLeave;

@Service
public class UserAskForOvertimeService extends AbstractService<UserAskForOvertime>{
	@Resource UserAskForOvertimeDAO userAskForOvertimeDAO;
	@Resource CompanyLeaveDAO companyLeaveDAO;
	@Resource UserLeaveService userLeaveService;
	@Resource UserLeaveDAO userLeaveDAO;
	
	@Override
	protected AbstractDAO<UserAskForOvertime> getDAO() {
		return userAskForOvertimeDAO;
	}
	
	@Transactional
	public PagingList<UserAskForOvertime> getListByPage(Integer page, String userId) throws Exception {
		return userAskForOvertimeDAO.getListByPage(page, userId);
	}
	
	@Transactional
	public List<UserAskForOvertime> getSumListGroupByNow(String userId) throws Exception {
		return userAskForOvertimeDAO.getSumListGroupByNow(userId);
	}
	
	@Transactional
	public Map<Long, CompanyLeave> getCompanyOvertimeMapping() throws Exception {
		Map<Long, CompanyLeave> mapping = new HashMap<Long, CompanyLeave>();
		List<CompanyLeave> list = companyLeaveDAO.getListByType(CompanyLeave.TYPE_OVERTIME);
		for(CompanyLeave leave : list){
			mapping.put(leave.getLeaveId(), leave);
		}
		return mapping;
	}
	
	/*
	 * 更新加班資料
	 * @type 1是新增，2是更新
	 */
	@Transactional
	public boolean updateUserAskOvertime(UserAskForOvertime userAskForOvertime, Integer type) throws Exception{
		Calendar calendar = Calendar.getInstance();
		Map<Long, CompanyLeave> overtimeMap = getCompanyOvertimeMapping();
		switch (type) {
		case 1:
			userAskForOvertimeDAO.persist(userAskForOvertime);
			//CQI的調班專屬邏輯，可以折抵事假時數
			if(overtimeMap.get(userAskForOvertime.getOvertimeId()).getLeaveName().equals("調班")) {
				//取得事假資料
				CompanyLeave companyLeave = companyLeaveDAO.getOvertimeByName("事假");
				if(null==companyLeave) {
					return false;
				}
				//取得剩餘可以請的事假資料
				UserLeave userLeave = userLeaveDAO.getOneBy2Id(userAskForOvertime.getSysUserId(), companyLeave.getLeaveId());
				if(null==userLeave) {
					userLeave = new UserLeave();
					userLeave.setLeaveId(companyLeave.getLeaveId());
					userLeave.setCount(userAskForOvertime.getSpendTime());
					userLeave.setSysUserId(userAskForOvertime.getSysUserId());
					userLeave.setStatus(1);
					userLeave.setCreateDate(calendar.getTime());
					userLeave.setUpdateDate(calendar.getTime());
				}else {
					userLeave.setCount(userLeave.getCount() + userAskForOvertime.getSpendTime());
					userLeave.setUpdateDate(calendar.getTime());
				}
				userLeaveDAO.saveOrUpdate(userLeave);
			}
			break;
		case 2:
			UserAskForOvertime userAskForOvertimeOri = userAskForOvertimeDAO.get(userAskForOvertime.getAskForOvertimeId());
			if(null == userAskForOvertimeOri){
				return false;
			}else{
				//CQI的調班專屬邏輯，可以折抵事假時數
				if(overtimeMap.get(userAskForOvertime.getOvertimeId()).getLeaveName().equals("調班")) {
					//取得事假資料
					CompanyLeave companyLeave = companyLeaveDAO.getOvertimeByName("事假");
					if(null==companyLeave) {
						return false;
					}
					//取得剩餘可以請的事假資料
					UserLeave userLeave = userLeaveDAO.getOneBy2Id(userAskForOvertime.getSysUserId(), companyLeave.getLeaveId());
					if(null==userLeave) {
						return false;
					}else {
						userLeave.setCount(userLeave.getCount() - userAskForOvertimeOri.getSpendTime() + userAskForOvertime.getSpendTime());
						userLeave.setUpdateDate(calendar.getTime());
					}
					userLeaveDAO.saveOrUpdate(userLeave);
				}
				userAskForOvertimeOri.setOvertimeId(userAskForOvertime.getOvertimeId());
				userAskForOvertimeOri.setSpendTime(userAskForOvertime.getSpendTime());
				userAskForOvertimeOri.setStartTime(userAskForOvertime.getStartTime());
				userAskForOvertimeOri.setEndTime(userAskForOvertime.getEndTime());
				userAskForOvertimeOri.setUpdateDate(calendar.getTime());
				userAskForOvertimeDAO.update(userAskForOvertimeOri);
			}
			break;
		default:
			return false;
		}
		return true;
	}
	
	/*
	 * 刪除加班紀錄
	 */
	@Transactional
	public boolean deleteAskOvertime(Long askForLeaveId, SysUser operator) throws Exception{
		UserAskForOvertime userAskForOvertime = userAskForOvertimeDAO.get(askForLeaveId);
		if(null==userAskForOvertime){
			return false;
		}
		if(!userAskForOvertime.getSysUserId().equals(operator.getSysUserId())){
			return false;
		}
		Calendar calendar = Calendar.getInstance();
		userAskForOvertime.setStatus(0);
		userAskForOvertime.setUpdateDate(calendar.getTime());
		userAskForOvertimeDAO.update(userAskForOvertime);
		Map<Long, CompanyLeave> overtimeMap = getCompanyOvertimeMapping();
		//CQI的調班專屬邏輯，可以折抵事假時數
		if(overtimeMap.get(userAskForOvertime.getOvertimeId()).getLeaveName().equals("調班")) {
			//取得事假資料
			CompanyLeave companyLeave = companyLeaveDAO.getOvertimeByName("事假");
			if(null==companyLeave) {
				return false;
			}
			//取得剩餘可以請的事假資料
			UserLeave userLeave = userLeaveDAO.getOneBy2Id(userAskForOvertime.getSysUserId(), companyLeave.getLeaveId());
			if(null==userLeave) {
				return false;
			}else {
				userLeave.setCount(userLeave.getCount() - userAskForOvertime.getSpendTime());
				userLeave.setUpdateDate(calendar.getTime());
			}
			userLeaveDAO.saveOrUpdate(userLeave);
		}
		return true;
	}
	
	@Transactional
	public Map<String, List<UserAskForOvertime>> getTodayOvertime(){
		Map<String, List<UserAskForOvertime>> dataMap = new HashMap<>();
		List<UserAskForOvertime> todayOvertime = userAskForOvertimeDAO.getTodayOvertime();
		logger.debug("Test : " + todayOvertime.size());
		for(UserAskForOvertime userAskForOvertime:todayOvertime) {
			if(null == dataMap.get(userAskForOvertime.getSysUserId())) {
				List<UserAskForOvertime> userOvertime = new ArrayList<UserAskForOvertime>();
				userOvertime.add(userAskForOvertime);
				dataMap.put(userAskForOvertime.getSysUserId(), userOvertime);
			}else {
				dataMap.get(userAskForOvertime.getSysUserId()).add(userAskForOvertime);
			}
		}
		return dataMap;
	}
}
