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
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.SysUserShiftService;
import com.cqi.hr.util.SessionUtils;
import com.cqi.hr.util.StringUtils;


@Controller
@RequestMapping("/security/sysUserShift")
public class SysUserShiftController extends AbstractController<SysUserShift> {	
	@Resource SysUserService sysUserService;
	@Resource SysUserShiftService sysUserShiftService;
	private static String FUNCTION_NAME = "Sys User Shift";
	
	@RequestMapping(method=RequestMethod.GET)
	public String index(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " list");
		try {
			model.addAttribute("userList", sysUserService.getUserList());
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "/sysUserShift/sysUserShift-list";
	}
	
	
	@RequestMapping(method=RequestMethod.GET, value="manager")
	public String indexManager(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " indexManager");
		try {
			model.addAttribute("userList", sysUserService.getUserList());
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "/sysUserShift/manager/sysUserShift-list";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxDataLoading")
	public String ajaxDataLoading(HttpServletRequest req, String searchUserName, Integer page, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser checkUser = sysUserService.get(operator.getSysUserId());
			PagingList<SysUserShift> shiftList = new PagingList<SysUserShift>();
			if(checkUser!=null ) {
				if(checkUser.getRoleId().equals("1")) {
					shiftList = sysUserShiftService.getList(page, null);
				}else {
					shiftList = sysUserShiftService.getList(page, checkUser);
				}
			}
			
			createPagingInfo(model, shiftList);
			model.addAttribute("mapEnableRule2User", sysUserService.getMapEnableRule2User());
			return "/sysUserShift/sysUserShift-list.table";
		} catch (Exception e) {
			logger.debug(FUNCTION_NAME + " ajaxDataLoading error: ", e);
		}
		return "redirect:/logout";
	}
	
	/*
	@RequestMapping(method=RequestMethod.POST, value="manager/ajaxDataLoading")
	public String ajaxManagerDataLoading(HttpServletRequest req, Long queryUserId, Integer page, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		try {
			//SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser queryUser = null;
			if(queryUserId!=null) {
				queryUser = sysUserService.get(queryUserId);
			}
			model.addAttribute("userMap", sysUserService.getUserMapping());
			PagingList<SysUserShift> shiftList = sysUserShiftService.getList(page, queryUser);
			createPagingInfo(model, shiftList);
		} catch (Exception e) {
			logger.debug(FUNCTION_NAME + " ajaxDataLoading error: ", e);
		}
		return "/sysUserShift/manager/sysUserShift-list.table";
	}
	*/
	
	@RequestMapping(method=RequestMethod.GET, value="/{shiftId}")
	public void ajaxQuery(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long shiftId){
		logger.info(FUNCTION_NAME + " ajaxQuery: " + shiftId);
		Map<Object, Object> map = null;
		try{
			String result = "";
			SysUserShift data = sysUserShiftService.get(shiftId);
			map = createResponseMsg(!StringUtils.hasText(result), "", result);
			map.put("sysUserShift", data);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxQuery error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/add")
	public void ajaxAdd(HttpServletRequest req, HttpServletResponse resp, @Valid SysUserShift sysUserShift){
		logger.info(FUNCTION_NAME + " ajaxAdd: ");
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				Calendar now = Calendar.getInstance();
				if(!StringUtils.hasText(sysUserShift.getSysUserId())) {
					sysUserShift.setSysUserId(operator.getSysUserId());
				}
				sysUserShift.setCreateDate(now.getTime());
				sysUserShift.setUpdateDate(now.getTime());
				sysUserShift.setStatus(Constant.STATUS_ENABLE);
				String errorMsg = sysUserShiftService.addShift(sysUserShift);
				if(StringUtils.hasText(errorMsg)) {
					map = createResponseMsg(false, "", errorMsg);
				}else {
					map = createResponseMsg(true, "", "");
				}
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxAdd error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/update")
	public void ajaxUpdate(HttpServletRequest req, HttpServletResponse resp, @Valid SysUserShift sysUserShift){
		logger.info(FUNCTION_NAME + " ajaxUpdate: ");
		Map<Object, Object> map = null;
		try{
			String errorMsg = sysUserShiftService.updateShift(sysUserShift);
			map = createResponseMsg(!StringUtils.hasText(errorMsg), "", errorMsg);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxUpdate error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
		
}
