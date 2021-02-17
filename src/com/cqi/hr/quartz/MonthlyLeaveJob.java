package com.cqi.hr.quartz;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cqi.hr.service.UserAskForLeaveService;
import com.cqi.hr.service.UserLeaveService;
import com.cqi.hr.util.DateUtils;


/** 每月結算請假紀錄 **/
public class MonthlyLeaveJob extends BasicJob {
	@Resource
	private UserAskForLeaveService userAskForLeaveService;
	@Resource
	private UserLeaveService userLeaveService;

	public void setUserAskForLeaveService(UserAskForLeaveService userAskForLeaveService) {
		this.userAskForLeaveService = userAskForLeaveService;
	}

	public void setUserLeaveService(UserLeaveService userLeaveService) {
		this.userLeaveService = userLeaveService;
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
				userAskForLeaveService.getLastMonthlySummary();
				userLeaveService.monthlyLeaveDataUpdate();
				//job done, unlock
				webConfigBean.setExecute(false);
			}
		} catch (Exception e) {
			logger.error(JOB_NAME + " execute fail.", e);
		}
		logger.info(JOB_NAME + " end." + DateUtils.formatDuring(start,new Date()));
	}
}
