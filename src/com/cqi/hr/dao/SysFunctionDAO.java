package com.cqi.hr.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysFunction;

@Repository
public class SysFunctionDAO extends AbstractDAO<SysFunction> {
	@Override
	protected Class<SysFunction> getEntityClass() {
		return SysFunction.class;
	}
	
	public PagingList<SysFunction> getListByPage(Integer page, String searchFunctionName) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(StringUtils.hasText(searchFunctionName)){
			criteria.add(Restrictions.like("functionName", "%"+searchFunctionName+"%"));
		}
		return createPagingList(Constant.PAGE_SIZE, page, criteria, convertOrders(new String[]{"moduleName desc", "subModuleName desc", "functionName desc"}));
	}
	
	public SysFunction getByFunctionName(String functionName) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("functionName", functionName));
		return (SysFunction) criteria.uniqueResult();
	}
	
}

