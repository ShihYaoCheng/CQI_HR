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

/**
 * 使用者的加班時數
 */
@Entity
@Table(name = "user_shift_quota")
public class UserShiftQuota implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_shift_quota_id", unique = true, nullable = false, length = 11)
	private Long userShiftQuotaId;
	
	@Column(name = "sys_user_id")
	private String sysUserId;
	
	
	@Column(name = "count")
	private Double count;
	
	@Column(name = "quota")
	private Double quota;
	
	@Column(name = "status")
	private Integer status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 19)
	private Date createTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time", length = 19)
	private Date updateTime;

	public UserShiftQuota() {
	}

	public UserShiftQuota(Long userShiftQuotaId) {
		this.userShiftQuotaId = userShiftQuotaId;
	}

	public UserShiftQuota(Long userShiftQuotaId, String sysUserId, Double count, Double quota, Integer status) {
		this.userShiftQuotaId = userShiftQuotaId;
		this.sysUserId = sysUserId;
		this.count = count;
		this.quota = quota;
		this.status = status;
		this.createTime = new Date();
	}

	
	public Long getUserShiftQuotaId() {
		return userShiftQuotaId;
	}

	public void setUserShiftQuotaId(Long userShiftQuotaId) {
		this.userShiftQuotaId = userShiftQuotaId;
	}
	
	
	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}

	public Double getCount() {
		return count;
	}

	public void setCount(Double count) {
		this.count = count;
	}
	
	public Double getQuota() {
		return quota;
	}

	public void setQuota(Double quota) {
		this.quota = quota;
	}
	
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

}
