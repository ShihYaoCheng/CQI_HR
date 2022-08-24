package com.cqi.hr.service;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.SysUserAbsenceDAO;
import com.cqi.hr.dao.SysUserDAO;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.SysUserAbsence;

@Service
public class SysUserAbsenceService extends AbstractService<SysUserAbsence>{ 

	@Resource SysUserAbsenceDAO sysUserAbsenceDAO;
	@Resource SysUserDAO sysUserDAO;
	
	@Override
	protected AbstractDAO<SysUserAbsence> getDAO() {
		return sysUserAbsenceDAO;
	}
	
	@Transactional
	public List<SysUserAbsence> getSysUserAbsenceList(String sysUserId) throws Exception{
		return sysUserAbsenceDAO.getSysUserAbsenceBySysUserId(sysUserId);
	}
	
	
	@Transactional
	public boolean  updateLeaveOfAbsence(String sysUserId, String status , String effectiveDate ,String expirationDate){
		boolean result= false;
		try{

			if(effectiveDate !="" && expirationDate !="") {
				
				if(status.equals(Constant.SYSUSER_leave_of_absence) )
				{
					Date EffectiveDate = new SimpleDateFormat("yyyy/MM/dd").parse(effectiveDate) ;
					Date ExpirationDate = new SimpleDateFormat("yyyy/MM/dd").parse(expirationDate) ;					
					SysUserAbsence data = sysUserAbsenceDAO.getSysUserAbsenceBySysUserIdAndDate( sysUserId , EffectiveDate ,ExpirationDate);
					
					Date date = new Date();
					Timestamp timestamp = new Timestamp(date.getTime());
					
					if(data != null)
					{
						data.setSysUserId(sysUserId);
						data.setEffectiveDate(EffectiveDate);
						data.setExpirationDate(ExpirationDate);							
						data.setUpdateDate(timestamp);
						result =true;
						saveOrUpdate(data);
					}else
					{
						data = new  SysUserAbsence();
						data.setSysUserId(sysUserId);
						data.setEffectiveDate(EffectiveDate);
						data.setExpirationDate(ExpirationDate);
						data.setCreateDate(timestamp);
						data.setStatus("y");
						data.setEffectiveStatus("n");
						data.setExpirationStatus("n");
						saveOrUpdate(data);
						result =true;						
					}
				}
			}

			
		}catch(Exception e){
			logger.error("SysUserAbsenceService  updateLeaveOfAbsence : ", e);
			return result;
		}
		return result;
	}
	
	
	@Transactional
	public boolean  updateQuartz(){
		boolean result= false;
		try{
			/* 判斷 留職停薪開始
			 * */
			 List<SysUserAbsence> SysUserAbsence =  sysUserAbsenceDAO.getAllSysUserAbsence();
			 Date date = new Date();
			 for(SysUserAbsence item : SysUserAbsence )
			 {
				 if(date.after(item.getEffectiveDate()))
				 {
					 item.setEffectiveStatus("y");
					 saveOrUpdate(item);
					 SysUser sysUser = sysUserDAO.getOneBySysUserId(item.GetSysUserId());
					 if(sysUser != null)
					 {
						 sysUser.setStatus(Constant.SYSUSER_leave_of_absence);					 
						 sysUserDAO.update(sysUser);
					 }
				 }				 
			 }
			 
			 /* 判斷 留職停薪結束
			  * */
			 SysUserAbsence =  sysUserAbsenceDAO.getAllSysUserAbsence();
			 for(SysUserAbsence item : SysUserAbsence )
			 {
				 if(date.after(item.getExpirationDate()))
				 {
					 item.setExpirationStatus("y");
					 saveOrUpdate(item);
					 SysUser sysUser = sysUserDAO.getOneBySysUserId(item.GetSysUserId());
					 if(sysUser != null)
					 {
						 sysUser.setStatus(Constant.SYSUSER_ENABLE);					 
					 	sysUserDAO.update(sysUser);
					 }
				 }
			 }
			 
			 /* 判斷 留職停薪開始/結束  資料關閉
			  * */
			 SysUserAbsence =  sysUserAbsenceDAO.getAllSysUserAbsence();
			 for(SysUserAbsence item : SysUserAbsence )
			 {								
				 if(item.GetEffectiveStatus().equals("y") && item.GetExpirationStatus().equals("y"))
				 {
					 item.setStatus("n");
					 saveOrUpdate(item);
				 }
			 }

		}catch(Exception e){
			logger.error("SysUserAbsenceService  updateQuartz : ", e);
			return result;
		}
		return result;
	}
	

}
