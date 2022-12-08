package com.cqi.hr.service;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.AttendanceRecordDAO;
import com.cqi.hr.dao.CompanyLeaveDAO;
import com.cqi.hr.dao.DailyAttendanceRecordDAO;
import com.cqi.hr.dao.SpecialDateAboutWorkDAO;
import com.cqi.hr.dao.SysUserDAO;
import com.cqi.hr.dao.SysUserShiftDAO;
import com.cqi.hr.dao.UserAskForLeaveDAO;
import com.cqi.hr.dao.UserAskForOvertimeDAO;
import com.cqi.hr.dao.UserShiftQuotaDAO;
import com.cqi.hr.entity.AttendanceRecord;
import com.cqi.hr.entity.CompanyLeave;
import com.cqi.hr.entity.DailyAttendanceRecord;
import com.cqi.hr.entity.SpecialDateAboutWork;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.SysUserShift;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.entity.UserAskForOvertime;
import com.cqi.hr.entity.UserShiftQuota;
import com.cqi.hr.util.DateUtils;

@Service
public class DailyAttendanceRecordService extends AbstractService<DailyAttendanceRecord>{
	
	@Resource DailyAttendanceRecordDAO dailyAttendanceRecordDAO;
	@Resource SysUserDAO sysUserDAO;
	@Resource SysUserShiftDAO sysUserShiftDAO;
	@Resource AttendanceRecordDAO attendanceRecordDAO;
	@Resource UserAskForOvertimeDAO userAskForOvertimeDAO;
	@Resource UserAskForLeaveDAO userAskForLeaveDAO;
	@Resource SpecialDateAboutWorkDAO specialDateAboutWorkDAO;
	@Resource CompanyLeaveDAO companyLeaveDAO;
	
	@Resource SysUserShiftService sysUserShiftService ;
	
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
	
	@Override
	protected AbstractDAO<DailyAttendanceRecord> getDAO() {
		return dailyAttendanceRecordDAO;
	}
	
	@Transactional
	public List<DailyAttendanceRecord> getDailyAttendanceRecordsByYearAndMonth(Integer year, Integer month, SysUser operator) throws Exception{
		return dailyAttendanceRecordDAO.getListByYearAndMonth(year, month,  operator);
	}
	
	@SuppressWarnings("deprecation")
	@Transactional
	public void calculateYesterdayAttendanceRecord() throws Exception{
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar = DateUtils.clearTime(calendar);
		Date date = calendar.getTime();
		List<DailyAttendanceRecord> dailyAttendanceRecordsList = new ArrayList<DailyAttendanceRecord>();
		List<SysUser> userList = new ArrayList<SysUser>();
		// prod
		userList = sysUserDAO.getEnableRole2UserOrGraduationInMonth(calendar.getTime());
		
		// only for test
		//userList.add(sysUserDAO.get("1198842813042872"));
		//date = sdfDate.parse("2022-11-23");
		
		
		for(SysUser sysUser:userList) {
			if(sysUser.getUserName().equals("JZ")) {continue;}
			
			DailyAttendanceRecord oldDailyAttendanceRecord = dailyAttendanceRecordDAO.getOneByUserIdAndDate(sysUser.getSysUserId(), date);
			if(oldDailyAttendanceRecord != null ) {dailyAttendanceRecordDAO.delete(oldDailyAttendanceRecord);}
			DailyAttendanceRecord newDailyAttendanceRecord = calculateAttendanceRecord(date, sysUser );
			if(newDailyAttendanceRecord != null) { dailyAttendanceRecordsList.add( newDailyAttendanceRecord) ;}
			
		} //end for
		
		//save to db
		dailyAttendanceRecordDAO.saveOrUpdateAll(dailyAttendanceRecordsList);
		System.out.println("end calculateYesterdayAttendanceRecord");
	}

