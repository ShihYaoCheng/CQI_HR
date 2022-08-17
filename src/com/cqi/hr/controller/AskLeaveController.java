package com.cqi.hr.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.CompanyLeave;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.entity.UserLeave;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.UserAskForLeaveService;
import com.cqi.hr.service.UserLeaveService;
import com.cqi.hr.util.AsanaUtils;
import com.cqi.hr.util.DateUtils;
import com.cqi.hr.util.SessionUtils;
import com.cqi.hr.util.StringUtils;

import net.sf.json.JSONArray;


@Controller
@RequestMapping("/security/askLeave")
public class AskLeaveController extends AbstractController<UserAskForLeave> {	
	@Resource SysUserService sysUserService;
	@Resource UserLeaveService userLeaveService;
	@Resource UserAskForLeaveService userAskForLeaveService;
	private static String FUNCTION_NAME = "Ask Leave";
	
	@RequestMapping(method=RequestMethod.GET)
	public String index(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " list");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser dataUser = sysUserService.get(operator.getSysUserId());
			if(StringUtils.hasText(dataUser.getGender()) && dataUser.getGender().equals(Constant.GENDER_FEMALE)) {
				model.addAttribute("cqiLeaveList", userLeaveService.getCompanyLeaveList());
			}else {
				model.addAttribute("cqiLeaveList", userLeaveService.getCompanyLeaveListWithoutMenstruation());
			}
			model.addAttribute("dataUser", dataUser);
			model.addAttribute("operator", checkUser);
			model.addAttribute("userMenstruationLeaveThisMonth", userAskForLeaveService.getMenstruationLeaveByUserIdAndMonth(dataUser.getSysUserId(),0));
			model.addAttribute("userMenstruationLeaveLastMonth", userAskForLeaveService.getMenstruationLeaveByUserIdAndMonth(dataUser.getSysUserId(),-1));
			
