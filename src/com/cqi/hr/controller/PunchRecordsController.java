package com.cqi.hr.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.PunchRecords;
import com.cqi.hr.service.PunchRecordsService;
import com.cqi.hr.service.SysUserService;


@Controller
@RequestMapping("/security/punchRecords")
public class PunchRecordsController extends AbstractController<PunchRecords> {	
	@Resource SysUserService sysUserService;
	@Resource PunchRecordsService punchRecordsService;
	private static String FUNCTION_NAME = "PunchRecords Record"; 
	
	@RequestMapping(method=RequestMethod.GET)
	public String index(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " index");
		try {
			
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "/punchRecords/punchRecords-list";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="ajaxDataLoading")
	public String ajaxDataLoading(HttpServletRequest req, ModelMap model, Integer page) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		try{
			model.addAttribute("cardMap", sysUserService.getUserCardMapping());
			PagingList<PunchRecords> punchRecordsList = punchRecordsService.getListByPage(page);
			createPagingInfo(model, punchRecordsList);
		} catch (Exception e) {
			logger.debug("ajaxDataLoading " + FUNCTION_NAME + " fail : ", e);
		}
		return "/punchRecords/punchRecords-list.table";
	}
}
