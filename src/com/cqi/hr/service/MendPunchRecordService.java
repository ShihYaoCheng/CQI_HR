package com.cqi.hr.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.AttendanceRecordDAO;
import com.cqi.hr.dao.DailyAttendanceRecordDAO;
import com.cqi.hr.dao.MendPunchRecordDAO;
import com.cqi.hr.dao.SysUserDAO;
import com.cqi.hr.entity.AttendanceRecord;
import com.cqi.hr.entity.DailyAttendanceRecord;
import com.cqi.hr.entity.MendPunchRecord;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.util.DateUtils;

@Service
public class MendPunchRecordService extends AbstractService<MendPunchRecord>{
	@Resource MendPunchRecordDAO mendPunchRecordDAO ;
	@Resource DailyAttendanceRecordDAO dailyAttendanceRecordDAO;
	@Resource AttendanceRecordDAO attendanceRecordDAO;
	@Resource SysUserDAO sysUserDAO;
	
	@Resource DailyAttendanceRecordService dailyAttendanceRecordService;
	@Resource AttendanceRecordService attendanceRecordService;
	
	@Override
	protected AbstractDAO<MendPunchRecord> getDAO() {
		return mendPunchRecordDAO;
	}

	@Transactional
	public PagingList<MendPunchRecord> getListByPage(Integer page, String userId) throws Exception {
		return mendPunchRecordDAO.getListByPage(page, userId);
	}

	//新增補卡申請紀錄
	@Transactional
	public boolean addMendPunchRecord(MendPunchRecord mendPunchRecord, SysUser operator) throws Exception {
		//一般使用者只能新增自己資料
		if(operator.getRoleId().equals("2") 
				&& !(mendPunchRecord.getSysUserId().equals(operator.getSysUserId()))) {
			return false;
		}
		
		mendPunchRecord.setApprovedStatus("P");
		mendPunchRecord.setCreateDate(new Date());
		mendPunchRecordDAO.persist(mendPunchRecord);
		//mendPunchRecordDAO.saveOrUpdate(mendPunchRecord);
		return true;
	}

	//刪除(取消)補卡申請紀錄
	@Transactional
	public boolean deleteMendPunchRecord(Long mendId, SysUser operator) throws Exception {
		MendPunchRecord mendPunchRecord = mendPunchRecordDAO.get(mendId);
		if (mendPunchRecord == null) {
			return false;
		}
		
		//一般使用者只能刪除自己資料
		if(operator.getRoleId().equals("2") && !mendPunchRecord.getSysUserId().equals(operator.getSysUserId())) {
			return false;
		}else {
			if (operator.getRoleId().equals("1")) {
				mendPunchRecord.setApprovedStatus("N");
			}else if (operator.getRoleId().equals("2")) {
				mendPunchRecord.setApprovedStatus("0");
			}
			
			
			mendPunchRecord.setModifyDate(new Date());
			mendPunchRecord.setModifyUser(operator.getSysUserId());
			mendPunchRecordDAO.update(mendPunchRecord);
			
			return true;
		}
	}

	@SuppressWarnings("deprecation")
	@Transactional
	public boolean proveMendPunchRecord(Long mendId, SysUser operator) throws Exception {
		MendPunchRecord mendPunchRecord = mendPunchRecordDAO.get(mendId);
		if (mendPunchRecord == null) {
			return false;
		}
		if(operator.getRoleId().equals("1") && mendPunchRecord.getApprovedStatus().equals("P") ) {
			mendPunchRecord.setApprovedStatus("Y");
			mendPunchRecord.setModifyDate(new Date());
			mendPunchRecord.setModifyUser(operator.getSysUserId());
			mendPunchRecordDAO.update(mendPunchRecord);
			
			
			SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
			SysUser mendUser = sysUserDAO.getOneBySysUserId(mendPunchRecord.getSysUserId());
			Date mendDate = DateUtils.clearTime(mendPunchRecord.getMendPunchTime());
			//update attendance record
			AttendanceRecord attendanceRecord =  attendanceRecordDAO.getOneByUserIdAndDate(mendUser.getSysUserId(), mendDate);
			if (attendanceRecord == null) {
				attendanceRecord = new AttendanceRecord();
				attendanceRecord.setSysUserId(mendUser.getSysUserId());
				attendanceRecord.setAttendanceDate(mendDate);
				attendanceRecord.setArriveTime(sdfTime.format(mendPunchRecord.getMendPunchTime()));
				attendanceRecord.setOriginalData(sdfTime.format(mendPunchRecord.getMendPunchTime()));
				attendanceRecord.setStatus(1);
				attendanceRecord.setCreateDate(new Date());
			}else {
				Date arriveTimeDate = sdfTime.parse(attendanceRecord.getArriveTime());
				arriveTimeDate.setYear(mendDate.getYear());
				arriveTimeDate.setMonth(mendDate.getMonth());
				arriveTimeDate.setDate(mendDate.getDate());
				Date leaveTimeDate = (attendanceRecord.getLeaveTime() == null || attendanceRecord.getLeaveTime().isEmpty() ) ? arriveTimeDate : sdfTime.parse(attendanceRecord.getLeaveTime());
				leaveTimeDate.setYear(mendDate.getYear());
				leaveTimeDate.setMonth(mendDate.getMonth());
				leaveTimeDate.setDate(mendDate.getDate());
				if(mendPunchRecord.getMendPunchTime().before(arriveTimeDate)) {
					attendanceRecord.setLeaveTime(attendanceRecord.getArriveTime());
					attendanceRecord.setArriveTime(sdfTime.format(mendPunchRecord.getMendPunchTime()));
				}else if(mendPunchRecord.getMendPunchTime().after(leaveTimeDate)) {
					attendanceRecord.setLeaveTime(sdfTime.format(mendPunchRecord.getMendPunchTime()));
				}
				attendanceRecord.setOriginalData(attendanceRecord.getOriginalData()+";"+sdfTime.format(mendPunchRecord.getMendPunchTime()));
				attendanceRecord.setUpdateDate(new Date());
				
			}
			attendanceRecordDAO.saveOrUpdate(attendanceRecord);
			
			//update dailyAttendanceRecord
			DailyAttendanceRecord oldDailyAttendanceRecord = dailyAttendanceRecordDAO.getOneByUserIdAndDate(mendUser.getSysUserId(), mendDate);
			if(oldDailyAttendanceRecord != null ) {dailyAttendanceRecordDAO.delete(oldDailyAttendanceRecord);}
			DailyAttendanceRecord dailyAttendanceRecord = dailyAttendanceRecordService.calculateAttendanceRecord (mendDate ,mendUser);
			if(dailyAttendanceRecord != null ) {dailyAttendanceRecordDAO.saveOrUpdate(dailyAttendanceRecord);}
			
			
			return true;
		}else {
			return false;
		}
		
		
	}
	
}
