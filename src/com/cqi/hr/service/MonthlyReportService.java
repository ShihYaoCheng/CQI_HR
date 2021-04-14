package com.cqi.hr.service;


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
import com.cqi.hr.dao.CompanyLeaveDAO;
import com.cqi.hr.dao.DailyAttendanceRecordDAO;
import com.cqi.hr.dao.MonthlyReportDAO;
import com.cqi.hr.dao.SpecialDateAboutWorkDAO;
import com.cqi.hr.dao.SysUserDAO;
import com.cqi.hr.dao.UserAskForLeaveDAO;
import com.cqi.hr.dao.UserAskForOvertimeDAO;
import com.cqi.hr.dao.UserLeaveQuotaMonthlyDAO;
import com.cqi.hr.entity.CompanyLeave;
import com.cqi.hr.entity.DailyAttendanceRecord;
import com.cqi.hr.entity.MonthlyReport;
import com.cqi.hr.entity.SpecialDateAboutWork;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.SysUserShift;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.entity.UserLeaveHistory;
import com.cqi.hr.entity.UserLeaveQuotaMonthly;
import com.cqi.hr.util.DateUtils;

@Service
public class MonthlyReportService extends AbstractService<MonthlyReport>{
	@Resource MonthlyReportDAO monthlyReportDAO;
	@Resource SysUserDAO sysUserDAO;
	@Resource DailyAttendanceRecordDAO dailyAttendanceRecordDAO;
	@Resource UserAskForOvertimeDAO userAskForOvertimeDAO;
	@Resource UserAskForLeaveDAO userAskForLeaveDAO;
	@Resource SpecialDateAboutWorkDAO specialDateAboutWorkDAO;
	@Resource UserLeaveQuotaMonthlyDAO userLeaveQuotaMonthlyDAO;
	@Resource CompanyLeaveDAO companyLeaveDAO;

	@Resource SysUserShiftService sysUserShiftService ;
	
