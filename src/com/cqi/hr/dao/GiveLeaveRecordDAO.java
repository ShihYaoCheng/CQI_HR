package com.cqi.hr.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.GiveLeaveRecord;

@Repository
public class GiveLeaveRecordDAO extends AbstractDAO<GiveLeaveRecord> {
	@Override
	protected Class<GiveLeaveRecord> getEntityClass() {
		return GiveLeaveRecord.class;
	}
	
	@SuppressWarnings("unchecked")
	public GiveLeaveRecord getWithUserAndRule(String userId, Long ruleId, Long leaveId) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("sysUserId", userId));
		criteria.add(Restrictions.eq("ruleId", ruleId));
		criteria.add(Restrictions.eq("leaveId", leaveId));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		List<GiveLeaveRecord> dataList = criteria.list();
		if(dataList!=null && dataList.size()==1) {
			return dataList.get(0);
		}
		return null;
	}

}

