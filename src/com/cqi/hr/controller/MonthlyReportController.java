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
import com.cqi.hr.entity.MonthlyReport;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserLeaveQuotaMonthly;
import com.cqi.hr.service.MonthlyReportService;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.UserLeaveService;
import com.cqi.hr.util.SessionUtils;


@Controller
@RequestMapping("/security/MonthlyReport")
public class MonthlyReportController extends AbstractController<MonthlyReport> {	
	@Resource SysUserService sysUserService;
	@Resource UserLeaveService userLeaveService;
	@Resource MonthlyReportService monthlyReportService;
	private static String FUNCTION_NAME = "MonthlyReport"; 
	
	@RequestMapping(method=RequestMethod.GET, value="manager")
	public String managerIndex(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " managerIndex");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser checkUser = sysUserService.get(operator.getSysUserId());
			if(checkUser!=null ) {
				return "/MonthlyReport/monthlyReport";
			}
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "redirect:/logout";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="manager/ajaxDataLoading")
	public String managerAjaxDataLoading(HttpServletRequest req, Integer year, Integer month, ModelMap model) {
		logger.info(FUNCTION_NAME + " managerAjaxDataLoading");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser checkUser = sysUserService.get(operator.getSysUserId());
			if(checkUser!=null ) {
				List<MonthlyReport> monthlyReportList = new ArrayList<MonthlyReport>();
				if(checkUser.getRoleId().equals("1")) {
					monthlyReportList = monthlyReportService.getMonthlyReportByYearAndMonth(year, month, null);
				}else {
					monthlyReportList = monthlyReportService.getMonthlyReportByYearAndMonth(year, month, checkUser);
				}
				
				Map<Long, CompanyLeave> mapLeave= userLeaveService.getCompanyLeaveAllMapping();
				model.addAttribute("userMap", sysUserService.getUserMapping());
				model.addAttribute("mappingLeave", mapLeave);
				model.addAttribute("monthlyReportList", monthlyReportList);
				return "/MonthlyReport/monthlyReport.table";
			}
		} catch (Exception e) {
			logger.debug("ajaxDataLoading " + FUNCTION_NAME + " fail : ", e);
		}
		return "redirect:/logout";
	}	
}
