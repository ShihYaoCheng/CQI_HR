package com.cqi.hr.controller;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.entity.UserLeave;
import com.cqi.hr.entity.UserLeaveHistory;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.UserLeaveService;
import com.cqi.hr.util.SessionUtils;


@Controller
@RequestMapping("/security/userLeaveNew")
public class UserLeaveNewController extends AbstractController<UserLeave> {	
	@Resource SysUserService sysUserService;
	@Resource UserLeaveService userLeaveService;
	private static String FUNCTION_NAME = "user leave New"; 
	
	@RequestMapping(method=RequestMethod.GET)
	public String list(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " list");
		try {
			model.addAttribute("nowCalendar", Calendar.getInstance());
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "/userLeaveNew/userLeave-list";
	}
	
	/*
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
	*/
	
	/**
	 * 改採取group by取得統計的結果
	 * @param req
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST, value="ajaxDataLoading")
	public String ajaxDataLoading(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			logger.info("operator : " + BeanUtils.describe(operator).toString());
			model.addAttribute("mappingLeave", userLeaveService.getCompanyLeaveMapping());
			
			List<UserAskForLeave> userAskForLeaveList = userLeaveService.getAskForLeaveGroupByNow(operator.getSysUserId());
			logger.info("Data size : " + userAskForLeaveList.size());
			model.addAttribute("userAskForLeaveList", userAskForLeaveList);
			
			
		} catch (Exception e) {
			logger.debug("ajaxDataLoading " + FUNCTION_NAME + " fail : ", e);
		}
		return "/userLeaveNew/userLeave-list.table";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxDataLoadingHistory")
	public String ajaxDataLoadingHistory(HttpServletRequest req, Integer year, Integer month, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxDataLoadingHistory");
		try {
			if(year!=null && year>2017 && month!=null && month>0 && month<13 ) {
				SysUser operator = SessionUtils.getLoginInfo(req);
				logger.info("operator : " + BeanUtils.describe(operator).toString());
				List<UserLeaveHistory> userLeaveHistoryList = userLeaveService.getLeaveHistoryByYearAndMonth(year, month, operator); ;
				logger.info("Data size : " + userLeaveHistoryList.size());
				
				model.addAttribute("mappingLeave", userLeaveService.getCompanyLeaveAllMapping());
				model.addAttribute("userLeaveHistoryList", userLeaveHistoryList);				
			}
		} catch (Exception e) {
			logger.debug("ajaxDataLoading " + FUNCTION_NAME + " fail : ", e);
		}
		return "/userLeaveNew/userLeaveHistory-list.table";
	}
}
