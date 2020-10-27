package com.cqi.hr.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cqi.hr.entity.LineUser;
import com.cqi.hr.service.LineBotService;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.UserAskForOvertimeService;
import com.cqi.hr.service.UserLeaveService;
import com.cqi.hr.service.UserOvertimeService;


@Controller
@RequestMapping("/lineBot")
public class LineBotController extends AbstractController<LineUser> {
	@Resource SysUserService sysUserService;
	@Resource UserLeaveService userLeaveService;
	@Resource UserOvertimeService userOvertimeService;
	@Resource UserAskForOvertimeService userAskForOvertimeService;
	@Resource LineBotService linebotService;
	private static String FUNCTION_NAME = "Line Bot"; 
	
	@RequestMapping(method=RequestMethod.GET)
	public String index(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " list");
		try {
			model.addAttribute("cqiOvertimeList", userLeaveService.getCompanyOvertimeList());
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "/askOvertime/askOvertime-list";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxDataLoading")
	public String ajaxDataLoading(HttpServletRequest req, String searchUserName, Integer page, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		try {
			
		} catch (Exception e) {
			logger.debug(FUNCTION_NAME + " ajaxDataLoading error: ", e);
		}
		return "/askOvertime/askOvertime-list.table";
	}
	
}
