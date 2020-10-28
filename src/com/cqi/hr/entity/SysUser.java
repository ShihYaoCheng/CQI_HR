package com.cqi.hr.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "sys_user")
public class SysUser implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "SYS_USER_ID", unique = true, nullable = false)
	private String sysUserId;
	
	@Column(name = "SYS_ROLE_ID")
	private String roleId;
	
	@Column(name = "USER_NAME", length = 50)
	private String userName;
	
	@Column(name = "ORIGINAL_NAME", length = 20)
	private String originalName;
	
	@Column(name = "PASSWORD", length = 100)
	private String password;
	
	@Column(name = "TELEPHONE", length = 20)
	private String telephone;
	
	@Column(name = "EXTENSION", length = 3)
	private String extension;
	
	@Column(name = "EMAIL", length = 50)
	private String email;
	
	@Column(name = "gender", length = 5)
	private String gender;
	
	@Column(name = "department", length = 45)
	private String department;
	
	@Column(name = "asana_id", length = 50)
	private String asanaId;
	
	@Column(name = "default_workspaces_id", length = 100)
	private String defaultWorkspacesId;
	
	@Column(name = "default_workspaces_name", length = 20)
	private String defaultWorkspacesName;
	
	@Column(name = "default_project_id", length = 3)
	private String defaultProjectId;
	
	@Column(name = "default_project_name", length = 50)
	private String defaultProjectName;
	
	@Column(name = "group_name", length = 50)
	private String groupName;
	
	@Column(name = "line_id", length = 50)
	private String lineId;
	
	@Column(name = "project_manager", length = 3)
	private Integer projectManager;
	
	@Column(name = "department_master", length = 3)
	private Integer departmentMaster;

	@Column(name = "finance_master", length = 3)
	private Integer financeMaster;
	
	@Column(name = "administration_manager", length = 3)
	private Integer administrationManager;
	
	@Column(name = "company_god", length = 3)
	private Integer companyGod;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "inauguration_date", length = 19)
	private Date inaugurationDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "graduation_date", length = 19)
	private Date graduationDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE", length = 19)
	private Date createDate;
	
	@Column(name = "CREATE_USER", length = 50)
	private String createUser;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFY_DATE", length = 19)
	private Date modifyDate;
	
	@Column(name = "MODIFY_USER", length = 50)
	private String modifyId;
	
	@Column(name = "cardid", length = 50)
	private String cardId;
	
	@Column(name = "STATUS", length = 3)
	private String status;

	public SysUser() {}

	public SysUser(String userId, String roleId, String userName,
			String password, String telephone,
			String extension, String email, Date createDate, String createUser,
			Date modifyDate, String modifyId, String status) {
		super();
		this.sysUserId = userId;
		this.roleId = roleId;
		this.userName = userName;
		this.password = password;
		this.telephone = telephone;
		this.extension = extension;
		this.email = email;
		this.createDate = createDate;
		this.createUser = createUser;
		this.modifyDate = modifyDate;
		this.modifyId = modifyId;
		this.status = status;
	}

	public String getSysUserId() {
		return sysUserId;
	}
	
	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getAsanaId() {
		return asanaId;
	}

	public void setAsanaId(String asanaId) {
		this.asanaId = asanaId;
	}

	public String getDefaultWorkspacesId() {
		return defaultWorkspacesId;
	}

	public void setDefaultWorkspacesId(String defaultWorkspacesId) {
		this.defaultWorkspacesId = defaultWorkspacesId;
	}

	public String getDefaultWorkspacesName() {
		return defaultWorkspacesName;
	}

	public void setDefaultWorkspacesName(String defaultWorkspacesName) {
		this.defaultWorkspacesName = defaultWorkspacesName;
	}

	public String getDefaultProjectId() {
		return defaultProjectId;
	}

	public void setDefaultProjectId(String defaultProjectId) {
		this.defaultProjectId = defaultProjectId;
	}

	public String getDefaultProjectName() {
		return defaultProjectName;
	}

	public void setDefaultProjectName(String defaultProjectName) {
		this.defaultProjectName = defaultProjectName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public Integer getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(Integer projectManager) {
		this.projectManager = projectManager;
	}

	public Integer getDepartmentMaster() {
		return departmentMaster;
	}

	public void setDepartmentMaster(Integer departmentMaster) {
		this.departmentMaster = departmentMaster;
	}

	public Integer getFinanceMaster() {
		return financeMaster;
	}

	public void setFinanceMaster(Integer financeMaster) {
		this.financeMaster = financeMaster;
	}

	public Integer getAdministrationManager() {
		return administrationManager;
	}

	public void setAdministrationManager(Integer administrationManager) {
		this.administrationManager = administrationManager;
	}

	public Integer getCompanyGod() {
		return companyGod;
	}

	public void setCompanyGod(Integer companyGod) {
		this.companyGod = companyGod;
	}

	public Date getGraduationDate() {
		return graduationDate;
	}

	public void setGraduationDate(Date graduationDate) {
		this.graduationDate = graduationDate;
	}

	public Date getInaugurationDate() {
		return inaugurationDate;
	}

	public void setInaugurationDate(Date inaugurationDate) {
		this.inaugurationDate = inaugurationDate;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}
	
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getModifyId() {
		return this.modifyId;
	}

	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
}
