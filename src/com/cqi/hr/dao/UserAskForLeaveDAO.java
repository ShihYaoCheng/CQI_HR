package com.cqi.hr.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.util.DateUtils;

@Repository
public class UserAskForLeaveDAO extends AbstractDAO<UserAskForLeave> {
	@Override
	protected Class<UserAskForLeave> getEntityClass() {
		return UserAskForLeave.class;
	}

	public PagingList<UserAskForLeave> getListByPage(Integer page, String userId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(StringUtils.hasText(userId)){
			criteria.add(Restrictions.eq("sysUserId", userId));
		}
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		return createPagingList(Constant.PAGE_SIZE, page, criteria, convertOrders(new String[]{"startTime DESC", "endTime DESC"}));
	}
	
	/**
	 * Get the Sum of count in this month
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<UserAskForLeave> getSumListGroupByNow(String userId) throws Exception{
		logger.info("getSumListGroupByNow");
		Date startDayOfMonth = DateUtils.getFirstDateOfThisMonth();
		Calendar calendarEnd = Calendar.getInstance();
		calendarEnd.setTime(startDayOfMonth);
		calendarEnd.add(Calendar.MONTH, 1);
		logger.info("startDayOfMonth : " + startDayOfMonth);
		logger.info("calendarEnd : " + calendarEnd.getTime());
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(StringUtils.hasText(userId)){
			criteria.add(Restrictions.eq("sysUserId", userId));
		}
		criteria.add(Restrictions.between("startTime", startDayOfMonth, calendarEnd.getTime()));
		criteria.add(Restrictions.between("endTime", startDayOfMonth, calendarEnd.getTime()));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.sum("spendTime").as("spendTime"));
		projList.add(Projections.groupProperty("sysUserId"));
		projList.add(Projections.groupProperty("leaveId"));

		//add other fields you need in the projection list
		criteria.setProjection(projList);
		return criteria.list();
	}
	
	/**
	 * For Calendar
	 * @param start
	 * @param end
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<UserAskForLeave> getMonthlyData(Date start, Date end, String userId) throws Exception{
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(StringUtils.hasText(userId)){
			criteria.add(Restrictions.eq("sysUserId", userId));
		}
		criteria.add(Restrictions.between("startTime", start, end));
		criteria.add(Restrictions.between("endTime", start, end));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		return criteria.list();
	}
	
	/**
	 * Get monthly row data
	 * @param year
	 * @param month
	 * @param userId
	 * @param leaveId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getMonthlyRowdata(Integer year, Integer month, String userId, Long leaveId) throws Exception{
		Date startDayOfMonth = DateUtils.getFirstDateByYearAndMonth(year, month);
		Date endDayTimeOfMonth = DateUtils.getLastDateByYearAndMonth(year, month);

		logger.info("startDayOfMonth : " + startDayOfMonth);
		logger.info("endDayTimeOfMonth : " + endDayTimeOfMonth);
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(StringUtils.hasText(userId)){
			criteria.add(Restrictions.eq("sysUserId", userId));
		}
		//criteria.add(Restrictions.eq("leaveId", leaveId));
		criteria.add(Restrictions.between("startTime", startDayOfMonth, endDayTimeOfMonth));
		criteria.add(Restrictions.between("endTime", startDayOfMonth, endDayTimeOfMonth));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.sum("spendTime"));
		projList.add(Projections.groupProperty("sysUserId"));
		projList.add(Projections.groupProperty("leaveId"));

		//add other fields you need in the projection list
		criteria.setProjection(projList);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public Double getAfterDataByMonth(String userId, Long leaveId) throws Exception{
		Date firstMonthDay = DateUtils.getFirstDateOfThisMonth();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(StringUtils.hasText(userId)){
			criteria.add(Restrictions.eq("sysUserId", userId));
		}
		criteria.add(Restrictions.ge("startTime", firstMonthDay));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		criteria.add(Restrictions.eq("leaveId", leaveId));
		criteria.setProjection(Projections.sum("spendTime"));
		List<Double> queryData = criteria.list();
		if(queryData.size()>0) {
			if(queryData.get(0)!=null) {
				return queryData.get(0);
			}
		}
		return 0.0;
	}
	
	@SuppressWarnings("unchecked")
	public boolean checkTodayHasData(String userId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("sysUserId", userId));
		Calendar todayCalendar = Calendar.getInstance();
		todayCalendar.set(Calendar.HOUR_OF_DAY, 10);
		todayCalendar.set(Calendar.MINUTE, 30);
		criteria.add(Restrictions.le("startTime", todayCalendar.getTime()));
		criteria.add(Restrictions.ge("endTime", todayCalendar.getTime()));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		List<UserAskForLeave> list = criteria.list();
		if(list.size()>0) {
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserAskForLeave> getTodayLeave(){
		Calendar today = Calendar.getInstance();
		Calendar tommorrow = Calendar.getInstance();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));

		tommorrow.add(Calendar.DAY_OF_YEAR, 1);
		Criterion rest1 = Restrictions.and(Restrictions.le("startTime", DateUtils.clearTime(today.getTime())), 
				Restrictions.ge("endTime", DateUtils.clearTime(tommorrow.getTime())));
		Criterion rest2 = Restrictions.or(Restrictions.between("startTime", DateUtils.clearTime(today.getTime()), DateUtils.clearTime(tommorrow.getTime())), 
				Restrictions.between("endTime", DateUtils.clearTime(today.getTime()), DateUtils.clearTime(tommorrow.getTime())));
		criteria.add(Restrictions.or(rest1, rest2));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<UserAskForLeave> checkTimeOverCross(UserAskForLeave data){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		criteria.add(Restrictions.gt("endTime", data.getStartTime()));
		criteria.add(Restrictions.lt("startTime", data.getEndTime()));
		criteria.add(Restrictions.eq("sysUserId", data.getSysUserId()));
		return criteria.list();
	}
	
	//20201223 add by sam
	@SuppressWarnings("unchecked")
	public UserAskForLeave getOneByUserIdAndDate (String userId, Date leaveDate) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("sysUserId", userId));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		
		Calendar thisDate = Calendar.getInstance();
		Calendar nextDate = Calendar.getInstance();
		thisDate.setTime(leaveDate);
		nextDate.setTime(DateUtils.nextDate(leaveDate));
		Criterion rest1 = Restrictions.and(Restrictions.le("startTime", DateUtils.clearTime(thisDate.getTime())), 
				Restrictions.ge("endTime", DateUtils.clearTime(nextDate.getTime())));
		Criterion rest2 = Restrictions.or(Restrictions.between("startTime", DateUtils.clearTime(thisDate.getTime()), DateUtils.clearTime(nextDate.getTime())), 
				Restrictions.between("endTime", DateUtils.clearTime(thisDate.getTime()), DateUtils.clearTime(nextDate.getTime())));
		criteria.add(Restrictions.or(rest1, rest2));
		
		List<UserAskForLeave> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
		
	}
}

