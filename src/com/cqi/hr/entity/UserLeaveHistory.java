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
@Table(name = "user_leave_history")
public class UserLeaveHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1861834532615717380L;
	private Long historyId;
	private String sysUserId;
	private Long leaveId;
	private Double count;
	private Integer dateOfYear;
	private Integer dateOfMonth;
	private Integer status;
	private Date createDate;
	private Date updateDate;

	public UserLeaveHistory() {
	}

	public UserLeaveHistory(Long historyId) {
		this.historyId = historyId;
	}

	public UserLeaveHistory(Long historyId, String sysUserId, Long leaveId, Double count, Integer status,
			Integer dateOfYear, Integer dateOfMonth, Date createDate, Date updateDate) {
		this.historyId = historyId;
		this.sysUserId = sysUserId;
		this.leaveId = leaveId;
		this.count = count;
		this.dateOfYear = dateOfYear;
		this.dateOfMonth = dateOfMonth;
		this.status = status;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "history_id", unique = true, nullable = false, length = 11)
	public Long getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
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
	
	@Column(name = "date_of_year")
	public Integer getDateOfYear() {
		return dateOfYear;
	}

	public void setDateOfYear(Integer dateOfYear) {
		this.dateOfYear = dateOfYear;
	}

	@Column(name = "date_of_month")
	public Integer getDateOfMonth() {
		return dateOfMonth;
	}

	public void setDateOfMonth(Integer dateOfMonth) {
		this.dateOfMonth = dateOfMonth;
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
