package com.cqi.hr.service;

import java.util.Date;

public interface CreateInfo {
	public void setCreateUser(String userId);
	public void setCreateDate(Date date);
	public String getCreateUser();
	public Date getCreateDate();
}
