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
 * 使用者的請假紀錄
 */
@Entity
@Table(name = "user_ask_for_leave")
public class UserAskForLeave implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1861834532615717380L;
	private Long askForLeaveId;
	private String sysUserId;
	private Long leaveId;
	private Double spendTime;
	private Date startTime;
	private Date endTime;
	private Integer status;
	private String description;
	private String asanaTaskId;
	private Date createDate;
	private Date updateDate;

	public UserAskForLeave() {
	}

	public UserAskForLeave(Long askForLeaveId) {
		this.askForLeaveId = askForLeaveId;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ask_for_leave_id", unique = true, nullable = false, length = 11)
	public Long getAskForLeaveId() {
		return askForLeaveId;
	}

	public void setAskForLeaveId(Long askForLeaveId) {
		this.askForLeaveId = askForLeaveId;
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

	@Column(name = "spend_time")
	public Double getSpendTime() {
		return spendTime;
	}

	public void setSpendTime(Double spendTime) {
		this.spendTime = spendTime;
	}

	@Column(name = "start_time")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(name = "end_time")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "asana_task_id")
	public String getAsanaTaskId() {
		return asanaTaskId;
	}

	public void setAsanaTaskId(String asanaTaskId) {
		this.asanaTaskId = asanaTaskId;
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
