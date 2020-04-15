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
@Table(name = "user_overtime")
public class UserOvertime implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userOvertimeId;
	private String sysUserId;
	private Long overtimeId;
	private Double count;
	private Integer status;
	private Date createDate;
	private Date updateDate;

	public UserOvertime() {
	}

	public UserOvertime(Long userOvertimeId) {
		this.userOvertimeId = userOvertimeId;
	}

	public UserOvertime(Long userOvertimeId, String sysUserId, Double count, Integer status,
			Date createDate, Date updateDate) {
		this.userOvertimeId = userOvertimeId;
		this.sysUserId = sysUserId;
		this.count = count;
		this.status = status;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_overtime_id", unique = true, nullable = false, length = 11)
	public Long getUserOvertimeId() {
		return userOvertimeId;
	}

	public void setUserOvertimeId(Long userOvertimeId) {
		this.userOvertimeId = userOvertimeId;
	}
	
	@Column(name = "sys_user_id")
	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}

	@Column(name = "overtime_id")
	public Long getOvertimeId() {
		return overtimeId;
	}

	public void setOvertimeId(Long overtimeId) {
		this.overtimeId = overtimeId;
	}

	@Column(name = "count")
	public Double getCount() {
		return count;
	}

	public void setCount(Double count) {
		this.count = count;
	}
	
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}
