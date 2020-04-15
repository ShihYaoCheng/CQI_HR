package com.cqi.hr.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.ApplyForAutoAskLeave;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.service.ApplyForAutoAskLeaveService;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.UserLeaveService;
import com.cqi.hr.util.DateUtils;
import com.cqi.hr.util.SessionUtils;


@Controller
@RequestMapping("/security/applyForAutoAskLeave")
public class ApplyForAutoAskLeaveController extends AbstractController<ApplyForAutoAskLeave> {	
	@Resource SysUserService sysUserService;
	@Resource ApplyForAutoAskLeaveService applyForAutoAskLeaveService;
	@Resource UserLeaveService userLeaveService;
	private static String FUNCTION_NAME = "applyForAutoAskLeave"; 
	
	@RequestMapping(method=RequestMethod.GET)
	public String index(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " list");
		try {
			model.addAttribute("cqiLeaveList", userLeaveService.getCompanyLeaveList());
			model.addAttribute("nowCalendar", Calendar.getInstance());
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "/applyForAutoAskLeave/list";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxDataLoading")
	public String ajaxDataLoading(HttpServletRequest req, Integer page, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			logger.info("operator : " + BeanUtils.describe(operator).toString());
			PagingList<ApplyForAutoAskLeave> dataList = applyForAutoAskLeaveService.getListByPage(page, operator.getSysUserId());
			logger.info("Data size : " + dataList.getDatas().size());
			
			model.addAttribute("mappingLeave", userLeaveService.getCompanyLeaveMapping());
			createPagingInfo(model, dataList);
		} catch (Exception e) {
			logger.error("ajaxDataLoading " + FUNCTION_NAME + " fail : ", e);
		}
		return "/applyForAutoAskLeave/list.table";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{autoId}")
	public void ajaxQuery(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long autoId){
		logger.info(FUNCTION_NAME + " ajaxQuery: " + autoId);
		Map<Object, Object> map = null;
		try{
			ApplyForAutoAskLeave applyForAutoAskLeave = applyForAutoAskLeaveService.get(autoId);
			map = createResponseMsg(true, "", "");
			map.put("applyForAutoAskLeave", applyForAutoAskLeave);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxQuery error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/add")
	public void ajaxAdd(HttpServletRequest req, HttpServletResponse resp, @Valid ApplyForAutoAskLeave applyForAutoAskLeave){
		logger.info(FUNCTION_NAME + " ajaxAdd: ");
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				Calendar nowCal = Calendar.getInstance();
				Calendar autoCal = Calendar.getInstance();
				autoCal.set(Calendar.YEAR, applyForAutoAskLeave.getDateOfYear());
				autoCal.set(Calendar.MONTH, (applyForAutoAskLeave.getDateOfMonth() - 1));
				if(DateUtils.diff(autoCal.getTime(), nowCal.getTime()) < 0) {
					map = createResponseMsg(false, "", "請勿輸入過去的年月份");
				}else {
					applyForAutoAskLeave.setSysUserId(operator.getSysUserId());
					applyForAutoAskLeave.setStatus(Constant.STATUS_ENABLE);
					applyForAutoAskLeave.setCreateDate(nowCal.getTime());
					applyForAutoAskLeaveService.persist(applyForAutoAskLeave);
					map = createResponseMsg(true, "", "");
				}
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxAdd error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/update")
	public void ajaxUpdate(HttpServletRequest req, HttpServletResponse resp, @Valid ApplyForAutoAskLeave applyForAutoAskLeave){
		logger.info(FUNCTION_NAME + " ajaxUpdate: ");
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				Calendar nowCal = Calendar.getInstance();
				Calendar autoCal = Calendar.getInstance();
				autoCal.set(Calendar.YEAR, applyForAutoAskLeave.getDateOfYear());
				autoCal.set(Calendar.MONTH, (applyForAutoAskLeave.getDateOfMonth() - 1));
				if(DateUtils.diff(autoCal.getTime(), nowCal.getTime()) < 0) {
					map = createResponseMsg(false, "", "請勿輸入過去的年月份");
				}else {
					ApplyForAutoAskLeave dbData = applyForAutoAskLeaveService.get(applyForAutoAskLeave.getAutoId());
					dbData.setDateOfMonth(applyForAutoAskLeave.getDateOfMonth());
					dbData.setDateOfYear(applyForAutoAskLeave.getDateOfYear());
					dbData.setGetIntoOfficesTime(applyForAutoAskLeave.getGetIntoOfficesTime());
					dbData.setUpdateDate(nowCal.getTime());
					applyForAutoAskLeaveService.update(applyForAutoAskLeave);
					map = createResponseMsg(true, "", "");
				}
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxUpdate error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{autoId}")
	public void ajaxDisable(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long autoId){
		logger.info(FUNCTION_NAME + " ajaxDisable: " + autoId);
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				ApplyForAutoAskLeave data = applyForAutoAskLeaveService.get(autoId);
				if(null!=data) {
					data.setStatus(Constant.STATUS_DISABLE);
					data.setUpdateDate(new Date());
					applyForAutoAskLeaveService.update(data);
					map = createResponseMsg(true, "", "");
				}else {
					map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
				}
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxDelete error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.PATCH, value="/{autoId}")
	public void ajaxEnable(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long autoId){
		logger.info(FUNCTION_NAME + " ajaxEnable: " + autoId);
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				ApplyForAutoAskLeave data = applyForAutoAskLeaveService.get(autoId);
				if(null!=data) {
					data.setStatus(Constant.STATUS_ENABLE);
					data.setUpdateDate(new Date());
					applyForAutoAskLeaveService.update(data);
					map = createResponseMsg(true, "", "");
				}else {
					map = createResponseMsg(false, "", Constant.RECORD_NOT_EXIST);
				}
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxDelete error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
}
