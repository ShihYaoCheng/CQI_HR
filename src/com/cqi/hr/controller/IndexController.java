package com.cqi.hr.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.asana.Client;
import com.asana.OAuthApp;
import com.asana.models.Project;
import com.asana.models.Team;
import com.asana.models.User;
import com.asana.models.Workspace;
import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.AttendanceRecord;
import com.cqi.hr.entity.SysFunction;
import com.cqi.hr.entity.SysRole;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.service.AttendanceRecordService;
import com.cqi.hr.service.CreateInfo;
import com.cqi.hr.service.SpecialDateAboutWorkService;
import com.cqi.hr.service.SysFunctionService;
import com.cqi.hr.service.SysPrivilegeService;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.SysUserShiftService;
import com.cqi.hr.service.UserAskForLeaveService;
import com.cqi.hr.service.UserAskForOvertimeService;
import com.cqi.hr.util.DateUtils;
import com.cqi.hr.util.SessionUtils;
import com.cqi.hr.vo.WeatherLocation;
import com.cqi.hr.vo.WeatherTime;
import com.cqi.hr.vo.WeatherType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Controller
public class IndexController extends AbstractController<CreateInfo> {	
	@Resource SysUserService sysUserService;
	@Resource SysFunctionService sysFunctionService;
	@Resource SysPrivilegeService sysPrivilegeService;
	@Resource UserAskForLeaveService userAskForLeaveService;
	@Resource UserAskForOvertimeService userAskForOvertimeService;
	@Resource SpecialDateAboutWorkService specialDateAboutWorkService;
	@Resource AttendanceRecordService attendanceRecordService;
	@Resource SysUserShiftService sysUserShiftService;
	
