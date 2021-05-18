package com.cqi.hr.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.SysUserShift;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.entity.WorkFromHome;

@Repository
public class WorkFromHomeDao extends AbstractDAO<WorkFromHome>{

	@Override
	protected Class<WorkFromHome> getEntityClass() {
		return WorkFromHome.class;
	}	

	public PagingList<WorkFromHome> getPageByUserId(Integer page, SysUser sysUser) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(sysUser!=null){
			criteria.add(Restrictions.eq("sysUserId", sysUser.getSysUserId()));
		}
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		return createPagingList(Constant.PAGE_SIZE, page, criteria, convertOrders(new String[]{"workDate DESC","sysUserId"}));
	}

	public String addWorkFromHome(WorkFromHome workFromHome) throws Exception {
		WorkFromHome criteria = new WorkFromHome();
		criteria.setSysUserId(workFromHome.getSysUserId());
		criteria.setWorkDate(workFromHome.getWorkDate());
		criteria.setStatus(1);
		List<WorkFromHome> dbDataList = getByExample(criteria, "workDate desc");
		if(dbDataList.size()>0) {
			return Constant.DATA_DUPLICATED;
		}
		
		persist(workFromHome);
		return "";
	}
	
	
	
}
