package com.cqi.hr.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 假別資料
 */
@Entity
@Table(name = "company_leave")
public class CompanyLeave implements Serializable {

	public static final Integer TYPE_LEAVE = -1;
	public static final Integer TYPE_OVERTIME = 1;
	
	//事假
	public static final String OCCUPIED_LEAVE = "事假";
	public static final Long OCCUPIED_LEAVE_ID = 1L;
	//調班
	public static final String SHIFT_OVERTIME = "排班";
	public static final Long SHIFT_OVERTIME_ID = 2L;
	//生理假
	public static final String MENSTRUATION_LEAVE = "生理假";
	public static final Long SHIFT_MENSTRUATION_ID = 3L;
	//特休
	public static final String ANNUAL_LEAVE = "特休";
	public static final Long ANNUAL_LEAVE_ID = 4L;
	//病假
	public static final String SICK_LEAVE = "病假";
	public static final Long SICK_LEAVE_ID = 5L;
	/**
	 * 
	 */
	private static final long serialVersionUID = -1861834532615717380L;
	private Long leaveId;
	private String leaveName;
	private Integer type;
	private Integer unitType;
	private double calculateBase;
	private Integer status;
	private String description;
	private Date createDate;
	private Date updateDate;

	public CompanyLeave() {
	}

	public CompanyLeave(Long leaveId) {
		this.leaveId = leaveId;
	}

	public CompanyLeave(Long leaveId, String leaveName, Integer type, Integer status,
			String description, Date createDate, Date updateDate) {
		this.leaveId = leaveId;
		this.leaveName = leaveName;
		this.type = type;
		this.status = status;
		this.description = description;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "leave_id", unique = true, nullable = false, length = 11)
	public Long getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(Long leaveId) {
		this.leaveId = leaveId;
	}
	
	@Column(name = "leave_name")
	public String getLeaveName() {
		return leaveName;
	}

	public void setLeaveName(String leaveName) {
		this.leaveName = leaveName;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "type")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "unit_type")
	public Integer getUnitType() {
		return unitType;
	}

	public void setUnitType(Integer unitType) {
		this.unitType = unitType;
	}

	@Column(name = "calculate_base")
	public Double getCalculateBase() {
		return calculateBase;
	}

	public void setCalculateBase(Double calculateBase) {
		this.calculateBase = calculateBase;
	}
	
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "create_time")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "update_time")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}
