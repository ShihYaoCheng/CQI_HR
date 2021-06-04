package com.cqi.hr.quartz;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cqi.hr.service.UserShiftQuotaService;
import com.cqi.hr.util.DateUtils;

public class GiveShiftQuotaJob extends BasicJob{
	
	@Resource private UserShiftQuotaService userShiftQuotaService;
	
	public void setUserShiftQuotaService(UserShiftQuotaService userShiftQuotaService) {
		this.userShiftQuotaService = userShiftQuotaService;
	}
	

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info(JOB_NAME + " start.");
		Date start = new Date();
		try {
			logger.info(webConfigBean.getExecute());
			if(!webConfigBean.getExecute()){
				//lock this thread
				webConfigBean.setExecute(true);
				
				userShiftQuotaService.GiveShiftQuota();
				
				
				//job done, unlock
				webConfigBean.setExecute(false);
			}
		} catch (Exception e) {
			logger.error(JOB_NAME + " execute fail.", e);
		}
		logger.info(JOB_NAME + " end." + DateUtils.formatDuring(start,new Date()));
		
	}
	
	

}
