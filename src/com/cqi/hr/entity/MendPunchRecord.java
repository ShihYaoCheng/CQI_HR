package com.cqi.hr.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 補卡紀錄
 */
@Entity
@Table(name = "mend_punch_record")
public class MendPunchRecord implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "mend_id", unique = true, nullable = false)
	private Long mendId;
	
	@Column(name = "sys_user_id")
	private String sysUserId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "mend_punch_time", length = 19)
	private Date mendPunchTime;
	
	@Column (name = "mend_reason", length = 100)
	private String mendReason;
	
	@Column(name = "approved_status", length = 1)
	private String approvedStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", length = 19)
	private Date createDate;
	
	@Column(name = "modify_user")
	private String modifyUser;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_date", length = 19)
	private Date modifyDate;
	
	
	public MendPunchRecord() {
		
	}
	
	public MendPunchRecord(String sysUserId, Date mendPunchTime, String approvedStatus) {
		this.sysUserId = sysUserId;
		this.mendPunchTime = mendPunchTime;
		this.approvedStatus = approvedStatus;
		this.createDate = new Date();
	}
	
	public Long getMendId() {
		return mendId;
	}

	public void setMendId(Long mendId) {
		this.mendId = mendId;
	}
	
	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}
	
	public Date getMendPunchTime() {
		return mendPunchTime;
	}
	
	public void setMendPunchTime(Date mendPunchTime) {
		this.mendPunchTime = mendPunchTime;
	}

	public String getMendReason() {
		return mendReason;
	}
	
	public void setMendReason(String mendReason) {
		this.mendReason = mendReason;
	}
	
	public String getApprovedStatus() {
		return approvedStatus;
	}
	
	public void setApprovedStatus(String approvedStatus) {
		this.approvedStatus = approvedStatus;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	
}
