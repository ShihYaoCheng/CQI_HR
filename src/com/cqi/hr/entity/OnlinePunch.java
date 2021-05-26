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

@Entity
@Table(name = "online_punch")
public class OnlinePunch implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "online_punch_id", unique = true, nullable = false)
	private Long onlinePunchId;
	
	@Column(name = "sys_user_id", nullable = false)
	private String sysUserId;
	
	@Column(name = "work_date", nullable = false)
	private Date workDate;
	
	@Column(name = "punch_in_pw")
	private String punchInPw;
	
	@Column(name = "punch_out_pw")
	private String punchOutPw;
	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 19)
	private Date createTime;
	
	@Column(name = "modify_by")
	private String modifyBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_time", length = 19)
	private Date modifyTime;
	
	public OnlinePunch() {
		
	}
	
	public OnlinePunch(String sysUserId, Date workDate, String punchInPw, String punchOutPw) {
		this.sysUserId = sysUserId;
		this.workDate = workDate;
		this.punchInPw = punchInPw;
		this.punchOutPw = punchOutPw;
		this.createTime = new Date();
	}
	public Long getOnlinePunchId() {
		return onlinePunchId;
	}

	public void setOnlinePunchId(Long onlinePunchId) {
		this.onlinePunchId = onlinePunchId;
	}
	
	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}
	
	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	
	public String getPunchInPw() {
		return punchInPw;
	}
	
	public void setPunchInPw(String punchInPw) {
		this.punchInPw = punchInPw;
	}
	
	public String getPunchOutPw() {
		return punchOutPw;
	}
	
	public void setPunchOutPw(String punchOutPw) {
		this.punchOutPw = punchOutPw;
	}
	
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getModifyBy() {
		return modifyBy;
	}
	
	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}
	
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	

}
