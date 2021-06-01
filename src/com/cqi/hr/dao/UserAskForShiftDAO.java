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
import com.cqi.hr.entity.UserAskForShift;
import com.cqi.hr.util.DateUtils;

@Repository
public class UserAskForShiftDAO extends AbstractDAO<UserAskForShift>{	
	@Override
	protected Class<UserAskForShift> getEntityClass() {
		return UserAskForShift.class;
	}

	public UserAskForShift getOneByOvertimeId(Long userAskForOvertimeId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("askForOvertimeId", userAskForOvertimeId));
		List<UserAskForShift> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
	}
	
	public UserAskForShift getOneByLeaveId(Long askForLeaveId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("askForLeaveId", askForLeaveId));
		List<UserAskForShift> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
	}
	
}
