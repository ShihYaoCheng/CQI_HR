package com.cqi.hr.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.SpecialDateAboutWorkDAO;
import com.cqi.hr.dao.WorkFromHomeDao;
import com.cqi.hr.entity.AttendanceRecord;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SpecialDateAboutWork;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.SysUserShift;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.entity.WorkFromHome;
import com.cqi.hr.util.DateUtils;
import com.cqi.hr.util.StringUtils;

@Service
@Transactional
public class WorkFromHomeService  extends AbstractService<WorkFromHome>{
	@Resource WorkFromHomeDao workFromHomeDao;
	@Resource SpecialDateAboutWorkDAO specialDateAboutWorkDAO;
	
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
	
	public String addWorkFromHome(WorkFromHome workFromHome) throws Exception{
		int level = workFromHome.getLevel();
		int days = 0;
		switch (level) {
		case 1:
			days = 3;
			break;
		case 2:
			days = 7;
			break;
		case 3:
			days = 14;
			break;
		case 4:
			days = 30;
			break;
		case 5:
			days = 1;
			break;

		default:
			return "程度不合法";
			
		}
		
		
		for (int i = 0; i < days; i++,workFromHome.setWorkDate(DateUtils.nextDate(workFromHome.getWorkDate()))) {
			SpecialDateAboutWork s = specialDateAboutWorkDAO.getOneByDate(workFromHome.getWorkDate());
			if (s != null && s.getIsWorkDay() == 0) {
				continue;
			}
			WorkFromHome tmp = new WorkFromHome(
				workFromHome.getSysUserId()	, workFromHome.getLevel(), workFromHome.getWorkDate(), workFromHome.getDescription()
				, workFromHome.getApprovalBy(), workFromHome.getStatus()
			);
			
			logger.info(workFromHome.getWorkDate());
			String errorMsg = workFromHomeDao.addWorkFromHome(tmp);
			if(StringUtils.hasText(errorMsg)) {
				return errorMsg;
			}
		}
		
		return "";
		
	}

	public boolean deleteWorkFromHome(Long workFromHomeId, SysUser dataUser) throws Exception {
		WorkFromHome workFromHome = workFromHomeDao.get(workFromHomeId);
		if (workFromHome == null) {
			return false;
		}else {
			workFromHome.setStatus(0);
			workFromHome.setModifyTime(new Date());
			workFromHome.setModifyBy(dataUser.getSysUserId());
			workFromHomeDao.update(workFromHome);
			return true;
		}
		
	}

	
}
