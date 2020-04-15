package com.cqi.hr.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cqi.hr.entity.GiveLeaveRule;

@Repository
public class GiveLeaveRuleDAO extends AbstractDAO<GiveLeaveRule> {
	@Override
	protected Class<GiveLeaveRule> getEntityClass() {
		return GiveLeaveRule.class;
	}

	@SuppressWarnings("unchecked")
	public List<GiveLeaveRule> getGiveLeaveRule(Long leaveId) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("leaveId", leaveId));
		criteria.add(Restrictions.eq("status", 1));
		criteria.addOrder(Order.desc("yearsAfterAppointment"));
		return criteria.list();
	}
}

