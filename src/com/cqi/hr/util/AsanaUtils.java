package com.cqi.hr.util;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.asana.Client;
import com.asana.OAuthApp;
import com.asana.models.Task;
import com.asana.models.User;
import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.CompanyLeave;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.entity.UserAskForOvertime;

public class AsanaUtils {
	private static Logger logger = Logger.getLogger("AsanaUtils");
	public static boolean addLeaveTask(String token, SysUser sysUser, UserAskForLeave leave, Map<Long, CompanyLeave> leaveMapping) {
		try {
			Client client = getAsanaOAuth(token);
			client.headers.put("Asana-Enable", "string_ids");
	        User me = client.users.me().execute();
	        logger.info("me=" + me.name);
	        Task demoTask = client.tasks.createInWorkspace(sysUser.getDefaultWorkspacesId())
	                .data("name", getAsanaTaskName(sysUser, leave, leaveMapping))
	                .data("notes", leave.getDescription())
	                .data("projects", sysUser.getDefaultProjectId())
	                .data("assignee", sysUser.getSysUserId())
	                .data("due_on", new SimpleDateFormat("yyyy-MM-dd").format(leave.getStartTime()))
	                .execute();
	        logger.info("Task " + demoTask);
	        logger.info("Task " + demoTask.gid + " created.");
	        leave.setAsanaTaskId(demoTask.gid);
	        return true;
		} catch (IOException e) {
			logger.info("addLeaveTask IOException : " + e.getMessage());
		}
        return false;
	}
	
	public static boolean updateLeaveTask(String token, SysUser sysUser, UserAskForLeave leave, Map<Long, CompanyLeave> leaveMapping) {
		try {
			Client client = getAsanaOAuth(token);
			client.headers.put("Asana-Enable", "string_ids");
	        User me = client.users.me().execute();
	        logger.info("me=" + me.name);
	        
	        Task demoTask = client.tasks.update(leave.getAsanaTaskId())
	                .data("name", getAsanaTaskName(sysUser, leave, leaveMapping))
	                .data("notes", leave.getDescription())
	                .data("assignee", me.gid)
	                .data("due_on", new SimpleDateFormat("yyyy-MM-dd").format(leave.getStartTime()))
	                .execute();
	        logger.info("Task " + demoTask.gid + " update.");
	        return true;
		} catch (IOException e) {
			logger.error("updateLeaveTask Exception : ", e);
		}
        return false;
	}
	
	public static boolean deleteLeaveTask(String token, UserAskForLeave leave) {
		try {
			Client client = getAsanaOAuth(token);
			client.headers.put("Asana-Enable", "string_ids");
	        User me = client.users.me().execute();
	        logger.info("me=" + me.name);
	        
	        Task demoTask = client.tasks.delete(leave.getAsanaTaskId())
	                .execute();
	        logger.info("Task " + leave.getAsanaTaskId() + " delete.");
	        return true;
		}catch (Exception e) {
			logger.error("deleteAsanaTask Exception : ", e);
		}
		return false;
	}
	
	public static boolean addOvertimeTask(String token, SysUser sysUser, UserAskForOvertime overtime, Map<Long, CompanyLeave> leaveMapping) {
		try {
			Client client = getAsanaOAuth(token);
			client.headers.put("Asana-Enable", "string_ids");
	        User me = client.users.me().execute();
	        logger.info("me=" + me.name);
	        Task demoTask = client.tasks.createInWorkspace(sysUser.getDefaultWorkspacesId())
	                .data("name", getAsanaTaskNameForOvertime(sysUser, overtime, leaveMapping))
	                .data("notes", overtime.getDescription())
	                .data("projects", sysUser.getDefaultProjectId())
	                .data("assignee", me.gid)
	                .data("due_on", new SimpleDateFormat("yyyy-MM-dd").format(overtime.getStartTime()))
	                .execute();
	        logger.info("Task " + demoTask.gid + " created.");
	        overtime.setAsanaTaskId(demoTask.gid);
	        return true;
		} catch (IOException e) {
			logger.info("addOvertimeTask IOException : " + e.getMessage());
		}
        return false;
	}
	
	public static boolean updateOvertimeTask(String token, SysUser sysUser, UserAskForOvertime overtime, Map<Long, CompanyLeave> leaveMapping) {
		try {
			Client client = getAsanaOAuth(token);
			client.headers.put("Asana-Enable", "string_ids");
	        User me = client.users.me().execute();
	        logger.info("me=" + me.name);
	        
	        Task demoTask = client.tasks.update(overtime.getAsanaTaskId())
	                .data("name", getAsanaTaskNameForOvertime(sysUser, overtime, leaveMapping))
	                .data("notes", overtime.getDescription())
	                .data("assignee", me.gid)
	                .data("due_on", new SimpleDateFormat("yyyy-MM-dd").format(overtime.getStartTime()))
	                .execute();
	        logger.info("Task " + demoTask.gid + " update.");
	        return true;
		} catch (IOException e) {
			logger.error("updateOvertimeTask Exception : ", e);
		}
        return false;
	}
	
