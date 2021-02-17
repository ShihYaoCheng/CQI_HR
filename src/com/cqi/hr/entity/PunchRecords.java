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
@Table(name = "punch_records")
public class PunchRecords implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	
	@Column(name = "card_id", length = 200)
	private String cardId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "time", length = 19)
	private Date time;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", length = 19)
	private Date createDate;
	
	@Column(name = "editor", length = 200)
	private String editor;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", length = 19)
	private Date modifyDate;
	
	@Column(name = "status", length = 1)
	private String status;

	public PunchRecords(String cardID, Date time,Date createDate, Date modifyDate, String status) {
		this.cardId = cardID;
		this.time = time;
		this.createDate = createDate;
		this.modifyDate = modifyDate;
		this.status = status;
	}

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
