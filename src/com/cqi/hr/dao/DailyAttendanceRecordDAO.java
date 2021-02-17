package com.cqi.hr.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.AttendanceRecord;
import com.cqi.hr.entity.DailyAttendanceRecord;
import com.cqi.hr.entity.MonthlyReport;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.util.DateUtils;

@Repository
public class DailyAttendanceRecordDAO extends AbstractDAO<DailyAttendanceRecord> {
	@Override
	protected Class<DailyAttendanceRecord> getEntityClass() {
		return DailyAttendanceRecord.class;
	}

	@SuppressWarnings("unchecked")
	public DailyAttendanceRecord getOneByUserIdAndDate(String userId, Date attendanceDate) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("sysUserId", userId));
		criteria.add(Restrictions.eq("attendanceDate", attendanceDate));
		List<DailyAttendanceRecord> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<DailyAttendanceRecord> getMonthlyData(Date start, Date end, String userId) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(StringUtils.hasText(userId)){
			criteria.add(Restrictions.eq("sysUserId", userId));
		}
		criteria.add(Restrictions.between("attendanceDate", start, end));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<DailyAttendanceRecord> getListByYearAndMonth(Integer year, Integer month, SysUser operator) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		Date startDate = DateUtils.getFirstDateByYearAndMonth(year, month-1);
		Date endDate = DateUtils.getLastDateByYearAndMonth(year, month-1);
		criteria.add(Restrictions.between("attendanceDate", startDate, endDate));
		if(operator!=null) {
			criteria.add(Restrictions.eq("sysUserId", operator.getSysUserId()));
		}
		criteria.addOrder(Order.asc("sysUserId"));
		criteria.addOrder(Order.asc("attendanceDate"));
		List<DailyAttendanceRecord> dailyAttendanceRecordList = criteria.list();
		return dailyAttendanceRecordList;
	}
	
}

