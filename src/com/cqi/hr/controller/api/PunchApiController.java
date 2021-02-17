package com.cqi.hr.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cqi.hr.controller.AbstractController;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.service.PunchRecordsService;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.UserAskForLeaveService;
import com.cqi.hr.service.UserLeaveService;
import com.cqi.hr.vo.ResponseVo;




@RestController
@RequestMapping("/punch")
public class PunchApiController extends AbstractController<UserAskForLeave> {	
	@Resource PunchRecordsService punchRecordsService;
	private static String FUNCTION_NAME = "Punch API";
		
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseVo PostRequestPunchRecord(@RequestHeader("token")String token ,@RequestParam("CardID")String cardID,@RequestParam("Time")Long time ){
		logger.info(FUNCTION_NAME + " PostRequestPunchRecord");
		
		if( !token.equals("cqiservices") ) {
			return new ResponseVo(412,"token error","");
		}
		try{
			punchRecordsService.savePunchRecordsAndCreateAttendanceRecord(cardID, time);
			return new ResponseVo(200,"ok","");
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " PostRequestPunchRecord error: ", e);
			return new ResponseVo(500,"server execute error",e.toString());
		}
	}
	
}
