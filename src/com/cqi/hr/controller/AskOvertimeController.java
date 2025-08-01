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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.entity.UserAskForOvertime;
import com.cqi.hr.entity.UserLeave;
import com.cqi.hr.entity.UserShiftQuota;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.UserAskForLeaveService;
import com.cqi.hr.service.UserAskForOvertimeService;
import com.cqi.hr.service.UserLeaveService;
import com.cqi.hr.service.UserShiftQuotaService;
import com.cqi.hr.util.AsanaUtils;
import com.cqi.hr.util.DateUtils;
import com.cqi.hr.util.SessionUtils;
import com.cqi.hr.util.StringUtils;


@Controller
@RequestMapping("/security/askOvertime")
public class AskOvertimeController extends AbstractController<UserAskForOvertime> {	
	@Resource SysUserService sysUserService;
	@Resource UserLeaveService userLeaveService;
	@Resource UserShiftQuotaService userOvertimeQuotaService;
	@Resource UserAskForOvertimeService userAskForOvertimeService;

	@Resource UserAskForLeaveService userAskForLeaveService;
	
	private static String FUNCTION_NAME = "Ask Overtime"; 
	
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
		return "/askOvertime/askOvertime-list";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxDataLoading")
	public String ajaxDataLoading(HttpServletRequest req, String searchUserName, Integer page, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			model.addAttribute("mappingOvertime", userLeaveService.getCompanyOvertimeMapping());
			PagingList<UserAskForOvertime> askOvertimeList = userAskForOvertimeService.getListByPage(page, operator.getSysUserId());
			createPagingInfo(model, askOvertimeList);

			model.addAttribute("operator",operator);
			model.addAttribute("mapEnableRule2User", sysUserService.getMapEnableRule2User());
		} catch (Exception e) {
			logger.debug(FUNCTION_NAME + " ajaxDataLoading error: ", e);
		}
		return "/askOvertime/askOvertime-list.table";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxOvertimeQuota")
	public String ajaxOvertimeQuota(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxOvertimeQuota");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			//使用者剩餘的假期
			List<UserShiftQuota> userOvertimeList = userOvertimeQuotaService.getListByUserId(operator.getSysUserId());
			model.addAttribute("userOvertimeList", userOvertimeList);
			model.addAttribute("mapEnableRule2User", sysUserService.getMapEnableRule2User());
		} catch (Exception e) {
			logger.debug(FUNCTION_NAME + " ajaxDataLoading error: ", e);
		}
		return "/askOvertime/askOvertimeQuota-list.table";
	}
	
	
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
						if(isSuccessLeave || isSuccessOvertime){
							String token = SessionUtils.getAsanaToken(req);
							if(token != null) {
								boolean addLeaveTaskSucceed = AsanaUtils.addLeaveTask(token, operator, userAskForLeave, userLeaveService.getCompanyLeaveMapping());
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
	
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{userAskForOvertimeId}")
	public void ajaxDelete(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long userAskForOvertimeId){
		logger.info(FUNCTION_NAME + " ajaxDelete: " + userAskForOvertimeId);
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			
			String result = "";
			boolean isSuccess = userAskForOvertimeService.deleteAskOvertime(userAskForOvertimeId, operator);
			if(isSuccess){
				String token = SessionUtils.getAsanaToken(req);
				if(token != null) {
					UserAskForOvertime userAskForOvertime = userAskForOvertimeService.get(userAskForOvertimeId);
					boolean deleteTaskSucceed = AsanaUtils.deleteOvertimeTask(token, userAskForOvertime);
					if(!deleteTaskSucceed) {
						userAskForOvertime.setDescription("Asana刪除Task失敗\n" + userAskForOvertime.getDescription());
						//將Asana狀況儲存
						userAskForOvertimeService.update(userAskForOvertime);
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
	
	/**
	 * 當月統計
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET, value="summary")
	public String indexSummary(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " list Summary");
		try {
			model.addAttribute("nowCalendar", Calendar.getInstance());
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "/askOvertime/summary/askOvertimeSummary-list";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="summary/ajaxDataLoading")
	public String ajaxDataLoadingSummary(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxDataLoadingSummary");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			model.addAttribute("mappingOvertime", userLeaveService.getCompanyOvertimeMapping());
			List<UserAskForOvertime> askOvertimeList = userAskForOvertimeService.getSumListGroupByNow(operator.getSysUserId());
			model.addAttribute("dataList", askOvertimeList);
		} catch (Exception e) {
			logger.debug(FUNCTION_NAME + " ajaxDataLoading error: ", e);
		}
		return "/askOvertime/summary/askOvertimeSummary-list.table";
	}
}
