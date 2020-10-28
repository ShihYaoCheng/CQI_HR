package com.cqi.hr.dao;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.PunchRecords;

@Repository
public class PunchRecordsDAO extends AbstractDAO<PunchRecords> {
	@Override
	protected Class<PunchRecords> getEntityClass() {
		return PunchRecords.class;
	}
	
	public PagingList<PunchRecords> getListByPage(Integer page) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		return createPagingList(Constant.PAGE_SIZE, page, criteria, convertOrders(new String[]{"time desc"}));
	}

}

