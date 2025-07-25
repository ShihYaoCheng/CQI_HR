package com.cqi.hr.dao;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
import com.cqi.hr.entity.SysUserAbsence;
import com.cqi.hr.util.DateUtils;

@Repository
public class SysUserDAO extends AbstractDAO<SysUser> {
	@Override
	protected Class<SysUser> getEntityClass() {
		return SysUser.class;
	}

	@SuppressWarnings("unchecked")
	public List<SysUser> getEnableUser() throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<SysUser> getEnableUserOrderByDesc(String propertyName) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		criteria.addOrder(Order.desc(propertyName));
		criteria.addOrder(Order.desc("createDate"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<SysUser> getEnableRole2User() throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		criteria.add(Restrictions.eq("roleId", "2"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<SysUser> getEnableRole2UserStatus() throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		Criterion rest1 = Restrictions.and(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		Criterion rest2 = Restrictions.and(Restrictions.eq("status", Constant.SYSUSER_leave_of_absence));
		criteria.add(Restrictions.or(rest1, rest2));
		criteria.add(Restrictions.eq("roleId", "2"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<SysUser> getEnableRole2LineUser() throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		criteria.add(Restrictions.eq("roleId", "2"));
		criteria.add(Restrictions.isNotNull("lineId"));
		criteria.add(Restrictions.ne("lineId", ""));
		return criteria.list();
	}

	/**
	 * 取得在職員工或是這個月離職的員工，主要用於統計出勤的UserList用
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<SysUser> getEnableUserOrGraduationInMonth(Date theMonth) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		// criteria.add(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		Criterion rest1 = Restrictions.and(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		Calendar calendarMonth = Calendar.getInstance();
		calendarMonth.setTime(theMonth);
		Date startDate = DateUtils.getFirstDateByYearAndMonth(calendarMonth.get(Calendar.YEAR),
				calendarMonth.get(Calendar.MONTH));
		Date endDate = DateUtils.getLastDateByYearAndMonth(calendarMonth.get(Calendar.YEAR),
				calendarMonth.get(Calendar.MONTH));
		logger.info("startDate : " + startDate);
		logger.info("endDate : " + endDate);
		Criterion rest2 = Restrictions.and(Restrictions.between("graduationDate", startDate, endDate));
		criteria.add(Restrictions.or(rest1, rest2));
		return criteria.list();
	}

	/**
	 * 取得在職一般員工或是這個月離職的員工，主要用於統計出勤的UserList用
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<SysUser> getEnableRole2UserOrGraduationInMonth(Date theMonth) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		Criterion rest1 = Restrictions.and(Restrictions.eq("status", Constant.SYSUSER_ENABLE));

		Calendar calendarMonth = Calendar.getInstance();
		calendarMonth.setTime(theMonth);
		Date startDate = DateUtils.getFirstDateByYearAndMonth(calendarMonth.get(Calendar.YEAR),
				calendarMonth.get(Calendar.MONTH));
		Date endDate = DateUtils.getLastDateByYearAndMonth(calendarMonth.get(Calendar.YEAR),
				calendarMonth.get(Calendar.MONTH));
		logger.info("startDate : " + startDate);
		logger.info("endDate : " + endDate);
		Criterion rest2 = Restrictions.and(Restrictions.between("graduationDate", startDate, endDate));
		criteria.add(Restrictions.or(rest1, rest2));
		criteria.add(Restrictions.eq("roleId", "2"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public Map<String, SysUser> getMapEnableUserOrGraduationInMonth(Date theMonth) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		// criteria.add(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		Criterion rest1 = Restrictions.and(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		Calendar calendarMonth = Calendar.getInstance();
		calendarMonth.setTime(theMonth);
		Date startDate = DateUtils.getFirstDateByYearAndMonth(calendarMonth.get(Calendar.YEAR),
				calendarMonth.get(Calendar.MONTH));
		Date endDate = DateUtils.getLastDateByYearAndMonth(calendarMonth.get(Calendar.YEAR),
				calendarMonth.get(Calendar.MONTH));
		logger.info("startDate : " + startDate);
		logger.info("endDate : " + endDate);
		Criterion rest2 = Restrictions.and(Restrictions.between("graduationDate", startDate, endDate));
		criteria.add(Restrictions.or(rest1, rest2));
		List<SysUser> list = criteria.list();
		Map<String, SysUser> map = new HashMap<String, SysUser>();
		for (SysUser sysUser : list) {
			map.put(sysUser.getSysUserId(), sysUser);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public SysUser getOneByOriginalName(String originalName) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("originalName", originalName));
		List<SysUser> list = criteria.list();
		if (list.size() == 1) {
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
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	public PagingList<SysUser> getListByPage(Integer page, String searchUserName) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if (StringUtils.hasText(searchUserName)) {
			Criterion rest1 = Restrictions.and(Restrictions.like("userName", "%" + searchUserName + "%"));
			Criterion rest2 = Restrictions.and(Restrictions.like("originalName", "%" + searchUserName + "%"));
			criteria.add(Restrictions.or(rest1, rest2));
		}
		return createPagingList(Constant.PAGE_SIZE, page, criteria,
				convertOrders(new String[] { "inaugurationDate desc", "sysUserId desc" }));
	}

	/*
	 * 取得在職清單(在職，留職停薪)
	 */
	public PagingList<SysUser> getSysUserStatusListByPage(Integer page, String searchUserName) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if (StringUtils.hasText(searchUserName)) {
			Criterion rest1 = Restrictions.and(Restrictions.like("userName", "%" + searchUserName + "%"));
			Criterion rest2 = Restrictions.and(Restrictions.like("originalName", "%" + searchUserName + "%"));
			criteria.add(Restrictions.or(rest1, rest2));
		}
		criteria.add(Restrictions.not(Restrictions.eq("status", Constant.SYSUSER_DISABLE)));
		criteria.add(Restrictions.eq("roleId", "2"));
		return createPagingList(Constant.PAGE_SIZE, page, criteria,
				convertOrders(new String[] { "inaugurationDate desc", "sysUserId desc" }));
	}

	@SuppressWarnings("unchecked")
	public List<SysUser> getIntervalOfInauguration(Date startDate, Date endDate) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.between("inaugurationDate", startDate, endDate));
		criteria.add(Restrictions.eq("status", Constant.SYSUSER_ENABLE));
		return criteria.list();
	}

	/*
	 * 檢查 公假是否符合堆定
	 */
	@SuppressWarnings("unchecked")
	public List<SysUser> getIntervalOfInaugurationByDay(Integer minDayCount, Integer maxDayCount) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		// criteria.add(Restrictions.between("inaugurationDate", startDate, endDate));
		criteria.add(Restrictions.eq("status", Constant.SYSUSER_ENABLE));

		criteria.add(Restrictions.isNotNull("inaugurationDate"));

		List<SysUser> dataList = criteria.list();
		List<SysUser> result = new ArrayList<SysUser>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		Calendar calendarStart = Calendar.getInstance();
		// calendarStart.setTime(DateUtils.getTodayWithoutHourMinSec());
		for (SysUser item : dataList) {
			// LocalDate dateBefore = LocalDate.parse(item.getInaugurationDate().getTime() ,
			// formatter);

			//入職時間
			LocalDate dateBefore = item.getInaugurationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			dateBefore.format(formatter);
			//今天日期
			LocalDate dateAfter = calendarStart.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			dateAfter.format(formatter);
			long onTheJobDays = ChronoUnit.DAYS.between(dateBefore, dateAfter);

			// 抓取是否 有 留職停薪資料
			Criteria criteriaA = sessionFactory.getCurrentSession().createCriteria(SysUserAbsence.class);
			criteriaA.add(Restrictions.eq("sysUserId", item.getSysUserId()));
			List<SysUserAbsence> SysUserAbsenceList = criteriaA.list();
			// 減掉 留職停薪 期間日期
			for (SysUserAbsence rd : SysUserAbsenceList) {
				// LocalDate dateBeforeA = LocalDate.parse(rd.getEffectiveDate().toString() ,
				// formatter);
				LocalDate dateBeforeA = rd.getEffectiveDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				dateBeforeA.format(formatter);
				// LocalDate dateAfterA = LocalDate.parse( rd.getExpirationDate().toString(),
				// formatter);
				LocalDate dateAfterA = rd.getExpirationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				dateAfterA.format(formatter);
				onTheJobDays = -ChronoUnit.DAYS.between(dateBeforeA, dateAfterA);
			}
			/* 符合 在職天數區間資料 */

			if (minDayCount <= onTheJobDays && onTheJobDays <= maxDayCount) {
				result.add(item);
			}

		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<SysUser> getFemale() throws Exception {
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
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public SysUser getDepartmentMaster() throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("departmentMaster", Constant.STATUS_ENABLE));
		List<SysUser> list = criteria.list();
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public SysUser getFinanceMaster() throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("financeMaster", Constant.STATUS_ENABLE));
		List<SysUser> list = criteria.list();
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public SysUser getAdministrationManager() throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("administrationManager", Constant.STATUS_ENABLE));
		List<SysUser> list = criteria.list();
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public SysUser getCompanyGold() throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("companyGod", Constant.STATUS_ENABLE));
		List<SysUser> list = criteria.list();
		if (list.size() == 1) {
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
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	// 20210111 sam for punchRecord mapping
	@SuppressWarnings("unchecked")
	public SysUser getOneByCardId(String cardId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("cardId", cardId));
		List<SysUser> list = criteria.list();
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public SysUser getOneBySysUserId(String sysUserId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("sysUserId", sysUserId));
		List<SysUser> list = criteria.list();
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}
}
