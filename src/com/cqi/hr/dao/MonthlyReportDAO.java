package com.cqi.hr.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.MonthlyReport;
import com.cqi.hr.entity.SysUser;

@Repository
public class MonthlyReportDAO extends AbstractDAO<MonthlyReport> {
	@Override
	protected Class<MonthlyReport> getEntityClass() {
		return MonthlyReport.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<MonthlyReport> getListByYearAndMonth(Integer year, Integer month, SysUser operator) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("dateOfYear", year));
		criteria.add(Restrictions.eq("dateOfMonth", month));
		if(operator!=null) {
			criteria.add(Restrictions.eq("sysUserId", operator.getSysUserId()));
		}
		criteria.addOrder(Order.asc("sysUserId"));
		List<MonthlyReport> monthlyReportsList = criteria.list();
		return monthlyReportsList;
	}
	
	@SuppressWarnings("unchecked")
	public MonthlyReport getOneByYearAndMonth(Integer year, Integer month, Long leaveId, String operatorId) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("dateOfYear", year));
		criteria.add(Restrictions.eq("dateOfMonth", month));
		criteria.add(Restrictions.eq("sysUserId", operatorId));
		criteria.add(Restrictions.eq("leaveId", leaveId));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		criteria.addOrder(Order.asc("sysUserId"));
		criteria.addOrder(Order.asc("leaveId"));
		List<MonthlyReport> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
	}
}

