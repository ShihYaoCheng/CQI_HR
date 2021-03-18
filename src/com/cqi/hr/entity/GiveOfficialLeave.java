package com.cqi.hr.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cqi.hr.util.StringListConverter;

@Entity
@Table(name = "give_official_leave")
public class GiveOfficialLeave implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "give_official_leave_id", unique = true, nullable = false)
	private Long giveOfficialLeaveId;
	

	
	@Column(name = "leave_id")
	private Long leaveId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "official_leave_start_time")
	private Date officialLeaveStartDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "official_leave_end_time")
	private Date officialLeaveEndDate;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "status")
	private Integer status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	private Date createDate;
	
	@Column(name = "create_by")
	private String createBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	private Date updateDate;
	
	public GiveOfficialLeave() {
		
	}
	
	public Long getGiveOfficialLeaveId() {
		return giveOfficialLeaveId;
	}

	public void setGiveOfficialLeaveId(Long giveOfficialLeaveId) {
		this.giveOfficialLeaveId = giveOfficialLeaveId;
	}
	

	public Long getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(Long leaveId) {
		this.leaveId = leaveId;
	}
	
	public Date getOfficialLeaveStartDate() {
		return officialLeaveStartDate;
	}
	
	public void setOfficialLeaveStartDate(Date officialLeaveStartDate) {
		this.officialLeaveStartDate = officialLeaveStartDate;
	}
	
	public Date getOfficialLeaveEndDate() {
		return officialLeaveEndDate;
	}
	
	public void setOfficialLeaveEndDate(Date officialLeaveEndDate) {
		this.officialLeaveEndDate = officialLeaveEndDate;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	@Column(name = "create_by")
	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
}
