package com.cqi.hr.service;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.SysRoleDAO;
import com.cqi.hr.dao.SysUserDAO;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysRole;
import com.cqi.hr.entity.SysUser;

@Service
public class SysUserService extends AbstractService<SysUser>{
	@Resource SysUserDAO sysUserDAO;
	@Resource SysRoleDAO sysRoleDAO;
	
	@Override
	protected AbstractDAO<SysUser> getDAO() {
		return sysUserDAO;
	}
	
	@Transactional
	public PagingList<SysUser> getListByPage(Integer page, String searchUserName) throws Exception {
		return sysUserDAO.getListByPage(page, searchUserName);
	}

	@Transactional
	public PagingList<SysUser> getSysUserStatusListByPage(Integer page, String searchUserName) throws Exception {
		return sysUserDAO.getSysUserStatusListByPage(page, searchUserName);
	}
	
	@Transactional
	public PagingList<SysRole> getRoleListByPage(Integer page, String searchRoleName) throws Exception {
		return sysRoleDAO.getListByPage(page, searchRoleName);
	}



	@Transactional
	public List<SysRole> getSysRoleList() throws Exception{
		return sysRoleDAO.get();
	}
	
	@Transactional
	public SysRole getSysRoleByRoleName(String roleName) throws Exception{
		return sysRoleDAO.getByRoleName(roleName);
	}
	
	@Transactional
	public String saveOrUpdate(SysUser sysUser, SysUser su){
		try{
			SysUser databaseUser = sysUserDAO.get(sysUser.getSysUserId());
			if(null != databaseUser) {
				databaseUser.setOriginalName(sysUser.getOriginalName());
				databaseUser.setUserName(sysUser.getUserName());
				databaseUser.setGender(sysUser.getGender());
				databaseUser.setEmail(sysUser.getEmail());
				databaseUser.setDepartment(sysUser.getDepartment());
				databaseUser.setInaugurationDate(sysUser.getInaugurationDate());
				databaseUser.setGraduationDate(sysUser.getGraduationDate());
				if(!StringUtils.hasText(sysUser.getCardId())) {
					sysUser.setCardId("NULL");
				}
				databaseUser.setCardId(sysUser.getCardId());
				databaseUser.setDefaultProjectId(sysUser.getDefaultProjectId());
				databaseUser.setModifyDate(new Date());
				databaseUser.setModifyId(su.getSysUserId());
				update(databaseUser);
			}
		}catch(Exception e){
			logger.error("save error", e);
			return Constant.NETWORK_BUSY;
		}
		return "";
	}
	
	@Transactional
	public SysUser saveOrUpdateAsanaUser(SysUser sysUser){
		try{
			SysUser databaseUser = sysUserDAO.get(sysUser.getSysUserId());
			if(null==databaseUser) {
				persist(sysUser);
				return sysUser;
			}else {
				databaseUser.setModifyDate(new Date());
				databaseUser.setEmail(sysUser.getEmail());
				databaseUser.setUserName(sysUser.getUserName());
				update(databaseUser);
				return databaseUser;
			}
		}catch(Exception e){
			logger.error("save error", e);
		}
		return null;
	}
	
	@Transactional
	public String updateSelfInfo(SysUser sysUser, String userName, String password, String newPassword){
		try{
			if(StringUtils.hasText(password) && !sysUser.getPassword().equals(password)){
				return Constant.PASSWORD_INCORRECT;
			}else if(StringUtils.hasText(newPassword)){
				sysUser.setPassword(newPassword);
			}
			
			if(StringUtils.hasText(userName)){
				sysUser.setUserName(userName);
			}
			saveOrUpdate(sysUser);
		}catch(Exception e){
			logger.error("save error", e);
			return Constant.NETWORK_BUSY;
		}
		return "";
	}
	
	@Transactional
	public Map<String, SysUser> getUserMapping() throws Exception {
		Map<String, SysUser> mapping = new HashMap<>();
		List<SysUser> list = sysUserDAO.get();
		for(SysUser user : list){
			mapping.put(user.getSysUserId(), user);
		}
		return mapping;
	}
	
	@Transactional
	public Map<String, SysUser> getUserCardMapping() throws Exception {
		Map<String, SysUser> mapping = new HashMap<>();
		List<SysUser> list = sysUserDAO.get();
		for(SysUser user : list){
			if(StringUtils.hasText(user.getCardId()) && !user.getCardId().equals("NULL")) {
				mapping.put(user.getCardId(), user);
			}
		}
		return mapping;
	}
	
	@Transactional
	public List<SysUser> getUserList() throws Exception {
		return sysUserDAO.getEnableUser();
	}
	
	@Transactional
	public List<SysUser> getAllUserList() throws Exception {
		return sysUserDAO.get();
	}
	
	
	@Transactional
	public List<SysUser> getEnableUserOderByList(String propertyName) throws Exception {
		List<SysUser> listSysUsers = sysUserDAO.getEnableUserOrderByDesc(propertyName);
		return listSysUsers;
	}
	
	@Transactional
	public SysUser getByLineId(String lineId) throws Exception{
		return sysUserDAO.getByLineId(lineId);
	}
	
	@Transactional
	public Map<String, SysUser> getMapEnableRule2User() throws Exception {
		Map<String, SysUser> mapping = new HashMap<>();
		List<SysUser> list = sysUserDAO.getEnableRole2User();
		for(SysUser user : list){
			mapping.put(user.getSysUserId(), user);
		}
		return mapping;
	}

	@Transactional
	public Map<String, SysUser> getEnableRole2UserStatus() throws Exception {
		Map<String, SysUser> mapping = new HashMap<>();
		List<SysUser> list = sysUserDAO.getEnableRole2UserStatus();
		for(SysUser user : list){
			mapping.put(user.getSysUserId(), user);
		}
		return mapping;
	}

	@Transactional
	public Map<String, SysUser> getOneBySysUserId(String sysUserId)throws Exception{		
		Map<String, SysUser> mapping = new HashMap<>();
		SysUser user = sysUserDAO.getOneBySysUserId(sysUserId);
		if( user != null){
			mapping.put(user.getSysUserId(), user);
		}
		return mapping;
	}

	@Transactional
	public boolean  updateLeaveOfAbsence(String sysUserId, String status, String cardid , String effectiveDate ,String expirationDate){
		boolean result= false;
		try{
			SysUser sysUser = sysUserDAO.getOneBySysUserId(sysUserId);
			if(sysUser != null )
			{
				if(effectiveDate !="" && expirationDate !="" && status.equals(Constant.SYSUSER_leave_of_absence)  )
				{
					Date today = new Date();
					Date EffectiveDate = new SimpleDateFormat("yyyy/MM/dd").parse(effectiveDate) ;
					//Date ExpirationDate = new SimpleDateFormat("yyyy/MM/dd").parse(expirationDate) ;								
					if( today.after(EffectiveDate) )
					{
						status = Constant.SYSUSER_leave_of_absence;	
					}else
					{
						status = Constant.SYSUSER_ENABLE;
					}
					
				}
				sysUser.setStatus(status);
				sysUser.setCardId(cardid);
				saveOrUpdate(sysUser);
				result = true;
				
			}
		}catch(Exception e){
			logger.error("save error", e);
			return result;
		}
		return result;
	}
}
