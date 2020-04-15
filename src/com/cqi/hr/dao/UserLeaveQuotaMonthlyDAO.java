package com.cqi.hr.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserLeaveQuotaMonthly;

@Repository
public class UserLeaveQuotaMonthlyDAO extends AbstractDAO<UserLeaveQuotaMonthly> {
	@Override
	protected Class<UserLeaveQuotaMonthly> getEntityClass() {
		return UserLeaveQuotaMonthly.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserLeaveQuotaMonthly> getListByYearAndMonth(Integer year, Integer month, SysUser operator) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("dateOfYear", year));
		criteria.add(Restrictions.eq("dateOfMonth", month));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		if(operator!=null) {
			criteria.add(Restrictions.eq("sysUserId", operator.getSysUserId()));
		}
		criteria.addOrder(Order.asc("sysUserId"));
		criteria.addOrder(Order.asc("leaveId"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public UserLeaveQuotaMonthly getOneByYearAndMonth(Integer year, Integer month, Long leaveId, String operatorId) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("dateOfYear", year));
		criteria.add(Restrictions.eq("dateOfMonth", month));
		criteria.add(Restrictions.eq("sysUserId", operatorId));
		criteria.add(Restrictions.eq("leaveId", leaveId));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		criteria.addOrder(Order.asc("sysUserId"));
		criteria.addOrder(Order.asc("leaveId"));
		List<UserLeaveQuotaMonthly> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
	}
}

