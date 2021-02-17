package com.cqi.hr.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 出勤打卡紀錄
 */
@Entity
@Table(name = "attendance_record")
public class AttendanceRecord implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7849656582560697467L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "attendance_id", unique = true, nullable = false, length = 11)
	private Long attendanceId;
		
	@Column(name = "sys_user_id")
	private String sysUserId;
	
	@Column(name = "attendance_date")
	private Date attendanceDate;
	
	@Column(name = "arrive_time")
	private String arriveTime;
	
	@Column(name = "leave_time")
	private String leaveTime;
	
	@Column(name = "original_data")
	private String originalData;
	
	@Column(name = "status")
	private Integer status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	private Date updateDate;

	public AttendanceRecord() {
	}

	public Long getAttendanceId() {
		return attendanceId;
	}

	public void setAttendanceId(Long attendanceId) {
		this.attendanceId = attendanceId;
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

	public String getOriginalData() {
		return originalData;
	}

	public void setOriginalData(String originalData) {
		this.originalData = originalData;
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
	
	public double getAttendHours() {
		if(arriveTime == null || leaveTime == null || arriveTime.isEmpty() || leaveTime.isEmpty()) {
			return 0.0;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		double hours = 0.0;
		try {
			Date dateArriveTime = sdf.parse(arriveTime);
			Date dateLeaveTime = sdf.parse(leaveTime);
			hours = (dateLeaveTime.getTime() -dateArriveTime.getTime())/(1000*60*60);
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return hours;
		
	}
	

}
