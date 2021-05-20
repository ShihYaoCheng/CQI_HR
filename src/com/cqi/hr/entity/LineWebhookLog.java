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

@Entity
@Table(name = "line_webhook_log")
public class LineWebhookLog implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "LOG_ID", unique = true, nullable = false)
	private String logId;
	
	@Column(name = "line_id", length = 50)
	private String lineId;
	
	@Column(name = "type", length = 45)
	private String type;
	
	@Column(name = "DATA", length = 1000)
	private String data;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", length = 19)
	private Date createTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 19)
	private Date updateTime;
	
	@Column(name = "STATUS", length = 3)
	private Integer status;

	public LineWebhookLog() {}
	
	public LineWebhookLog(String type, String data ) {
		this.type = type;
		this.data = data;
		this.status = 1;
		this.createTime = new Date();
	}
	
	public LineWebhookLog(String lineId,String type, String data ) {
		this.lineId = lineId;
		this.type = type;
		this.data = data;
		this.status = 1;
		this.createTime = new Date();
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}
	
	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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
