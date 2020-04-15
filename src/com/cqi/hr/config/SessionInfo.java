package com.cqi.hr.config;

import com.cqi.hr.entity.SysUser;

public class SessionInfo {
	private SysUser logInInfo;
	
	public SysUser getLogInInfo() {
		if(logInInfo==null) logInInfo = new SysUser();
		return logInInfo;
	}
	
	public void setLogInInfo(SysUser logInInfo) {
		this.logInInfo = logInInfo;
	}
	
}
