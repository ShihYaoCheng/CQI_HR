package com.cqi.hr.service;


import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.CompanyLeaveDAO;
import com.cqi.hr.dao.UserAskForLeaveDAO;
import com.cqi.hr.dao.UserLeaveQuotaMonthlyDAO;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserLeaveQuotaMonthly;

@Service
public class UserLeaveQuotaMonthlyService extends AbstractService<UserLeaveQuotaMonthly>{
	@Resource UserLeaveQuotaMonthlyDAO userLeaveQuotaMonthlyDAO;
	@Resource CompanyLeaveDAO companyLeaveDAO;
	@Resource UserAskForLeaveDAO userAskForLeaveDAO;
	
	@Override
	protected AbstractDAO<UserLeaveQuotaMonthly> getDAO() {
		return userLeaveQuotaMonthlyDAO;
	}
	
	/**
	 * Get Leave History By Year and Month
	 * @throws Exception
	 */
	@Transactional
	public List<UserLeaveQuotaMonthly> getLeaveHistoryByYearAndMonth(Integer year, Integer month, SysUser operator) throws Exception{
		return userLeaveQuotaMonthlyDAO.getListByYearAndMonth(year, month,  operator);
	}
}
