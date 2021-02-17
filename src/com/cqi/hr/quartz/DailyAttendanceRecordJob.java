package com.cqi.hr.quartz;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cqi.hr.service.DailyAttendanceRecordService;
import com.cqi.hr.service.UserAskForLeaveService;
import com.cqi.hr.service.UserLeaveService;
import com.cqi.hr.util.DateUtils;


/** 日結出勤紀錄 每日計算前日出勤紀錄**/
public class DailyAttendanceRecordJob extends BasicJob {
	@Resource
	private DailyAttendanceRecordService dailyAttendanceRecordService;
	
	
	public void setDailyAttendanceRecordService(DailyAttendanceRecordService dailyAttendanceRecordService) {
		this.dailyAttendanceRecordService = dailyAttendanceRecordService;
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
				dailyAttendanceRecordService.calculateYesterdayAttendanceRecord();
				//job done, unlock
				webConfigBean.setExecute(false);
			}
		} catch (Exception e) {
			logger.error(JOB_NAME + " execute fail.", e);
		}
		logger.info(JOB_NAME + " end." + DateUtils.formatDuring(start,new Date()));
	}
}
