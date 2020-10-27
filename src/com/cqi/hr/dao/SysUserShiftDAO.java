package com.cqi.hr.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.SysUserShift;

@Repository
public class SysUserShiftDAO extends AbstractDAO<SysUserShift>{	
	@Override
	protected Class<SysUserShift> getEntityClass() {
		return SysUserShift.class;
	}
	
	public PagingList<SysUserShift> getPageByUserId(Integer page, SysUser sysUser) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(sysUser!=null){
			criteria.add(Restrictions.eq("sysUserId", sysUser.getSysUserId()));
		}
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		return createPagingList(Constant.PAGE_SIZE, page, criteria, convertOrders(new String[]{"enableMonth DESC"}));
	}
}
