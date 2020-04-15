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
 * 使用者的加班紀錄
 */
@Entity
@Table(name = "user_ask_for_overtime")
public class UserAskForOvertime implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4365486921723291911L;
	private Long askForOvertimeId;
	private String sysUserId;
	private Long overtimeId;
	private Double spendTime;
	private Date startTime;
	private Date endTime;
	private Integer status;
	private String description;
	private String asanaTaskId;
	private Date createDate;
	private Date updateDate;

	public UserAskForOvertime() {
	}

	public UserAskForOvertime(Long askForOvertimeId) {
		this.askForOvertimeId = askForOvertimeId;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ask_for_overtime_id", unique = true, nullable = false, length = 11)
	public Long getAskForOvertimeId() {
		return askForOvertimeId;
	}

	public void setAskForOvertimeId(Long askForOvertimeId) {
		this.askForOvertimeId = askForOvertimeId;
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
