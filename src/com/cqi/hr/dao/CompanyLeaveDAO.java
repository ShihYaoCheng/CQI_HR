package com.cqi.hr.dao;

import java.util.List;

import javax.annotation.Nonnull;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.cqi.hr.entity.CompanyLeave;

@Repository
public class CompanyLeaveDAO extends AbstractDAO<CompanyLeave> {
	@Override
	protected Class<CompanyLeave> getEntityClass() {
		return CompanyLeave.class;
	}

	@SuppressWarnings("unchecked")
	public List<CompanyLeave> getListByType(@Nonnull Integer type) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("type", type));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public CompanyLeave getOvertimeByName(@Nonnull String name) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("leaveName", name));
		List<CompanyLeave> dataList = criteria.list();
		if(dataList.size()==1) {
			return dataList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CompanyLeave> getListWithoutMenstruation(@Nonnull Integer type) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("type", type));
		criteria.add(Restrictions.ne("leaveId", CompanyLeave.SHIFT_MENSTRUATION_ID));
		return criteria.list();
	}
}

