package com.cqi.hr.controller;

import java.io.IOException;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.asana.Client;
import com.asana.OAuthApp;
import com.asana.models.Project;
import com.asana.models.Team;
import com.asana.models.User;
import com.asana.models.Workspace;
import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.SysFunction;
import com.cqi.hr.entity.SysRole;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.service.CreateInfo;
import com.cqi.hr.service.SpecialDateAboutWorkService;
import com.cqi.hr.service.SysFunctionService;
import com.cqi.hr.service.SysPrivilegeService;
import com.cqi.hr.service.SysUserService;
import com.cqi.hr.service.UserAskForLeaveService;
import com.cqi.hr.service.UserAskForOvertimeService;
import com.cqi.hr.util.DateUtils;
import com.cqi.hr.util.SessionUtils;


@Controller
public class IndexController extends AbstractController<CreateInfo> {	
	@Resource SysUserService sysUserService;
	@Resource SysFunctionService sysFunctionService;
	@Resource SysPrivilegeService sysPrivilegeService;
	@Resource UserAskForLeaveService userAskForLeaveService;
	@Resource UserAskForOvertimeService userAskForOvertimeService;
	@Resource SpecialDateAboutWorkService specialDateAboutWorkService;
	
	@RequestMapping(value = "/robots.txt")
	public void robots(HttpServletRequest request, HttpServletResponse response) {
	    try {
	        response.getWriter().write("User-agent: *\nDisallow: /\n");
	    } catch (IOException e) {
	    	logger.error("robots", e);
	    }
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/security/index")
	public String index(HttpServletRequest req, Integer page, ModelMap model) {
		logger.info("/index");
		
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
			client.headers.put("Asana-Enable", "string_ids");
			if (!req.getParameter("state").isEmpty()) {
				String token = app.fetchToken(req.getParameter("code"));
				logger.info("token : " + token);
				app = new OAuthApp(Constant.ASANA_CLIENT_ID,
						Constant.ASANA_CLIENT_SECRET,
						Constant.ASANA_REDIRECT_URL,
					    token
					);
				client = Client.oauth(app);
				client.headers.put("Asana-Enable", "string_ids");

		        System.out.println("isAuthorized=" + app.isAuthorized());
		        User me = client.users.me().execute();
		        System.out.println("me=" + me.name);
		        
		        // find your "Personal Projects" project
		        Workspace personalWorkspace = null;
		        for (Workspace workspace : client.workspaces.findAll()) {
		        	logger.info("workspace : " + workspace);
		        	logger.info("workspace.id : " + workspace.gid);
		        	logger.info("workspace.name : " + workspace.name);
		            if (workspace.name.equals("formoga.com")) {
		            	personalWorkspace = workspace;
		                break;
		            }
		        }
		        // 排除非formoga的人
		        if(personalWorkspace!=null && personalWorkspace.gid.equals("102086551088779")) {
		        	// find Team
			        Team defaultTeam = null;
			        List<Team> teams = client.teams.findByOrganization(personalWorkspace.gid).execute();
			        logger.info("teams : " + teams.size());
			        for(Team team:teams) {
			        	logger.info("team.id : " + team.gid);
			        	logger.info("team.name : " + team.name);
			        	if(me.name.indexOf("Wendell Chuang")>=0) {
			        		if (team.name.equals("CQI Services")) {
			        			defaultTeam = team;
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
			        	logger.info("project.id : " + project.gid);
			        	logger.info("project.name : " + project.name);
			        	if (project.name.indexOf("通知(請假)")>=0) {
			                demoProject = project;
			                break;
			            }
			        }
//			        Task demoTask = client.tasks.createInWorkspace(personalProjects.id)
//			                .data("name", "demo task created at " + new Date())
//			                .data("projects", Arrays.asList(demoProject.id))
//			                .execute();
//			        System.out.println("Task " + demoTask.id + " created.");
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
			        sysUserService.saveOrUpdateAsanaUser(sysUser);
			        
			        SessionUtils.setLoginInfo(req, sysUser);
			        SessionUtils.setAsanaToken(req, token);
					
					//setup menu
				
					Map<String, Map<String, List<SysFunction>>>  menu = new HashMap<String, Map<String, List<SysFunction>>>();
					SysRole sr =  sysFunctionService.getUserMenu(sysUser.getRoleId());
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
						session.setAttribute("userName", sysUser.getUserName());
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
			}
		}catch (Exception e) {
			logger.error("today Exception: ", e);
		}
		return "/today";
	}
}
