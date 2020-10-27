package com.cqi.hr.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.EmergenceOvertimeSign;

@Repository
public class EmergenceOvertimeSignDAO extends AbstractDAO<EmergenceOvertimeSign> {
	@Override
	protected Class<EmergenceOvertimeSign> getEntityClass() {
		return EmergenceOvertimeSign.class;
	}
	
	@SuppressWarnings("unchecked")
	public EmergenceOvertimeSign getOneByToken(@Nonnull String token) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("token", token));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		List<EmergenceOvertimeSign> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<EmergenceOvertimeSign> findByLevel(@Nonnull String level) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		switch (level) {
		case Constant.LINE_EMERGENCE_LEVEL_DEPARTMENT:
			criteria.add(Restrictions.isNull("departmentSignTime"));
			break;
		case Constant.LINE_EMERGENCE_LEVEL_FINANCE:
			criteria.add(Restrictions.isNotNull("departmentSignTime"));
			criteria.add(Restrictions.isNull("financeSignTime"));
			break;
		case Constant.LINE_EMERGENCE_LEVEL_COMPANY:
			criteria.add(Restrictions.isNotNull("departmentSignTime"));
			criteria.add(Restrictions.isNotNull("financeSignTime"));
			break;
		default:
			break;
		}
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<EmergenceOvertimeSign> findByDate(Date startTime, Date endTime) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.between("createTime", startTime, endTime));
		return criteria.list();
	}
}