			//使用者剩餘的假期
			UserLeave userMenstruationLeaveQuota = userLeaveService.getMenstruationLeaveQuotaByUserId(operator.getSysUserId());
			model.addAttribute("userMenstruationLeaveQuota", userMenstruationLeaveQuota);
			
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "/askLeave/askLeave-list";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxDataLoading")
	public String ajaxDataLoading(HttpServletRequest req, String searchUserName, Integer page, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser checkUser = sysUserService.get(operator.getSysUserId());
			if(checkUser!=null && checkUser.getRoleId().equals("1")) {
//				if(searchUserName != null && searchUserName.length() > 0 ) {
//				}
			}else
			{
				//使用者剩餘的假期
				List<UserLeave> userLeaveList = userLeaveService.getListByUserId(operator.getSysUserId());
				model.addAttribute("userLeaveList", userLeaveList);
				model.addAttribute("mappingLeave", userLeaveService.getCompanyLeaveMapping());
				PagingList<UserAskForLeave> askLeaveList = userAskForLeaveService.getListByPage(page, operator.getSysUserId());
				createPagingInfo(model, askLeaveList);
			}			
			model.addAttribute("userMap", sysUserService.getMapEnableRule2User());
		} catch (Exception e) {
			logger.debug(FUNCTION_NAME + " ajaxDataLoading error: ", e);
		}
		return "/askLeave/askLeave-list.table";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxLeaveQuota")
	public String ajaxLeaveQuota(HttpServletRequest req, String searchUserName , ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxLeaveQuota");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser checkUser = sysUserService.get(operator.getSysUserId());
			if(checkUser!=null && checkUser.getRoleId().equals("1")) {				
				List<UserLeave> userLeaveList = new ArrayList<UserLeave>();
				if(   searchUserName != null && searchUserName.length() > 0)
				{
					userLeaveList = userLeaveService.getSearchUserLeave(searchUserName);
				}
				else
				{
					userLeaveList = userLeaveService.getAllUserLeave();					
				}
				model.addAttribute("userLeaveList", userLeaveList);
				model.addAttribute("userMap", sysUserService.getMapEnableRule2User());
				
			}
			else
			{
				//使用者剩餘的假期
				List<UserLeave> userLeaveList = userLeaveService.getListByUserId(operator.getSysUserId());				
				model.addAttribute("userLeaveList", userLeaveList);
				model.addAttribute("userMap", sysUserService.getOneBySysUserId(operator.getSysUserId()));				
			}						
			model.addAttribute("mappingLeave", userLeaveService.getCompanyLeaveMapping());
		} catch (Exception e) {
			logger.debug(FUNCTION_NAME + " ajaxDataLoading error: ", e);
		}
		return "/askLeave/askLeaveQuota-list.table";
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/update")
	public void ajaxUpdate(HttpServletRequest req, HttpServletResponse resp, @Valid UserAskForLeave userAskForLeave){
		logger.info(FUNCTION_NAME + " ajaxUpdate: ");
		Map<Object, Object> map = null;
		try{
			String result = "";
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				String errorMsg = userAskForLeaveService.checkRule(userAskForLeave);
				if(StringUtils.hasText(errorMsg)) {
					map = createResponseMsg(false, "", errorMsg);
				}else {
					if(!DateUtils.isTheSameMonth(userAskForLeave.getStartTime(), userAskForLeave.getEndTime())) {
						map = createResponseMsg(false, "", Constant.DIFFERENT_MONTH);
					}else {
						userAskForLeave.setUpdateDate(new Date());
						boolean isSuccess = userLeaveService.updateUserLeave(userAskForLeave, 2);
						if(isSuccess){
							String token = SessionUtils.getAsanaToken(req);
							if(token != null && StringUtils.hasText(userAskForLeave.getAsanaTaskId())) {
								boolean updateTaskSucceed = AsanaUtils.updateLeaveTask(token, operator, userAskForLeave, userLeaveService.getCompanyLeaveMapping());
								if(!updateTaskSucceed) {
									userAskForLeave.setDescription("Asana更新Task失敗\n" + userAskForLeave.getDescription());
									//將Asana狀況儲存
									userAskForLeaveService.update(userAskForLeave);
								}
							}
							map = createResponseMsg(!StringUtils.hasText(result), Constant.SUCCESS, result);
						}else{
							map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
						}
					}
				}
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxUpdate error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/add")
	public void ajaxAdd(HttpServletRequest req, HttpServletResponse resp, @Valid UserAskForLeave userAskForLeave){
		logger.info(FUNCTION_NAME + " ajaxAdd: ");
		Map<Object, Object> map = null;
		try{
			String result = "";
			SysUser operator = SessionUtils.getLoginInfo(req);
			
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				userAskForLeave.setSysUserId(operator.getSysUserId());
				String errorMsg = userAskForLeaveService.checkRule(userAskForLeave);
				if(StringUtils.hasText(errorMsg)) {
					map = createResponseMsg(false, "", errorMsg);
				}else {
					if(!DateUtils.isTheSameMonth(userAskForLeave.getStartTime(), userAskForLeave.getEndTime())) {
						map = createResponseMsg(false, "", Constant.DIFFERENT_MONTH);
					}else {
						if(userAskForLeave.getLeaveId()==CompanyLeave.SHIFT_MENSTRUATION_ID) {
							SysUser dataUser = sysUserService.get(operator.getSysUserId());
							if(dataUser.getGender()!=null && StringUtils.hasText(dataUser.getGender()) && dataUser.getGender().equals(Constant.GENDER_FEMALE)) {
								userAskForLeave.setStatus(1);
								Calendar calendar = Calendar.getInstance();
								userAskForLeave.setCreateDate(calendar.getTime());
								userAskForLeave.setUpdateDate(calendar.getTime());
								boolean isSuccess = userLeaveService.updateUserLeave(userAskForLeave, 1);
								if(isSuccess){
									String token = SessionUtils.getAsanaToken(req);
									if(token != null) {
										boolean addTaskSucceed = AsanaUtils.addLeaveTask(token, operator, userAskForLeave, userLeaveService.getCompanyLeaveMapping());
										if(!addTaskSucceed) {
											userAskForLeave.setDescription("Asana新增Task失敗\n" + userAskForLeave.getDescription());
										}
										logger.info("Asana Id ajaxAdd : " + userAskForLeave.getAsanaTaskId());
										//將Asana狀況儲存，AsanaId和Description
										userAskForLeaveService.update(userAskForLeave);
									}
									map = createResponseMsg(!StringUtils.hasText(result), Constant.SUCCESS, result);
								}else{
									map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
								}
							}else {
								map = createResponseMsg(false, "", "無法新增，女性才可以請生理假。");
							}
						}else {
							userAskForLeave.setSysUserId(operator.getSysUserId());
							userAskForLeave.setStatus(1);
							Calendar calendar = Calendar.getInstance();
							userAskForLeave.setCreateDate(calendar.getTime());
							userAskForLeave.setUpdateDate(calendar.getTime());
							boolean isSuccess = userLeaveService.updateUserLeave(userAskForLeave, 1);
							if(isSuccess){
								String token = SessionUtils.getAsanaToken(req);
								if(token != null) {
									boolean addTaskSucceed = AsanaUtils.addLeaveTask(token, operator, userAskForLeave, userLeaveService.getCompanyLeaveMapping());
									if(!addTaskSucceed) {
										userAskForLeave.setDescription("Asana新增Task失敗\n" + userAskForLeave.getDescription());
									}
									logger.info("Asana Id ajaxAdd : " + userAskForLeave.getAsanaTaskId());
									//將Asana狀況儲存，AsanaId和Description
									userAskForLeaveService.update(userAskForLeave);
								}
								map = createResponseMsg(!StringUtils.hasText(result), Constant.SUCCESS, result);
							}else{
								map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
							}
						}
					}
				}
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxAdd error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{userAskForLeaveId}")
	public void ajaxDelete(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long userAskForLeaveId){
		logger.info(FUNCTION_NAME + " ajaxDelete: " + userAskForLeaveId);
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			
			String result = "";
			boolean isSuccess = userLeaveService.deleteAskLeave(userAskForLeaveId, operator);
			if(isSuccess){
				String token = SessionUtils.getAsanaToken(req);
				if(token != null) {
					UserAskForLeave userAskForLeave = userAskForLeaveService.get(userAskForLeaveId);
					boolean deleteTaskSucceed = AsanaUtils.deleteLeaveTask(token, userAskForLeave);
					if(!deleteTaskSucceed) {
						userAskForLeave.setDescription("Asana刪除Task失敗\n" + userAskForLeave.getDescription());
						//將Asana狀況儲存
						userAskForLeaveService.update(userAskForLeave);
					}
					map = createResponseMsg(!StringUtils.hasText(result), Constant.SUCCESS, result);
				}else {
					map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
				}
			}else{
				map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxDelete error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{askForLeaveId}")
	public void ajaxQuery(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long askForLeaveId){
		logger.info(FUNCTION_NAME + " ajaxQuery: " + askForLeaveId);
		Map<Object, Object> map = null;
		try{
			String result = "";
			UserAskForLeave userAskForLeave = userAskForLeaveService.get(askForLeaveId);
			map = createResponseMsg(!StringUtils.hasText(result), "", result);
			map.put("userAskForLeave", userAskForLeave);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxQuery error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="calendar")
	public String calendar(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " CalendarData");
		try {
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "CalendarData error:", e);
		}
		return "/index/calendar";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="calendar/ajaxDataLoading")
	public void ajaxCalendarDataLoading(HttpServletRequest req, HttpServletResponse resp, String start, String end, String userId){
		logger.info(FUNCTION_NAME + " ajaxCalendarDataLoading, start : " + start + ", end : " + end);
		JSONArray jsonArray = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try{
			Date startDate = sdf.parse(start);
			Date endDate = sdf.parse(end);
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser databaseUser = sysUserService.get(operator.getSysUserId());
			if(databaseUser!=null && StringUtils.hasText(databaseUser.getSysUserId())){
				jsonArray = userAskForLeaveService.getCalendarData(startDate, endDate, userId);
			}else{
				jsonArray = new JSONArray();
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxCalendarDataLoading error: ", e);
			jsonArray = new JSONArray();
		}
		returnJsonArray(req, resp, jsonArray);
	}
	
}
