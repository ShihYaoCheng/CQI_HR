package com.cqi.hr.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.AttendanceRecord;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.UserLeave;
import com.cqi.hr.entity.UserShiftQuota;

@Repository
public class UserShiftQuotaDAO extends AbstractDAO<UserShiftQuota> {
	@Override
	protected Class<UserShiftQuota> getEntityClass() {
		return UserShiftQuota.class;
	}

	public PagingList<UserShiftQuota> getListByPage(Integer page, String userId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(StringUtils.hasText(userId)){
			criteria.add(Restrictions.eq("sysUserId", userId));
		}
		return createPagingList(Constant.PAGE_SIZE, page, criteria, convertOrders(new String[]{"create_time DESC"}));
	}
	
	@SuppressWarnings("unchecked")
	public UserShiftQuota getOneByUserId(String userId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("sysUserId", userId));
		List<UserShiftQuota> list = criteria.list();
		if(list.size()==1){
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserShiftQuota> getListByUserId(String userId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(StringUtils.hasText(userId)){
			criteria.add(Restrictions.eq("sysUserId", userId));
		}
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		return criteria.list();
	}
	
	
}

