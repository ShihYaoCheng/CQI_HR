package com.cqi.hr.quartz;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cqi.hr.service.SysUserShiftService;
import com.cqi.hr.util.DateUtils;


/**
 * Monthly Shift 月初班別自動生成
 * if 沒有這個月的資料，自動複製前一個月份的班別
 * run 月初一執行
 */
public class MonthlyShiftJob extends BasicJob {
	@Resource
	private SysUserShiftService sysUserShiftService;

	public void setSysUserShiftService(SysUserShiftService sysUserShiftService) {
		this.sysUserShiftService = sysUserShiftService;
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
				sysUserShiftService.checkThisMonthShift();
				//job done, unlock
				webConfigBean.setExecute(false);
			}
		} catch (Exception e) {
			logger.error(JOB_NAME + " execute fail.", e);
		}
		logger.info(JOB_NAME + " end." + DateUtils.formatDuring(start,new Date()));
	}
}
