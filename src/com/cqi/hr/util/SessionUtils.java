package com.cqi.hr.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cqi.hr.config.SessionInfo;
import com.cqi.hr.entity.SysUser;

public class SessionUtils {
	private static final String SESSION_INFO = "__session_info";
	private static final String ASANA_INFO = "__asana_info";
	
	/**
	 * @param request
	 * @return
	 * 一定會回傳SessionInfo不會null。
	 */
	private static final SessionInfo getSessionInfo(HttpServletRequest request) {
		SessionInfo info = null;
		if(request!=null) {
			HttpSession session = request.getSession();
			info = (SessionInfo)session.getAttribute(SESSION_INFO);
			if(info==null) {
				info = new SessionInfo();
				session.setAttribute(SESSION_INFO, info);
			}
			
		}
		return info==null?new SessionInfo():info;
	}
	public static final SysUser getLoginInfo(HttpServletRequest request) {
		return getSessionInfo(request).getLogInInfo();
	}
	public static final void setLoginInfo(HttpServletRequest request, SysUser logInInfo) {
		SessionInfo sessionInfo = getSessionInfo(request);
		sessionInfo.setLogInInfo(logInInfo);
	}
	public static final void logout(HttpServletRequest request){
		HttpSession session = request.getSession();
		session.removeAttribute(SESSION_INFO);
	}
	
	public static final void setAsanaToken(HttpServletRequest request, String token) {
		if(request!=null) {
			HttpSession session = request.getSession();
			session.setAttribute(ASANA_INFO, token);
			
		}
	}
	
	public static final String getAsanaToken(HttpServletRequest request) {
		String token = null;
		if(request!=null) {
			HttpSession session = request.getSession();
			token = (String) session.getAttribute(ASANA_INFO);			
		}
		return token;
	}
}
