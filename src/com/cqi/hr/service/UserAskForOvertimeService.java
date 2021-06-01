package com.cqi.hr.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.CompanyLeaveDAO;
import com.cqi.hr.dao.EmergenceOvertimeSignDAO;
import com.cqi.hr.dao.LineImageUrlDAO;
import com.cqi.hr.dao.SpecialDateAboutWorkDAO;
import com.cqi.hr.dao.SysUserDAO;
import com.cqi.hr.dao.UserAskForOvertimeDAO;
import com.cqi.hr.dao.UserLeaveDAO;
import com.cqi.hr.dao.UserShiftQuotaDAO;
import com.cqi.hr.entity.CompanyLeave;
import com.cqi.hr.entity.EmergenceOvertimeSign;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SpecialDateAboutWork;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserAskForOvertime;
import com.cqi.hr.entity.UserLeave;
import com.cqi.hr.entity.UserShiftQuota;
import com.cqi.hr.util.DateUtils;
import com.cqi.hr.util.MD5Utils;
import com.cqi.hr.util.StringUtils;

@Service
public class UserAskForOvertimeService extends AbstractService<UserAskForOvertime>{
	@Resource LineBotService lineBotService;
	@Resource UserAskForOvertimeDAO userAskForOvertimeDAO;
	@Resource CompanyLeaveDAO companyLeaveDAO;
	@Resource UserLeaveService userLeaveService;
	@Resource UserLeaveDAO userLeaveDAO;
	@Resource SysUserDAO sysUserDAO;
	@Resource EmergenceOvertimeSignDAO emergenceOvertimeSignDAO;
	@Resource LineImageUrlDAO lineImageUrlDAO;
	@Resource SpecialDateAboutWorkDAO specialDateAboutWorkDAO;
	@Resource UserShiftQuotaDAO userShiftQuotaDAO;
	
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
	
