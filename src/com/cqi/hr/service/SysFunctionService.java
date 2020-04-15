package com.cqi.hr.service;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.SysFunctionDAO;
import com.cqi.hr.dao.SysRoleDAO;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysFunction;
import com.cqi.hr.entity.SysRole;
import com.cqi.hr.entity.SysUser;

@Service
public class SysFunctionService extends AbstractService<SysFunction>{
	@Resource SysFunctionDAO sysFunctionDAO;
	@Resource SysRoleDAO sysRoleDAO;
	@Override
	protected AbstractDAO<SysFunction> getDAO() {
		return sysFunctionDAO;
	}
	
	@Transactional
	public SysRole getUserMenu(String roleId){
		try {
			SysRole sr = sysRoleDAO.get(roleId);
			return sr;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Transactional
	public PagingList<SysFunction> getListByPage(Integer page, String searchFunctionName) throws Exception {
		return sysFunctionDAO.getListByPage(page, searchFunctionName);
	}

	@Transactional
	public SysFunction getByFunctionName(String functionName) throws Exception{
		return sysFunctionDAO.getByFunctionName(functionName);
	}
	@Transactional
	public String saveOrUpdate(SysFunction sysFunction, SysUser operator){
		try{
			SysFunction sysFunctionOld = getByFunctionName(sysFunction.getFunctionName());
			if(sysFunctionOld != null){
				if((StringUtils.hasText(sysFunction.getFunctionId()) && !sysFunctionOld.getFunctionId().equalsIgnoreCase(sysFunction.getFunctionId()))){
					return Constant.MANGA_ROLE_NAME_DUPLICATED;
				}	
			}
			if(!StringUtils.hasText(sysFunction.getFunctionId())){
				sysFunction.setFunctionId(sysFunctionDAO.getNextId("functionId", 3));
				
			}
			saveOrUpdate(sysFunction);
			
			//sr.set
			//saveOrUpdate(sysUser);
		}catch(Exception e){
			logger.error("save error", e);
			return Constant.NETWORK_BUSY;
		}
		return "";
	}
}
