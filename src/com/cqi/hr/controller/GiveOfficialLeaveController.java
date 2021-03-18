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
import com.cqi.hr.dao.GiveOfficialLeaveUserListDAO;
import com.cqi.hr.entity.GiveOfficialLeave;
import com.cqi.hr.entity.GiveOfficialLeaveUserList;
import com.cqi.hr.entity.MendPunchRecord;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.SysUserShift;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.service.GiveOfficialLeaveService;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.SysUserShiftService;
import com.cqi.hr.service.UserLeaveService;
import com.cqi.hr.util.AsanaUtils;
import com.cqi.hr.util.SessionUtils;
import com.cqi.hr.util.StringUtils;

@Controller
@RequestMapping("/security/GiveOfficialLeaveManage")
public class GiveOfficialLeaveController extends AbstractController<GiveOfficialLeave> {	
	@Resource SysUserService sysUserService;
	@Resource GiveOfficialLeaveService giveOfficialLeaveService;
	@Resource UserLeaveService userLeaveService;
	
	@Resource GiveOfficialLeaveUserListDAO giveOfficialLeaveUserListDAO;
	private static String FUNCTION_NAME = "Give Official Leave Manage";
	
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
		return "/giveOfficialLeave/giveOfficialLeave";
	}
	

	@RequestMapping(method=RequestMethod.POST, value="ajaxDataLoading")
	public String ajaxDataLoading(HttpServletRequest req, String searchUserName, Integer page, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser checkUser = sysUserService.get(operator.getSysUserId());
			PagingList<GiveOfficialLeave> queryList = new PagingList<GiveOfficialLeave>();
			if(checkUser!=null ) {
				queryList = giveOfficialLeaveService.getList(page);
			}
			model.addAttribute("mappingLeave", userLeaveService.getCompanyLeaveMapping());
			
			createPagingInfo(model, queryList);
			return "/giveOfficialLeave/giveOfficialLeave.table";
		} catch (Exception e) {
			logger.debug(FUNCTION_NAME + " ajaxDataLoading error: ", e);
		}
		return "redirect:/logout";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{giveOfficialLeaveId}")
	public void ajaxQuery(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long giveOfficialLeaveId){
		logger.info(FUNCTION_NAME + " ajaxQuery: " + giveOfficialLeaveId);
		Map<Object, Object> map = null;
		try{
			String result = "";
			List<GiveOfficialLeaveUserList> data = giveOfficialLeaveUserListDAO.getUserList(giveOfficialLeaveId);
			map = createResponseMsg(!StringUtils.hasText(result), "", result);
			map.put("giveOfficialLeaveUserList", data);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxQuery error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/add")
	public void ajaxAdd(HttpServletRequest req, HttpServletResponse resp, @Valid GiveOfficialLeave giveOfficialLeave){
		logger.info(FUNCTION_NAME + " ajaxAdd: ");
		Map<Object, Object> map = null;
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser dataUser = sysUserService.get(operator.getSysUserId());
			if(dataUser == null){
				map = createResponseMsg(false, "", "請重新登入");
			}
			boolean isSuccess = giveOfficialLeaveService.addGiveOfficialLeave(giveOfficialLeave, dataUser);
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
	
	@RequestMapping(method=RequestMethod.POST, value="/save/{giveOfficialLeaveUserListId}")
	public void ajaxUpdate(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long giveOfficialLeaveUserListId, String[] sysUserIds){
		logger.info(FUNCTION_NAME + " ajaxUpdate userList: " + giveOfficialLeaveUserListId);
		Map<Object, Object> map = null;
		try{
			String result = "";
			SysUser operator = SessionUtils.getLoginInfo(req);
			result = giveOfficialLeaveService.saveOfficialLeaveUserList(giveOfficialLeaveUserListId, sysUserIds, operator);
			map = createResponseMsg(!StringUtils.hasText(result), Constant.SUCCESS, result);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + "ajaxUpdate error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{giveOfficialLeaveId}")
	public void ajaxDelete(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long giveOfficialLeaveId){
		logger.info(FUNCTION_NAME + " ajaxDelete: " + giveOfficialLeaveId);
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			
			String result = "";
			boolean isSuccess = giveOfficialLeaveService.deleteOfficialLeave(giveOfficialLeaveId, operator);
			if(isSuccess){
				map = createResponseMsg(!StringUtils.hasText(result), Constant.SUCCESS, result);
				
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
