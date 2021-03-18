package com.cqi.hr.service;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.DailyAttendanceRecordDAO;
import com.cqi.hr.dao.GiveOfficialLeaveDAO;
import com.cqi.hr.dao.GiveOfficialLeaveUserListDAO;
import com.cqi.hr.dao.SysUserDAO;
import com.cqi.hr.dao.UserAskForLeaveDAO;
import com.cqi.hr.entity.DailyAttendanceRecord;
import com.cqi.hr.entity.GiveOfficialLeave;
import com.cqi.hr.entity.GiveOfficialLeaveUserList;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.util.DateUtils;

@Service
public class GiveOfficialLeaveService extends AbstractService<GiveOfficialLeave>{
	@Resource GiveOfficialLeaveDAO giveOfficialLeaveDAO;
	@Resource GiveOfficialLeaveUserListDAO giveOfficialLeaveUserListDAO;
	@Resource UserAskForLeaveDAO userAskForLeaveDAO;
	@Resource DailyAttendanceRecordDAO dailyAttendanceRecordDAO;
	@Resource SysUserDAO sysUserDAO;
	
	@Resource DailyAttendanceRecordService dailyAttendanceRecordService;

	@Override
	protected AbstractDAO<GiveOfficialLeave> getDAO(){
		return giveOfficialLeaveDAO;
	}

	@Transactional
	public PagingList<GiveOfficialLeave> getList(Integer page) throws Exception{
		
		return giveOfficialLeaveDAO.getPageByUserId(page);
	}

	@Transactional
	public boolean addGiveOfficialLeave(GiveOfficialLeave giveOfficialLeave, SysUser dataUser) throws Exception{
		
		return giveOfficialLeaveDAO.addGiveOfficialLeave(giveOfficialLeave, dataUser);
	}

	@Transactional
	public String saveOfficialLeaveUserList(Long giveOfficialLeaveId, String[] sysUserIds, SysUser operator) {
		try {
			GiveOfficialLeave giveOfficialLeave = giveOfficialLeaveDAO.get(giveOfficialLeaveId);
			//delete
			deleteOfficialLeaveUserList(giveOfficialLeaveId, operator);
			
			//save
			if (sysUserIds.length >0) {
				for (String sId : sysUserIds) {
					UserAskForLeave userAskForLeave = new UserAskForLeave();
					userAskForLeave.setSysUserId(sId);
					userAskForLeave.setLeaveId(giveOfficialLeave.getLeaveId());
					userAskForLeave.setSpendTime((double) DateUtils.diffHours(giveOfficialLeave.getOfficialLeaveStartDate(), giveOfficialLeave.getOfficialLeaveEndDate()));
					userAskForLeave.setStartTime(giveOfficialLeave.getOfficialLeaveStartDate());
					userAskForLeave.setEndTime(giveOfficialLeave.getOfficialLeaveEndDate());
					userAskForLeave.setStatus(1);
					userAskForLeave.setDescription("公司公假: "+giveOfficialLeave.getDescription());
					userAskForLeave.setCreateDate(new Date());
					userAskForLeave.setCreateBy(operator.getSysUserId());
					userAskForLeaveDAO.persist(userAskForLeave);
					
					giveOfficialLeaveUserListDAO.saveOrUpdate(new GiveOfficialLeaveUserList(giveOfficialLeaveId,sId,userAskForLeave.getAskForLeaveId()));
					
					//update dailyAttendanceRecord
					Date date = DateUtils.clearTime(giveOfficialLeave.getOfficialLeaveStartDate());
					SysUser sysUser = sysUserDAO.getOneBySysUserId(sId);
					dailyAttendanceRecordService.updateDailyAttendanceRecordByDateAndUser (date ,sysUser);
				}
			}
			
			return "";
		} catch (Exception e) {
			logger.error("save error, giveOfficialLeaveId:"+giveOfficialLeaveId +";", e);
			return Constant.NETWORK_BUSY+ e;
		}
	}

	@Transactional
	public boolean deleteOfficialLeave(Long giveOfficialLeaveId, SysUser operator) throws Exception {
		deleteOfficialLeaveUserList(giveOfficialLeaveId, operator) ;
		GiveOfficialLeave giveOfficialLeave = giveOfficialLeaveDAO.get(giveOfficialLeaveId);
		giveOfficialLeaveDAO.delete(giveOfficialLeave);
		return true;
	}
	
	@Transactional
	public boolean deleteOfficialLeaveUserList(Long giveOfficialLeaveId, SysUser operator) throws Exception {
		GiveOfficialLeave giveOfficialLeave = giveOfficialLeaveDAO.get(giveOfficialLeaveId);
		List<GiveOfficialLeaveUserList> oldData = giveOfficialLeaveUserListDAO.getUserList(giveOfficialLeaveId);
		for(GiveOfficialLeaveUserList g :oldData){
			userAskForLeaveDAO.delete(userAskForLeaveDAO.get(g.getUserAskForLeaveId()));
			
			//update dailyAttendanceRecord
			Date date = DateUtils.clearTime(giveOfficialLeave.getOfficialLeaveStartDate());
			SysUser sysUser = sysUserDAO.getOneBySysUserId(g.getSysUserId());
			dailyAttendanceRecordService.updateDailyAttendanceRecordByDateAndUser (date ,sysUser);
		}
		giveOfficialLeaveUserListDAO.deleteAll(oldData);
		return true;
	}
	

}
