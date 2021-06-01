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
import com.cqi.hr.dao.UserAskForShiftDAO;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.entity.UserAskForOvertime;
import com.cqi.hr.entity.UserAskForShift;
import com.cqi.hr.util.DateUtils;

@Service
@Transactional
public class UserAskForShiftService extends AbstractService<UserAskForShift>{
	@Resource UserAskForShiftDAO userAskForShiftDAO;
	@Resource SysUserDAO sysUserDAO;
	@Resource UserAskForLeaveDAO userAskForLeaveDAO;
	
	@Override
	protected AbstractDAO<UserAskForShift> getDAO() {
		return userAskForShiftDAO;
	}
	
	public void addShift(UserAskForOvertime userAskForOvertime, UserAskForLeave userAskForLeave) throws Exception {
		userAskForShiftDAO.saveOrUpdate(new UserAskForShift(userAskForOvertime.getAskForOvertimeId(), userAskForLeave.getAskForLeaveId()));
		
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
