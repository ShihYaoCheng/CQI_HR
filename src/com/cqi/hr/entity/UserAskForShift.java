package com.cqi.hr.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_ask_for_shift")
public class UserAskForShift implements Serializable {
	private static final long serialVersionUID = -1L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_ask_for_shift_id", unique = true, nullable = false, length = 11)
	private Long userAskForShiftId;
	
	@Column(name = "ask_for_overtime_id",  nullable = false, length = 11)
	private Long askForOvertimeId;
	
	@Column(name = "ask_for_leave_id",  nullable = false, length = 11)
	private Long askForLeaveId;
	
	
	public UserAskForShift() {
	}
	
	public UserAskForShift(Long askForOvertimeId, Long askForLeaveId) {
		this.askForOvertimeId = askForOvertimeId;
		this.askForLeaveId = askForLeaveId;
	}
	
	
	public Long getUserAskForShiftId() {
		return userAskForShiftId;
	}

	public void setUserAskForShiftId(Long userAskForShiftId) {
		this.userAskForShiftId = userAskForShiftId;
	}
	
	public Long getAskForOvertimeId() {
		return askForOvertimeId;
	}

	public void setAskForOvertimeId(Long askForOvertimeId) {
		this.askForOvertimeId = askForOvertimeId;
	}
	
	public Long getAskForLeaveId() {
		return askForLeaveId;
	}

	public void setAskForLeaveId(Long askForLeaveId) {
		this.askForLeaveId = askForLeaveId;
	}
}
