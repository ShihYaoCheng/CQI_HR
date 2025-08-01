package com.cqi.hr.controller;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.SysUserShift;
import com.cqi.hr.entity.WorkFromHome;
import com.cqi.hr.service.LineBotService;
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
	@Resource LineBotService lineBotService;
	private static String FUNCTION_NAME = "WorkFromHome";
	
	@ModelAttribute("UserList")
	public List<SysUser> UserList() throws Exception {
		return sysUserService.getUserList();
	}
	
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
			
			if (checkUser!=null) {
				if(checkUser.getRoleId().equals("1")) {
					WorkFromHomeList = workFromHomeService.getList(page, null);
				}else {
					WorkFromHomeList = workFromHomeService.getList(page, checkUser);
				}
			}
			
			createPagingInfo(model, WorkFromHomeList);
			model.addAttribute("operator",operator);
			model.addAttribute("mapEnableRule2User", sysUserService.getMapEnableRule2User());
			return "/WorkFromHome/workFromHome-table";
		} catch (Exception e) {
			logger.debug(FUNCTION_NAME + " ajaxDataLoading error: ", e);
		}
		return "redirect:/logout";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/add")
	public void ajaxAdd(HttpServletRequest req, HttpServletResponse resp, @Valid WorkFromHome workFromHome){
		logger.info(FUNCTION_NAME + " ajaxAdd: ");
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				Calendar now = Calendar.getInstance();
				workFromHome.setCreateTime(now.getTime());
				workFromHome.setModifyTime(now.getTime());
				workFromHome.setStatus(Constant.STATUS_ENABLE);
				
				Date startDate = workFromHome.getWorkDate();//紀錄起始日期已傳送line給user
				
				String errorMsg = workFromHomeService.addWorkFromHome(workFromHome);
				if(StringUtils.hasText(errorMsg)) {
					map = createResponseMsg(false, "", errorMsg);
				}else {
					workFromHome.setWorkDate(startDate);
					if (lineBotService.sendWFHToUser(workFromHome)) {
						map = createResponseMsg(true, "", "");
					} else {
						map = createResponseMsg(false, "", "line : sendWFHToUser error");
					}
				}
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxAdd error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{workFromHomeId}")
	public void ajaxDelete(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long workFromHomeId){
		logger.info(FUNCTION_NAME + " ajaxDelete: " + workFromHomeId);
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser dataUser = sysUserService.get(operator.getSysUserId());
			if(dataUser == null){
				map = createResponseMsg(false, "", "請重新登入");
			}
			boolean isSuccess = workFromHomeService.deleteWorkFromHome(workFromHomeId,dataUser);
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
	
	@RequestMapping(method=RequestMethod.GET, value="/{workFromHomeId}")
	public void ajaxQuery(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long workFromHomeId){
		logger.info(FUNCTION_NAME + " ajaxQuery: " + workFromHomeId);
		Map<Object, Object> map = null;
		try{
			String result = "";
			WorkFromHome data = workFromHomeService.get(workFromHomeId);
			map = createResponseMsg(!StringUtils.hasText(result), "", result);
			map.put("workFromHome", data);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxQuery error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/update")
	public void ajaxUpdate(HttpServletRequest req, HttpServletResponse resp, @Valid WorkFromHome workFromHome){
		logger.info(FUNCTION_NAME + " ajaxUpdate: ");
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser dataUser = sysUserService.get(operator.getSysUserId());
			if(dataUser == null){
				map = createResponseMsg(false, "", "請重新登入");
			}
			
			String errorMsg = workFromHomeService.updateWorkFromHome(workFromHome,dataUser);
			map = createResponseMsg(!StringUtils.hasText(errorMsg), "", errorMsg);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxUpdate error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	
}
