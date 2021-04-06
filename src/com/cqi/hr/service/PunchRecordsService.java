package com.cqi.hr.service;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.text.StyleConstants.ColorConstants;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.AttendanceRecordDAO;
import com.cqi.hr.dao.PunchRecordsDAO;
import com.cqi.hr.dao.SysUserDAO;
import com.cqi.hr.entity.AttendanceRecord;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.PunchRecords;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.util.DateUtils;
import com.cqi.hr.vo.ResponseVo;

@Service
public class PunchRecordsService extends AbstractService<PunchRecords>{
	@Resource PunchRecordsDAO punchRecordsDAO;
	@Resource SysUserDAO sysUserDAO;
	@Resource AttendanceRecordDAO attendanceRecordDAO;
	
	@Override
	protected AbstractDAO<PunchRecords> getDAO() {
		return punchRecordsDAO;
	}
	
	@Transactional
	public PagingList<PunchRecords> getListByPage(Integer page) throws Exception {
		return punchRecordsDAO.getListByPage(page);
	}

	@Transactional
	public void savePunchRecordsAndCreateAttendanceRecord(String cardID, Long time) throws Exception {
		SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		
		//Step 1 save Punch Record
		Date PunchTime = new Date(time*1000);
		PunchRecords punchRecord = new PunchRecords(cardID, PunchTime, new Date(), new Date(), "1");
		punchRecordsDAO.saveOrUpdate(punchRecord);
		
		//Step 2 Create or Update AttendanceRecord
		SysUser sUser = sysUserDAO.getOneByCardId(cardID);
		if (sUser != null) {
			AttendanceRecord attendanceRecord = attendanceRecordDAO.getOneByUserIdAndDate(sUser.getSysUserId(), DateUtils.clearTime(PunchTime)); 
			if(attendanceRecord == null) {
				attendanceRecord = new AttendanceRecord();
				attendanceRecord.setSysUserId(sUser.getSysUserId()); 
				attendanceRecord.setAttendanceDate(DateUtils.clearTime(PunchTime));
				attendanceRecord.setArriveTime(sdfTime.format(PunchTime));
				attendanceRecord.setOriginalData(sdfTime.format(PunchTime));
				attendanceRecord.setStatus(1);
				attendanceRecord.setCreateDate(new Date());
				
			}else {
				Date arriveTimeDate = sdfTime.parse(attendanceRecord.getArriveTime());
				arriveTimeDate.setYear(PunchTime.getYear());
				arriveTimeDate.setMonth(PunchTime.getMonth());
				arriveTimeDate.setDate(PunchTime.getDate());
				Date leaveTimeDate = (attendanceRecord.getLeaveTime() == null || attendanceRecord.getLeaveTime().isEmpty() ) ? arriveTimeDate : sdfTime.parse(attendanceRecord.getLeaveTime());
				leaveTimeDate.setYear(PunchTime.getYear());
				leaveTimeDate.setMonth(PunchTime.getMonth());
				leaveTimeDate.setDate(PunchTime.getDate());
				
				if(PunchTime.before(arriveTimeDate)) {
					attendanceRecord.setLeaveTime(attendanceRecord.getArriveTime());
					attendanceRecord.setArriveTime(sdfTime.format(PunchTime));
				}else if(PunchTime.after(leaveTimeDate)) {
					attendanceRecord.setLeaveTime(sdfTime.format(PunchTime));
				}
				attendanceRecord.setOriginalData(attendanceRecord.getOriginalData()+";"+sdfTime.format(PunchTime));
				attendanceRecord.setUpdateDate(new Date());
			}
			attendanceRecordDAO.saveOrUpdate(attendanceRecord);
		}
		System.out.println("end savePunchRecordsAndCreateAttendanceRecord");
	}
}
