package com.cqi.hr.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "work_from_home")
public class WorkFromHome implements Serializable {
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "work_from_home_id", unique = true, nullable = false)
	private Long workFromHomeId;
	
	@Column(name = "sys_user_id")
	private String sysUserId;
	
	@Column(name = "level")
	private Integer level;
	
	@Column(name = "work_date")
	private Date workDate;
	
	
	@Column (name = "description", length = 100)
	private String description;
	
	@Column (name = "approval_by", length = 50)
	private String approvalBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "approval_time", length = 19)
	private Date approvalTime;
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "create_by")
	private String createBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 19)
	private Date createTime;
	
	@Column(name = "modify_by")
	private String modifyBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_time", length = 19)
	private Date modifyTime;
	
	public WorkFromHome() {
		
	}
	
	public WorkFromHome(String sysUserId,Integer level, Date workDate, String description, String approvalBy, Integer status) {
		this.sysUserId = sysUserId;
		this.level = level;
		this.workDate = workDate;
		this.description = description;
		this.approvalBy = approvalBy;
		this.status = status;
		this.createTime = new Date();
	}
	
	public Long getWorkFromHomeId() {
		return workFromHomeId;
	}

	public void setWorkFromHomeId(Long workFromHomeId) {
		this.workFromHomeId = workFromHomeId;
	}
	
	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}
	
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	
	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public String getApprovalBy() {
		return approvalBy;
	}

	public void setApprovalBy(String approvalBy) {
		this.approvalBy = approvalBy;
	}
	
	public Date getApprovalTime() {
		return approvalTime;
	}

	public void setApprovalTime(Date approvalTime) {
		this.approvalTime = approvalTime;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getCreateBy() {
		return createBy;
	}
	
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getModifyBy() {
		return modifyBy;
	}
	
	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}
	
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	
	
}
