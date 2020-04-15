package com.cqi.hr.controller;

import java.util.Calendar;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserLeave;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.UserLeaveService;
import com.cqi.hr.util.SessionUtils;


@Controller
@RequestMapping("/security/userLeave")
public class UserLeaveController extends AbstractController<UserLeave> {	
	@Resource SysUserService sysUserService;
	@Resource UserLeaveService userLeaveService;
	private static String FUNCTION_NAME = "user leave"; 
	
	@RequestMapping(method=RequestMethod.GET)
	public String list(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " list");
		try {
			model.addAttribute("cqiLeaveList", userLeaveService.getCompanyLeaveList());
			model.addAttribute("nowCalendar", Calendar.getInstance());
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "/userLeave/userLeave-list";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxDataLoading")
	public String ajaxDataLoading(HttpServletRequest req, Integer page, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			logger.info("operator : " + BeanUtils.describe(operator).toString());
			PagingList<UserLeave> userLeaveList = userLeaveService.getListByPage(page, operator.getSysUserId());
			logger.info("Data size : " + userLeaveList.getDatas().size());
			
			model.addAttribute("mappingLeave", userLeaveService.getCompanyLeaveMapping());
			createPagingInfo(model, userLeaveList);
		} catch (Exception e) {
			logger.debug("ajaxDataLoading " + FUNCTION_NAME + " fail : ", e);
		}
		return "/userLeave/userLeave-list.table";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="manager")
	public String managerIndex(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " managerIndex");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser checkUser = sysUserService.get(operator.getSysUserId());
			if(checkUser!=null && checkUser.getRoleId().equals("1")) {
				model.addAttribute("nowCalendar", Calendar.getInstance());
				return "/userLeave/manager/managerUserLeave-list";
			}
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "redirect:/logout";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="manager/ajaxDataLoading")
	public String managerAjaxDataLoading(HttpServletRequest req, Integer page, ModelMap model) {
		logger.info(FUNCTION_NAME + " managerAjaxDataLoading");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser checkUser = sysUserService.get(operator.getSysUserId());
			if(checkUser!=null && checkUser.getRoleId().equals("1")) {
				PagingList<UserLeave> userLeaveList = userLeaveService.getListByPage(page, null);
				model.addAttribute("userMap", sysUserService.getUserMapping());
				model.addAttribute("mappingLeave", userLeaveService.getCompanyLeaveMapping());
				createPagingInfo(model, userLeaveList);
				return "/userLeave/manager/managerUserLeave-list.table";
			}
		} catch (Exception e) {
			logger.debug("ajaxDataLoading " + FUNCTION_NAME + " fail : ", e);
		}
		return "redirect:/logout";
	}
	
}
