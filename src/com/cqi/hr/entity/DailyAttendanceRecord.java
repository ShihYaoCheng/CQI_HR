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
 * 日結出席紀錄
 */
@Entity
@Table(name = "daily_attendance_record")
public class DailyAttendanceRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "daily_attendance_record_id", unique = true, nullable = false, length = 11)
	private Long dailyAttendanceRecordId;
	
	@Column(name = "sys_user_id")
	private String sysUserId;
	
	@Column(name = "attendance_date")
	private Date attendanceDate;
	
	@Column(name = "arrive_time")
	private String arriveTime;
	
	@Column(name = "leave_time")
	private String leaveTime;
	
	@Column(name = "attend_hours")
	private Double attendHours;
	
	@Column(name = "overtime_hours")
	private Double overtimeHours;
	
	@Column(name = "leave_hours")
	private Double leaveHours;
	
	@Column(name = "absence_hours")
	private Double absenceHours;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_date")
	private Date modifyDate;
	
	

	public DailyAttendanceRecord() {
	}


	public DailyAttendanceRecord( String sysUserId, Date attendanceDate,String arriveTime,String leaveTime
			, Double attendHours, Double overtimeHours, Double leaveHours, Double absenceHours) {
		this.sysUserId = sysUserId;
		this.attendanceDate = attendanceDate;
		this.arriveTime = arriveTime;
		this.leaveTime = leaveTime;
		this.attendHours = attendHours;
		this.overtimeHours = overtimeHours;
		this.leaveHours = leaveHours;
		this.absenceHours = absenceHours;
		this.createDate = new Date();
	}


	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}

	public Date getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
	}
	
	public String getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}

	public String getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(String leaveTime) {
		this.leaveTime = leaveTime;
	}
	
	public Double getAttendHours() {
		return attendHours;
	}

	public void setAttendHours(Double attendHours) {
		this.attendHours = attendHours;
	}
	
	public Double getOvertimeHours() {
		return overtimeHours;
	}

	public void setOvertimeHours(Double overtimeHours) {
		this.overtimeHours = overtimeHours;
	}
	
	public Double getLeaveHours() {
		return leaveHours;
	}

	public void setLeaveHours(Double leaveHours) {
		this.leaveHours = leaveHours;
	}
	
	public Double getAbsenceHours() {
		return absenceHours;
	}

	public void setAbsenceHours(Double absenceHours) {
		this.absenceHours = absenceHours;
	}
	
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

}
