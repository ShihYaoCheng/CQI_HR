package com.cqi.hr.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.WorkFromHomeDao;
import com.cqi.hr.entity.AttendanceRecord;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.SysUserShift;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.entity.WorkFromHome;
import com.cqi.hr.util.DateUtils;

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
	
	@SuppressWarnings("unchecked")
	public Map<String, WorkFromHome> getMapToday() throws Exception{
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("workDate", DateUtils.getTodayWithoutHourMinSec());
		return (Map<String, WorkFromHome>) getDAO().queryToMap("sysUserId", criteria);
	}
}