	@RequestMapping(value = "/robots.txt")
	public void robots(HttpServletRequest request, HttpServletResponse response) {
	    try {
	        response.getWriter().write("User-agent: *\nDisallow: /\n");
	    } catch (IOException e) {
	    	logger.error("robots", e);
	    }
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/")
	public String welcome(HttpServletRequest req) {
		//logger.info("welcome");
		try {
			SysUser operator = SessionUtils.getLoginInfo(req);
			if(operator!=null) {
				SysUser checkUser = sysUserService.get(operator.getSysUserId());
				if(checkUser!=null) {
					return "redirect:/security/index";
				}
			}
		} catch (Exception e) {
			logger.error("welcome error : ", e);
		}
		return "/login";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/security/index")
	public String index(HttpServletRequest req, Integer page, ModelMap model) {
		logger.info("/index");
		SysUser operator = SessionUtils.getLoginInfo(req);
		try {
			model.addAttribute("attendanceToday", attendanceRecordService.getUserToday(operator));
		} catch (Exception e) {
			logger.error("getUserToday AttendanceRecord Error : ", e);
		}
		return "/index/main-form";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/backstage")
	public String backstage(HttpServletRequest req, Integer page, ModelMap model) {
		logger.info("/backstage");
		
		return "/index_old";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/loginPage")
	public String loginPage(HttpServletRequest req, Integer page, ModelMap model) {
		logger.info("/loginPage");
		
		return "/login";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/login")
	public void login(HttpServletRequest req, HttpServletResponse resp, Integer page, ModelMap model, String userId, String password) {
		logger.info("/check login");
		Map<Object, Object> map = null;
		try{
			logger.info("userId: " + userId);
			userId = userId.toLowerCase();
			SysUser su = sysUserService.get(userId);
			logger.info(BeanUtils.describe(su));
			Boolean result = false;
			if(su == null){
				result = false;
				map = createResponseMsg(false, "", "帳號密碼錯誤，請重新確認您輸入的資訊是否正確!");
			}else if(!su.getPassword().equals(password)){
				result = false;
				map = createResponseMsg(false, "", "帳號密碼錯誤，請重新確認您輸入的資訊是否正確!");
			}else{
				SessionUtils.setLoginInfo(req, su);
				result = true;
				
				//setup menu
			
				Map<String, Map<String, List<SysFunction>>>  menu = new HashMap<String, Map<String, List<SysFunction>>>();
				SysRole sr =  sysFunctionService.getUserMenu(su.getRoleId());
				if(null!=sr){
					List<SysFunction> allFuntionList = sysFunctionService.getList();
					Map<String, Integer> privilegeMap = new HashMap<String,Integer>();
					
					int exist = 0;
					for(SysFunction asf:allFuntionList){
						exist = 0;
						for(SysFunction sf:sr.getSysPrivilegeSet()){
							if(asf.getFunctionId().equals(sf.getFunctionId())){
								exist = 1;
								privilegeMap.put(asf.getFunctionUrl().trim(), 1);
								
								if(Constant.VISIBLE.equals(sf.getIsVisible())){
									Map<String, List<SysFunction>> functionMap = menu.get(sf.getModuleName());
									
									if(functionMap == null){
										functionMap = new HashMap<String, List<SysFunction>>();
										
									}
									
									List<SysFunction> functionList = functionMap.get(sf.getSubModuleName());
									if(functionList == null){
										functionList = new ArrayList<SysFunction>();
									}
									functionList.add(sf);
									functionMap.put(sf.getSubModuleName(), functionList);
									menu.put(sf.getModuleName(), functionMap);
								}
							}
						}
						if(exist == 0){
							privilegeMap.put(asf.getFunctionUrl().trim(), 0);
						}
					}
					HttpSession session = req.getSession();
					session.setAttribute(Constant.ROLE_PRIVILEGE, privilegeMap);
					session.setAttribute("userMenu", menu);
					session.setAttribute("userName", su.getUserName());
					map = createResponseMsg(result, "security/index", "");
				}else{
					map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
				}
			}
		}catch(Exception e){
			logger.error("error:", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/loginAsana")
	public void loginAsana(HttpServletRequest req, HttpServletResponse resp, Integer page, ModelMap model, String userId, String password) {
		logger.info("/check login Asana");
		Map<Object, Object> map = null;
		try{
			OAuthApp app = new OAuthApp(Constant.ASANA_CLIENT_ID,
					Constant.ASANA_CLIENT_SECRET,
					Constant.ASANA_REDIRECT_URL
				);
			Client client = Client.oauth(app);
			client.headers.put("asana-enable", "string_ids");
			
			String state = UUID.randomUUID().toString();
			String url = app.getAuthorizationUrl(state);
			logger.info("app.getAuthorizationUrl : " + url);
			
			 
//			if (request.param("state").equals(state)) {
//				String token = app.fetchToken(request.params("code"));
//				// ... 
//			} else {
//				// error! possible CSRF attack
//				logger.info("/check login Asana");
//			}
//		
//			User me = client.users.me().execute();
//			System.out.println("Hello " + me.name);
			map = createResponseMsg(true, url, "");
		}catch(Exception e){
			logger.error("error:", e);
			map = createResponseMsg(false, "", Constant.NETWORK_BUSY);
		}
		returnJsonMap(req, resp, map);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/asanaCallback")
	public String asanaCallback(HttpServletRequest req, HttpServletResponse resp, Integer page, ModelMap model, String userId, String password) {
		logger.info("asanaCallback");
		try{
			logger.info("state : " + req.getParameter("state"));
			logger.info("code : " + req.getParameter("code"));
			OAuthApp app = new OAuthApp(Constant.ASANA_CLIENT_ID,
					Constant.ASANA_CLIENT_SECRET,
					Constant.ASANA_REDIRECT_URL
				);
			Client client = Client.oauth(app);
			if (!req.getParameter("state").isEmpty()) {
				String token = app.fetchToken(req.getParameter("code"));
				String refreshToken = app.credential.getRefreshToken();
				//logger.info("token : " + token + ", refreshToken : " + refreshToken);
				app = new OAuthApp(Constant.ASANA_CLIENT_ID,
						Constant.ASANA_CLIENT_SECRET,
						Constant.ASANA_REDIRECT_URL,
					    token
					);
				client = Client.oauth(app);
				client.headers.put("Asana-Enable", "string_ids");

		        //System.out.println("isAuthorized=" + app.isAuthorized());
		        User me = client.users.me().execute();
		        logger.info("me=" + me.name);
		        
		        // find your "Personal Projects" project
		        Workspace personalWorkspace = null;
		        for (Workspace workspace : client.workspaces.findAll()) {
		        	//logger.info("workspace : " + workspace);
		        	//logger.info("workspace.id : " + workspace.gid);
		        	//logger.info("workspace.name : " + workspace.name);
		            if (workspace.name.equals("formoga.com")) {
		            	personalWorkspace = workspace;
		            	//logger.info("personalWorkspace=" + personalWorkspace.gid);
		                break;
		            }
		        }
		        
		        // 排除非formoga的人
		        if(personalWorkspace!=null && personalWorkspace.gid.equals("102086551088779")) {
		        	//logger.info("is formoga" );
		        	// find Team
			        Team defaultTeam = null;
			        List<Team> teams = client.teams.findByOrganization(personalWorkspace.gid).execute();
			        //logger.info("teams : " + teams.size());
			        for(Team team:teams) {
			        	//logger.info("team.id : " + team.gid);
			        	//logger.info("team.name : " + team.name);
			        	if(me.name.indexOf("Wendell Chuang")>=0) {
			        		if (team.name.equals("CQI Services")) {
			        			defaultTeam = team;
			        			//logger.info("defaultTeam=" + defaultTeam.name);
				                break;
				            }
			        	}
			        }
			        
			        List<Project> projects = null;
			        if(defaultTeam==null) {
			        	projects = client.projects.findByWorkspace(personalWorkspace.gid).execute();
			        }else {
			        	projects = client.projects.findByTeam(defaultTeam.gid).execute();
			        }
			        Project demoProject = null;
			        for (Project project : projects) {
			        	//logger.info("project.id : " + project.gid);
			        	//logger.info("project.name : " + project.name);
			        	if (project.name.indexOf("通知(請假)")>=0 || project.name.indexOf("請假")>=0) {
			                demoProject = project;
			                //logger.info("demoProject=" + demoProject.name);
			                break;
			            }
			        }
			        if (demoProject == null) {
			        	logger.info("Error: No Default Project" );
					}
			        
			        //logger.info("create SysUser " );
			        SysUser sysUser = new SysUser();
			        sysUser.setSysUserId(me.gid);
			        sysUser.setUserName(me.name);
			        String userRoleId = "2";
			        sysUser.setRoleId(userRoleId);
			        sysUser.setAsanaId(me.gid);
			        sysUser.setDefaultWorkspacesId(personalWorkspace.gid);
			        sysUser.setDefaultWorkspacesName(personalWorkspace.name);
			        sysUser.setDefaultProjectId(demoProject.gid);
			        sysUser.setDefaultProjectName(demoProject.name);
			        sysUser.setEmail(me.email);
			        Calendar calendar = Calendar.getInstance();
			        sysUser.setCreateDate(calendar.getTime());
			        sysUser.setModifyDate(calendar.getTime());
			        sysUser.setStatus(Constant.SYSUSER_ENABLE);
			        SysUser loginedUser = sysUserService.saveOrUpdateAsanaUser(sysUser);
			        if(null == loginedUser) {
			        	logger.error("saveOrUpdateAsanaUser error");
			        	return "redirect:/";
			        }
			        SessionUtils.setLoginInfo(req, loginedUser);
			        SessionUtils.setAsanaToken(req, token, refreshToken, req.getParameter("code"));
					
					//setup menu
			        logger.info("setup menu" );
					Map<String, Map<String, List<SysFunction>>>  menu = new HashMap<String, Map<String, List<SysFunction>>>();
					SysRole sr =  sysFunctionService.getUserMenu(loginedUser.getRoleId());
					if(null!=sr){
						List<SysFunction> allFuntionList = sysFunctionService.getList();
						Map<String, Integer> privilegeMap = new HashMap<String,Integer>();
						
						int exist = 0;
						for(SysFunction asf:allFuntionList){
							exist = 0;
							for(SysFunction sf:sr.getSysPrivilegeSet()){
								if(asf.getFunctionId().equals(sf.getFunctionId())){
									exist = 1;
									privilegeMap.put(asf.getFunctionUrl().trim(), 1);
									
									if(Constant.VISIBLE.equals(sf.getIsVisible())){
										Map<String, List<SysFunction>> functionMap = menu.get(sf.getModuleName());
										
										if(functionMap == null){
											functionMap = new HashMap<String, List<SysFunction>>();
											
										}
										
										List<SysFunction> functionList = functionMap.get(sf.getSubModuleName());
										if(functionList == null){
											functionList = new ArrayList<SysFunction>();
										}
										functionList.add(sf);
										functionMap.put(sf.getSubModuleName(), functionList);
										menu.put(sf.getModuleName(), functionMap);
									}
								}
							}
							if(exist == 0){
								privilegeMap.put(asf.getFunctionUrl().trim(), 0);
							}
						}
						HttpSession session = req.getSession();
						session.setAttribute(Constant.ROLE_PRIVILEGE, privilegeMap);
						session.setAttribute("userMenu", menu);
						session.setAttribute("userName", loginedUser.getUserName());
						return "redirect:/security/index";
					}else{
						return "redirect:/";
					}
		        }else {
		        	// error! possible CSRF attack
					logger.info("is not formoga workspace account");
					return "redirect:/";
		        }
			} else {
				// error! possible CSRF attack
				logger.info("no state");
				return "redirect:/";
			}
		}catch(Exception e){
			logger.error("error:", e);
			return "redirect:/";
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/logout")
	public String logout(HttpServletRequest req, HttpServletResponse resp, Integer page, ModelMap model, String userId, String password) {
		logger.info("/logout");
		SessionUtils.logout(req);
		return "redirect:/loginPage";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/logoutManager")
	public String logoutManager(HttpServletRequest req, HttpServletResponse resp, Integer page, ModelMap model, String userId, String password) {
		logger.info("/logoutManager");
		SessionUtils.logout(req);
		return "redirect:/backstage";
	}
	
	/**
	 * 顯示今日出勤狀況 
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET, value="/today")
	public String today(HttpServletRequest req, ModelMap model) {
		logger.info("today");
		Calendar startWork = DateUtils.clearTime(Calendar.getInstance());
		model.put("startWork", startWork);
		model.put("nowCalendar", Calendar.getInstance());
		try {
			boolean isWorkDay = specialDateAboutWorkService.isWorkDay();
			model.put("isWorkDay", isWorkDay);
			if(isWorkDay) {
				model.put("userList", sysUserService.getEnableUserOderByList("department"));
				model.put("todayLeave", userAskForLeaveService.getTodayLeave());
				model.put("todayOvertime", userAskForOvertimeService.getTodayOvertime());
				model.put("shift", sysUserShiftService.getMapThisMonth());
				Map<String, AttendanceRecord>  a = attendanceRecordService.getMapToday();
				model.put("attendance", attendanceRecordService.getMapToday());
			}
		}catch (Exception e) {
			logger.error("today Exception: ", e);
		}
		return "/today";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/weather")
	public String weather(HttpServletRequest req, ModelMap model) {
		logger.info("weather");
		try {
			HttpClient client = new DefaultHttpClient();  
	        HttpGet getMethod = new HttpGet("http://opendata.cwb.gov.tw/api/v1/rest/datastore/F-D0047-005?Authorization=CWB-F2972DE2-EB6B-44DE-94D9-17522E4455F4&format=JSON&sort=time");
	        
	        HttpResponse response = client.execute(getMethod);
	        // Get the response  
	        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	        StringBuilder responseString = new StringBuilder();
	        String line = "";
	        while ((line = rd.readLine()) != null) {  
	        	responseString.append(line);  
	        }
	        logger.debug("data : " + responseString.toString());
	        JSONObject jsonObject = JSONObject.fromObject(responseString.toString());
	        if(jsonObject.optBoolean("success")) {
	        	JSONObject jsonRecords = jsonObject.optJSONObject("records");
	        	JSONArray jsonArrayLocations = jsonRecords.optJSONArray("locations");
	        	if(jsonArrayLocations.size()==1) {
	        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        		JSONObject jsonCity = jsonArrayLocations.optJSONObject(0);
		        	JSONArray jsonArrayLocation = jsonCity.optJSONArray("location");
		        	Map<String, WeatherLocation> locationMap = new HashMap<>();
		        	for(int i=0;i<jsonArrayLocation.size();i++) {
		        		JSONObject jsonLocation = jsonArrayLocation.optJSONObject(i);
		        		if(jsonLocation.optString("locationName").equals("桃園區")) {
		        			WeatherLocation weatherLocation = new WeatherLocation();
			        		List<WeatherType> weatherTypeList = new ArrayList<>();
			        		weatherLocation.setLocationName(jsonLocation.optString("locationName"));
			        		for(int j=0;j<jsonLocation.optJSONArray("weatherElement").size();j++) {
			        			logger.debug("type size : " + jsonLocation.optJSONArray("weatherElement").size());
			        			JSONObject jsonWeatherType = jsonLocation.optJSONArray("weatherElement").optJSONObject(j);
			        			if(jsonWeatherType.optString("description").equals("天氣現象")) {
				        			WeatherType weatherType = new WeatherType();
				        			List<WeatherTime> weatherTimeList = new ArrayList<>();
				        			weatherType.setWeatherType(jsonWeatherType.optString("description"));
				        			for(int k=0;k<jsonWeatherType.optJSONArray("time").size();k++) {
				        				logger.debug(jsonWeatherType.optString("description") + " time size : " + jsonWeatherType.optJSONArray("time").size());
				        				JSONObject jsonTime = jsonWeatherType.optJSONArray("time").optJSONObject(k);
				        				WeatherTime weatherTime = new WeatherTime();
				        				if(!jsonTime.optString("startTime").isEmpty()) {
					        				weatherTime.setStartTime(sdf.parse(jsonTime.optString("startTime")));
					        				weatherTime.setEndTime(sdf.parse(jsonTime.optString("endTime")));
				        				}
				        				if(jsonTime.optJSONArray("elementValue").size()>0) {
				        					weatherTime.setElementValue(jsonTime.optJSONArray("elementValue").optJSONObject(0).optString("value"));
				        					weatherTimeList.add(weatherTime);
				        					logger.debug("儲存=====================");
				        				}
				        			}
				        			weatherType.setWeatherTimeList(weatherTimeList);
				        			weatherTypeList.add(weatherType);
			        			}
			        		}
			        		weatherLocation.setWeatherTypeList(weatherTypeList);
			        		locationMap.put(jsonLocation.optString("locationName"), weatherLocation);
		        		}
		        	}
		        	model.put("locationMap", locationMap);
	        	}
	        }
//			boolean isWorkDay = specialDateAboutWorkService.isWorkDay();
//			model.put("isWorkDay", isWorkDay);
//			if(isWorkDay) {
//				model.put("userList", sysUserService.getEnableUserOderByList("department"));
//				model.put("todayLeave", userAskForLeaveService.getTodayLeave());
//				model.put("todayOvertime", userAskForOvertimeService.getTodayOvertime());
//			}
		}catch (Exception e) {
			logger.error("weather Exception: ", e);
		}
		return "/weather.table";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/loginLine")
	public String loginLine(HttpServletRequest req, HttpServletResponse resp, @RequestParam String lineId) {
		logger.info("login line");
		try{
			SysUser su = sysUserService.getByLineId(lineId);
			logger.info(BeanUtils.describe(su));
			if(su == null){
				return "/404";
			}else{
				SessionUtils.setLoginInfo(req, su);
				Map<String, Integer> privilegeMap = new HashMap<String,Integer>();
				privilegeMap.put("/security/emergence", 1);
				HttpSession session = req.getSession();
				session.setAttribute(Constant.ROLE_PRIVILEGE, privilegeMap);
				return "redirect:/security/emergence";
			}
		}catch(Exception e){
			logger.error("error:", e);
			return "/404";
		}
	}
}
