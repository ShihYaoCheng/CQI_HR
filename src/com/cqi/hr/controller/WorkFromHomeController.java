package com.cqi.hr.controller;
import java.util.Calendar;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.SysUserShift;
import com.cqi.hr.entity.WorkFromHome;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.SysUserShiftService;
import com.cqi.hr.service.WorkFromHomeService;
import com.cqi.hr.util.SessionUtils;
import com.cqi.hr.util.StringUtils;

@Controller
@RequestMapping("/security/WorkFromHome")
public class WorkFromHomeController extends AbstractController<WorkFromHome>{

	@Resource SysUserService sysUserService;
	@Resource WorkFromHomeService workFromHomeService;
	private static String FUNCTION_NAME = "WorkFromHome";
	
	@RequestMapping(method=RequestMethod.GET)
	public String index(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " list");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			model.addAttribute("operator",operator);
			model.addAttribute("mapEnableRule2User", sysUserService.getMapEnableRule2User());
			
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "/WorkFromHome/workFromHome";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxDataLoading")
	public String ajaxDataLoading(HttpServletRequest req, String searchUserName, Integer page, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser checkUser = sysUserService.get(operator.getSysUserId());
			PagingList<WorkFromHome> WorkFromHomeList = new PagingList<WorkFromHome>();
			WorkFromHomeList = workFromHomeService.getList(page, null);
			
			
			createPagingInfo(model, WorkFromHomeList);
			model.addAttribute("operator",operator);
			model.addAttribute("mapEnableRule2User", sysUserService.getMapEnableRule2User());
			return "/WorkFromHome/workFromHome-table";
		} catch (Exception e) {
			logger.debug(FUNCTION_NAME + " ajaxDataLoading error: ", e);
		}
		return "redirect:/logout";
	}
	
}
