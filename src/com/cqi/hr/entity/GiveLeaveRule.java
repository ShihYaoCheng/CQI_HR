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
@Table(name = "give_leave_rule")
public class GiveLeaveRule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1861834532615717380L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "rule_id", unique = true, nullable = false, length = 11)
	private Long ruleId;
	
	@Column(name = "leave_id")
	private Long leaveId;
	
	@Column(name = "years_after_appointment")
	private Double yearsAfterAppointment;
	
	@Column(name = "quota")
	private Double quota;
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "description")
	private String description;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	private Date updateDate;

	public GiveLeaveRule() {
	}
	
	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}
	
	public Long getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(Long leaveId) {
		this.leaveId = leaveId;
	}
	
	public Double getYearsAfterAppointment() {
		return yearsAfterAppointment;
	}

	public void setYearsAfterAppointment(Double yearsAfterAppointment) {
		this.yearsAfterAppointment = yearsAfterAppointment;
	}

	public Double getQuota() {
		return quota;
	}

	public void setQuota(Double quota) {
		this.quota = quota;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
