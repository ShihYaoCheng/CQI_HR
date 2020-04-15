package com.cqi.hr.quartz;

import org.apache.log4j.Logger;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.cqi.hr.config.WebConfigBean;


public abstract class BasicJob extends QuartzJobBean{
	protected final String JOB_NAME = this.getClass().getSimpleName();
	protected Logger logger = Logger.getLogger(this.getClass());
	protected WebConfigBean webConfigBean;
	
	public void setWebConfigBean(WebConfigBean webConfigBean) {
		this.webConfigBean = webConfigBean;
	}
}
