package com.cqi.hr.service;


import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.CompanyLeaveDAO;
import com.cqi.hr.dao.UserAskForLeaveDAO;
import com.cqi.hr.dao.UserLeaveHistoryDAO;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserLeaveHistory;

@Service
public class UserLeaveHistoryService extends AbstractService<UserLeaveHistory>{
	@Resource UserLeaveHistoryDAO userLeaveHistoryDAO;
	@Resource CompanyLeaveDAO companyLeaveDAO;
	@Resource UserAskForLeaveDAO userAskForLeaveDAO;
	
	@Override
	protected AbstractDAO<UserLeaveHistory> getDAO() {
		return userLeaveHistoryDAO;
	}
	
	/**
	 * Get Leave History By Year and Month
	 * @throws Exception
	 */
	@Transactional
	public List<UserLeaveHistory> getLeaveHistoryByYearAndMonth(Integer year, Integer month, SysUser operator) throws Exception{
		return userLeaveHistoryDAO.getListByYearAndMonth(year, month,  operator);
	}
}
