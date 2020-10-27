package com.cqi.hr.vo;

import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserAskForOvertime;
import com.linecorp.bot.model.message.flex.component.Box;

public class FlexMessageVo {

	private String imageUrl;
	private String altText;
	private SysUser targetUser;
	private SysUser sysUser;
	private UserAskForOvertime userAskForOvertime;
	private Box header;
	private Box body;
	private Box footer;
	private String confirmParam;
	private String rejectParam;
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getAltText() {
		return altText;
	}
	public void setAltText(String altText) {
		this.altText = altText;
	}
	public SysUser getTargetUser() {
		return targetUser;
	}
	public void setTargetUser(SysUser targetUser) {
		this.targetUser = targetUser;
	}
	public SysUser getSysUser() {
		return sysUser;
	}
	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}
	public UserAskForOvertime getUserAskForOvertime() {
		return userAskForOvertime;
	}
	public void setUserAskForOvertime(UserAskForOvertime userAskForOvertime) {
		this.userAskForOvertime = userAskForOvertime;
	}
	public Box getHeader() {
		return header;
	}
	public void setHeader(Box header) {
		this.header = header;
	}
	public Box getBody() {
		return body;
	}
	public void setBody(Box body) {
		this.body = body;
	}
	public Box getFooter() {
		return footer;
	}
	public void setFooter(Box footer) {
		this.footer = footer;
	}
	public String getConfirmParam() {
		return confirmParam;
	}
	public void setConfirmParam(String confirmParam) {
		this.confirmParam = confirmParam;
	}
	public String getRejectParam() {
		return rejectParam;
	}
	public void setRejectParam(String rejectParam) {
		this.rejectParam = rejectParam;
	}
}