	public DailyAttendanceRecord calculateAttendanceRecord(Date date, SysUser sysUser ) throws Exception {
		SysUserShift sysUserShift = sysUserShiftDAO.getOneByIdAndDate(sysUser.getSysUserId(), date);
		SpecialDateAboutWork specialDateAboutWork = specialDateAboutWorkDAO.getOneByDate(date);
		AttendanceRecord userAttendanceRecord = attendanceRecordDAO.getOneByUserIdAndDate(sysUser.getSysUserId(),date);
		UserAskForOvertime userAskForOvertime = userAskForOvertimeDAO.getOneByUserIdAndDate(sysUser.getSysUserId(),date);
		List<UserAskForLeave> userAskForLeave = userAskForLeaveDAO.getListByUserIdAndDate(sysUser.getSysUserId(),date);
		double attendHours=0.0,absenceHours = 0.0,overtimeHours = 0.0,leaveHours=0.0;
		
		// not work date 
		if( specialDateAboutWork !=null  && specialDateAboutWork.getIsWorkDay() == 0 ) {
			if(userAttendanceRecord == null && userAskForOvertime != null && userAskForLeave == null) { //只有加班 --> 勤務?
				return null;
			}else if(userAttendanceRecord != null && userAskForOvertime == null && userAskForLeave == null) { //只有出勤 -->幫使用者申請加班?
				attendHours = userAttendanceRecord.getAttendHours();
			
			}else if(userAttendanceRecord != null && userAskForOvertime != null && userAskForLeave == null) { //有出勤,加班紀錄
				//calculate
				attendHours = userAttendanceRecord.getAttendHours();
				overtimeHours = userAskForOvertime.getSpendTime();
				
				//save
				if(attendHours < overtimeHours) {
					overtimeHours = attendHours;
				}
			}else {
				return null;
			}
		}else { // work date
			if(userAttendanceRecord == null|| userAttendanceRecord.getArriveTime() == null || userAttendanceRecord.getLeaveTime() == null 
					|| userAttendanceRecord.getArriveTime().isEmpty() || userAttendanceRecord.getLeaveTime().isEmpty() ) { //沒出勤紀錄or出勤紀錄異常
				if(userAskForOvertime == null && userAskForLeave == null){
					absenceHours = 8.0;
				}else if(userAskForOvertime == null && userAskForLeave != null) { //只有請假紀錄 ,未請8小時表示有缺席
					
					leaveHours = 0;
					for(int j = 0 ;j<userAskForLeave.size();j++) {
						CompanyLeave companyLeave = companyLeaveDAO.get(userAskForLeave.get(j).getLeaveId());
						double leaveHourTemp = (companyLeave != null && companyLeave.getUnitType() == 1 ) ? 8.0 : userAskForLeave.get(j).getSpendTime();
						leaveHours +=  leaveHourTemp;
						
					}
					
					absenceHours = 8 - leaveHours;
					
				}else if(userAskForOvertime != null && userAskForLeave == null) { //只有加班 --> 勤務?
					absenceHours = 8.0;
				}else if(userAskForOvertime != null && userAskForLeave != null) { //有請假,加班紀錄
					
					leaveHours = 0;
					for(int j = 0 ;j<userAskForLeave.size();j++) {
						CompanyLeave companyLeave = companyLeaveDAO.get(userAskForLeave.get(j).getLeaveId());
						double leaveHourTemp = (companyLeave != null && companyLeave.getUnitType() == 1 ) ? 8.0 : userAskForLeave.get(j).getSpendTime();
						leaveHours +=  leaveHourTemp;
						
					}
					
					absenceHours = 8 - leaveHours;
					
				}
				
			}else {//有出勤紀錄
				
				Date boardTime = (sysUserShift == null) ? sdfTime.parse("09:00") :sdfTime.parse(sysUserShift.getBoardTime());
				Date finishTime = (sysUserShift == null) ? sdfTime.parse("18:00") :sdfTime.parse(sysUserShift.getFinishTime());
				Date attendanceArriveTime = sdfTime.parse(userAttendanceRecord.getArriveTime());
				Date attendanceLeaveTime = sdfTime.parse(userAttendanceRecord.getLeaveTime());
				
				
				if(userAskForOvertime == null && userAskForLeave == null) { //只有出勤 
					for(int i = boardTime.getHours(); i < finishTime.getHours() ; i++) {
						if(i == 12) {
							continue;
						} //出勤
						else if( (attendanceArriveTime.getHours() < i && attendanceLeaveTime.getHours() >= i+1 )
								|| (attendanceArriveTime.getHours() == i && attendanceArriveTime.getMinutes() <= 15 && attendanceLeaveTime.getHours() >= i+1) ){
							attendHours++;
						}else {
							absenceHours++;
						}
					}
				}else if(userAttendanceRecord != null && userAskForOvertime == null && userAskForLeave != null) { //有出勤,請假紀錄
					
					
					for(int i = boardTime.getHours(); i < finishTime.getHours() ; i++) {
						if(i == 12) {
							continue;
						}else if( (attendanceArriveTime.getHours() < i && attendanceLeaveTime.getHours() >= i+1 )
								|| (attendanceArriveTime.getHours() == i && attendanceArriveTime.getMinutes() <= 15 && attendanceLeaveTime.getHours() >= i+1) ){
							attendHours++;
						}
					}
					
					
					leaveHours = 0;
					for(int j = 0 ;j<userAskForLeave.size();j++) {
						CompanyLeave companyLeave = companyLeaveDAO.get(userAskForLeave.get(j).getLeaveId());
						double leaveHourTemp = (companyLeave != null && companyLeave.getUnitType() == 1 ) ? 8.0 : userAskForLeave.get(j).getSpendTime();
						leaveHours +=  leaveHourTemp;
						
					}
					
					
					absenceHours = 8 - attendHours - leaveHours;
					
					
				}else if(userAttendanceRecord != null && userAskForOvertime != null && userAskForLeave == null) { //有出勤,加班紀錄
					for(int i = boardTime.getHours(); i < attendanceLeaveTime.getHours() ; i++) {
						if(i == 12) {
							continue;
						}else if( (attendanceArriveTime.getHours() < i && attendanceLeaveTime.getHours() >= i+1 )
								|| (attendanceArriveTime.getHours() == i && attendanceArriveTime.getMinutes() <= 15 && attendanceLeaveTime.getHours() >= i+1) )
						{
							if(attendHours >= 8.0) {//滿8小變加班時數
								overtimeHours++;
							}else {
								attendHours++;
							}
						}else {
							absenceHours++;
						}
					}
					
				}else if(userAttendanceRecord != null && userAskForOvertime != null && userAskForLeave != null) { //有出勤,加班,請假紀錄
					
					//Date leaveStartTime = userAskForLeave.getStartTime(); 
					//Date leaveEndTime = userAskForLeave.getEndTime(); 
					/*
					for(int i = boardTime.getHours(); i < attendanceLeaveTime.getHours() ; i++) {
						if(i == 12) {
							continue;
						}else if( (attendanceArriveTime.getHours() < i && attendanceLeaveTime.getHours() >= i+1 )
								|| (attendanceArriveTime.getHours() == i && attendanceArriveTime.getMinutes() <= 15 && attendanceLeaveTime.getHours() >= i+1) )
						{
							if(attendHours+leaveHours >= 8.0) {//滿8小變加班時數
								overtimeHours++;
							}else {
								attendHours++;
							}
						}else if( leaveStartTime.getHours() <= i  && leaveEndTime.getHours() >= i+1 ){ //區間內有無請假
							leaveHours++;
						}else {
							absenceHours++;
						}
					}
					*/
					
					for(int i = boardTime.getHours(); i < finishTime.getHours() ; i++) {
						if(i == 12) {
							continue;
						}else if( (attendanceArriveTime.getHours() < i && attendanceLeaveTime.getHours() >= i+1 )
								|| (attendanceArriveTime.getHours() == i && attendanceArriveTime.getMinutes() <= 15 && attendanceLeaveTime.getHours() >= i+1) ){
							attendHours++;
						}
					}
					
					
					leaveHours = 0;
					for(int j = 0 ;j<userAskForLeave.size();j++) {
						CompanyLeave companyLeave = companyLeaveDAO.get(userAskForLeave.get(j).getLeaveId());
						double leaveHourTemp = (companyLeave != null && companyLeave.getUnitType() == 1 ) ? 8.0 : userAskForLeave.get(j).getSpendTime();
						leaveHours +=  leaveHourTemp;
						
					}
					
					
					absenceHours = 8 - attendHours - leaveHours;
					
					
				}else {
					return null;
				}
				
				
				
				
				
			}//end 有出勤紀錄
		}// end work date
			
		System.out.println(sysUser.getSysUserId()+": " + date+ " attendHours:"+attendHours+" absenceHours:"+absenceHours+" overtimeHours:"+overtimeHours+" leaveHours:"+leaveHours);
		return new DailyAttendanceRecord(sysUser.getSysUserId(), date, (userAttendanceRecord != null && userAttendanceRecord.getArriveTime() != null) ? userAttendanceRecord.getArriveTime() : "", (userAttendanceRecord != null && userAttendanceRecord.getLeaveTime() != null) ? userAttendanceRecord.getLeaveTime() : ""
			, attendHours, overtimeHours, leaveHours,  absenceHours);
	}

	@Transactional
	public void updateDailyAttendanceRecordByDateAndUser(Date date, SysUser sysUser) throws Exception {
		DailyAttendanceRecord oldDailyAttendanceRecord = dailyAttendanceRecordDAO.getOneByUserIdAndDate(sysUser.getSysUserId(), date);
		if(oldDailyAttendanceRecord != null ) {dailyAttendanceRecordDAO.delete(oldDailyAttendanceRecord);}
		DailyAttendanceRecord dailyAttendanceRecord = calculateAttendanceRecord (date ,sysUser);
		if(dailyAttendanceRecord != null ) {dailyAttendanceRecordDAO.saveOrUpdate(dailyAttendanceRecord);}
		
		
	} 
}
