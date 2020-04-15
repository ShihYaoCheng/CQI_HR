package com.cqi.hr.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.AttendanceRecord;

@Repository
public class AttendanceRecordDAO extends AbstractDAO<AttendanceRecord> {
	@Override
	protected Class<AttendanceRecord> getEntityClass() {
		return AttendanceRecord.class;
	}

	@SuppressWarnings("unchecked")
	public AttendanceRecord getOneByUserIdAndDate(String userId, Date attendanceDate) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("sysUserId", userId));
		criteria.add(Restrictions.eq("attendanceDate", attendanceDate));
		List<AttendanceRecord> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<AttendanceRecord> getMonthlyData(Date start, Date end, String userId) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(StringUtils.hasText(userId)){
			criteria.add(Restrictions.eq("sysUserId", userId));
		}
		criteria.add(Restrictions.between("attendanceDate", start, end));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		return criteria.list();
	}
}

