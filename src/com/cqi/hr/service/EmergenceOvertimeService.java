package com.cqi.hr.service;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.CompanyLeaveDAO;
import com.cqi.hr.dao.EmergenceOvertimeSignDAO;
import com.cqi.hr.dao.LineImageUrlDAO;
import com.cqi.hr.dao.SpecialDateAboutWorkDAO;
import com.cqi.hr.dao.SysUserDAO;
import com.cqi.hr.dao.UserAskForOvertimeDAO;
import com.cqi.hr.dao.UserLeaveDAO;
import com.cqi.hr.entity.EmergenceOvertimeSign;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserAskForOvertime;
import com.cqi.hr.util.LineUtils;
import com.cqi.hr.util.StringUtils;

@Service
public class EmergenceOvertimeService extends AbstractService<EmergenceOvertimeSign>{
	@Resource LineBotService lineBotService;
	@Resource UserAskForOvertimeDAO userAskForOvertimeDAO;
	@Resource CompanyLeaveDAO companyLeaveDAO;
	@Resource UserLeaveService userLeaveService;
	@Resource UserLeaveDAO userLeaveDAO;
	@Resource SysUserDAO sysUserDAO;
	@Resource EmergenceOvertimeSignDAO emergenceOvertimeSignDAO;
	@Resource LineImageUrlDAO lineImageUrlDAO;
	@Resource SpecialDateAboutWorkDAO specialDateAboutWorkDAO;
	
	@Override
	protected AbstractDAO<EmergenceOvertimeSign> getDAO() {
		return emergenceOvertimeSignDAO;
	}
	
//	@Transactional
//	public List<EmergenceOvertimeSign> findByLevel(String level) throws Exception {
//		return emergenceOvertimeSignDAO.findByLevel(level);
//	}
	
	@Transactional
	public List<EmergenceOvertimeSign> findByDate(Date startTime, Date endTime) throws Exception {
		return emergenceOvertimeSignDAO.findByDate(startTime, endTime);
	}
	
	@Transactional
	public boolean sign(String level, String sign, Long signId, SysUser sysUser) throws Exception {
		boolean succeed = false;
		EmergenceOvertimeSign data = getDAO().get(signId);
		if(data!=null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar signDate = Calendar.getInstance();
			UserAskForOvertime userAskForOvertime = userAskForOvertimeDAO.get(data.getAskForOvertimeId());
			SysUser requestUser = sysUserDAO.get(userAskForOvertime.getSysUserId());
			switch (level) {
			case Constant.LINE_EMERGENCE_LEVEL_DEPARTMENT:
				data.setDepartmentSignerId(sysUser.getSysUserId());
				data.setDepartmentSignerLineId(sysUser.getLineId());
				data.setDepartmentSignTime(signDate.getTime());
				if(sign.equals(Constant.LINE_EMERGENCE_REJECT)) {
					data.setStatus(Constant.LINE_EMERGENCE_REJECT_BY_DEPARTMENT);
				}
				getDAO().update(data);
				succeed = true;
    			if(StringUtils.hasText(requestUser.getLineId())) {
    				if(sign.equals(Constant.LINE_EMERGENCE_REJECT)) {
    					LineUtils.postToLine("您於" + sdf.format(data.getCreateTime()) + "的申請已遭部門主管退回", requestUser.getLineId());
    					userAskForOvertime.setStatus(Constant.STATUS_DISABLE);
		    			userAskForOvertimeDAO.update(userAskForOvertime);
    				}else {
    					LineUtils.postToLine("您於" + sdf.format(data.getCreateTime()) + "的申請已由部門主管確認", requestUser.getLineId());
    				}
    			}
				break;
			case Constant.LINE_EMERGENCE_LEVEL_FINANCE:
				data.setFinanceSignerId(sysUser.getSysUserId());
				data.setFinanceSignerLineId(sysUser.getLineId());
				data.setFinanceSignTime(signDate.getTime());
				if(sign.equals(Constant.LINE_EMERGENCE_REJECT)) {
					data.setStatus(Constant.LINE_EMERGENCE_REJECT_BY_FINANCE);
				}
				getDAO().update(data);
				succeed = true;
				if(StringUtils.hasText(requestUser.getLineId())) {
    				if(sign.equals(Constant.LINE_EMERGENCE_REJECT)) {
    					LineUtils.postToLine("您於" + sdf.format(data.getCreateTime()) + "的申請已遭財務退回", requestUser.getLineId());
    					userAskForOvertime.setStatus(Constant.STATUS_DISABLE);
		    			userAskForOvertimeDAO.update(userAskForOvertime);
    				}else {
    					LineUtils.postToLine("您於" + sdf.format(data.getCreateTime()) + "的申請已由財務確認", requestUser.getLineId());
    				}
    			}
				break;
			case Constant.LINE_EMERGENCE_LEVEL_ADMINISTRATION:
				data.setAdministrationSignerId(sysUser.getSysUserId());
				data.setAdministrationSignerLineId(sysUser.getLineId());
				data.setAdministrationSignTime(signDate.getTime());
				if(sign.equals(Constant.LINE_EMERGENCE_REJECT)) {
					data.setStatus(Constant.LINE_EMERGENCE_REJECT_BY_ADMINISTRATION);
				}
				getDAO().update(data);
				succeed = true;
				if(StringUtils.hasText(requestUser.getLineId())) {
    				if(sign.equals(Constant.LINE_EMERGENCE_REJECT)) {
    					LineUtils.postToLine("您於" + sdf.format(data.getCreateTime()) + "的申請已遭行政退回", requestUser.getLineId());
    					userAskForOvertime.setStatus(Constant.STATUS_DISABLE);
		    			userAskForOvertimeDAO.update(userAskForOvertime);
    				}else {
    					LineUtils.postToLine("您於" + sdf.format(data.getCreateTime()) + "的申請已由行政確認", requestUser.getLineId());
    				}
    			}
				break;
			case Constant.LINE_EMERGENCE_LEVEL_COMPANY:
				data.setCompanySignerId(sysUser.getSysUserId());
				data.setCompanySignerLineId(sysUser.getLineId());
				data.setCompanySignTime(signDate.getTime());
				if(sign.equals(Constant.LINE_EMERGENCE_REJECT)) {
					data.setStatus(Constant.LINE_EMERGENCE_REJECT_BY_COMPANY);
				}
				getDAO().update(data);
				succeed = true;
				if(StringUtils.hasText(requestUser.getLineId())) {
    				if(sign.equals(Constant.LINE_EMERGENCE_REJECT)) {
    					LineUtils.postToLine("您於" + sdf.format(data.getCreateTime()) + "的申請已遭總經理退回", requestUser.getLineId());
    					userAskForOvertime.setStatus(Constant.STATUS_DISABLE);
		    			userAskForOvertimeDAO.update(userAskForOvertime);
    				}else {
    					LineUtils.postToLine("您於" + sdf.format(data.getCreateTime()) + "的申請已由總經理確認", requestUser.getLineId());
    				}
    			}
				break;
			default:
				break;
			}
		}
		return succeed;
	}
	
}
