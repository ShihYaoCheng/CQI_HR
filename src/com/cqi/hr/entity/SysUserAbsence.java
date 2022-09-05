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
@Table(name = "sys_user_absence")
public class SysUserAbsence {

	
	public SysUserAbsence() {}
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id" , unique = true, nullable = false)
	private String id;

	@Column(name = "sys_user_id")
	private String sysUserId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "effective_date", length = 19)
	private Date effectiveDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expiration_date", length = 19)
	private Date expirationDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", length = 19)
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_date", length = 19)
	private Date updateDate;
	
	
	@Column(name = "STATUS", length = 3)
	private String status;
	
	@Column(name = "effective_status", length = 3)
	private String effectiveStatus;
	
	@Column(name = "expiration_status", length = 3)
	private String expirationStatus;
	
	public String getSysUserId() {
		return sysUserId;
	}
	
	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}
	
	public Date getEffectiveDate() {
		return this.effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
	public Date getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getEffectiveStatus() {
		return this.effectiveStatus;
	}

	public void setEffectiveStatus(String effectiveStatus) {
		this.effectiveStatus = effectiveStatus;
	}
	
	public String getExpirationStatus() {
		return this.expirationStatus;
	}

	public void setExpirationStatus(String expirationStatus) {
		this.expirationStatus = expirationStatus;
	}
	
	
}
