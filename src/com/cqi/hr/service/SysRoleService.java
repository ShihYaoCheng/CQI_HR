package com.cqi.hr.service;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.SysRoleDAO;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysRole;
import com.cqi.hr.entity.SysUser;

@Service
public class SysRoleService extends AbstractService<SysRole>{
	@Resource SysRoleDAO sysRoleDAO;
	@Resource SysPrivilegeService sysPrivilegeService;
	@Override
	protected AbstractDAO<SysRole> getDAO() {
		return sysRoleDAO;
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
	public SysRole getByRoleName(String roleName) throws Exception{
		return sysRoleDAO.getByRoleName(roleName);
	}
	
	@Transactional
	public String saveOrUpdate(String sysRoleId, String roleName, String[] sysFunctions, SysUser operator){
		try{
			SysRole sr = getByRoleName(roleName);
			if(sr != null){
				if(!sr.getSysRoleId().equalsIgnoreCase(sysRoleId) && roleName.equalsIgnoreCase(roleName)){
					return Constant.MANGA_ROLE_NAME_DUPLICATED;
				}	
			}
			if(!StringUtils.hasText(sysRoleId)){
				sr = new SysRole();
				sr.setSysRoleId(sysRoleDAO.getNextId("sysRoleId",3));
			}
			sr.setRoleName(roleName);
			sr.setCreateUser(operator.getUserName());
			saveOrUpdate(sr);
			
			return sysPrivilegeService.saveOrUpdate(sysRoleId, Arrays.asList(sysFunctions));
			//sr.set
			//saveOrUpdate(sysUser);
		}catch(Exception e){
			logger.error("save error", e);
			return Constant.NETWORK_BUSY;
		}
	}
}
