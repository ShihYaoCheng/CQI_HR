package com.cqi.hr.service;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.PunchRecordsDAO;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.PunchRecords;
import com.cqi.hr.entity.SysUser;

@Service
public class PunchRecordsService extends AbstractService<PunchRecords>{
	@Resource PunchRecordsDAO punchRecordsDAO;
	
	@Override
	protected AbstractDAO<PunchRecords> getDAO() {
		return punchRecordsDAO;
	}
	
	@Transactional
	public PagingList<PunchRecords> getListByPage(Integer page) throws Exception {
		return punchRecordsDAO.getListByPage(page);
	}
}
