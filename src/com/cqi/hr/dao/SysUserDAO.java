package com.cqi.hr.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.util.DateUtils;

@Repository
public class SysUserDAO extends AbstractDAO<SysUser> {
	@Override
	protected Class<SysUser> getEntityClass() {
		return SysUser.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<SysUser> getEnableUser() throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<SysUser> getEnableUserOrderByDesc(String propertyName) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		criteria.addOrder(Order.desc(propertyName));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<SysUser> getEnableRole2User()throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		criteria.add(Restrictions.eq("roleId", "2"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<SysUser> getEnableRole2LineUser()throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		criteria.add(Restrictions.eq("roleId", "2"));
		criteria.add(Restrictions.isNotNull("lineId"));
		criteria.add(Restrictions.ne("lineId",""));
		return criteria.list();
	}
	
	/**
	 * 取得在職員工或是這個月離職的員工，主要用於統計出勤的UserList用
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<SysUser> getEnableUserOrGraduationInMonth(Date theMonth) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		//criteria.add(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		Criterion rest1 = Restrictions.and(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		Calendar calendarMonth = Calendar.getInstance();
		calendarMonth.setTime(theMonth);
		Date startDate = DateUtils.getFirstDateByYearAndMonth(calendarMonth.get(Calendar.YEAR), calendarMonth.get(Calendar.MONTH));
		Date endDate = DateUtils.getLastDateByYearAndMonth(calendarMonth.get(Calendar.YEAR), calendarMonth.get(Calendar.MONTH));
		logger.info("startDate : " + startDate);
		logger.info("endDate : " + endDate);
		Criterion rest2 = Restrictions.and(Restrictions.between("graduationDate", startDate, endDate));
		criteria.add(Restrictions.or(rest1, rest2));
		return criteria.list();
	}
	
	
	/**
	 * 取得在職一般員工或是這個月離職的員工，主要用於統計出勤的UserList用
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<SysUser> getEnableRole2UserOrGraduationInMonth(Date theMonth) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		Criterion rest1 = Restrictions.and(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		
		Calendar calendarMonth = Calendar.getInstance();
		calendarMonth.setTime(theMonth);
		Date startDate = DateUtils.getFirstDateByYearAndMonth(calendarMonth.get(Calendar.YEAR), calendarMonth.get(Calendar.MONTH));
		Date endDate = DateUtils.getLastDateByYearAndMonth(calendarMonth.get(Calendar.YEAR), calendarMonth.get(Calendar.MONTH));
		logger.info("startDate : " + startDate);
		logger.info("endDate : " + endDate);
		Criterion rest2 = Restrictions.and(Restrictions.between("graduationDate", startDate, endDate));
		criteria.add(Restrictions.or(rest1, rest2));
		criteria.add(Restrictions.eq("roleId", "2"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, SysUser> getMapEnableUserOrGraduationInMonth(Date theMonth) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		//criteria.add(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		Criterion rest1 = Restrictions.and(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		Calendar calendarMonth = Calendar.getInstance();
		calendarMonth.setTime(theMonth);
		Date startDate = DateUtils.getFirstDateByYearAndMonth(calendarMonth.get(Calendar.YEAR), calendarMonth.get(Calendar.MONTH));
		Date endDate = DateUtils.getLastDateByYearAndMonth(calendarMonth.get(Calendar.YEAR), calendarMonth.get(Calendar.MONTH));
		logger.info("startDate : " + startDate);
		logger.info("endDate : " + endDate);
		Criterion rest2 = Restrictions.and(Restrictions.between("graduationDate", startDate, endDate));
		criteria.add(Restrictions.or(rest1, rest2));
		List<SysUser> list = criteria.list();
		Map<String, SysUser> map = new HashMap<String, SysUser>();
		for(SysUser sysUser:list) {
			map.put(sysUser.getSysUserId(), sysUser);
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public SysUser getOneByOriginalName(String originalName) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("originalName", originalName));
		List<SysUser> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public SysUser getOneByOriginalNameAndDepartment(String originalName, String department) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("originalName", originalName));
		criteria.add(Restrictions.eq("department", department));
		List<SysUser> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
	}

	public PagingList<SysUser> getListByPage(Integer page, String searchUserName) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(StringUtils.hasText(searchUserName)){
			Criterion rest1 = Restrictions.and(Restrictions.like("userName", "%"+searchUserName+"%"));
			Criterion rest2 = Restrictions.and(Restrictions.like("originalName", "%"+searchUserName+"%"));
			criteria.add(Restrictions.or(rest1, rest2));
		}
		return createPagingList(Constant.PAGE_SIZE, page, criteria, convertOrders(new String[]{"inaugurationDate desc", "status desc"}));
	}
	
	@SuppressWarnings("unchecked")
	public List<SysUser> getIntervalOfInauguration(Date startDate, Date endDate) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.between("inaugurationDate", startDate, endDate));
		criteria.add(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<SysUser> getFemale() throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("gender", Constant.GENDER_FEMALE));
		criteria.add(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public SysUser getProjectManager(String groupName) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("groupName", groupName));
		criteria.add(Restrictions.eq("projectManager", Constant.STATUS_ENABLE));
		List<SysUser> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
	}
		
	@SuppressWarnings("unchecked")
	public SysUser getDepartmentMaster() throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("departmentMaster", Constant.STATUS_ENABLE));
		List<SysUser> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public SysUser getFinanceMaster() throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("financeMaster", Constant.STATUS_ENABLE));
		List<SysUser> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public SysUser getAdministrationManager() throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("administrationManager", Constant.STATUS_ENABLE));
		List<SysUser> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public SysUser getCompanyGold() throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("companyGod", Constant.STATUS_ENABLE));
		List<SysUser> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public SysUser getByLineId(String lineId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("lineId", lineId));
		criteria.add(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		List<SysUser> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
	}
	
	//20210111 sam for punchRecord mapping
	@SuppressWarnings("unchecked")
	public SysUser getOneByCardId(String cardId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("cardId", cardId));
		List<SysUser> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public SysUser getOneBySysUserId(String sysUserId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("sysUserId", sysUserId));
		List<SysUser> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
	}
}

