package com.cqi.hr.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SpecialDateAboutWork;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.SysUserShift;
import com.cqi.hr.util.DateUtils;

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
		return createPagingList(Constant.PAGE_SIZE, page, criteria, convertOrders(new String[]{"enableMonth DESC","sysUserId"}));
	}
	
	@SuppressWarnings("unchecked")
	public SysUserShift getOneByIdAndDate(String sysUserId,Date theDay)  throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("sysUserId", sysUserId));
		Date enableDate = DateUtils.clearTime(theDay);
		enableDate.setDate(1);
		criteria.add(Restrictions.eq("enableMonth", enableDate));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		List<SysUserShift> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
	}
}
