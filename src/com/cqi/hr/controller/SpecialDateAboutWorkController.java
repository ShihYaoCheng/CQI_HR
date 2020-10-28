package com.cqi.hr.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SpecialDateAboutWork;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.service.SpecialDateAboutWorkService;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.util.SessionUtils;
import net.sf.json.JSONArray;


@Controller
@RequestMapping("/security/specialDateAboutWork")
public class SpecialDateAboutWorkController extends AbstractController<SpecialDateAboutWork> {	
	@Resource SysUserService sysUserService;
	@Resource SpecialDateAboutWorkService specialDateAboutWorkService;
	private static String FUNCTION_NAME = "specialDateAboutWork"; 
	
	@RequestMapping(method=RequestMethod.GET)
	public String list(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " list");
		try {
			model.addAttribute("nowCalendar", Calendar.getInstance());
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + " list error:", e);
		}
		return "/specialDateAboutWork/list";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxDataLoading")
	public String ajaxDataLoading(HttpServletRequest req, Integer page, ModelMap model) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			logger.debug("operator : " + BeanUtils.describe(operator).toString());
			PagingList<SpecialDateAboutWork> dataList = specialDateAboutWorkService.getListByPage(page);
			logger.debug("Data size : " + dataList.getDatas().size());
			createPagingInfo(model, dataList);
		} catch (Exception e) {
			logger.error("ajaxDataLoading " + FUNCTION_NAME + " fail : ", e);
		}
		return "/specialDateAboutWork/list.table";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="ajaxCalendarDataLoading")
	public void ajaxCalendarDataLoading(HttpServletRequest req, HttpServletResponse resp, String start, String end) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		JSONArray jsonArray = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try{
			Date startDate = sdf.parse(start);
			Date endDate = sdf.parse(end);
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser databaseUser = sysUserService.get(operator.getSysUserId());
			if(databaseUser!=null){
				jsonArray = specialDateAboutWorkService.getCalendarSimpleData(startDate, endDate);
			}else{
				jsonArray = new JSONArray();
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxCalendarDataLoading error: ", e);
			jsonArray = new JSONArray();
		}
		returnJsonArray(req, resp, jsonArray);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{dateId}")
	public void ajaxQuery(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long dateId){
		logger.info(FUNCTION_NAME + " ajaxQuery: " + dateId);
		Map<Object, Object> map = null;
		try{
			SpecialDateAboutWork data = specialDateAboutWorkService.get(dateId);
			map = createResponseMsg(true, "", "");
			map.put("specialDateAboutWork", data);
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxQuery error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/add")
	public void ajaxAdd(HttpServletRequest req, HttpServletResponse resp, @Valid SpecialDateAboutWork specialDateAboutWork){
		logger.info(FUNCTION_NAME + " ajaxAdd: ");
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				SpecialDateAboutWork dbData = specialDateAboutWorkService.getOneByDate(specialDateAboutWork.getTheDay());
				if(null == dbData) {
					specialDateAboutWork.setStatus(Constant.STATUS_ENABLE);
					specialDateAboutWork.setCreateDate(new Date());
					specialDateAboutWorkService.persist(specialDateAboutWork);
					map = createResponseMsg(true, "", "");
				}else {
					map = createResponseMsg(false, "", Constant.DATA_DUPLICATED);
				}
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxAdd error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/update")
	public void ajaxUpdate(HttpServletRequest req, HttpServletResponse resp, @Valid SpecialDateAboutWork specialDateAboutWork){
		logger.info(FUNCTION_NAME + " ajaxUpdate: ");
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				SpecialDateAboutWork dbData = specialDateAboutWorkService.get(specialDateAboutWork.getDateId());
				dbData.setIsWorkDay(specialDateAboutWork.getIsWorkDay());
				dbData.setTheDay(specialDateAboutWork.getTheDay());
				dbData.setUpdateDate(new Date());
				specialDateAboutWorkService.update(dbData);
				map = createResponseMsg(true, "", "");
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxUpdate error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{autoId}")
	public void ajaxDisable(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long dateId){
		logger.info(FUNCTION_NAME + " ajaxDisable: " + dateId);
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				SpecialDateAboutWork data = specialDateAboutWorkService.get(dateId);
				if(null!=data) {
					data.setStatus(Constant.STATUS_DISABLE);
					data.setUpdateDate(new Date());
					specialDateAboutWorkService.update(data);
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
	public void ajaxEnable(HttpServletRequest req, HttpServletResponse resp, @PathVariable Long dateId){
		logger.info(FUNCTION_NAME + " ajaxEnable: " + dateId);
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				SpecialDateAboutWork data = specialDateAboutWorkService.get(dateId);
				if(null!=data) {
					data.setStatus(Constant.STATUS_ENABLE);
					data.setUpdateDate(new Date());
					specialDateAboutWorkService.update(data);
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
	
	@RequestMapping(method=RequestMethod.POST, value="uploadFile")
	public @ResponseBody void ajaxUploadFile(MultipartHttpServletRequest req, HttpServletResponse resp){
		logger.info(FUNCTION_NAME + " ajaxUploadFile: ");
		Map<Object, Object> map = null;
		try{
			SysUser operator = SessionUtils.getLoginInfo(req);
			
			if(operator == null){
				map = createResponseMsg(false, "", "請重新登入");
			}else{
				Iterator<String> itr =  req.getFileNames();
				
				MultipartFile multipartFile = req.getFile(itr.next());
				
				//MultipartFile multipartFile = req.getFile("excelFile");
				logger.info(" ajaxUploadFile: " + multipartFile);
				if(multipartFile!=null) {
					logger.info(" ajaxUploadFile222: " + multipartFile.getOriginalFilename());
					if(multipartFile.getOriginalFilename().contains(".json")) {
						specialDateAboutWorkService.readSpecialDate(multipartFile);
						map = createResponseMsg(true, "", "");
					}else {
						map = createResponseMsg(false, "", "此功能僅匯入json格式檔案，感謝您的支持與愛護!");
					}
				}else {
					logger.error(" ajaxUploadFile222 nullpointexception ");
					map = createResponseMsg(false, "", "找不到檔案，請確認是否已選擇檔案上傳");
				}
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxAdd error: ", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
}
