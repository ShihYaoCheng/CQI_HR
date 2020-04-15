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
@Table(name = "apply_for_auto_ask_leave")
public class ApplyForAutoAskLeave implements Serializable {

	private static final long serialVersionUID = -1861834532615717380L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "auto_id", unique = true, nullable = false, length = 11)
	private Long autoId;
	
	@Column(name = "sys_user_id")
	private String sysUserId;
	
	@Column(name = "date_of_year")
	private Integer dateOfYear;
	
	@Column(name = "date_of_month")
	private Integer dateOfMonth;
	
	@Column(name = "get_into_offices_time")
	private String getIntoOfficesTime;
	
	@Column(name = "status")
	private Integer status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	private Date updateDate;

	public ApplyForAutoAskLeave() {
	}
	
	public Long getAutoId() {
		return autoId;
	}

	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}

	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
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

	public String getGetIntoOfficesTime() {
		return getIntoOfficesTime;
	}

	public void setGetIntoOfficesTime(String getIntoOfficesTime) {
		this.getIntoOfficesTime = getIntoOfficesTime;
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
