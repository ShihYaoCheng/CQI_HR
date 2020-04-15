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
 * 使用者剩下的假
 */
@Entity
@Table(name = "user_leave")
public class UserLeave implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1861834532615717380L;
	private Long userLeaveId;
	private String sysUserId;
	private Long leaveId;
	private Double count;
	private Integer status;
	private Date createDate;
	private Date updateDate;

	public UserLeave() {
	}

	public UserLeave(Long userLeaveId) {
		this.userLeaveId = userLeaveId;
	}

	public UserLeave(Long userLeaveId, String sysUserId, Long leaveId, Double count, Integer status,
			Date createDate, Date updateDate) {
		this.userLeaveId = userLeaveId;
		this.sysUserId = sysUserId;
		this.leaveId = leaveId;
		this.count = count;
		this.status = status;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_leave_id", unique = true, nullable = false, length = 11)
	public Long getUserLeaveId() {
		return userLeaveId;
	}

	public void setUserLeaveId(Long userLeaveId) {
		this.userLeaveId = userLeaveId;
	}
	
	@Column(name = "leave_id")
	public Long getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(Long leaveId) {
		this.leaveId = leaveId;
	}
	
	@Column(name = "sys_user_id")
	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
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
