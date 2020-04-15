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
 * 使用者的請假剩餘額度月結
 */
@Entity
@Table(name = "user_leave_quota_monthly")
public class UserLeaveQuotaMonthly implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 211942803666670499L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "monthly_id", unique = true, nullable = false, length = 11)
	private Long monthlyId;
	
	@Column(name = "sys_user_id")
	private String sysUserId;
	
	@Column(name = "leave_id")
	private Long leaveId;
	
	@Column(name = "monthly_summary_quota")
	private Double monthlySummaryQuota;
	
	@Column(name = "date_of_year")
	private Integer dateOfYear;
	
	@Column(name = "date_of_month")
	private Integer dateOfMonth;
	
	@Column(name = "status")
	private Integer status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	private Date updateDate;

	public UserLeaveQuotaMonthly() {
	}

	public Long getMonthlyId() {
		return monthlyId;
	}

	public void setMonthlyId(Long monthlyId) {
		this.monthlyId = monthlyId;
	}

	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}

	public Long getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(Long leaveId) {
		this.leaveId = leaveId;
	}

	public Double getMonthlySummaryQuota() {
		return monthlySummaryQuota;
	}

	public void setMonthlySummaryQuota(Double monthlySummaryQuota) {
		this.monthlySummaryQuota = monthlySummaryQuota;
	}

	public Integer getDateOfYear() {
		return dateOfYear;
	}

	public void setDateOfYear(Integer dateOfYear) {
		this.dateOfYear = dateOfYear;
	}

	public Integer getDateOfMonth() {
		return dateOfMonth;
	}

	public void setDateOfMonth(Integer dateOfMonth) {
		this.dateOfMonth = dateOfMonth;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
