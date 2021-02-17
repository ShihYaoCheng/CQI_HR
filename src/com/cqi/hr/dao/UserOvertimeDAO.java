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
import com.cqi.hr.entity.UserOvertime;

@Repository
public class UserOvertimeDAO extends AbstractDAO<UserOvertime> {
	@Override
	protected Class<UserOvertime> getEntityClass() {
		return UserOvertime.class;
	}

	public PagingList<UserOvertime> getListByPage(Integer page, String userId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(StringUtils.hasText(userId)){
			criteria.add(Restrictions.eq("sysUserId", userId));
		}
		return createPagingList(Constant.PAGE_SIZE, page, criteria, convertOrders(new String[]{"create_time DESC"}));
	}
	
	@SuppressWarnings("unchecked")
	public UserOvertime getOneByUserId(String userId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("sysUserId", userId));
		List<UserOvertime> list = criteria.list();
		if(list.size()==1){
			return list.get(0);
		}
		return null;
	}
	
	
}

