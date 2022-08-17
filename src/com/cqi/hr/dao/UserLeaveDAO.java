package com.cqi.hr.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.CompanyLeave;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.entity.UserLeave;
import com.cqi.hr.util.DateUtils;

@Repository
public class UserLeaveDAO extends AbstractDAO<UserLeave> {
	@Override
	protected Class<UserLeave> getEntityClass() {
		return UserLeave.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserLeave> getListByUserId(String userId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(StringUtils.hasText(userId)){
			criteria.add(Restrictions.eq("sysUserId", userId));
		}
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		return criteria.list();
	}

	public PagingList<UserLeave> getListByPage(Integer page, String userId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		if(StringUtils.hasText(userId)){
			criteria.add(Restrictions.eq("sysUserId", userId));
			return createPagingList(Constant.PAGE_SIZE, page, criteria, convertOrders(new String[]{"leaveId asc"}));
		}
		return createPagingList(Constant.PAGE_SIZE, page, criteria, convertOrders(new String[]{"sysUserId asc", "leaveId asc"}));
	}
	
	@SuppressWarnings("unchecked")
	public UserLeave getOneBy2Id(String userId, Long leaveId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("sysUserId", userId));
		criteria.add(Restrictions.eq("leaveId", leaveId));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		List<UserLeave> list = criteria.list();
		if(list.size()==1){
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<UserLeave> getAllUserLeave() throws Exception {
		
		Criteria SysUsercriteria = sessionFactory.getCurrentSession().createCriteria(SysUser.class);
		SysUsercriteria.add(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		SysUsercriteria.add(Restrictions.eq("roleId", "2"));
		List<SysUser> dataList =  SysUsercriteria.list();
		List<String> SysUserList = new ArrayList<String>();
		for(SysUser user : dataList )
		{
			SysUserList.add(user.getSysUserId());
		}
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());	
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));	
		criteria.addOrder(Order.asc("sysUserId"));
		List<UserLeave> UserLeaveData = criteria.list();
		
		List<UserLeave> result = new 	ArrayList<UserLeave>();
		
		for(UserLeave item : UserLeaveData){
			if(SysUserList.contains(item.getSysUserId()))
			{
				result.add(item);
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<UserLeave> getSearchUserLeave(String OriginalName) throws Exception {
		
		Criteria SysUsercriteria = sessionFactory.getCurrentSession().createCriteria(SysUser.class);
		SysUsercriteria.add(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		SysUsercriteria.add(Restrictions.eq("roleId", "2"));
		SysUsercriteria.add(Restrictions.like("originalName", OriginalName));
		
		/* 不找第一筆 找名子包含 字元 全部找出來*/
		List<SysUser> dataList =  SysUsercriteria.list();
		List<String> SysUserList = new ArrayList<String>();
		for(SysUser user : dataList )
		{
			SysUserList.add(user.getSysUserId());
		}
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());	
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));	
		criteria.addOrder(Order.asc("sysUserId"));
		List<UserLeave> UserLeaveData = criteria.list();
		
		List<UserLeave> result = new ArrayList<UserLeave>();
		
		for(UserLeave item : UserLeaveData){
			if(SysUserList.contains(item.getSysUserId()))
			{
				result.add(item);
			}
		}

		return result;
	}
	
}

