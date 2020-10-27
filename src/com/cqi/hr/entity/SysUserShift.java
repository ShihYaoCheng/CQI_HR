package com.cqi.hr.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * menu
 * */
@Entity
@Table(name = "sys_user_shift")
public class SysUserShift implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "shift_id")
	private Long shiftId;
	
	@Column(name = "sys_user_id")
	private String sysUserId;
	
	@Column(name = "board_time", length=10)
	private String boardTime;
	
	@Column(name = "finish_time", length=10)
	private String finishTime;
	
	@Column(name = "enable_month")
	private Date enableMonth;
	
	@Column(name = "status")
	private Integer status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	private Date updateDate;

	public SysUserShift() {}

	public Long getShiftId() {
		return shiftId;
	}

	public void setShiftId(Long shiftId) {
		this.shiftId = shiftId;
	}

	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}

	public String getBoardTime() {
		return boardTime;
	}

	public void setBoardTime(String boardTime) {
		this.boardTime = boardTime;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public Date getEnableMonth() {
		return enableMonth;
	}

	public void setEnableMonth(Date enableMonth) {
		this.enableMonth = enableMonth;
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
