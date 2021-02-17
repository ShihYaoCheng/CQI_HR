package com.cqi.hr.service;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.SysUserDAO;
import com.cqi.hr.dao.SysUserShiftDAO;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.SysUserShift;
import com.cqi.hr.util.DateUtils;

@Service
@Transactional
public class SysUserShiftService extends AbstractService<SysUserShift>{
	@Resource SysUserShiftDAO sysUserShiftDAO;
	@Resource SysUserDAO sysUserDAO;
	
	@Override
	protected AbstractDAO<SysUserShift> getDAO() {
		return sysUserShiftDAO;
	}
	
	public String addShift(SysUserShift sysUserShift) throws Exception{
		SysUserShift criteria = new SysUserShift();
		criteria.setSysUserId(sysUserShift.getSysUserId());
		criteria.setEnableMonth(sysUserShift.getEnableMonth());
		List<SysUserShift> dbDataList = getDAO().getByExample(criteria, "enableMonth desc");
		if(dbDataList.size()>0) {
			return Constant.DATA_DUPLICATED;
		}
		
		getDAO().persist(sysUserShift);
		return "";
	}
	
	public String updateShift(SysUserShift sysUserShift) throws Exception{
		SysUserShift criteria = new SysUserShift();
		criteria.setShiftId(sysUserShift.getShiftId());
		SysUserShift dbData = getDAO().get(sysUserShift.getShiftId());
		if(dbData==null || !dbData.getShiftId().equals(sysUserShift.getShiftId())) {
			return Constant.RECORD_NOT_EXIST;
		}
		if(dbData.getEnableMonth().compareTo(sysUserShift.getEnableMonth())!=0) {
			SysUserShift criteriaDate = new SysUserShift();
			criteriaDate.setSysUserId(sysUserShift.getSysUserId());
			criteriaDate.setEnableMonth(sysUserShift.getEnableMonth());
			List<SysUserShift> dbDataDateList = getDAO().getByExample(criteriaDate, "enableMonth desc");
			if(dbDataDateList.size()>0) {
				for(SysUserShift dateData:dbDataDateList) {
					if(!dateData.getShiftId().equals(sysUserShift.getShiftId())) {
						return Constant.DATA_DUPLICATED;
					}
				}
			}
		}
		dbData.setBoardTime(sysUserShift.getBoardTime());
		dbData.setFinishTime(sysUserShift.getFinishTime());
		dbData.setEnableMonth(sysUserShift.getEnableMonth());
		dbData.setUpdateDate(new Date());
		getDAO().update(dbData);
		return "";
	}
	
	public SysUserShift getByUserIdAndDate(SysUser sysUser, Date queryMonth) throws Exception {
		SysUserShift sysUserShift = new SysUserShift();
		sysUserShift.setSysUserId(sysUser.getSysUserId());
		sysUserShift.setEnableMonth(queryMonth);
		List<SysUserShift> dataList = sysUserShiftDAO.getByExample(sysUserShift, "enableMonth desc");
		if(dataList.size()==1) {
			return dataList.get(0);
		}
		return null;
	}
	
	public PagingList<SysUserShift> getList(Integer page, SysUser sysUser) throws Exception{
		return sysUserShiftDAO.getPageByUserId(page, sysUser);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, SysUserShift> getMapThisMonth() throws Exception{
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("enableMonth", DateUtils.getFirstDateOfThisMonth());
		return (Map<String, SysUserShift>) getDAO().queryToMap("sysUserId", criteria);
	}
	
	
	
	public void checkThisMonthShift() throws Exception {
		SysUser criteria = new SysUser();
		criteria.setRoleId("2");
		criteria.setStatus(Constant.SYSUSER_ENABLE);
		//標準化契約員工
		List<SysUser> employeeList = sysUserDAO.getByExample(criteria, "sysUserId desc");
		Date thisMonthFirst = DateUtils.getFirstDateOfThisMonth();
		Date lastMonthFirst = DateUtils.getFirstDateOfLastMonth();
		Calendar now = Calendar.getInstance();
		for(SysUser employee: employeeList) {
			if(employee.getUserName().equals("JZ")) {
				continue;
			}
			//逐一掃
			logger.info("Employee : " + employee.getUserName());
			SysUserShift shiftCriteria = new SysUserShift();
			shiftCriteria.setSysUserId(employee.getSysUserId());
			shiftCriteria.setEnableMonth(thisMonthFirst);
			List<SysUserShift> thisMonthShiftList = sysUserShiftDAO.getByExample(shiftCriteria, "enableMonth desc");
			if(thisMonthShiftList.size()==0) {
				//沒有資料，要新增
				SysUserShift shiftLastCriteria = new SysUserShift();
				shiftLastCriteria.setSysUserId(employee.getSysUserId());
				shiftLastCriteria.setEnableMonth(lastMonthFirst);
				List<SysUserShift> lastMonthShiftList = sysUserShiftDAO.getByExample(shiftLastCriteria, "enableMonth desc");
				if(lastMonthShiftList.size()>0) {
					SysUserShift newShift = new SysUserShift();
					newShift.setSysUserId(employee.getSysUserId());
					newShift.setBoardTime(lastMonthShiftList.get(0).getBoardTime());
					newShift.setFinishTime(lastMonthShiftList.get(0).getFinishTime());
					newShift.setCreateDate(now.getTime());
					newShift.setEnableMonth(thisMonthFirst);
					newShift.setUpdateDate(now.getTime());
					newShift.setStatus(Constant.STATUS_ENABLE);
					sysUserShiftDAO.persist(newShift);
				}else {
					// 新進人員會沒資料
					SysUserShift newShift = new SysUserShift();
					newShift.setSysUserId(employee.getSysUserId());
					newShift.setBoardTime("09:00");
					newShift.setFinishTime("18:00");
					newShift.setCreateDate(now.getTime());
					newShift.setEnableMonth(thisMonthFirst);
					newShift.setUpdateDate(now.getTime());
					newShift.setStatus(Constant.STATUS_ENABLE);
					sysUserShiftDAO.persist(newShift);
				}
			}else if(thisMonthShiftList.size()>1) {
				// TODO: 怎麼可能
				logger.error("這個月的資料有兩個");
			}else {
				//有資料不管它
				logger.info("這個月有資料");
			}
		}
	}
	
	//add by sam 20201222
	@SuppressWarnings("unchecked")
	public Map<String, SysUserShift> getMapLastMonth() throws Exception{
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("enableMonth", DateUtils.getFirstDateOfLastMonth());
		return (Map<String, SysUserShift>) getDAO().queryToMap("sysUserId", criteria);
	}
	
	//add by sam 20210201
	@SuppressWarnings({ "unchecked", "deprecation" })
	public Map<String, SysUserShift> getMapByDate(Date date) throws Exception{
		Map<String, Object> criteria = new HashMap<String, Object>();
		Date enableDate = DateUtils.clearTime(date);
		enableDate.setDate(1);
		criteria.put("enableMonth", enableDate);
		return (Map<String, SysUserShift>) getDAO().queryToMap("sysUserId", criteria);
		
	}
	
	

	//add by sam 20201225
	@SuppressWarnings("unchecked")
	public Map<String, SysUserShift> getMapByYearAndMonth(Integer year, Integer month) throws Exception{
		Map<String, Object> criteria = new HashMap<String, Object>();
		Date enableDate = DateUtils.getFirstDateByYearAndMonth(year, month-1);
		criteria.put("enableMonth", enableDate);
		return (Map<String, SysUserShift>) getDAO().queryToMap("sysUserId", criteria);
	}
}
