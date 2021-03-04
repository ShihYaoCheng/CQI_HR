package com.cqi.hr.controller;

import java.util.List;
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
import com.cqi.hr.entity.MendPunchRecord;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.service.MendPunchRecordService;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.util.AsanaUtils;
import com.cqi.hr.util.SessionUtils;
import com.cqi.hr.util.StringUtils;

@Controller
@RequestMapping("/security/MendPunchRecord")
public class MendPunchRecordController extends AbstractController<MendPunchRecord>{
	@Resource SysUserService sysUserService;
	@Resource MendPunchRecordService mendPunchRecordService;
	private static String FUNCTION_NAME = "Mend Punch Record"; 

	
	@RequestMapping(method=RequestMethod.GET)
	public String index(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " index");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser dataUser = sysUserService.get(operator.getSysUserId());
			if (dataUser !=null) {
				model.addAttribute("operator",operator);
				model.addAttribute("userMap", sysUserService.getUserMapping());
				model.addAttribute("mapEnableRule2User", sysUserService.getMapEnableRule2User());
				return "/MendPunchRecord/mendPunchRecord-view";
			}
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "redirect:/logout";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxDataLoading")
	public String ajaxDataLoading(HttpServletRequest req, Integer page, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser dataUser = sysUserService.get(operator.getSysUserId());
			
			
			PagingList<MendPunchRecord> mendPunchRecordList =new PagingList<MendPunchRecord>() ;
			if(dataUser!=null ) {
				
				if(dataUser.getRoleId().equals("1")) {
					mendPunchRecordList = mendPunchRecordService.getListByPage(page, null);
				}else {
					mendPunchRecordList = mendPunchRecordService.getListByPage(page, dataUser.getSysUserId());
				}
				
			}
			model.addAttribute("operator",operator);
			model.addAttribute("userMap", sysUserService.getUserMapping());
			model.addAttribute("mapEnableRule2User", sysUserService.getMapEnableRule2User());
			createPagingInfo(model, mendPunchRecordList);
			
			return "/MendPunchRecord/mendPunchRecord.table";
		} catch (Exception e) {
			logger.debug(FUNCTION_NAME + " ajaxDataLoading error: ", e);
		}
		return "redirect:/logout";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/add")
	public void ajaxAdd(HttpServletRequest req, HttpServletResponse resp, @Valid MendPunchRecord mendPunchRecord){
		logger.info(FUNCTION_NAME + " ajaxAdd: ");
		Map<Object, Object> map = null;
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser dataUser = sysUserService.get(operator.getSysUserId());
			if(dataUser == null){
				map = createResponseMsg(false, "", "請重新登入");
			}
			boolean isSuccess = mendPunchRecordService.addMendPunchRecord(mendPunchRecord, dataUser);
			if (isSuccess) {
				map = createResponseMsg(true, Constant.SUCCESS, "");
			}
			else {
				map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
			}
			
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxAdd error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{mendId}")
	public void ajaxDelete(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long mendId){
		logger.info(FUNCTION_NAME + " ajaxDelete: " + mendId);
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser dataUser = sysUserService.get(operator.getSysUserId());
			if(dataUser == null){
				map = createResponseMsg(false, "", "請重新登入");
			}
			boolean isSuccess = mendPunchRecordService.deleteMendPunchRecord(mendId, dataUser);
			if(isSuccess){
				map = createResponseMsg(true, Constant.SUCCESS, "");
			}else{
				map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxDelete error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{mendId}")
	public void ajaxUpdate(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long mendId){
		logger.info(FUNCTION_NAME + " ajaxDelete: " + mendId);
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			
			boolean isSuccess = mendPunchRecordService.proveMendPunchRecord(mendId, operator);
			if(isSuccess){
				map = createResponseMsg(true, Constant.SUCCESS, "");
			}else{
				map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxDelete error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
}
