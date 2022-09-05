package com.cqi.hr.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.SysUserAbsence;
import com.cqi.hr.service.SysUserAbsenceService;
import com.cqi.hr.service.SysUserService;

import com.cqi.hr.util.SessionUtils;
import com.cqi.hr.util.StringUtils;


@Controller
@RequestMapping("/security/sysUserAbsence")
public class sysUserAbsenceController extends AbstractController<SysUser> {	
	@Resource SysUserService sysUserService;
	@Resource SysUserAbsenceService sysUserAbsenceService;
	private static String FUNCTION_NAME = "sys user"; 
	
	@ModelAttribute("SysUserList")
	public List<SysUser> SysUserList() throws Exception {
		return sysUserService.getAllUserList();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String list(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " list");
		try {
			model.addAttribute("sysRoleList", sysUserService.getSysRoleList());
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "/sysUserAbsence/sysUserAbsence-list";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxDataLoading")
	public String ajaxDataLoading(HttpServletRequest req, String searchUserName, Integer page, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		try {
			PagingList<SysUser> sysUserList = sysUserService.getSysUserStatusListByPage(page, searchUserName);
			
			createPagingInfo(model, sysUserList);

			List<SysUserAbsence> SysUserAbsenceList = sysUserAbsenceService.getSysUserAbsenceList();
			model.addAttribute("SysUserAbsenceList", SysUserAbsenceList);
			model.addAttribute("userMap", sysUserService.getMapEnableRule2User());

		} catch (Exception e) {
			logger.debug("ajaxDataLoading " + FUNCTION_NAME + " fail : ", e);
		}
		return "/sysUserAbsence/sysUserAbsence-list.table";
	}
	
	
	@RequestMapping(method=RequestMethod.GET, value="u")
	public String self(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " list");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser dbUser = sysUserService.get(operator.getSysUserId());
			model.addAttribute("sysUser", dbUser);
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "/sysUserAbsence/sysUserAbsence";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{sysUserId}")
	public void ajaxQuery(HttpServletRequest req, HttpServletResponse resp,  ModelMap model , @PathVariable String sysUserId){
		logger.info(FUNCTION_NAME + " ajaxQuery: " + sysUserId);
		Map<Object, Object> map = null;
		try{
			String result = "";
			SysUser sysUser = sysUserService.get(sysUserId);
			List<SysUserAbsence> SysUserAbsenceList = sysUserAbsenceService.getSysUserAbsenceList(sysUserId);
			SysUserAbsence SysUserAbsence = sysUserAbsenceService.getSysUserAbsenceBySysUserId(sysUserId);
			map = createResponseMsg(!StringUtils.hasText(result), "", result);
			map.put("sysUser", sysUser);
			map.put("SysUserAbsence", SysUserAbsence);
			model.addAttribute("SysUserAbsenceList", SysUserAbsenceList);
			
			
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxQuery error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="updateUserInfo")
	public void ajaxUpdateUserSelfInfo(HttpServletRequest req, HttpServletResponse resp,String sysUserId, String status, String cardid, String effectiveDate ,String expirationDate){
		logger.info(FUNCTION_NAME + " ajaxUpdateUserSelfInfo");
		Map<Object, Object> map = null;
		try{
			String result = "";
			//SysUser operator = SessionUtils.getLoginInfo(req);
			
			
			
			if(sysUserService.updateLeaveOfAbsence(sysUserId ,status ,cardid ,effectiveDate ,expirationDate ) )
			{
				sysUserAbsenceService.updateLeaveOfAbsence(sysUserId ,status,effectiveDate ,expirationDate );
			}
//			result = sysUserService.updateSelfInfo(operator, userName, password, newPassword);
//			if(!StringUtils.hasText(result) && StringUtils.hasText(userName)){
//				HttpSession session = req.getSession();
//				session.setAttribute("userName", userName);
//			}
//			map = createResponseMsg(!StringUtils.hasText(result), Constant.SUCCESS, result);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + "ajaxUpdate error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	

	
}
