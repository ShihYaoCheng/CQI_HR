package com.cqi.hr.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysRole;

@Repository
public class SysRoleDAO extends AbstractDAO<SysRole> {
	@Override
	protected Class<SysRole> getEntityClass() {
		return SysRole.class;
	}
	
	public void getUserMenu(String roleId){
		
	}

	public PagingList<SysRole> getListByPage(Integer page, String searchRoleName) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(StringUtils.hasText(searchRoleName)){
			criteria.add(Restrictions.like("roleName", "%"+searchRoleName+"%"));
		}
		return createPagingList(Constant.PAGE_SIZE, page, criteria, convertOrders(new String[]{"roleName desc"}));
	}
	
	public SysRole getByRoleName(String roleName) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("roleName", roleName));
		return (SysRole) criteria.uniqueResult();
	}
	
}

