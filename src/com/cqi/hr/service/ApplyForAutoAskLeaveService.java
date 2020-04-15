package com.cqi.hr.service;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.ApplyForAutoAskLeaveDAO;
import com.cqi.hr.dao.SpecialDateAboutWorkDAO;
import com.cqi.hr.dao.SysUserDAO;
import com.cqi.hr.dao.UserAskForLeaveDAO;
import com.cqi.hr.entity.ApplyForAutoAskLeave;
import com.cqi.hr.entity.CompanyLeave;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SpecialDateAboutWork;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.entity.UserLeave;
import com.cqi.hr.util.AsanaUtils;
import com.cqi.hr.util.DateUtils;

@Service
public class ApplyForAutoAskLeaveService extends AbstractService<ApplyForAutoAskLeave>{
	@Resource ApplyForAutoAskLeaveDAO applyForAutoAskLeaveDAO;
	@Resource SysUserDAO sysUserDAO;
	@Resource SpecialDateAboutWorkDAO specialDateAboutWorkDAO;
	@Resource UserAskForLeaveDAO userAskForLeaveDAO;
	@Resource UserLeaveService userLeaveService;
	@Resource SpecialDateAboutWorkService specialDateAboutWorkService;
	
	@Override
	protected AbstractDAO<ApplyForAutoAskLeave> getDAO() {
		return applyForAutoAskLeaveDAO;
	}
	
	@Transactional
	public List<ApplyForAutoAskLeave> getListByUserId(String userId) throws Exception{
		return applyForAutoAskLeaveDAO.getListByUserId(userId);
	}
	
	@Transactional
	public PagingList<ApplyForAutoAskLeave> getListByPage(Integer page, String userId) throws Exception {
		return applyForAutoAskLeaveDAO.getListByPage(page, userId);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public void autoAskLeave() throws Exception {
		//確認今天日期是否上班
		if(specialDateAboutWorkService.isWorkDay()) {
			//上班日的話搜尋本月份申請單
			List<ApplyForAutoAskLeave> applyList = applyForAutoAskLeaveDAO.getListThisMonth();
			if(applyList.size()>0) {
				//進行自動申請事假作業
				Map<String, SysUser> userMap = (Map<String, SysUser>) sysUserDAO.queryToMap("sysUserId");
				for(ApplyForAutoAskLeave data:applyList) {
					//判斷有沒有請假了
					boolean checkTodayHasData = userAskForLeaveDAO.checkTodayHasData(data.getSysUserId());
					if(!checkTodayHasData) {
						UserAskForLeave askForLeave = new UserAskForLeave();
						askForLeave.setSysUserId(data.getSysUserId());
						askForLeave.setDescription("睡過頭");
						Calendar startTime = DateUtils.clearTime(Calendar.getInstance());
						Integer hour = Integer.parseInt(data.getGetIntoOfficesTime().split(":")[0]);
						startTime.set(Calendar.HOUR_OF_DAY, hour);
						askForLeave.setStartTime(startTime.getTime());
						askForLeave.setSpendTime((double) (12-hour));
						Calendar endTime = DateUtils.clearTime(Calendar.getInstance());
						endTime.set(Calendar.HOUR_OF_DAY, 12);
						askForLeave.setEndTime(endTime.getTime());
						askForLeave.setLeaveId(CompanyLeave.OCCUPIED_LEAVE_ID);
						askForLeave.setStatus(Constant.STATUS_ENABLE);
						askForLeave.setCreateDate(new Date());
						boolean isSuccess = userLeaveService.updateUserLeave(askForLeave, 1);
						if(isSuccess) {
							boolean addTaskSucceed = AsanaUtils.addLeaveTask(Constant.CQI_GAMES_ASANA_TOKEN, userMap.get(data.getSysUserId()), askForLeave, userLeaveService.getCompanyLeaveMapping());
							if(!addTaskSucceed) {
								askForLeave.setDescription("Asana新增Task失敗\n" + askForLeave.getDescription());
							}
							logger.info("Asana Id ajaxAdd : " + askForLeave.getAsanaTaskId());
							//將Asana狀況儲存，AsanaId和Description
							userAskForLeaveDAO.update(askForLeave);
						}
					}
				}
			}
		}
	}
}
