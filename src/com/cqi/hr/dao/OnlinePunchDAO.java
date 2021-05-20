package com.cqi.hr.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cqi.hr.entity.LineWebhookLog;
import com.cqi.hr.entity.OnlinePunch;

@Repository
public class OnlinePunchDAO extends AbstractDAO<OnlinePunch> {
	@Override
	protected Class<OnlinePunch> getEntityClass() {
		return OnlinePunch.class;
	}

	public OnlinePunch getOneByUserIdAndDate(String sysUserId, Date workDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("sysUserId", sysUserId));
		criteria.add(Restrictions.eq("workDate", workDate));
		List<OnlinePunch> list = criteria.list();
		if(list.size()>=1) {
			return list.get(0);
		}
		return null;
		
	}
	
}

