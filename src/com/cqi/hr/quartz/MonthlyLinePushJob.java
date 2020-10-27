package com.cqi.hr.quartz;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cqi.hr.service.LineBotService;
import com.cqi.hr.util.DateUtils;


/** 每月25號推送新增 **/
public class MonthlyLinePushJob extends BasicJob {
	@Resource
	private LineBotService lineBotService;

	public void setLineBotService(LineBotService lineBotService) {
		this.lineBotService = lineBotService;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0)throws JobExecutionException {
		logger.info(JOB_NAME + " start.");
		Date start = new Date();
		try {
			logger.info(webConfigBean.getExecute());
			if(!webConfigBean.getExecute()){
				//lock this thread
				webConfigBean.setExecute(true);
				lineBotService.monthlyEmergenceLineBot();
				//job done, unlock
				webConfigBean.setExecute(false);
			}
		} catch (Exception e) {
			logger.error(JOB_NAME + " execute fail.", e);
		}
		logger.info(JOB_NAME + " end." + DateUtils.formatDuring(start,new Date()));
	}
}
