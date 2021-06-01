package com.cqi.hr.controller;

import java.util.Calendar;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.EmergenceOvertimeSign;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.service.EmergenceOvertimeService;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.UserAskForOvertimeService;
import com.cqi.hr.service.UserLeaveService;
import com.cqi.hr.service.UserShiftQuotaService;
import com.cqi.hr.util.DateUtils;
import com.cqi.hr.util.SessionUtils;


@Controller
@RequestMapping("/security/emergence")
public class EmergenceOvertimeController extends AbstractController<EmergenceOvertimeSign> {
	@Resource SysUserService sysUserService;
	@Resource UserLeaveService userLeaveService;
	@Resource UserShiftQuotaService userOvertimeService;
	@Resource UserAskForOvertimeService userAskForOvertimeService;
	@Resource EmergenceOvertimeService emergenceOvertimeService;
	private static String FUNCTION_NAME = "Emergence"; 
	
	@RequestMapping(method=RequestMethod.GET)
	public String index(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " list");
		try {
			model.addAttribute("cqiOvertimeList", userLeaveService.getCompanyOvertimeList());
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "/emergence/emergence-list";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxDataLoading")
	public String ajaxDataLoading(HttpServletRequest req, ModelMap model, @RequestParam String year, @RequestParam String month) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(operator!=null) {
				Calendar startTime = Calendar.getInstance();
				startTime.set(Calendar.YEAR, Integer.parseInt(year));
				startTime.set(Calendar.MONTH, Integer.parseInt(month)-1);
				startTime.set(Calendar.DAY_OF_MONTH, 1);
				Calendar endTime = Calendar.getInstance();
				endTime.set(Calendar.YEAR, Integer.parseInt(year));
				endTime.set(Calendar.MONTH, Integer.parseInt(month));
				endTime.set(Calendar.DAY_OF_MONTH, 1);
				model.addAttribute("userMap", sysUserService.getUserMapping());
				model.addAttribute("askOvertimeMap", userAskForOvertimeService.findByCreateTime(
						DateUtils.clearTime(startTime.getTime()), DateUtils.clearTime(endTime.getTime())));
				model.addAttribute("emergenceOvertimeList", emergenceOvertimeService.findByDate(
						DateUtils.clearTime(startTime.getTime()), DateUtils.clearTime(endTime.getTime())));
			}
		} catch (Exception e) {
			logger.debug(FUNCTION_NAME + " ajaxDataLoading error: ", e);
		}
		return "/emergence/emergence-list.table";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/confirm/department/{signId}")
	public void ajaxConfirmDepartment(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long signId){
		logger.info(FUNCTION_NAME + " ajaxConfirmDepartment: ");
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				if(emergenceOvertimeService.sign(Constant.LINE_EMERGENCE_LEVEL_DEPARTMENT, Constant.LINE_EMERGENCE_CONFIRM, signId, operator)) {
					map = createResponseMsg(true, "", "");
				}else {
					map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
				}
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxConfirmDepartment error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/confirm/finance/{signId}")
	public void ajaxFinanceCompany(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long signId){
		logger.info(FUNCTION_NAME + " ajaxFinanceCompany: ");
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				if(emergenceOvertimeService.sign(Constant.LINE_EMERGENCE_LEVEL_FINANCE, Constant.LINE_EMERGENCE_CONFIRM, signId, operator)) {
					map = createResponseMsg(true, "", "");
				}else {
					map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
				}
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxFinanceCompany error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/confirm/administration/{signId}")
	public void ajaxConfirmAdministration(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long signId){
		logger.info(FUNCTION_NAME + " ajaxConfirmAdministration: ");
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				if(emergenceOvertimeService.sign(Constant.LINE_EMERGENCE_LEVEL_ADMINISTRATION, Constant.LINE_EMERGENCE_CONFIRM, signId, operator)) {
					map = createResponseMsg(true, "", "");
				}else {
					map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
				}
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxConfirmAdministration error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/confirm/company/{signId}")
	public void ajaxConfirmCompany(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long signId){
		logger.info(FUNCTION_NAME + " ajaxConfirmCompany: ");
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				if(emergenceOvertimeService.sign(Constant.LINE_EMERGENCE_LEVEL_COMPANY, Constant.LINE_EMERGENCE_CONFIRM, signId, operator)) {
					map = createResponseMsg(true, "", "");
				}else {
					map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
				}
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxConfirmCompany error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/reject/department/{signId}")
	public void ajaxRejectDepartment(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long signId){
		logger.info(FUNCTION_NAME + " ajaxRejectDepartment: ");
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				if(emergenceOvertimeService.sign(Constant.LINE_EMERGENCE_LEVEL_DEPARTMENT, Constant.LINE_EMERGENCE_REJECT, signId, operator)) {
					map = createResponseMsg(true, "", "");
				}else {
					map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
				}
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxRejectDepartment error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/reject/finance/{signId}")
	public void ajaxRejectFinance(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long signId){
		logger.info(FUNCTION_NAME + " ajaxRejectFinance: ");
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				if(emergenceOvertimeService.sign(Constant.LINE_EMERGENCE_LEVEL_FINANCE, Constant.LINE_EMERGENCE_REJECT, signId, operator)) {
					map = createResponseMsg(true, "", "");
				}else {
					map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
				}
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxRejectFinance error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/reject/administration/{signId}")
	public void ajaxRejectAdministration(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long signId){
		logger.info(FUNCTION_NAME + " ajaxRejectAdministration: ");
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				if(emergenceOvertimeService.sign(Constant.LINE_EMERGENCE_LEVEL_ADMINISTRATION, Constant.LINE_EMERGENCE_REJECT, signId, operator)) {
					map = createResponseMsg(true, "", "");
				}else {
					map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
				}
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxRejectAdministration error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/reject/company/{signId}")
	public void ajaxRejectCompany(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long signId){
		logger.info(FUNCTION_NAME + " ajaxRejectCompany: ");
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				if(emergenceOvertimeService.sign(Constant.LINE_EMERGENCE_LEVEL_COMPANY, Constant.LINE_EMERGENCE_REJECT, signId, operator)) {
					map = createResponseMsg(true, "", "");
				}else {
					map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
				}
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxRejectCompany error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
}
