package com.cqi.hr.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysRole;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.service.SysFunctionService;
import com.cqi.hr.service.SysRoleService;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.util.SessionUtils;
import com.cqi.hr.util.StringUtils;


@Controller
@RequestMapping("/security/sysRole")
public class SysRoleController extends AbstractController<SysRole> {	
	@Resource SysUserService sysUserService;
	@Resource SysFunctionService sysFunctionService;
	@Resource SysRoleService sysRoleService;
	private static String FUNCTION_NAME = "sys role"; 
	
	@RequestMapping(method=RequestMethod.GET)
	public String list(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " list");
		try {
			model.addAttribute("sysRoleList", sysUserService.getSysRoleList());
			model.addAttribute("sysFunctionList", sysFunctionService.getList());
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + " list error:", e);
		}
		return "/sysRole/sysRole-list";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxDataLoading")
	public String ajaxDataLoading(HttpServletRequest req, String searchRoleName, Integer page, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		try {
			PagingList<SysRole> sysRoleList = sysUserService.getRoleListByPage(page, searchRoleName);
			createPagingInfo(model, sysRoleList);
		} catch (Exception e) {
			logger.debug("ajaxDataLoading " + FUNCTION_NAME + " fail : ", e);
		}
		return "/sysRole/sysRole-list.table";
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/{sysRoleId}")
	public void ajaxUpdate(HttpServletRequest req, HttpServletResponse resp, @PathVariable String sysRoleId, String roleName, String[] sysFunctions){
		logger.info(FUNCTION_NAME + " ajaxUpdate: " + sysRoleId);
		Map<Object, Object> map = null;
		try{
			String result = "";
			SysUser operator = SessionUtils.getLoginInfo(req);
			result = sysRoleService.saveOrUpdate(sysRoleId, roleName, sysFunctions, operator);
			map = createResponseMsg(!StringUtils.hasText(result), Constant.SUCCESS, result);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + "ajaxUpdate error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public void ajaxAdd(HttpServletRequest req, HttpServletResponse resp, String roleName, String[] sysFunctions){
		logger.info(FUNCTION_NAME + " ajaxAdd: " + roleName);
		Map<Object, Object> map = null;
		try{
			if(null==roleName){
				map = createResponseMsg(false, "", "請輸入角色名稱");
			}else if(null==sysFunctions || sysFunctions.length<1){
				map = createResponseMsg(false, "", "請選擇功能項目");
			}else{
				String result = "";
				SysUser operator = SessionUtils.getLoginInfo(req);
				
				result = sysRoleService.saveOrUpdate("", roleName, sysFunctions, operator);
				map = createResponseMsg(!StringUtils.hasText(result), Constant.SUCCESS, result);
			}
		}catch(Exception e){
			logger.error("mangaRole ajaxSave error: ", e);
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
			
			String result = "";
			SysUser sysUser = sysUserService.get(sysUserId);
			if("y".equalsIgnoreCase(sysUser.getStatus()))
				sysUser.setStatus("n");
			else
				sysUser.setStatus("y");
			result = sysUserService.saveOrUpdate(sysUser, operator);
			map = createResponseMsg(!StringUtils.hasText(result), Constant.SUCCESS, result);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxDelete error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{sysRoleId}")
	public void ajaxQuery(HttpServletRequest req, HttpServletResponse resp, @PathVariable String sysRoleId){
		logger.info(FUNCTION_NAME + " ajaxQuery: " + sysRoleId);
		Map<Object, Object> map = null;
		try{
			String result = "";
			SysRole sysRole = sysRoleService.get(sysRoleId);
			map = createResponseMsg(!StringUtils.hasText(result), "", result);
			map.put("sysRole", sysRole);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxQuery error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
}
