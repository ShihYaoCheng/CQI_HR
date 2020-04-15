package com.cqi.hr.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SpecialDateAboutWork;

@Repository
public class SpecialDateAboutWorkDAO extends AbstractDAO<SpecialDateAboutWork> {
	@Override
	protected Class<SpecialDateAboutWork> getEntityClass() {
		return SpecialDateAboutWork.class;
	}
	
	@SuppressWarnings("unchecked")
	public SpecialDateAboutWork getOneByDate(Date theDay) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("theDay", theDay));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		List<SpecialDateAboutWork> list = criteria.list();
		if(list.size()==1) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<SpecialDateAboutWork> getListBetweenDate(Date startDate, Date endDate) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.between("theDay", startDate, endDate));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		return criteria.list();
	}

	public PagingList<SpecialDateAboutWork> getListByPage(Integer page) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		return createPagingList(Constant.PAGE_SIZE, page, criteria, convertOrders(new String[]{"theDay desc"}));
	}
	
}

