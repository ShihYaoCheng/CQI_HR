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
import com.cqi.hr.entity.AttendanceRecord;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.UserAskForOvertime;
import com.cqi.hr.util.DateUtils;

@Repository
public class UserAskForOvertimeDAO extends AbstractDAO<UserAskForOvertime> {
	@Override
	protected Class<UserAskForOvertime> getEntityClass() {
		return UserAskForOvertime.class;
	}

	public PagingList<UserAskForOvertime> getListByPage(Integer page, String userId) throws Exception {
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
	public List<UserAskForOvertime> getSumListGroupByNow(String userId) throws Exception{
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
		projList.add(Projections.groupProperty("overtimeId").as("overtimeId"));

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
	public List<UserAskForOvertime> getMonthlyData(Date start, Date end, String userId) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(StringUtils.hasText(userId)){
			criteria.add(Restrictions.eq("sysUserId", userId));
		}
		criteria.add(Restrictions.between("startTime", start, end));
		criteria.add(Restrictions.between("endTime", start, end));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<UserAskForOvertime> findByIdAndCreateTime(Date start, Date end, Long overtimeId) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.between("createDate", start, end));
		criteria.add(Restrictions.eq("overtimeId", overtimeId));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		return criteria.list();
	}
	
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
		projList.add(Projections.groupProperty("overtimeId"));

		//add other fields you need in the projection list
		criteria.setProjection(projList);
		return criteria.list();
	}
	@SuppressWarnings("unchecked")
	public List<Object[]> getLastMonthlyRowdata(Integer year, Integer month, String userId, Long leaveId) throws Exception{
		Date startDayOfMonth = DateUtils.getFirstDateByYearAndMonth(year, month-1);
		Date endDayTimeOfMonth = DateUtils.getLastDateByYearAndMonth(year, month-1);

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
		projList.add(Projections.groupProperty("overtimeId"));

		//add other fields you need in the projection list
		criteria.setProjection(projList);
		return criteria.list();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public Double getAfterDataByMonth(String userId, Long overtimeId) throws Exception{
		Date firstMonthDay = DateUtils.getFirstDateOfThisMonth();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(StringUtils.hasText(userId)){
			criteria.add(Restrictions.eq("sysUserId", userId));
		}
		criteria.add(Restrictions.ge("startTime", firstMonthDay));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		criteria.add(Restrictions.eq("overtimeId", overtimeId));
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
	public List<UserAskForOvertime> getTodayOvertime(){
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
	public List<UserAskForOvertime> checkTimeOverCross(UserAskForOvertime data){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		criteria.add(Restrictions.ge("endTime", data.getStartTime()));
		criteria.add(Restrictions.le("startTime", data.getEndTime()));
		criteria.add(Restrictions.eq("sysUserId", data.getSysUserId()));
		return criteria.list();
	}

	//add by sam 20201223
	@SuppressWarnings("unchecked")
	public UserAskForOvertime getOneByUserIdAndDate(String sysUserId, Date date) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		criteria.add(Restrictions.eq("sysUserId", sysUserId));
		
		Calendar thisDate = Calendar.getInstance();
		Calendar nextDate = Calendar.getInstance();
		thisDate.setTime(date);
		nextDate.setTime(DateUtils.nextDate(date));
		Criterion rest1 = Restrictions.and(Restrictions.le("startTime", DateUtils.clearTime(thisDate.getTime())), 
				Restrictions.ge("endTime", DateUtils.clearTime(nextDate.getTime())));
		Criterion rest2 = Restrictions.or(Restrictions.between("startTime", DateUtils.clearTime(thisDate.getTime()), DateUtils.clearTime(nextDate.getTime())), 
				Restrictions.between("endTime", DateUtils.clearTime(thisDate.getTime()), DateUtils.clearTime(nextDate.getTime())));
		criteria.add(Restrictions.or(rest1, rest2));
		
		List<UserAskForOvertime> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
	}
}

