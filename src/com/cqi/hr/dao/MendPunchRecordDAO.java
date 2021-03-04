package com.cqi.hr.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.MendPunchRecord;
import com.cqi.hr.entity.PagingList;

@Repository
public class MendPunchRecordDAO extends AbstractDAO<MendPunchRecord> {

	@Override
	protected Class<MendPunchRecord> getEntityClass() {
		return MendPunchRecord.class;
	}

	public PagingList<MendPunchRecord> getListByPage(Integer page, String userId) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(StringUtils.hasText(userId)){
			criteria.add(Restrictions.eq("sysUserId", userId));
		}
		return createPagingList(Constant.PAGE_SIZE, page, criteria, convertOrders(new String[]{"sysUserId", "mendPunchTime DESC","approvedStatus DESC"}));
	}

}
