package com.cqi.hr.controller;

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
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.entity.UserAskForOvertime;
import com.cqi.hr.entity.UserAskForShift;
import com.cqi.hr.entity.UserLeave;
import com.cqi.hr.entity.UserShiftQuota;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.UserAskForLeaveService;
import com.cqi.hr.service.UserAskForOvertimeService;
import com.cqi.hr.service.UserAskForShiftService;
import com.cqi.hr.service.UserLeaveService;
import com.cqi.hr.service.UserShiftQuotaService;
import com.cqi.hr.util.AsanaUtils;
import com.cqi.hr.util.DateUtils;
import com.cqi.hr.util.SessionUtils;
import com.cqi.hr.util.StringUtils;


@Controller
@RequestMapping("/security/askShift")
public class AskShiftController extends AbstractController<UserAskForOvertime> {	
	@Resource SysUserService sysUserService;
	@Resource UserLeaveService userLeaveService;
	@Resource UserShiftQuotaService userShiftQuotaService;
	@Resource UserAskForOvertimeService userAskForOvertimeService;
	@Resource UserAskForLeaveService userAskForLeaveService;
	@Resource UserAskForShiftService userAskForShiftService;
	
	private static String FUNCTION_NAME = "Ask Shift"; 
	
	@RequestMapping(method=RequestMethod.GET)
	public String index(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " list");
		try {
			model.addAttribute("cqiOvertimeList", userLeaveService.getCompanyOvertimeList());

			SysUser operator = SessionUtils.getLoginInfo(req);
			model.addAttribute("operator",operator);
			model.addAttribute("mapEnableRule2User", sysUserService.getMapEnableRule2User());
			
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "/askShift/askShift";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxDataLoading")
	public String ajaxDataLoading(HttpServletRequest req, String searchUserName, Integer page, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser checkUser = sysUserService.get(operator.getSysUserId());
			model.addAttribute("operator",operator);
			model.addAttribute("mapEnableRule2User", sysUserService.getMapEnableRule2User());
			model.addAttribute("mappingOvertime", userLeaveService.getCompanyOvertimeMapping());
			
			PagingList<UserAskForOvertime> askOvertimeList = new PagingList<UserAskForOvertime>();
			if (checkUser!=null) {
				if(checkUser.getRoleId().equals("1")) {
					askOvertimeList = userAskForOvertimeService.getListByPage(page, null);
				}else {
					askOvertimeList = userAskForOvertimeService.getListByPage(page, operator.getSysUserId());
				}
			}createPagingInfo(model, askOvertimeList);
			
			
			model.addAttribute("mapUserAskForLeaveByOvertimeId", userAskForShiftService.mapUserAskForLeaveByOvertimeId());
			
		} catch (Exception e) {
			logger.debug(FUNCTION_NAME + " ajaxDataLoading error: ", e);
		}
		return "/askShift/askShift.table";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxShiftQuota")
	public String ajaxShiftQuota(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxShiftQuota");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser checkUser = sysUserService.get(operator.getSysUserId());
			//使用者剩餘的假期
			List<UserShiftQuota> userShiftQuotaList = new ArrayList<UserShiftQuota>();
			if(checkUser!=null ) {
				if(checkUser.getRoleId().equals("1")) {
					userShiftQuotaList = userShiftQuotaService.getListByUserId(null);
				}else {
					userShiftQuotaList = userShiftQuotaService.getListByUserId(checkUser.getSysUserId());
				}
				model.addAttribute("operator",operator);
				model.addAttribute("userShiftQuotaList", userShiftQuotaList);
				model.addAttribute("mapEnableRule2User", sysUserService.getMapEnableRule2User());
			}
			 
		} catch (Exception e) {
			logger.debug(FUNCTION_NAME + " ajaxDataLoading error: ", e);
		}
		return "/askShift/askShiftQuota.table";
	}
	
	
	
