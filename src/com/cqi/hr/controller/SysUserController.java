package com.cqi.hr.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.util.AsanaUtils;
import com.cqi.hr.util.SessionUtils;
import com.cqi.hr.util.StringUtils;


@Controller
@RequestMapping("/security/sysUser")
public class SysUserController extends AbstractController<SysUser> {	
	@Resource SysUserService sysUserService;
	private static String FUNCTION_NAME = "sys user"; 
	
	@RequestMapping(method=RequestMethod.GET)
	public String list(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " list");
		try {
			model.addAttribute("sysRoleList", sysUserService.getSysRoleList());
			model.addAttribute("projectMap", AsanaUtils.getTeamProject(Constant.CQI_GAMES_ASANA_TOKEN));
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "/sysUser/sysUser-list";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxDataLoading")
	public String ajaxDataLoading(HttpServletRequest req, String searchUserName, Integer page, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		try {
			PagingList<SysUser> sysUserList = sysUserService.getListByPage(page, searchUserName);
			createPagingInfo(model, sysUserList);
		} catch (Exception e) {
			logger.debug("ajaxDataLoading " + FUNCTION_NAME + " fail : ", e);
		}
		return "/sysUser/sysUser-list.table";
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/{sysUserId}")
	public void ajaxUpdate(HttpServletRequest req, HttpServletResponse resp, @Valid SysUser sysUser, BindingResult bindingResult){
		logger.info(FUNCTION_NAME + " ajaxUpdate: " + sysUser.getSysUserId());
		logger.info("Test : " + bindingResult.getErrorCount());
		Map<Object, Object> map = null;
		try{
			String result = "";
			SysUser operator = SessionUtils.getLoginInfo(req);
			result = sysUserService.saveOrUpdate(sysUser, operator);
			map = createResponseMsg(!StringUtils.hasText(result), Constant.SUCCESS, result);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + "ajaxUpdate error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public void ajaxAdd(HttpServletRequest req, HttpServletResponse resp, SysUser sysUser){
		logger.info(FUNCTION_NAME + " ajaxAdd: " + sysUser.getSysUserId());
		Map<Object, Object> map = null;
		try{
			String result = "";
			SysUser operator = SessionUtils.getLoginInfo(req);
			
			SysUser suo = sysUserService.get(sysUser.getSysUserId());
			if(suo != null){
				map = createResponseMsg(false, "", "ID重複");
			}else{
				sysUser.setStatus(Constant.SYSUSER_ENABLE);
				sysUser.setPassword(sysUser.getPassword());
				result = sysUserService.saveOrUpdate(sysUser, operator);
				map = createResponseMsg(!StringUtils.hasText(result), Constant.SUCCESS, result);
			}
		}catch(Exception e){
			logger.error("mangaRole ajaxSave error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxResetPasswrod")
	public void ajaxResetPassword(HttpServletRequest req, HttpServletResponse resp, String sysUserId){
		logger.info(FUNCTION_NAME + " ajaxResetPassword: " + sysUserId);
		Map<Object, Object> map = null;
		try{
			String result = "";
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser suo = sysUserService.get(sysUserId);
			suo.setPassword(suo.getSysUserId());
			result = sysUserService.saveOrUpdate(suo, operator);
			map = createResponseMsg(!StringUtils.hasText(result), Constant.SUCCESS, result);
			
		}catch(Exception e){
			logger.error("mangaRole ajaxResetPassword error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{sysUserId}")
	public void ajaxDelete(HttpServletRequest req, HttpServletResponse resp, @PathVariable String sysUserId){
		logger.info(FUNCTION_NAME + " ajaxDelete: " + sysUserId);
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(null != operator) {
				SysUser sysUser = sysUserService.get(sysUserId);
				if("y".equalsIgnoreCase(sysUser.getStatus())) {
					sysUser.setStatus("n");
				}else {
					sysUser.setStatus("y");
				}
				sysUserService.update(sysUser);
				map = createResponseMsg(true, Constant.SUCCESS, "");
			}else {
				map = createResponseMsg(false, "", "請重新登入");
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxDelete error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{sysUserId}")
	public void ajaxQuery(HttpServletRequest req, HttpServletResponse resp, @PathVariable String sysUserId){
		logger.info(FUNCTION_NAME + " ajaxQuery: " + sysUserId);
		Map<Object, Object> map = null;
		try{
			String result = "";
			SysUser sysUser = sysUserService.get(sysUserId);
			map = createResponseMsg(!StringUtils.hasText(result), "", result);
			map.put("sysUser", sysUser);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxQuery error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="updateUserInfo")
	public void ajaxUpdateUserSelfInfo(HttpServletRequest req, HttpServletResponse resp, String userName, String password, String newPassword){
		logger.info(FUNCTION_NAME + " ajaxUpdateUserSelfInfo");
		Map<Object, Object> map = null;
		try{
			String result = "";
			SysUser operator = SessionUtils.getLoginInfo(req);
			
			result = sysUserService.updateSelfInfo(operator, userName, password, newPassword);
			if(!StringUtils.hasText(result) && StringUtils.hasText(userName)){
				HttpSession session = req.getSession();
				session.setAttribute("userName", userName);
			}
			map = createResponseMsg(!StringUtils.hasText(result), Constant.SUCCESS, result);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + "ajaxUpdate error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="checkProjectPermission")
	public void ajaxCheckProjectPermission(HttpServletRequest req, HttpServletResponse resp, String sysUserId, String projectId){
		logger.info(FUNCTION_NAME + " ajaxUpdateUserSelfInfo");
		Map<Object, Object> map = null;
		try{
			String result = "";
			//SysUser operator = SessionUtils.getLoginInfo(req);
			if(StringUtils.hasText(sysUserId) && StringUtils.hasText(projectId)) {
				boolean hasPermission = AsanaUtils.checkProjectPermission(Constant.CQI_GAMES_ASANA_TOKEN, sysUserId, projectId);
				map = createResponseMsg(hasPermission, Constant.SUCCESS, result);
			}else {
				map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + "ajaxUpdate error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="u")
	public String self(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " list");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser dbUser = sysUserService.get(operator.getSysUserId());
			model.addAttribute("sysUser", dbUser);
			model.addAttribute("projectMap", AsanaUtils.getTeamProject(Constant.CQI_GAMES_ASANA_TOKEN));
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "/sysUser/sysUser";
	}
}
