package com.cqi.hr.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.GiveOfficialLeave;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;


@Repository
public class GiveOfficialLeaveDAO extends AbstractDAO<GiveOfficialLeave>{

	
	@Override
	protected Class<GiveOfficialLeave> getEntityClass() {
		return GiveOfficialLeave.class;
	}

	public PagingList<GiveOfficialLeave> getPageByUserId(Integer page) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());

		criteria.add(Restrictions.eq("status", Constant.STATUS_ENABLE));
		return createPagingList(Constant.PAGE_SIZE, page, criteria, convertOrders(new String[]{"officialLeaveStartDate DESC"}));
	}

	public boolean addGiveOfficialLeave(GiveOfficialLeave giveOfficialLeave, SysUser dataUser) throws Exception {
		GiveOfficialLeave criteria = new GiveOfficialLeave();
		criteria.setOfficialLeaveStartDate(giveOfficialLeave.getOfficialLeaveStartDate());
		criteria.setOfficialLeaveEndDate(giveOfficialLeave.getOfficialLeaveEndDate());
		List<GiveOfficialLeave> dbDataList = getByExample(criteria, "officialLeaveStartDate DESC");
		if(dbDataList.size()>0) {
			return false;
		}
		giveOfficialLeave.setCreateBy(dataUser.getSysUserId());
		giveOfficialLeave.setCreateDate(new Date());
		persist(giveOfficialLeave);
		return true;
	}
	
}