	public static boolean deleteOvertimeTask(String token, UserAskForOvertime overtime) {
		try {
			Client client = getAsanaOAuth(token);
			client.headers.put("Asana-Enable", "string_ids");
	        User me = client.users.me().execute();
	        logger.info("me=" + me.name);
	        
	        Task demoTask = client.tasks.delete(overtime.getAsanaTaskId())
	                .execute();
	        logger.info("Task " + overtime.getAsanaTaskId() + " delete.");
	        return true;
		}catch (Exception e) {
			logger.error("deleteAsanaTask Exception : ", e);
		}
		return false;
	}
	
	public static Client getAsanaOAuth(String token) {
		OAuthApp app;
		if(StringUtils.hasText(token)) {
			app = new OAuthApp(Constant.ASANA_CLIENT_ID,
					Constant.ASANA_CLIENT_SECRET,
					Constant.ASANA_REDIRECT_URL,
					token
				);
		}else {
			app = new OAuthApp(Constant.ASANA_CLIENT_ID,
					Constant.ASANA_CLIENT_SECRET,
					Constant.ASANA_REDIRECT_URL
				);
		}
		return Client.oauth(app);
	}
	
	public static String getAsanaTaskName(SysUser sysUser, UserAskForLeave leave, Map<Long, CompanyLeave> leaveMapping) {
		SimpleDateFormat dayMonthFormat = new SimpleDateFormat("MM/dd");
		SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
		StringBuilder asanaName = new StringBuilder();
        asanaName.append("[" + leaveMapping.get(leave.getLeaveId()).getLeaveName() + "]");
        asanaName.append(" " + sysUser.getUserName());
        if(DateUtils.isSameDay(leave.getStartTime(), leave.getEndTime())) {
        	asanaName.append(" " + dayMonthFormat.format(leave.getStartTime()));	        	
        }else {
        	asanaName.append(" " + dayMonthFormat.format(leave.getStartTime()) + "~" + dayMonthFormat.format(leave.getEndTime()));
        }
        asanaName.append(" " + leaveMapping.get(leave.getLeaveId()).getLeaveName());
        
        if(leave.getSpendTime()>20) {
        	asanaName.append(new DecimalFormat("#").format(leave.getSpendTime()));
        }else {
        	asanaName.append(StringUtils.chineseName[leave.getSpendTime().intValue()]);
        }
        if(leaveMapping.get(leave.getLeaveId()).getUnitType().equals(1)) {
        	asanaName.append("天");
        }else {
        	asanaName.append("小時");
        }
        
        if(DateUtils.isSameDay(leave.getStartTime(), leave.getEndTime())) {
        	asanaName.append(" " + hourFormat.format(leave.getStartTime()) + "~" + hourFormat.format(leave.getEndTime()));	        	
        }else {
        	asanaName.append(" " + dayMonthFormat.format(leave.getStartTime()) + " " + hourFormat.format(leave.getStartTime()) + " ~ " + dayMonthFormat.format(leave.getEndTime()) + " " + hourFormat.format(leave.getEndTime()) );
        }
        asanaName.append(" [CqiServ]");
        return asanaName.toString();
	}
	
	public static String getAsanaTaskNameForOvertime(SysUser sysUser, UserAskForOvertime overtime, Map<Long, CompanyLeave> leaveMapping) {
		SimpleDateFormat dayMonthFormat = new SimpleDateFormat("MM/dd");
		SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
		StringBuilder asanaName = new StringBuilder();
        asanaName.append("[" + leaveMapping.get(overtime.getOvertimeId()).getLeaveName() + "]");
        asanaName.append(" " + sysUser.getUserName());
        if(DateUtils.isSameDay(overtime.getStartTime(), overtime.getEndTime())) {
        	asanaName.append(" " + dayMonthFormat.format(overtime.getStartTime()));	        	
        }else {
        	asanaName.append(" " + dayMonthFormat.format(overtime.getStartTime()) + "~" + dayMonthFormat.format(overtime.getEndTime()));
        }
        asanaName.append(" " + leaveMapping.get(overtime.getOvertimeId()).getLeaveName());
        
        if(overtime.getSpendTime()>20) {
        	asanaName.append(new DecimalFormat("#").format(overtime.getSpendTime()));
        }else {
        	asanaName.append(StringUtils.chineseName[overtime.getSpendTime().intValue()]);
        }
        if(leaveMapping.get(overtime.getOvertimeId()).getUnitType().equals(1)) {
        	asanaName.append("天");
        }else {
        	asanaName.append("小時");
        }
        
        if(DateUtils.isSameDay(overtime.getStartTime(), overtime.getEndTime())) {
        	asanaName.append(" " + hourFormat.format(overtime.getStartTime()) + "~" + hourFormat.format(overtime.getEndTime()));	        	
        }else {
        	asanaName.append(" " + dayMonthFormat.format(overtime.getStartTime()) + " " + hourFormat.format(overtime.getStartTime()) + " ~ " + dayMonthFormat.format(overtime.getEndTime()) + " " + hourFormat.format(overtime.getEndTime()) );
        }
        asanaName.append(" [CqiServ]");
        return asanaName.toString();
	}
}
