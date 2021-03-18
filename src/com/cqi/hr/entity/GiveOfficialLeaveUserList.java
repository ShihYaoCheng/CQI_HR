package com.cqi.hr.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "give_official_leave_user_list")
public class GiveOfficialLeaveUserList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "_id", unique = true, nullable = false)
	private Long giveOfficialLeaveUserListId;
	
	@Column(name = "give_official_leave_id", nullable = false)
	private Long giveOfficialLeaveId;
	
	@Column(name = "sys_user_id", nullable = false)
	private String sysUserId;
	
	@Column(name = "user_ask_for_leave_id", nullable = false)
	private Long userAskForLeaveId;
	
	public GiveOfficialLeaveUserList() {
		
	}

	public GiveOfficialLeaveUserList(Long giveOfficialLeaveId, String sId,Long userAskForLeaveId) {
		this.giveOfficialLeaveId = giveOfficialLeaveId;
		this.sysUserId = sId;
		this.userAskForLeaveId = userAskForLeaveId;
	}

	public Long getGiveOfficialLeaveUserListId() {
		return giveOfficialLeaveUserListId;
	}

	public void setGiveOfficialLeaveUserListId(Long giveOfficialLeaveUserListId) {
		this.giveOfficialLeaveUserListId = giveOfficialLeaveUserListId;
	}

	public Long getGiveOfficialLeaveId() {
		return giveOfficialLeaveId;
	}

	public void setGiveOfficialLeaveId(Long giveOfficialLeaveId) {
		this.giveOfficialLeaveId = giveOfficialLeaveId;
	}

	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}

	public Long getUserAskForLeaveId() {
		return userAskForLeaveId;
	}

	public void setUserAskForLeaveId(Long userAskForLeaveId) {
		this.userAskForLeaveId = userAskForLeaveId;
	}
	
	
}
