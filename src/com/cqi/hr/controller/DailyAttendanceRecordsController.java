package com.cqi.hr.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cqi.hr.entity.CompanyLeave;
import com.cqi.hr.entity.DailyAttendanceRecord;
import com.cqi.hr.entity.MonthlyReport;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.SysUserShift;
import com.cqi.hr.entity.UserLeaveQuotaMonthly;
import com.cqi.hr.service.DailyAttendanceRecordService;
import com.cqi.hr.service.MonthlyReportService;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.SysUserShiftService;
import com.cqi.hr.service.UserLeaveService;
import com.cqi.hr.util.SessionUtils;


@Controller
@RequestMapping("/security/DailyAttendanceRecords")
public class DailyAttendanceRecordsController extends AbstractController<MonthlyReport> {	
	@Resource SysUserService sysUserService;
	@Resource UserLeaveService userLeaveService;
	@Resource SysUserShiftService sysUserShiftService;
	@Resource MonthlyReportService monthlyReportService;
	@Resource DailyAttendanceRecordService dailyAttendanceRecordService;
	
	private static String FUNCTION_NAME = "DailyAttendanceRecords"; 
	
	@RequestMapping(method=RequestMethod.GET, value="manager")
	public String managerIndex(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " managerIndex");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser checkUser = sysUserService.get(operator.getSysUserId());
			if(checkUser!=null ) {
				model.addAttribute("operator",operator);
				model.addAttribute("mapEnableRule2User", sysUserService.getMapEnableRule2User());
				return "/DailyAttendanceRecords/dailyAttendanceRecords";
			}
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "redirect:/logout";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="manager/ajaxDataLoading")
	public String managerAjaxDataLoading(HttpServletRequest req, Integer year, Integer month, String searchSysUserId, ModelMap model) {
		logger.info(FUNCTION_NAME + " managerAjaxDataLoading");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser checkUser = sysUserService.get(operator.getSysUserId());
			if(checkUser!=null ) {
				Map<String, SysUserShift> shiftMap = sysUserShiftService.getMapByYearAndMonth(year, month);
				List<DailyAttendanceRecord> dailyAttendanceRecordList = new ArrayList<DailyAttendanceRecord>();
				if(checkUser.getRoleId().equals("1") && searchSysUserId.equals("ALL")) {
					dailyAttendanceRecordList = dailyAttendanceRecordService.getDailyAttendanceRecordsByYearAndMonth(year, month, null);
				}else if (checkUser.getRoleId().equals("1")) {
					SysUser searchSysUser = sysUserService.get(searchSysUserId);
					dailyAttendanceRecordList = dailyAttendanceRecordService.getDailyAttendanceRecordsByYearAndMonth(year, month, searchSysUser);
				}else {
					dailyAttendanceRecordList = dailyAttendanceRecordService.getDailyAttendanceRecordsByYearAndMonth(year, month, checkUser );
				}
				
				model.addAttribute("userMap", sysUserService.getUserMapping());
				model.addAttribute("shiftMap", shiftMap );
				model.addAttribute("dailyAttendanceRecordList", dailyAttendanceRecordList);
				return "/DailyAttendanceRecords/dailyAttendanceRecords.table";
			}
		} catch (Exception e) {
			logger.debug("ajaxDataLoading " + FUNCTION_NAME + " fail : ", e);
		}
		return "redirect:/logout";
	}	
}
