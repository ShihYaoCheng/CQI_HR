package com.cqi.hr.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.LineImageUrl;

@Repository
public class LineImageUrlDAO extends AbstractDAO<LineImageUrl> {
	@Override
	protected Class<LineImageUrl> getEntityClass() {
		return LineImageUrl.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<LineImageUrl> getTypeList(String type) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Restrictions.eq("imageType", type));
		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		return criteria.list();
	}
	
}

