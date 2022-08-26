package com.cqi.hr.dao;


import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.SysUserAbsence;

@Repository
public class SysUserAbsenceDAO extends AbstractDAO<SysUserAbsence> {

	@Override
	protected Class<SysUserAbsence> getEntityClass() {
		return SysUserAbsence.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<SysUserAbsence> getSysUserAbsenceBySysUserId(String sysUserId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("sysUserId", sysUserId));
		criteria.add(Restrictions.eq("status", "y"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public SysUserAbsence getSysUserAbsenceBySysUserId(String sysUserId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("sysUserId", sysUserId));
		criteria.add(Restrictions.eq("status", "y"));
		List<SysUserAbsence> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public SysUserAbsence getSysUserAbsenceBySysUserIdAndDate(String sysUserId ,Date  effectiveDate , Date  expirationDate) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("sysUserId", sysUserId));
		criteria.add(Restrictions.eq("status", "y"));		
		Criterion rest1 = Restrictions.and(Restrictions.between("effectiveDate", effectiveDate , expirationDate));
		Criterion rest2 = Restrictions.and(Restrictions.between("expirationDate", effectiveDate , expirationDate ));
		criteria.add(Restrictions.or(rest1, rest2));
		
		List<SysUserAbsence> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<SysUserAbsence> getAllSysUserAbsence() throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("status", "y"));
		return criteria.list();
	}
	
}
