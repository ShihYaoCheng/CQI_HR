package com.cqi.hr.service;


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
import com.cqi.hr.dao.SysUserDAO;
import com.cqi.hr.dao.UserAskForLeaveDAO;
import com.cqi.hr.dao.UserAskForOvertimeDAO;
import com.cqi.hr.dao.UserAskForShiftDAO;
import com.cqi.hr.dao.UserShiftQuotaDAO;
import com.cqi.hr.entity.CompanyLeave;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.entity.UserAskForOvertime;
import com.cqi.hr.entity.UserAskForShift;
import com.cqi.hr.entity.UserShiftQuota;
import com.cqi.hr.util.DateUtils;

@Service
@Transactional
public class UserAskForShiftService extends AbstractService<UserAskForShift>{
	@Resource UserAskForShiftDAO userAskForShiftDAO;
	@Resource SysUserDAO sysUserDAO;
	@Resource UserAskForLeaveDAO userAskForLeaveDAO;
	@Resource UserAskForOvertimeDAO userAskForOvertimeDAO;
	@Resource UserShiftQuotaDAO userShiftQuotaDAO;
	
	@Override
	protected AbstractDAO<UserAskForShift> getDAO() {
		return userAskForShiftDAO;
	}
	
	public boolean addShift(UserAskForOvertime userAskForOvertime, UserAskForLeave userAskForLeave) throws Exception {
		
		//取得剩餘調班額度
		UserShiftQuota userShiftQuota = userShiftQuotaDAO.getOneByUserId(userAskForOvertime.getSysUserId());
		if (userShiftQuota == null || userShiftQuota.getCount() <= 0) {
			return false;
		}else {
			userShiftQuota.setCount(userShiftQuota.getCount() -1 );
			userShiftQuota.setUpdateTime(new Date());
		}
		userShiftQuotaDAO.update(userShiftQuota);
		
		userAskForShiftDAO.saveOrUpdate(new UserAskForShift(userAskForOvertime.getAskForOvertimeId(), userAskForLeave.getAskForLeaveId()));
		
		userAskForOvertime.setStatus(1);
		userAskForLeave.setStatus(1);
		userAskForLeaveDAO.update(userAskForLeave);
		userAskForOvertimeDAO.update(userAskForOvertime);
		
		return true;
	}

	
	public Map<Long, UserAskForLeave> mapUserAskForLeaveByOvertimeId() throws Exception {
		Map<Long, UserAskForLeave> mapping = new HashMap<>();
		List<UserAskForShift> list = userAskForShiftDAO.get();
		for(UserAskForShift s : list){
			
			mapping.put(s.getAskForOvertimeId(), userAskForLeaveDAO.get(s.getAskForLeaveId()));
		}
		
		
		return mapping;
	}

	public UserAskForShift getOneByOvertimeId(Long userAskForOvertimeId) throws Exception {
		
		return userAskForShiftDAO.getOneByOvertimeId(userAskForOvertimeId);
	}


	
	
	
}
