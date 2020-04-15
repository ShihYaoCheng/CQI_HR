package com.cqi.hr.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.ApplyForAutoAskLeave;
import com.cqi.hr.entity.PagingList;

@Repository
public class ApplyForAutoAskLeaveDAO extends AbstractDAO<ApplyForAutoAskLeave> {
	@Override
	protected Class<ApplyForAutoAskLeave> getEntityClass() {
		return ApplyForAutoAskLeave.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<ApplyForAutoAskLeave> getListByUserId(String userId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(StringUtils.hasText(userId)){
			criteria.add(Restrictions.eq("sysUserId", userId));
		}
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		return criteria.list();
	}

	public PagingList<ApplyForAutoAskLeave> getListByPage(Integer page, String userId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(StringUtils.hasText(userId)){
			criteria.add(Restrictions.eq("sysUserId", userId));
			return createPagingList(Constant.PAGE_SIZE, page, criteria, convertOrders(new String[]{"dateOfYear desc", "dateOfMonth desc"}));
		}
		return createPagingList(Constant.PAGE_SIZE, page, criteria, convertOrders(new String[]{"sysUserId asc", "dateOfYear desc", "dateOfMonth desc"}));
	}
	
	@SuppressWarnings("unchecked")
	public List<ApplyForAutoAskLeave> getListThisMonth() throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		Calendar calendar = Calendar.getInstance();
		criteria.add(Restrictions.eq("dateOfYear", calendar.get(Calendar.YEAR)));
		criteria.add(Restrictions.eq("dateOfMonth", (calendar.get(Calendar.MONTH) + 1)));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		return criteria.list();
	}
}

