package com.cqi.hr.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cqi.hr.entity.SysPrivilege;

@Repository
public class SysPrivilegeDAO extends AbstractDAO<SysPrivilege> {
	@Override
	protected Class<SysPrivilege> getEntityClass() {
		return SysPrivilege.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<SysPrivilege> getByRoleId(String roleId) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("roleId", roleId));
		return criteria.list();
	}
	
}

