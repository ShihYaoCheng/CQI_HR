package com.cqi.hr.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserLeaveQuotaMonthly;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.UserLeaveQuotaMonthlyService;
import com.cqi.hr.service.UserLeaveService;
import com.cqi.hr.util.SessionUtils;


@Controller
@RequestMapping("/security/userLeaveQuotaMonthly")
public class UserLeaveQuotaMonthlyController extends AbstractController<UserLeaveQuotaMonthly> {	
	@Resource SysUserService sysUserService;
	@Resource UserLeaveService userLeaveService;
	@Resource UserLeaveQuotaMonthlyService userLeaveQuotaMonthlyService;
	private static String FUNCTION_NAME = "userLeaveQuotaMonthly"; 
	
	@RequestMapping(method=RequestMethod.GET, value="manager")
	public String managerIndex(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " managerIndex");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser checkUser = sysUserService.get(operator.getSysUserId());
			if(checkUser!=null && checkUser.getRoleId().equals("1")) {
				return "/userLeaveQuotaMonthly/userLeaveQuota-list";
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
			if(checkUser!=null && checkUser.getRoleId().equals("1")) {
				List<UserLeaveQuotaMonthly> userLeaveHistoryList = userLeaveQuotaMonthlyService.getLeaveHistoryByYearAndMonth(year, month, null);
				model.addAttribute("userMap", sysUserService.getUserMapping());
				model.addAttribute("mappingLeave", userLeaveService.getCompanyLeaveAllMapping());
				model.addAttribute("userLeaveQuotaList", userLeaveHistoryList);
				return "/userLeaveQuotaMonthly/userLeaveQuota-list.table";
			}
		} catch (Exception e) {
			logger.debug("ajaxDataLoading " + FUNCTION_NAME + " fail : ", e);
		}
		return "redirect:/logout";
	}	
}
