package com.cqi.hr.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.util.SessionUtils;
import com.cqi.hr.util.StringUtils;

public class SecurityFilter implements Filter {
	protected Logger logger = Logger.getLogger(this.getClass());
	private String       ajaxNoAuthorityURI;
	
	@Override
	public void destroy() {}

	@SuppressWarnings("unchecked")
	@Override
	public void doFilter(ServletRequest servletReq, ServletResponse servletResp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)servletReq;
		HttpServletResponse resp = (HttpServletResponse)servletResp;
		HttpSession session = req.getSession();
		if(ajaxNoAuthorityURI==null) ajaxNoAuthorityURI = req.getContextPath()+"/ajax-error/no-authority";
		
		String path = req.getRequestURI();
		SysUser sysUser = SessionUtils.getLoginInfo(req);
		
		if (path.indexOf("/security") != -1 && !StringUtils.hasText(sysUser.getSysUserId())) {	//前台
			responseWithReLogin(req, resp, "/");
		}else if(path.indexOf("/security") != -1 && StringUtils.hasText(sysUser.getSysUserId())){
			Map<String, Integer> privilegeMap;
			if(session.getAttribute(Constant.ROLE_PRIVILEGE) != null){
				privilegeMap = (Map<String, Integer>)session.getAttribute(Constant.ROLE_PRIVILEGE);
				path = path.substring(path.indexOf("/security"));
				if(privilegeMap.get(path) != null && privilegeMap.get(path.trim()) == 0){
					logger.info("path: " + path);
					if(path.indexOf("ajax") != -1){
						logger.info("ajax");
						resp.sendRedirect(ajaxNoAuthorityURI);
					}else{
						responseWithNoPermission(req, resp, null);
					}
				}else{
					chain.doFilter(servletReq, servletResp);
				}
			}
		}
		else{
			chain.doFilter(servletReq, servletResp);
		}
	}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {}
	
	public void responseWithReLogin(HttpServletRequest req, HttpServletResponse resp, String page) {
		resp.setStatus(401);
		PrintWriter output;
		resp.setContentType("text/html;charset=utf-8");
		try {
			output = resp.getWriter();
			StringBuffer buf = new StringBuffer();
			URL url = new URL(req.getRequestURL().toString());
			buf.append("<script type=\"text/javascript\">window.alert(\"請重新登入\");top.location.href=\"https://")
					.append(url.getHost())
					.append(url.getPort()==-1?"":":")
					.append(url.getPort()==-1?"":url.getPort())
					.append(req.getContextPath().startsWith("/")?"":"/")
					.append(req.getContextPath())
					.append((page != null && !page.equals("/")) ? page : "")
					.append("\";</script>");
			output.println(buf.toString());
			output.close();
		} catch (IOException e) {
			logger.error("Security Filter responseWithReLogin has Error:", e);
		}
	}
	
	public void responseWithNoPermission(HttpServletRequest req, HttpServletResponse resp, String page) {
		resp.setStatus(401);
		PrintWriter output;
		resp.setContentType("text/html;charset=utf-8");
		try {
			output = resp.getWriter();
			StringBuffer buf = new StringBuffer();
			buf.append("<script type=\"text/javascript\">window.alert(\"權限不足\");history.go(-1);</script>");
			output.println(buf.toString());
			output.close();
		} catch (IOException e) {
			logger.error("Security Filter responseWithReLogin has Error:", e);
		}
	}
}