	@SuppressWarnings("unchecked")
	@Transactional
	public boolean checkEmergenceRule(UserAskForOvertime data) throws Exception {
		Map<Long, CompanyLeave> map = (Map<Long, CompanyLeave>) companyLeaveDAO.queryToMap("leaveId"); 
		if(map.get(data.getOvertimeId()).getLeaveName().equals("災害處理")) {
			SpecialDateAboutWork specialDateAboutWork = specialDateAboutWorkDAO.getOneByDate(DateUtils.clearTime(data.getStartTime()));
			Calendar askDay = Calendar.getInstance();
			askDay.setTime(DateUtils.clearTime(data.getStartTime()));
			if((askDay.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY && askDay.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY && null==specialDateAboutWork) 
					|| (null!=specialDateAboutWork && specialDateAboutWork.getIsWorkDay()==1)) {
				//工作日
				if(data.getSpendTime()>9) {
					return false;
				}
			}else {
				//休假日
				if(data.getSpendTime()<9) {
					return false;
				}
			}
			if(!StringUtils.hasText(data.getDescription())) {
				return false;
			}
		}
		return true;
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
			
			if(overtimeMap.get(userAskForOvertime.getOvertimeId()).getLeaveName().equals("調班")) {
				
				//取得剩餘調班額度
				UserShiftQuota userShiftQuota = userShiftQuotaDAO.getOneByUserId(userAskForOvertime.getSysUserId());
				if (userShiftQuota == null || userShiftQuota.getCount() <= 0) {
					return false;
				}else {
					userShiftQuota.setCount(userShiftQuota.getCount() -1 );
					userShiftQuota.setUpdateTime(new Date());
				}
				userShiftQuotaDAO.update(userShiftQuota);
				
				
			}else if(overtimeMap.get(userAskForOvertime.getOvertimeId()).getLeaveName().equals("災害處理")) {
				// 1. 產生災害處理單的對應認證碼
				EmergenceOvertimeSign sign = new EmergenceOvertimeSign();
				sign.setToken(MD5Utils.md5Hex(String.valueOf(new Date().getTime())));
				sign.setAskForOvertimeId(userAskForOvertime.getAskForOvertimeId());
				// 2. 尋找開發組主管
				SysUser user = sysUserDAO.get(userAskForOvertime.getSysUserId());
				SysUser manager = sysUserDAO.getProjectManager(user.getGroupName());
				sign.setProjectSignerId(manager.getSysUserId());
				sign.setDepartmentSignerId(sysUserDAO.getDepartmentMaster().getSysUserId());
				sign.setFinanceSignerId(sysUserDAO.getFinanceMaster().getSysUserId());
				sign.setAdministrationSignerId(sysUserDAO.getAdministrationManager().getSysUserId());
				sign.setCompanySignerId(sysUserDAO.getCompanyGold().getSysUserId());
				sign.setStatus(Constant.STATUS_ENABLE);
				sign.setCreateTime(userAskForOvertime.getStartTime());
				sign.setUpdateTime(calendar.getTime());
				emergenceOvertimeSignDAO.persist(sign);
				// 3. 向LINE發送簽核網址
				lineBotService.buildConfirmVo(sign, Constant.LINE_IMAGE_TYPE_PROJECT);
//				FlexMessageVo flexMessageVo = new FlexMessageVo();
//				flexMessageVo.setAltText(Constant.LINE_FLEX_MESSAGE_ALT_TEXT_EMERGENCE_REQUEST);
//				List<LineImageUrl> imageUrlList = lineImageUrlDAO.getTypeList(Constant.LINE_IMAGE_TYPE_PROJECT);
//				int random = (int) (Math.random()*imageUrlList.size());
//				flexMessageVo.setImageUrl(imageUrlList.get(random).getImageUrl());
//				flexMessageVo.setTargetUser(manager);
//				flexMessageVo.setSysUser(user);
//				flexMessageVo.setUserAskForOvertime(userAskForOvertime);
//				flexMessageVo.setConfirmParam(LineBotService.getEmergenceConfirmParameter(sign.getToken(), Constant.LINE_EMERGENCE_LEVEL_PROJECT));
//				flexMessageVo.setRejectParam(LineBotService.getEmergenceRejectParameter(sign.getToken(), Constant.LINE_EMERGENCE_LEVEL_PROJECT));
//				lineBotService.sendEmergenceSign(flexMessageVo);
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
				}else if(overtimeMap.get(userAskForOvertime.getOvertimeId()).getLeaveName().equals("災害處理")) {
					return false;
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
	public boolean deleteAskOvertime(Long askForOvertimeId, SysUser operator) throws Exception{
		UserAskForOvertime userAskForOvertime = userAskForOvertimeDAO.get(askForOvertimeId);
		if(null==userAskForOvertime){
			return false;
		}
		if(!userAskForOvertime.getSysUserId().equals(operator.getSysUserId())){
			return false;
		}
		String errorMsg = checkRule(userAskForOvertime);
		if(StringUtils.hasText(errorMsg)) {
			return false;
		}
		Calendar calendar = Calendar.getInstance();
		userAskForOvertime.setStatus(0);
		userAskForOvertime.setUpdateDate(calendar.getTime());
		userAskForOvertimeDAO.update(userAskForOvertime);
		Map<Long, CompanyLeave> overtimeMap = getCompanyOvertimeMapping();
		
		if(overtimeMap.get(userAskForOvertime.getOvertimeId()).getLeaveName().equals("調班")) {
			//取得剩餘調班額度
			UserShiftQuota userShiftQuota = userShiftQuotaDAO.getOneByUserId(userAskForOvertime.getSysUserId());
			if (userShiftQuota == null || userShiftQuota.getCount() <= 0) {
				return false;
			}else {
				userShiftQuota.setCount(userShiftQuota.getCount() + 1 );
				userShiftQuota.setUpdateTime(new Date());
			}
			userShiftQuotaDAO.update(userShiftQuota);
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
	
	@Transactional
	public Map<Long, UserAskForOvertime> findByCreateTime(Date startTime, Date endTime) throws Exception {
		Map<Long, UserAskForOvertime> map = new HashMap<>();
		CompanyLeave companyLeave = companyLeaveDAO.getOvertimeByName("災害處理");
		List<UserAskForOvertime> list = userAskForOvertimeDAO.findByIdAndCreateTime(startTime, endTime, companyLeave.getLeaveId());
		for(UserAskForOvertime data:list) {
			map.put(data.getAskForOvertimeId(), data);
		}
		return map;
	}
	
	@Transactional
	public String checkRule(UserAskForOvertime data) {
		//確認是否為上個月以前不可請的時間
		Calendar today = Calendar.getInstance();
		Calendar dataStartTime = Calendar.getInstance();
		dataStartTime.setTime(data.getStartTime());
		if(today.get(Calendar.MONTH)>dataStartTime.get(Calendar.MONTH)) {
			if(today.get(Calendar.DAY_OF_MONTH)>=4) {
				return Constant.LAST_MONTH_CLOSE;
			}
		}
		//確認時間有無重疊
		List<UserAskForOvertime> dataList = userAskForOvertimeDAO.checkTimeOverCross(data);
		if(dataList.size()>0) {
			for(UserAskForOvertime askOvertime: dataList) {
				if(!askOvertime.getAskForOvertimeId().equals(data.getAskForOvertimeId())) {
					return Constant.OVER_CROSS;
				}
			}
		}
		return "";
	}
}
