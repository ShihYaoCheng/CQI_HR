package com.cqi.hr.service;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.SysUserDAO;
import com.cqi.hr.dao.UserAskForOvertimeDAO;
import com.cqi.hr.dao.UserLeaveDAO;
import com.cqi.hr.dao.UserShiftQuotaDAO;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserAskForOvertime;
import com.cqi.hr.entity.UserLeave;
import com.cqi.hr.entity.UserShiftQuota;

@Transactional
@Service
public class UserShiftQuotaService extends AbstractService<UserShiftQuota>{
	@Resource UserShiftQuotaDAO userShiftQuotaDAO;
	@Resource UserLeaveDAO userLeaveDAO;
	@Resource UserAskForOvertimeDAO userAskForOvertimeDAO;
	@Resource SysUserDAO sysUserDAO;
	
	@Override
	protected AbstractDAO<UserShiftQuota> getDAO() {
		return userShiftQuotaDAO;
	}
	
	
	public PagingList<UserShiftQuota> getListByPage(Integer page, String userId) throws Exception {
		return userShiftQuotaDAO.getListByPage(page, userId);
	}
	
	
	public PagingList<UserAskForOvertime> getAskOvertimeListByPage(Integer page, String userId) throws Exception {
		return userAskForOvertimeDAO.getListByPage(page, userId);
	}
	
	public List<UserShiftQuota> getListByUserId(String userId) throws Exception {
		return userShiftQuotaDAO.getListByUserId(userId);
	}
	
	/*
	 * 更新加班資料
	 * @type 1是新增，2是更新
	 */
	
	public boolean updateUserOvertime(UserAskForOvertime userAskForOvertime, Integer type) throws Exception{
		//UserOvertime userOvertime = userOvertimeDAO.getOneByUserId(userAskForOvertime.getSysUserId());
		Calendar calendar = Calendar.getInstance();
		if(type.equals(1)){
			// TODO : 這一段要重新思考重寫
//			if(null == userOvertime){
//				newUserOvertime(userAskForOvertime.getSysUserId(), userAskForOvertime.getSpendTime(), calendar.getTime());
//			}else{
//				userOvertime.setCount(userOvertime.getCount() + userAskForOvertime.getSpendTime());
//				userOvertime.setUpdateDate(calendar.getTime());
//				userOvertimeDAO.update(userOvertime);
//			}
			userAskForOvertimeDAO.persist(userAskForOvertime);
			
			// TODO : 這一段要重新思考重寫
//			UserLeave userLeave = userLeaveDAO.getOneBy2Id(userAskForOvertime.getSysUserId(), 2L); //補修
//			if(null!=userLeave){
//				userLeave.setCount(userLeave.getCount() + (userAskForOvertime.getSpendTime() * 2));
//				userLeave.setUpdateDate(calendar.getTime());
//				userLeaveDAO.update(userLeave);
//			}else{
//				userLeave = new UserLeave();
//				userLeave.setLeaveId(2L);
//				userLeave.setSysUserId(userAskForOvertime.getSysUserId());
//				userLeave.setCreateDate(calendar.getTime());
//				userLeave.setUpdateDate(calendar.getTime());
//				userLeave.setStatus(1);
//				userLeave.setCount((userAskForOvertime.getSpendTime() * 2));					
//				userLeaveDAO.persist(userLeave);
//			}
		}else if(type.equals(2)){
			UserAskForOvertime userAskForOvertimeOri = userAskForOvertimeDAO.get(userAskForOvertime.getAskForOvertimeId());
			if(null == userAskForOvertimeOri){
				return false;
			}else{
				// TODO : 這一段要重新思考重寫
//				if(null == userOvertime){
//					newUserOvertime(userAskForOvertime.getSysUserId(), userAskForOvertime.getSpendTime(), calendar.getTime());
//				}else{
//					userOvertime.setCount(userOvertime.getCount() - userAskForOvertimeOri.getSpendTime() + userAskForOvertime.getSpendTime());
//					userOvertime.setUpdateDate(calendar.getTime());
//					userOvertimeDAO.update(userOvertime);
//				}
//				UserLeave userLeave = userLeaveDAO.getOneBy2Id(userAskForOvertime.getSysUserId(), 2L); //補修
//				if(null!=userLeave){
//					userLeave.setCount(userLeave.getCount() - (userAskForOvertimeOri.getSpendTime() * 2) + (userAskForOvertime.getSpendTime() * 2));
//					userLeave.setUpdateDate(calendar.getTime());
//					userLeaveDAO.update(userLeave);
//				}else{
//					userLeave = new UserLeave();
//					userLeave.setLeaveId(2L);
//					userLeave.setSysUserId(userAskForOvertime.getSysUserId());
//					userLeave.setCreateDate(calendar.getTime());
//					userLeave.setUpdateDate(calendar.getTime());
//					userLeave.setStatus(1);
//					userLeave.setCount((userAskForOvertime.getSpendTime() * 2));					
//					userLeaveDAO.persist(userLeave);
//				}
				userAskForOvertimeOri.setOvertimeId(userAskForOvertime.getOvertimeId());
				userAskForOvertimeOri.setSpendTime(userAskForOvertime.getSpendTime());
				userAskForOvertimeOri.setStartTime(userAskForOvertime.getStartTime());
				userAskForOvertimeOri.setEndTime(userAskForOvertime.getEndTime());
				userAskForOvertimeOri.setUpdateDate(calendar.getTime());
				userAskForOvertimeDAO.update(userAskForOvertimeOri);
			}
		}
		return true;
	}
	
