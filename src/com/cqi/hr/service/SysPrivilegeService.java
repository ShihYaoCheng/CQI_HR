package com.cqi.hr.service;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.SysPrivilegeDAO;
import com.cqi.hr.entity.SysPrivilege;

@Service
public class SysPrivilegeService extends AbstractService<SysPrivilege>{
	@Resource SysPrivilegeDAO sysPrivilegeDAO;
	
	@Override
	protected AbstractDAO<SysPrivilege> getDAO() {
		return sysPrivilegeDAO;
	}

	@Transactional
	public String saveOrUpdate(String roleId, List<String> functionIdList){
		try{
			List<SysPrivilege> spList = sysPrivilegeDAO.getByRoleId(roleId);
			if(spList.size() != 0){
				sysPrivilegeDAO.deleteAll(spList);
				spList.clear();
			}
			
			for(String functionId:functionIdList){
				SysPrivilege sp = new SysPrivilege();
				sp.setFunctionId(functionId);
				sp.setRoleId(roleId);
				spList.add(sp);
			}
			sysPrivilegeDAO.saveOrUpdateAll(spList);
		}catch(Exception e){
			logger.error("privilege saveOrUpdate erro: ", e);
			return Constant.NETWORK_BUSY;
		}
		return "";
	}
}
