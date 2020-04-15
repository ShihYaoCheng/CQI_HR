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
import com.cqi.hr.entity.SysFunction;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.service.SysFunctionService;
import com.cqi.hr.service.SysRoleService;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.util.SessionUtils;
import com.cqi.hr.util.StringUtils;


@Controller
@RequestMapping("/security/sysFunction")
public class SysFunctionController extends AbstractController<SysFunction> {	
	@Resource SysUserService sysUserService;
	@Resource SysFunctionService sysFunctionService;
	@Resource SysRoleService sysRoleService;
	private static String FUNCTION_NAME = "sys function"; 
	
	@RequestMapping(method=RequestMethod.GET)
	public String list(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " list");
		try {
			
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + " list error:", e);
		}
		return "/sysFunction/sysFunction-list";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxDataLoading")
	public String ajaxDataLoading(HttpServletRequest req, String searchFunctionName, Integer page, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		try {
			PagingList<SysFunction> sysFunctionList = sysFunctionService.getListByPage(page, searchFunctionName);
			createPagingInfo(model, sysFunctionList);
		} catch (Exception e) {
			logger.debug("ajaxDataLoading " + FUNCTION_NAME + " fail : ", e);
		}
		return "/sysFunction/sysFunction-list.table";
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/{functionId}")
	public void ajaxUpdate(HttpServletRequest req, HttpServletResponse resp, @PathVariable String functionId, SysFunction sysFunction){
		logger.info(FUNCTION_NAME + " ajaxUpdate: " + sysFunction.getFunctionName());
		Map<Object, Object> map = null;
		try{
			String result = "";
			SysUser operator = SessionUtils.getLoginInfo(req);
			result = sysFunctionService.saveOrUpdate(sysFunction, operator);
			map = createResponseMsg(!StringUtils.hasText(result), Constant.SUCCESS, result);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + "ajaxUpdate error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public void ajaxAdd(HttpServletRequest req, HttpServletResponse resp, SysFunction sysFunction){
		logger.info(FUNCTION_NAME + " ajaxAdd: " + sysFunction.getFunctionName());
		Map<Object, Object> map = null;
		try{
			String result = "";
			SysUser operator = SessionUtils.getLoginInfo(req);
			
			result = sysFunctionService.saveOrUpdate(sysFunction, operator);
			map = createResponseMsg(!StringUtils.hasText(result), Constant.SUCCESS, result);
			
		}catch(Exception e){
			logger.error("mangaRole ajaxSave error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{functionId}")
	public void ajaxDelete(HttpServletRequest req, HttpServletResponse resp, @PathVariable String functionId){
		logger.info(FUNCTION_NAME + " ajaxDelete: " + functionId);
		Map<Object, Object> map = null;
		try{
			//SysUser operator = SessionUtils.getLoginInfo(req);
			
			String result = "";
			SysFunction sysFunction = sysFunctionService.get(functionId);
			
			sysFunctionService.delete(sysFunction);
			map = createResponseMsg(!StringUtils.hasText(result), Constant.SUCCESS, result);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxDelete error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{functionId}")
	public void ajaxQuery(HttpServletRequest req, HttpServletResponse resp, @PathVariable String functionId){
		logger.info(FUNCTION_NAME + " ajaxQuery: " + functionId);
		Map<Object, Object> map = null;
		try{
			String result = "";
			SysFunction sysFunction = sysFunctionService.get(functionId);
			map = createResponseMsg(!StringUtils.hasText(result), "", result);
			map.put("sysFunction", sysFunction);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxQuery error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
}
