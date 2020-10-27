package com.cqi.hr.config;

import java.io.Serializable;

public class LogInInfo implements Serializable {
	private static final long serialVersionUID = -4746874909179676773L;
	private String userIcon;
	private String userId;
	private String userName;
	private String nickName;
	private String gender;
	private String status;
	
	private Boolean isCompanyMember;//是否為企業客戶
	private Boolean isCompanyEM;	//是否為企業管理者
	private Boolean isAdmin;		//是否為系統管理者
	private Boolean isNurse;		//是否為健康師
	private Boolean isNurseManager;	//是否為總健康師
	
	
	private String userClass;
	private String maskName;

	private String logoUrl;
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getUserId() {
		return userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Boolean getIsCompanyMember() {
		return isCompanyMember;
	}
	public void setIsCompanyMember(Boolean isCompanyMember) {
		this.isCompanyMember = isCompanyMember;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Boolean getIsCompanyEM() {
		return isCompanyEM;
	}
	public void setIsCompanyEM(Boolean isCompanyEM) {
		this.isCompanyEM = isCompanyEM;
	}
	public String getUserClass() {
		return userClass;
	}
	public void setUserClass(String userClass) {
		this.userClass = userClass;
	}
	
	public String getMaskName() {
		return maskName;
	}
	
	public void setMaskName(String maskName) {
		this.maskName = maskName;
	}
	public String getUserIcon() {
		return userIcon;
	}
	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}
	
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	public Boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public Boolean getIsNurse() {
		return isNurse;
	}
	public void setIsNurse(Boolean isNurse) {
		this.isNurse = isNurse;
	}
	public Boolean getIsNurseManager() {
		return isNurseManager;
	}
	public void setIsNurseManager(Boolean isNurseManager) {
		this.isNurseManager = isNurseManager;
	}
	
	
}
