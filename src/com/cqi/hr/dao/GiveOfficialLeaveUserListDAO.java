package com.cqi.hr.dao;

import java.util.List;

import javax.annotation.Nonnull;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.entity.CompanyLeave;
import com.cqi.hr.entity.GiveOfficialLeaveUserList;;

@Repository
public class GiveOfficialLeaveUserListDAO extends AbstractDAO<GiveOfficialLeaveUserList>{

	@Override
	protected Class<GiveOfficialLeaveUserList> getEntityClass() {
		return GiveOfficialLeaveUserList.class;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<GiveOfficialLeaveUserList> getUserList(@Nonnull Long giveOfficialLeaveId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("giveOfficialLeaveId", giveOfficialLeaveId));
		return criteria.list();
	}

	

}
