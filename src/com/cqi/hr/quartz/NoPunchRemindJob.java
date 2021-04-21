package com.cqi.hr.quartz;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cqi.hr.service.LineBotService;
import com.cqi.hr.service.SpecialDateAboutWorkService;
import com.cqi.hr.util.DateUtils;

public class NoPunchRemindJob extends BasicJob{
	
	@Resource private LineBotService lineBotService;
	@Resource private SpecialDateAboutWorkService specialDateAboutWorkService;
	
	public void setLineBotService(LineBotService lineBotService) {
		this.lineBotService = lineBotService;
	}
	public void setSpecialDateAboutWorkService(SpecialDateAboutWorkService specialDateAboutWorkService) {
		this.specialDateAboutWorkService = specialDateAboutWorkService;
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
				
				//work date
				if (specialDateAboutWorkService.isWorkDay()) {
					lineBotService.NoPunchRemind();
				}
				
				//job done, unlock
				webConfigBean.setExecute(false);
			}
		} catch (Exception e) {
			logger.error(JOB_NAME + " execute fail.", e);
		}
		logger.info(JOB_NAME + " end." + DateUtils.formatDuring(start,new Date()));
		
	}
	
	

}
