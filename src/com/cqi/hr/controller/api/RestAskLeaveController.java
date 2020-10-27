package com.cqi.hr.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cqi.hr.controller.AbstractController;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.UserAskForLeaveService;
import com.cqi.hr.service.UserLeaveService;


@RestController
@RequestMapping("/restful/askLeave")
public class RestAskLeaveController extends AbstractController<UserAskForLeave> {	
	@Resource SysUserService sysUserService;
	@Resource UserLeaveService userLeaveService;
	@Resource UserAskForLeaveService userAskForLeaveService;
	private static String FUNCTION_NAME = "Resful Ask Leave";
		
	
	@RequestMapping(method=RequestMethod.GET, value="zorUse")
	public Map<String, List<UserAskForLeave>> ajaxZorUse(){
		logger.info(FUNCTION_NAME + " ajaxZorUse");
		try{
			Map<String, List<UserAskForLeave>> map = userAskForLeaveService.getRestTodayLeave();
			return map;
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxZorUse error: ", e);
		}
		return new HashMap<>();
	}
	
}
