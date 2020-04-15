package com.cqi.hr.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.AttendanceRecord;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.service.AttendanceRecordService;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.UserAskForLeaveService;
import com.cqi.hr.util.SessionUtils;
import com.cqi.hr.util.StringUtils;

import net.sf.json.JSONArray;


@Controller
@RequestMapping("/security/attendanceRecord")
public class AttendanceRecordController extends AbstractController<AttendanceRecord> {	
	@Resource SysUserService sysUserService;
	@Resource AttendanceRecordService attendanceRecordService;
	@Resource UserAskForLeaveService userAskForLeaveService;
	private static String FUNCTION_NAME = "Attendance Record"; 
	
	@RequestMapping(method=RequestMethod.GET)
	public String index(HttpServletRequest req, ModelMap model) {
		logger.info(FUNCTION_NAME + " list");
		try {
			model.addAttribute("userList", sysUserService.getUserList());
		} catch (Exception e) {
			logger.error(FUNCTION_NAME + "index error:", e);
		}
		return "/attendance/attendanceRecord-list";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="ajaxDataLoading")
	public void ajaxDataLoading(HttpServletRequest req, HttpServletResponse resp, String start, String end, String sysUserId) {
		logger.info(FUNCTION_NAME + " ajaxDataLoading");
		JSONArray jsonArray = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try{
			Date startDate = sdf.parse(start);
			Date endDate = sdf.parse(end);
			SysUser operator = SessionUtils.getLoginInfo(req);
			SysUser databaseUser = sysUserService.get(operator.getSysUserId());
			if(databaseUser!=null && StringUtils.hasText(sysUserId) && StringUtils.hasText(databaseUser.getSysUserId())){
				jsonArray = userAskForLeaveService.getCalendarSimpleData(startDate, endDate, sysUserId);
				jsonArray.addAll(attendanceRecordService.getMonthlyData(startDate, endDate, sysUserId));
			}else{
				jsonArray = new JSONArray();
			}
		}catch(Exception e){
			logger.error(FUNCTION_NAME + " ajaxCalendarDataLoading error: ", e);
			jsonArray = new JSONArray();
		}
		returnJsonArray(req, resp, jsonArray);
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
					if(multipartFile.getOriginalFilename().indexOf("辰詮")>=0) {
						attendanceRecordService.parser26FloorExcelFile(multipartFile);
						map = createResponseMsg(true, "", "");
					}else if(multipartFile.getOriginalFilename().indexOf("25F")>=0) {
						attendanceRecordService.parser25FloorExcelFile(multipartFile);
						map = createResponseMsg(true, "", "");
					}else {
						map = createResponseMsg(false, "", "檔名無法辨識，26樓打卡紀錄的檔案名稱請以「辰詮」開頭，25樓打卡紀錄的檔案名稱請以「25F」開頭，感謝您的支持與愛護!");
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