	@RequestMapping(method=RequestMethod.POST, value="/add")
	public void ajaxAdd(HttpServletRequest req, HttpServletResponse resp, @Valid UserAskForOvertime userAskForOvertime, @Valid Date startTimeLeave, @Valid Date endTimeLeave){
		logger.info(FUNCTION_NAME + " ajaxAdd: ");
		Map<Object, Object> map = null;
		try{
			String result = "";
			SysUser operator = SessionUtils.getLoginInfo(req);
			Calendar calendar = Calendar.getInstance();
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				UserAskForLeave userAskForLeave = new UserAskForLeave();
				userAskForLeave.setSysUserId(operator.getSysUserId());
				userAskForLeave.setLeaveId(userAskForOvertime.getOvertimeId());
				userAskForLeave.setSpendTime(userAskForOvertime.getSpendTime());
				userAskForLeave.setStartTime(startTimeLeave);
				userAskForLeave.setEndTime(endTimeLeave);
				userAskForLeave.setDescription(userAskForOvertime.getDescription());
				
				String errorMsgLeave = userAskForLeaveService.checkRule(userAskForLeave);
				
				
				userAskForOvertime.setSysUserId(operator.getSysUserId());
				String errorMsg = userAskForOvertimeService.checkRule(userAskForOvertime);
				if(StringUtils.hasText(errorMsg) ||StringUtils.hasText(errorMsgLeave)) {
					map = createResponseMsg(false, "", errorMsg);
				}else {
					userAskForLeave.setStatus(1);
					userAskForLeave.setCreateDate(calendar.getTime());
					userAskForLeave.setUpdateDate(calendar.getTime());
					userAskForOvertime.setStatus(1);
					userAskForOvertime.setCreateDate(calendar.getTime());
					userAskForOvertime.setUpdateDate(calendar.getTime());
					if(!DateUtils.isTheSameMonth(userAskForLeave.getStartTime(), userAskForLeave.getEndTime())) {
						map = createResponseMsg(false, "", Constant.DIFFERENT_MONTH);
					}else if( !userAskForOvertimeService.checkEmergenceRule(userAskForOvertime)) {
						map = createResponseMsg(false, "", Constant.EMERGENCE_ILLEGAL);
					}else {
						boolean isSuccessLeave = userLeaveService.updateUserLeave(userAskForLeave, 1);
						boolean isSuccessOvertime = userAskForOvertimeService.updateUserAskOvertime(userAskForOvertime, 1);
						if(isSuccessLeave && isSuccessOvertime){
							//add shift
							userAskForShiftService.addShift(userAskForOvertime,userAskForLeave);
							
							
							String token = SessionUtils.getAsanaToken(req);
							if(token != null) {
								boolean addLeaveTaskSucceed = AsanaUtils.addLeaveTask(token, operator, userAskForLeave, userLeaveService.getCompanyOvertimeMapping());
								if(!addLeaveTaskSucceed) {
									userAskForLeave.setDescription("Asana新增Task失敗\n" + userAskForLeave.getDescription());
								}
								logger.info("Asana Id ajaxAdd : " + userAskForLeave.getAsanaTaskId());
								//將Asana狀況儲存，AsanaId和Description
								userAskForLeaveService.update(userAskForLeave);
							
								boolean addOvertimeTaskSucceed = AsanaUtils.addOvertimeTask(token, operator, userAskForOvertime, userLeaveService.getCompanyOvertimeMapping());
								if(!addOvertimeTaskSucceed) {
									userAskForOvertime.setDescription("Asana新增Task失敗\n" + userAskForOvertime.getDescription());
								}
								logger.info("Asana Id ajaxAdd : " + userAskForOvertime.getAsanaTaskId());
								//將Asana狀況儲存，AsanaId和Description
								userAskForOvertimeService.update(userAskForOvertime);
							}
							map = createResponseMsg(!StringUtils.hasText(result), Constant.SUCCESS, result);
						}else{
							map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
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
	
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{askForOvertimeId}")
	public void ajaxDelete(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long askForOvertimeId){
		logger.info(FUNCTION_NAME + " ajaxDelete: " + askForOvertimeId);
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			
			String result = "";
			
			//get userAskForShift
			UserAskForShift userAskForShift = userAskForShiftService.getOneByOvertimeId(askForOvertimeId);
			
			boolean isOvertimeSuccess = userAskForOvertimeService.deleteAskOvertime(askForOvertimeId, operator);
			boolean isLeaveSuccess = userLeaveService.deleteAskLeave(userAskForShift.getAskForLeaveId(), operator);
			
			if(isOvertimeSuccess && isLeaveSuccess){
				String token = SessionUtils.getAsanaToken(req);
				if(token != null) {
					UserAskForOvertime userAskForOvertime = userAskForOvertimeService.get(askForOvertimeId);
					boolean deleteOvertimeTaskSucceed = AsanaUtils.deleteOvertimeTask(token, userAskForOvertime);
					if(!deleteOvertimeTaskSucceed) {
						userAskForOvertime.setDescription("Asana刪除Task失敗\n" + userAskForOvertime.getDescription());
						//將Asana狀況儲存
						userAskForOvertimeService.update(userAskForOvertime);
					}
					UserAskForLeave userAskForLeave = userAskForLeaveService.get(userAskForShift.getAskForLeaveId());
					boolean deleteLeaveTaskSucceed = AsanaUtils.deleteLeaveTask(token, userAskForLeave);
					if(!deleteLeaveTaskSucceed) {
						userAskForLeave.setDescription("Asana刪除Task失敗\n" + userAskForLeave.getDescription());
						//將Asana狀況儲存
						userAskForLeaveService.update(userAskForLeave);
					}
					map = createResponseMsg(!StringUtils.hasText(result), Constant.SUCCESS, result);
				}else {
					map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
				}
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
	
/*
	@RequestMapping(method=RequestMethod.POST, value="/update")
	public void ajaxUpdate(HttpServletRequest req, HttpServletResponse resp, @Valid UserAskForOvertime userAskForOvertime){
		logger.info(FUNCTION_NAME + " ajaxUpdate: ");
		Map<Object, Object> map = null;
		try{
			String result = "";
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				userAskForOvertime.setUpdateDate(new Date());
				String errorMsg = userAskForOvertimeService.checkRule(userAskForOvertime);
				if(StringUtils.hasText(errorMsg)) {
					map = createResponseMsg(false, "", errorMsg);
				}else {
					boolean isSuccess = userAskForOvertimeService.updateUserAskOvertime(userAskForOvertime, 2);
					if(isSuccess){
						String token = SessionUtils.getAsanaToken(req);
						if(token != null && StringUtils.hasText(userAskForOvertime.getAsanaTaskId())) {
							boolean updateTaskSucceed = AsanaUtils.updateOvertimeTask(token, operator, userAskForOvertime, userLeaveService.getCompanyOvertimeMapping());
							if(!updateTaskSucceed) {
								userAskForOvertime.setDescription("Asana更新Task失敗\n" + userAskForOvertime.getDescription());
								//將Asana狀況儲存
								userAskForOvertimeService.update(userAskForOvertime);
							}
						}
						map = createResponseMsg(!StringUtils.hasText(result), Constant.SUCCESS, result);
					}else{
						map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
					}
				}
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxUpdate error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
*/
	
	@RequestMapping(method=RequestMethod.GET, value="/{askForOvertimeId}")
	public void ajaxQuery(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long askForOvertimeId){
		logger.info(FUNCTION_NAME + " ajaxQuery: " + askForOvertimeId);
		Map<Object, Object> map = null;
		try{
			String result = "";
			UserAskForOvertime userAskForOvertime = userAskForOvertimeService.get(askForOvertimeId);
			map = createResponseMsg(!StringUtils.hasText(result), "", result);
			map.put("userAskForOvertime", userAskForOvertime);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxQuery error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/UserShiftQuota/{userShiftQuotaId}")
	public void ajaxShiftQuotaQuery(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long userShiftQuotaId){
		logger.info(FUNCTION_NAME + " ajaxShiftQuotaQuery: " + userShiftQuotaId);
		Map<Object, Object> map = null;
		try{
			String result = "";
			UserShiftQuota userShiftQuota = userShiftQuotaService.get(userShiftQuotaId);
			map = createResponseMsg(!StringUtils.hasText(result), "", result);
			map.put("userShiftQuota", userShiftQuota);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxQuery error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/UserShiftQuota/update")
	public void ajaxShiftQuotaUpdate(HttpServletRequest req, HttpServletResponse resp, @Valid UserShiftQuota userShiftQuota){
		logger.info(FUNCTION_NAME + " ajaxShiftQuotaUpdate: ");
		Map<Object, Object> map = null;
		try{
			UserShiftQuota dbData = userShiftQuotaService.get(userShiftQuota.getUserShiftQuotaId());
			dbData.setUpdateTime(new Date());
			dbData.setQuota(userShiftQuota.getQuota());
			userShiftQuotaService.update(dbData);
			
			map = createResponseMsg(true, Constant.SUCCESS, "");
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxQuery error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
}
