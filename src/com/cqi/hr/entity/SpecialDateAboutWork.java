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
@Table(name = "special_date_about_work")
public class SpecialDateAboutWork implements Serializable {

	private static final long serialVersionUID = -1861834532615717380L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "date_id", unique = true, nullable = false, length = 11)
	private Long dateId;
	
	@Column(name = "day_desc")
	private String dayDesc;
	
	@Column(name = "the_day")
	private Date theDay;
	
	@Column(name = "is_work_day")
	private Integer isWorkDay;
	
	@Column(name = "status")
	private Integer status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	private Date updateDate;

	public SpecialDateAboutWork() {
	}

	public Long getDateId() {
		return dateId;
	}

	public void setDateId(Long dateId) {
		this.dateId = dateId;
	}

	public String getDayDesc() {
		return dayDesc;
	}

	public void setDayDesc(String dayDesc) {
		this.dayDesc = dayDesc;
	}

	public Date getTheDay() {
		return theDay;
	}

	public void setTheDay(Date theDay) {
		this.theDay = theDay;
	}

	public Integer getIsWorkDay() {
		return isWorkDay;
	}

	public void setIsWorkDay(Integer isWorkDay) {
		this.isWorkDay = isWorkDay;
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
