package com.cqi.hr.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserLeaveHistory;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.UserLeaveHistoryService;
import com.cqi.hr.service.UserLeaveService;
import com.cqi.hr.util.SessionUtils;


@Controller
@RequestMapping("/security/userLeaveHistory")
public class UserLeaveHistoryController extends AbstractController<UserLeaveHistory> {	
	@Resource SysUserService sysUserService;
	@Resource UserLeaveService userLeaveService;
	@Resource UserLeaveHistoryService userLeaveHistoryService;
	private static String FUNCTION_NAME = "user leave History"; 
	
	@RequestMapping(method=RequestMethod.GET, value="manager")
	public String managerIndex(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " managerIndex");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser checkUser = sysUserService.get(operator.getSysUserId());
			if(checkUser!=null && checkUser.getRoleId().equals("1")) {
				return "/userLeaveHistory/userLeaveHistory-list";
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
				List<UserLeaveHistory> userLeaveHistoryList = userLeaveHistoryService.getLeaveHistoryByYearAndMonth(year, month, null);
				model.addAttribute("userMap", sysUserService.getUserMapping());
				model.addAttribute("mappingLeave", userLeaveService.getCompanyLeaveAllMapping());
				model.addAttribute("userLeaveHistoryList", userLeaveHistoryList);
				return "/userLeaveHistory/userLeaveHistory-list.table";
			}
		} catch (Exception e) {
			logger.debug("ajaxDataLoading " + FUNCTION_NAME + " fail : ", e);
		}
		return "redirect:/logout";
	}	
}
