package com.cqi.hr.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserLeaveHistory;

@Repository
public class UserLeaveHistoryDAO extends AbstractDAO<UserLeaveHistory> {
	@Override
	protected Class<UserLeaveHistory> getEntityClass() {
		return UserLeaveHistory.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserLeaveHistory> getListByYearAndMonth(Integer year, Integer month, SysUser operator) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("dateOfYear", year));
		criteria.add(Restrictions.eq("dateOfMonth", month));
		if(operator!=null) {
			criteria.add(Restrictions.eq("sysUserId", operator.getSysUserId()));
		}
		criteria.addOrder(Order.asc("sysUserId"));
		criteria.addOrder(Order.asc("leaveId"));
		return criteria.list();
	}
}

