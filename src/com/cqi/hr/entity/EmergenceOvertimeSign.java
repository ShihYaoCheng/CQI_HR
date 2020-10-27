package com.cqi.hr.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "emergence_overtime_sign")
public class EmergenceOvertimeSign implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "SIGN_ID")
	private Long signId;
	
	@Column(name = "TOKEN", length = 50)
	private String token;
	
	@Column(name = "ASK_FOR_OVERTIME_ID", length = 11)
	private Long askForOvertimeId;
	
	@Column(name = "PROJECT_SIGNER_ID", length = 50)
	private String projectSignerId;
	
	@Column(name = "PROJECT_SIGNER_LINE_ID", length = 50)
	private String projectSignerLineId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROJECT_SIGN_TIME", length = 19)
	private Date projectSignTime;
	
	@Column(name = "DEPARTMENT_SIGNER_ID", length = 50)
	private String departmentSignerId;
	
	@Column(name = "DEPARTMENT_SIGNER_LINE_ID", length = 50)
	private String departmentSignerLineId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DEPARTMENT_SIGN_TIME", length = 19)
	private Date departmentSignTime;
	
	@Column(name = "FINANCE_SIGNER_ID", length = 50)
	private String financeSignerId;
	
	@Column(name = "FINANCE_SIGNER_LINE_ID", length = 50)
	private String financeSignerLineId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FINANCE_SIGN_TIME", length = 19)
	private Date financeSignTime;
	
	@Column(name = "ADMINISTRATION_SIGNER_ID", length = 50)
	private String administrationSignerId;
	
	@Column(name = "ADMINISTRATION_SIGNER_LINE_ID", length = 50)
	private String administrationSignerLineId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ADMINISTRATION_SIGN_TIME", length = 19)
	private Date administrationSignTime;
	
	@Column(name = "COMPANY_SIGNER_ID", length = 50)
	private String companySignerId;
	
	@Column(name = "COMPANY_SIGNER_LINE_ID", length = 50)
	private String companySignerLineId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "COMPANY_SIGN_TIME", length = 19)
	private Date companySignTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", length = 19)
	private Date createTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 19)
	private Date updateTime;
	
	@Column(name = "STATUS", length = 3)
	private Integer status;

	public EmergenceOvertimeSign() {}

	public Long getSignId() {
		return signId;
	}

	public void setSignId(Long signId) {
		this.signId = signId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getAskForOvertimeId() {
		return askForOvertimeId;
	}

	public void setAskForOvertimeId(Long askForOvertimeId) {
		this.askForOvertimeId = askForOvertimeId;
	}

	public String getProjectSignerId() {
		return projectSignerId;
	}

	public void setProjectSignerId(String projectSignerId) {
		this.projectSignerId = projectSignerId;
	}

	public String getProjectSignerLineId() {
		return projectSignerLineId;
	}

	public void setProjectSignerLineId(String projectSignerLineId) {
		this.projectSignerLineId = projectSignerLineId;
	}

	public Date getProjectSignTime() {
		return projectSignTime;
	}

	public void setProjectSignTime(Date projectSignTime) {
		this.projectSignTime = projectSignTime;
	}

	public String getDepartmentSignerId() {
		return departmentSignerId;
	}

	public void setDepartmentSignerId(String departmentSignerId) {
		this.departmentSignerId = departmentSignerId;
	}

	public String getDepartmentSignerLineId() {
		return departmentSignerLineId;
	}

	public void setDepartmentSignerLineId(String departmentSignerLineId) {
		this.departmentSignerLineId = departmentSignerLineId;
	}

	public Date getDepartmentSignTime() {
		return departmentSignTime;
	}

	public void setDepartmentSignTime(Date departmentSignTime) {
		this.departmentSignTime = departmentSignTime;
	}

	public String getFinanceSignerId() {
		return financeSignerId;
	}

	public void setFinanceSignerId(String financeSignerId) {
		this.financeSignerId = financeSignerId;
	}

	public String getFinanceSignerLineId() {
		return financeSignerLineId;
	}

	public void setFinanceSignerLineId(String financeSignerLineId) {
		this.financeSignerLineId = financeSignerLineId;
	}

	public Date getFinanceSignTime() {
		return financeSignTime;
	}

	public void setFinanceSignTime(Date financeSignTime) {
		this.financeSignTime = financeSignTime;
	}

	public String getAdministrationSignerId() {
		return administrationSignerId;
	}

	public void setAdministrationSignerId(String administrationSignerId) {
		this.administrationSignerId = administrationSignerId;
	}

	public String getAdministrationSignerLineId() {
		return administrationSignerLineId;
	}

	public void setAdministrationSignerLineId(String administrationSignerLineId) {
		this.administrationSignerLineId = administrationSignerLineId;
	}

	public Date getAdministrationSignTime() {
		return administrationSignTime;
	}

	public void setAdministrationSignTime(Date administrationSignTime) {
		this.administrationSignTime = administrationSignTime;
	}

	public String getCompanySignerId() {
		return companySignerId;
	}

	public void setCompanySignerId(String companySignerId) {
		this.companySignerId = companySignerId;
	}

	public String getCompanySignerLineId() {
		return companySignerLineId;
	}

	public void setCompanySignerLineId(String companySignerLineId) {
		this.companySignerLineId = companySignerLineId;
	}

	public Date getCompanySignTime() {
		return companySignTime;
	}

	public void setCompanySignTime(Date companySignTime) {
		this.companySignTime = companySignTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
