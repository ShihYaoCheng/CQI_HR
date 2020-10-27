package com.cqi.hr.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cqi.hr.config.SessionInfo;
import com.cqi.hr.entity.SysUser;

public class SessionUtils {
	private static final String SESSION_INFO = "__session_info";
	private static final String ASANA_INFO = "__asana_info";
	private static final String ASANA_REFRESH_TOKEN = "__asana_refresh_token";
	private static final String ASANA_CODE = "__asana_refresh_code";
	
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
	
	public static final void setAsanaToken(HttpServletRequest request, String token, String refreshToken, String code) {
		if(request!=null) {
			HttpSession session = request.getSession();
			session.setAttribute(ASANA_INFO, token);
			session.setAttribute(ASANA_REFRESH_TOKEN, refreshToken);
			session.setAttribute(ASANA_CODE, code);
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
	
	public static final String getAsanaRefreshToken(HttpServletRequest request) {
		String refreshToken = null;
		if(request!=null) {
			HttpSession session = request.getSession();
			refreshToken = (String) session.getAttribute(ASANA_REFRESH_TOKEN);			
		}
		return refreshToken;
	}
	
	public static final String getAsanaCode(HttpServletRequest request) {
		String code = null;
		if(request!=null) {
			HttpSession session = request.getSession();
			code = (String) session.getAttribute(ASANA_CODE);			
		}
		return code;
	}
}
