package com.cqi.hr.quartz;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cqi.hr.service.UserLeaveService;
import com.cqi.hr.util.DateUtils;


/** 特休新增 **/
public class AnnualLeaveJob extends BasicJob {
	@Resource
	private UserLeaveService userLeaveService;

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
				//特休假給予
				userLeaveService.annualLeaveGive();
				//事假給予
				userLeaveService.sickLeaveGive();
				//病假給予
//				userLeaveService.occupiedLeaveGive();
				//新增女性的生理假資料
				userLeaveService.menstruationLeaveGive();
				//job done, unlock
				webConfigBean.setExecute(false);
			}
		} catch (Exception e) {
			logger.error(JOB_NAME + " execute fail.", e);
		}
		logger.info(JOB_NAME + " end." + DateUtils.formatDuring(start,new Date()));
	}
}
