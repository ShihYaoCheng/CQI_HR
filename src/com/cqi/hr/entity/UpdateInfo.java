package com.cqi.hr.entity;

import java.util.Date;

public interface UpdateInfo {
	public void setUpdateUser(String userId);
	public void setUpdateDate(Date date);
	public String getUpdateUser();
	public Date getUpdateDate();
}
