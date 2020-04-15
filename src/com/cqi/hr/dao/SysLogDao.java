package com.cqi.hr.dao;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysLog;

@Repository
public class SysLogDao extends AbstractDAO<SysLog>{	
	@Override
	protected Class<SysLog> getEntityClass() {
		return SysLog.class;
	}
	
	public PagingList<SysLog> getListByPageAndCreateUserAndCreateDateFunctionId(Integer page, SysLog sysLog, Date dateFrom, Date dateTo) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if (StringUtils.hasText(sysLog.getCreateUser())) {
			criteria.add(Property.forName("createUser").like("%" + sysLog.getCreateUser().trim() + "%"));
		}
		if (dateFrom != null) {
			criteria.add(Restrictions.ge("createDate", dateFrom));
		}
		if (dateTo != null) {
			criteria.add(Restrictions.lt("createDate", dateTo));
		}
		if (StringUtils.hasText(sysLog.getFunctionId())) {
			criteria.add(Restrictions.eq("functionId", sysLog.getFunctionId()));
		}
		return createPagingList(Constant.PAGE_SIZE, page, criteria, Order.desc("createDate"));
		
	}
	
	public Integer getCountByCreateUserAndCreateDateFunctionId(SysLog sysLog, Date dateFrom, Date dateTo) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		
		if (StringUtils.hasText(sysLog.getCreateUser())) {
			criteria.add(Property.forName("createUser").like("%" + sysLog.getCreateUser().trim() + "%"));
		}
		if (dateFrom != null) {
			criteria.add(Restrictions.ge("createDate", dateFrom));
		}
		if (dateTo != null) {
			criteria.add(Restrictions.lt("createDate", dateTo));
		}
		if (StringUtils.hasText(sysLog.getFunctionId())) {
			criteria.add(Restrictions.eq("functionId", sysLog.getFunctionId()));
		}
		criteria.setProjection(Projections.rowCount());
		Number uniqueResult = (Number)criteria.uniqueResult();
		if(uniqueResult!=null) {
			return uniqueResult.intValue();			
		}
		return 0;
	}
}
