package com.cqi.hr.quartz;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cqi.hr.service.DailyAttendanceRecordService;
import com.cqi.hr.service.MonthlyReportService;
import com.cqi.hr.service.UserAskForLeaveService;
import com.cqi.hr.service.UserLeaveService;
import com.cqi.hr.util.DateUtils;


/** 月結報表 每月4號用每日出勤紀錄計算前個月報表**/
public class MonthlyReportJob extends BasicJob {
	@Resource
	private MonthlyReportService monthlyReportService;
	
	
	public void setMonthlyReportService(MonthlyReportService monthlyReportService) {
		this.monthlyReportService = monthlyReportService;
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
				monthlyReportService.calculateLastMonthReport();
				//job done, unlock
				webConfigBean.setExecute(false);
			}
		} catch (Exception e) {
			logger.error(JOB_NAME + " execute fail.", e);
		}
		logger.info(JOB_NAME + " end." + DateUtils.formatDuring(start,new Date()));
	}
}
