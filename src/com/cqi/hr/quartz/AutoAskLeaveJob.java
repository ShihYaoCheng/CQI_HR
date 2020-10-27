package com.cqi.hr.quartz;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cqi.hr.service.ApplyForAutoAskLeaveService;
import com.cqi.hr.util.DateUtils;


/** 自動請假 **/
public class AutoAskLeaveJob extends BasicJob {
	@Resource
	private ApplyForAutoAskLeaveService applyForAutoAskLeaveService;

	public void setApplyForAutoAskLeaveService(ApplyForAutoAskLeaveService applyForAutoAskLeaveService) {
		this.applyForAutoAskLeaveService = applyForAutoAskLeaveService;
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
				applyForAutoAskLeaveService.autoAskLeave();
				//job done, unlock
				webConfigBean.setExecute(false);
			}
		} catch (Exception e) {
			logger.error(JOB_NAME + " execute fail.", e);
		}
		logger.info(JOB_NAME + " end." + DateUtils.formatDuring(start,new Date()));
	}
}
