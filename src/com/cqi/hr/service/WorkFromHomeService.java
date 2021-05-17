package com.cqi.hr.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.WorkFromHomeDao;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.SysUserShift;
import com.cqi.hr.entity.WorkFromHome;

@Service
@Transactional
public class WorkFromHomeService  extends AbstractService<WorkFromHome>{
	@Resource WorkFromHomeDao workFromHomeDao;
	
	@Override
	protected AbstractDAO<WorkFromHome> getDAO() {
		return workFromHomeDao;
	}

	public PagingList<WorkFromHome> getList(Integer page, SysUser sysUser) throws Exception{
		return workFromHomeDao.getPageByUserId(page, sysUser);
	}
}