	@Override
	protected AbstractDAO<MonthlyReport> getDAO() {
		return monthlyReportDAO;
	}
	
	
	@Transactional
	public List<MonthlyReport> getMonthlyReportByYearAndMonth(Integer year, Integer month, SysUser operator) throws Exception{
		return monthlyReportDAO.getListByYearAndMonth(year, month,  operator);
	}


	
	@SuppressWarnings("deprecation")
	@Transactional
	public void calculateLastMonthReport() throws Exception {

		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		Date endDate = DateUtils.getLastDateByYearAndMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)) ;
		Date startDate;
		int year = calendar.get(Calendar.YEAR),month = calendar.get(Calendar.MONTH);
		Map<String,SysUserShift> mapSysUserShift = sysUserShiftService.getMapLastMonth();
		List<MonthlyReport> monthlyReportList = new ArrayList<MonthlyReport>();
		List<SysUser> userList = new ArrayList<SysUser>();
		//userList = sysUserDAO.getEnableRole2UserOrGraduationInMonth(calendar.getTime());
		
		//for test only
		//startDate= sdfDate.parse("2021-01-01");
		//endDate= sdfDate.parse("2021-01-30");
		//year = 2021; month = 0;
		userList.add(sysUserDAO.get("719802823720909"));
		userList.add(sysUserDAO.get("1147593956675609"));
		//userList.add(sysUserDAO.get("955100567255002"));
		
		
		for(SysUser sysUser:userList) {
			if(sysUser.getUserName().equals("JZ")) {continue;}
			double absentBase = 0.0, overBase = 0.0, taskBase = 0.0;
			double o1=0.0,o2=0.0,o3=0.0,o4=0.0,schedulingH=0.0,overH=0.0, workH=0.0, absenceH=0.0, leaveH=0.0;
			double L1=0.0,QL1=0.0,L2=0.0,L3=0.0,L4=0.0,QL4=0.0,L5=0.0,QL5=0.0,L6=0.0,L7=0.0,L8=0.0,L9=0.0;
			String shiftString = (mapSysUserShift.get(sysUser.getSysUserId()) == null) ?  "" : mapSysUserShift.get(sysUser.getSysUserId()).getBoardTime()+"~"+mapSysUserShift.get(sysUser.getSysUserId()).getFinishTime();
			startDate = DateUtils.getFirstDateByYearAndMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
			
			//cal needWorkHours
			if (startDate.before(sysUser.getInaugurationDate())) {
				startDate = DateUtils.offsetByDay(sysUser.getInaugurationDate(), 1) ;
			}
			List<SpecialDateAboutWork> sList = specialDateAboutWorkDAO.getVacationBetweenDate(startDate, endDate);
			int nonWorkDays = sList.size();
			double needWorkHours = (DateUtils.diffDays(startDate, endDate)+ 1 - nonWorkDays) * 8;
			
			//cal DailyAttendanceRecord
			List<DailyAttendanceRecord> dailyAttendanceRecordsList = dailyAttendanceRecordDAO.getMonthlyData(startDate, endDate, sysUser.getSysUserId());
			for (DailyAttendanceRecord d : dailyAttendanceRecordsList) {
				overH += d.getOvertimeHours();
				workH += d.getAttendHours();
				absenceH += d.getAbsenceHours();
				absentBase += d.getAbsenceHours() * (-1);
				leaveH += d.getLeaveHours();
			}
			
			//cal overtime
			List<Object[]> userAskForOvertimeList = userAskForOvertimeDAO.getMonthlyRowdata(year, month, sysUser.getSysUserId(), null);
			if(userAskForOvertimeList.size()>0) {
				for(Object[] dataArray:userAskForOvertimeList){
					System.out.println(dataArray.toString());
				}
			}
			
			//cal Leave
			List <CompanyLeave> clist = companyLeaveDAO.getWithOrderBy("leaveId");
			
			List<UserAskForLeave> userAskForLeavesList = userAskForLeaveDAO.getMonthlyData(startDate, endDate, sysUser.getSysUserId()) ;
			for(UserAskForLeave ul : userAskForLeavesList) {
				int leaveId = ul.getLeaveId().intValue();
				switch (leaveId) {
				case 1: //事假
					L1+= ul.getSpendTime();
					absentBase += clist.get(0).getCalculateBase() * ul.getSpendTime() ;
					break;
				case 2: //調班
					L2+= ul.getSpendTime();
					break;
				case 3: //生理假
					L3+= ul.getSpendTime();
					absentBase += clist.get(2).getCalculateBase() * ul.getSpendTime() * 8 ;
					break;
				case 4://特休
					L4+= ul.getSpendTime();
					needWorkHours -= ul.getSpendTime();
					break;
				case 5://病假
					L5+= ul.getSpendTime();
					absentBase += clist.get(4).getCalculateBase() * ul.getSpendTime() ;
					break;
				case 6://公假
					L6+= ul.getSpendTime();
					needWorkHours -= ul.getSpendTime();
					break;
				case 7://喪假
					L7+= ul.getSpendTime();
					break;
				case 8://產假
					L8+= ul.getSpendTime();
					break;
				case 9://陪產假
					L9+= ul.getSpendTime();
					break;

				default:
					break;
				}
			}
			
			
			//cal quota
			UserLeaveQuotaMonthly u = userLeaveQuotaMonthlyDAO.getOneByYearAndMonth(year, month+1,1L , sysUser.getSysUserId());
			QL1 =(u == null)? 0.0 : u.getMonthlySummaryQuota();
			u = userLeaveQuotaMonthlyDAO.getOneByYearAndMonth(year, month+1,4L , sysUser.getSysUserId());
			QL4 =(u == null)? 0.0 : u.getMonthlySummaryQuota();
			u = userLeaveQuotaMonthlyDAO.getOneByYearAndMonth(year, month+1,5L , sysUser.getSysUserId());
			QL5 =(u == null)? 0.0 : u.getMonthlySummaryQuota();
			

			
			MonthlyReport monthlyReport = new MonthlyReport(year,month+1,sysUser.getDepartment(),sysUser.getSysUserId(),sysUser.getUserName(),sysUser.getOriginalName(),sysUser.getStatus(),shiftString
					,absentBase, overBase, taskBase, o1, o2, o3, o4, overH, schedulingH, workH, needWorkHours, absenceH, leaveH
					,L1,QL1,L2,L3,L4,QL4,L5,QL5,L6,L7,L8,L9);
			monthlyReportList.add(monthlyReport);
		}
		
		//save to db
		monthlyReportDAO.saveOrUpdateAll(monthlyReportList);
		System.out.println("end calculateLastMonthReport");
	}
	
	
}
