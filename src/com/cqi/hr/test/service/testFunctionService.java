package com.cqi.hr.test.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.service.SysFunctionService;
import com.cqi.hr.test.AbstractTester;


public class testFunctionService  extends AbstractTester{
	
	//@Autowired private SysPrivilegeService sysPrivilegeService;
	@Autowired private SysFunctionService sysFunctionService;
	//@Autowired private SysRoleService sysRoleService;
	
	@Test
	@Transactional
	public void testGetRoleFunction() {
		try {
			logger.info("testGetRoleFunction");
			System.out.println("Test");
			sysFunctionService.getUserMenu("1");
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("Test e" + e.getMessage());
		}
		
	}
}