	/*
	 * 刪除加班紀錄
	 */
	public boolean deleteAskOvertime(Long askForLeaveId, SysUser operator) throws Exception{
		UserAskForOvertime userAskForOvertime = userAskForOvertimeDAO.get(askForLeaveId);
		if(null==userAskForOvertime){
			return false;
		}
		if(!userAskForOvertime.getSysUserId().equals(operator.getSysUserId())){
			return false;
		}
//		UserOvertime userOvertime = userOvertimeDAO.getOneByUserId(userAskForOvertime.getSysUserId());
//		if(null==userOvertime){
//			return false;
//		}
		Calendar calendar = Calendar.getInstance();
		// TODO : 這一段要重新思考重寫
//		userOvertime.setCount(userOvertime.getCount() - userAskForOvertime.getSpendTime());
//		userOvertime.setUpdateDate(calendar.getTime());
//		userOvertimeDAO.update(userOvertime);
		userAskForOvertime.setStatus(0);
		userAskForOvertime.setUpdateDate(calendar.getTime());
		userAskForOvertimeDAO.update(userAskForOvertime);
//		UserLeave userLeave = userLeaveDAO.getOneBy2Id(userAskForOvertime.getSysUserId(), 2L); //補修
//		if(null!=userLeave){
//			userLeave.setCount(userLeave.getCount() - (userAskForOvertime.getSpendTime() * 2));
//			userLeave.setUpdateDate(calendar.getTime());
//			userLeaveDAO.update(userLeave);
//		}else{
//			userLeave = new UserLeave();
//			userLeave.setLeaveId(2L);
//			userLeave.setSysUserId(userAskForOvertime.getSysUserId());
//			userLeave.setCreateDate(calendar.getTime());
//			userLeave.setUpdateDate(calendar.getTime());
//			userLeave.setStatus(1);
//			userLeave.setCount(0.0);					
//			userLeaveDAO.persist(userLeave);
//		}
		return true;
	}
	
	/*
	 * 新增UserOvertime資料
	 */
	public void newUserOvertime(String sysUserId, Double spendTime, Date nowDate) throws Exception{
		UserShiftQuota userShiftQuota = new UserShiftQuota();
		userShiftQuota.setSysUserId(sysUserId);
		userShiftQuota.setCreateTime(nowDate);
		userShiftQuota.setUpdateTime(nowDate);
		userShiftQuota.setStatus(1);
		userShiftQuota.setCount(spendTime);					
		userShiftQuotaDAO.persist(userShiftQuota);
	}


	public void GiveShiftQuota() throws Exception {
		List<SysUser> users = new ArrayList<SysUser>();
		users = sysUserDAO.getEnableRole2User();
		
		//for test
		//users.add(sysUserDAO.get("1198842813042872"));
		//users.add(sysUserDAO.get("1199963714775439"));
				
		
		for (SysUser user : users) {
			UserShiftQuota userShiftQuota = userShiftQuotaDAO.getOneByUserId(user.getSysUserId());
			if (userShiftQuota == null) {
				userShiftQuota = new UserShiftQuota(user.getSysUserId(), 1.0, 1.0, 1);
			} else {
				userShiftQuota.setCount(userShiftQuota.getQuota());
				userShiftQuota.setUpdateTime(new Date());
				
			}
			userShiftQuotaDAO.saveOrUpdate(userShiftQuota);
		}
		
	}
}
