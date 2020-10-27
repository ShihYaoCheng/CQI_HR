package com.cqi.hr.vo;

import com.linecorp.bot.model.message.flex.component.Box;

public class LineMessageVo {

	private String targetId;
	
	private String altText;
	
	private Box header;
	
	private Box body;
	
	private Box footer;

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getAltText() {
		return altText;
	}

	public void setAltText(String altText) {
		this.altText = altText;
	}

	public Box getHeader() {
		return header;
	}

	public void setHeader(Box header) {
		this.header = header;
	}

	public Box getBody() {
		return body;
	}

	public void setBody(Box body) {
		this.body = body;
	}

	public Box getFooter() {
		return footer;
	}

	public void setFooter(Box footer) {
		this.footer = footer;
	}
	
}
