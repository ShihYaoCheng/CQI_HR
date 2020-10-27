package com.cqi.hr.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "line_user")
public class LineUser implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "LINE_ID", unique = true, nullable = false)
	private String lineId;
	
	@Column(name = "USER_NAME", length = 50)
	private String userName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", length = 19)
	private Date createTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 19)
	private Date updateTime;
	
	@Column(name = "STATUS", length = 3)
	private Integer status;

	public LineUser() {}

	public LineUser(String lineId, String userName,
			Date createTime, Date updateTime, Integer status) {
		super();
		this.lineId = lineId;
		this.userName = userName;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.status = status;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
