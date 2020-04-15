package com.cqi.hr.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.UserLeave;

@Repository
public class UserLeaveDAO extends AbstractDAO<UserLeave> {
	@Override
	protected Class<UserLeave> getEntityClass() {
		return UserLeave.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserLeave> getListByUserId(String userId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(StringUtils.hasText(userId)){
			criteria.add(Restrictions.eq("sysUserId", userId));
		}
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		return criteria.list();
	}

	public PagingList<UserLeave> getListByPage(Integer page, String userId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		if(StringUtils.hasText(userId)){
			criteria.add(Restrictions.eq("sysUserId", userId));
			return createPagingList(Constant.PAGE_SIZE, page, criteria, convertOrders(new String[]{"leaveId asc"}));
		}
		return createPagingList(Constant.PAGE_SIZE, page, criteria, convertOrders(new String[]{"sysUserId asc", "leaveId asc"}));
	}
	
	@SuppressWarnings("unchecked")
	public UserLeave getOneBy2Id(String userId, Long leaveId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("sysUserId", userId));
		criteria.add(Restrictions.eq("leaveId", leaveId));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		List<UserLeave> list = criteria.list();
		if(list.size()==1){
			return list.get(0);
		}
		return null;
	}
	
}

