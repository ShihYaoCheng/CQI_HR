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
@Table(name = "monthly_report")
public class MonthlyReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 211942803666670499L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "monthly_report_id", unique = true, nullable = false, length = 11)
	private Long monthlyReportId;
	
	@Column(name = "date_of_year")
	private Integer dateOfYear;
	
	@Column(name = "date_of_month")
	private Integer dateOfMonth;
	
	@Column(name = "department")
	private String department;
	
	@Column(name = "sys_user_id")
	private String sysUserId;
	
	@Column(name = "user_name")
	private String userName;
	
	@Column(name = "original_name")
	private String originalName;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "user_shift")
	private String userShift;
	
	@Column(name = "absent_base")
	private Double absentBase;
	
	@Column(name = "overtime_base")
	private Double overtimeBase;
	
	@Column(name = "task_base")
	private Double taskBase;
	
	
	
	@Column(name = "O1")
	private Double o1;
	
	@Column(name = "O2")
	private Double o2;
	
	@Column(name = "O3")
	private Double o3;
	
	@Column(name = "O4")
	private Double o4;
	
	@Column(name = "overtime_hours")
	private Double overtimeHours;
	
	@Column(name = "scheduling_hours")
	private Double schedulingHours;
	
	@Column(name = "work_hours")
	private Double workHours;
	
	@Column(name = "need_work_hours")
	private Double needWorkHours;
	
	@Column(name = "absence_hours")
	private Double absenceHours;
	
	@Column(name = "leave_hours")
	private Double leaveHours;
	
	
	
	//事假
	@Column(name = "L1")
	private Double l1;
	
	@Column(name = "QL1")
	private Double ql1;
	
	//調班
	@Column(name = "L2")
	private Double l2;
	
	//生理假
	@Column(name = "L3")
	private Double l3;
	
	//特休
	@Column(name = "L4")
	private Double l4;
	
	@Column(name = "QL4")
	private Double ql4;
	
	//病假
	@Column(name = "L5")
	private Double l5;
	
	@Column(name = "QL5")
	private Double ql5;
	
	//公假
	@Column(name = "L6")
	private Double l6;
	
	//喪假
	@Column(name = "L7")
	private Double l7;
	
	//產假
	@Column(name = "L8")
	private Double l8;
	
	//陪產假
	@Column(name = "L9")
	private Double l9;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_date")
	private Date modifyDate;

	
	public MonthlyReport() {
	}
	
	public MonthlyReport(Integer dateOfYear, Integer dateOfMonth, String department,String sysUserId,String userName,String originalName,String status ,String userShift
			,Double abBase, Double otBase, Double taskBase, Double o1, Double o2, Double o3, Double o4
			,Double overtimeHours, Double schedulingHours, Double workHours, Double needWorkHours, Double absenceHours, Double leaveHours
			, Double l1, Double ql1, Double l2, Double l3, Double l4, Double ql4, Double l5, Double ql5, Double l6, Double l7, Double l8, Double l9) {
		this.dateOfYear = dateOfYear;
		this.dateOfMonth = dateOfMonth;
		this.department = department;
		this.sysUserId = sysUserId;
		this.userName = userName;
		this.originalName = originalName;
		this.status = status;
		this.userShift = userShift;
		this.absentBase = abBase;
		this.overtimeBase = otBase;
		this.taskBase = taskBase;
		this.o1 = o1;
		this.o2 = o2;
		this.o3 = o3;
		this.o4 = o4;
		this.overtimeHours = overtimeHours;
		this.schedulingHours = schedulingHours;
		this.workHours = workHours;
		this.needWorkHours = needWorkHours;
		this.absenceHours = absenceHours;
		this.leaveHours = leaveHours;
		this.l1 = l1;
		this.ql1 = ql1;
		this.l2 = l2;
		this.l3 = l3;
		this.l4 = l4;
		this.ql4 = ql4;
		this.l5 = l5;
		this.ql5 = ql5;
		this.l6 = l6;
		this.l7 = l7;
		this.l8 = l8;
		this.l9 = l9;
		this.createDate = new Date();
	}

	public Long getMonthlyReportId() {
		return monthlyReportId;
	}

	public void setMonthlyReportId(Long monthlyReportId) {
		this.monthlyReportId = monthlyReportId;
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

	
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	
	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getUserShift() {
		return userShift;
	}

	public void setUserShift(String userShift) {
		this.userShift = userShift;
	}
	
	// Base
	public Double getAbsentBase() {
		return absentBase;
	}

	public void setAbsentBase(Double absentBase) {
		this.absentBase = absentBase;
	}
	
	public Double getOverTimeBase() {
		return overtimeBase;
	}

	public void setOverTimeBase(Double overtimeBase) {
		this.overtimeBase = overtimeBase;
	}
	
	public Double getTaskBase() {
		return taskBase;
	}

	public void setTaskBase(Double taskBase) {
		this.taskBase = taskBase;
	}

	// overtime
	public Double getO1() {
		return o1;
	}

	public void setO1(Double o1) {
		this.o1 = o1;
	}

	public Double getO2() {
		return o2;
	}

	public void setO2(Double o2) {
		this.o2 = o2;
	}

	public Double getO3() {
		return o3;
	}

	public void setO3(Double o3) {
		this.o3 = o3;
	}

	public Double getO4() {
		return o4;
	}

	public void setO4(Double o4) {
		this.o4 = o4;
	}
	
	public Double getOvertimeHours() {
		return overtimeHours;
	}

	public void setOvertimeHours(Double overtimeHours) {
		this.overtimeHours = overtimeHours;
	}
	
	public Double getSchedulingHours() {
		return schedulingHours;
	}

	public void setSchedulingHours(Double schedulingHours) {
		this.schedulingHours = schedulingHours;
	}

	public Double getWorkHours() {
		return workHours;
	}

	public void setWorkHours(Double workHours) {
		this.workHours = workHours;
	}

	public Double getNeedWorkHours() {
		return needWorkHours;
	}

	public void setneedWorkHours(Double needWorkHours) {
		this.needWorkHours = needWorkHours;
	}

	public Double getAbsenceHours() {
		return absenceHours;
	}

	public void setAbsenceHours(Double absenceHours) {
		this.absenceHours = absenceHours;
	}
	
	public Double getLeaveHours() {
		return leaveHours;
	}

	public void setLeaveHours(Double leaveHours) {
		this.leaveHours = leaveHours;
	}
	
	//leave
	public Double getL1() {
		return l1;
	}

	public void setL1(Double l1) {
		this.l1 = l1;
	}

	public Double getQl1() {
		return ql1;
	}

	public void setQl1(Double ql1) {
		this.ql1 = ql1;
	}
	
	public Double getL2() {
		return l2;
	}

	public void setL2(Double l2) {
		this.l2 = l2;
	}

	public Double getL3() {
		return l3;
	}

	public void setL3(Double l3) {
		this.l3 = l3;
	}

	public Double getL4() {
		return l4;
	}

	public void setL4(Double l4) {
		this.l4 = l4;
	}
	
	public Double getQl4() {
		return ql4;
	}

	public void setQl4(Double ql4) {
		this.ql4 = ql4;
	}

	public Double getL5() {
		return l5;
	}

	public void setL5(Double l5) {
		this.l5 = l5;
	}
	
	public Double getQl5() {
		return ql5;
	}

	public void setQl5(Double ql5) {
		this.ql5 = ql5;
	}

	public Double getL6() {
		return l6;
	}

	public void setL6(Double l6) {
		this.l6 = l6;
	}

	public Double getL7() {
		return l7;
	}

	public void setL7(Double l7) {
		this.l7 = l7;
	}

	public Double getL8() {
		return l8;
	}

	public void setL8(Double l8) {
		this.l8 = l8;
	}

	public Double getL9() {
		return l9;
	}

	public void setL9(Double l9) {
		this.l9 = l9;
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
