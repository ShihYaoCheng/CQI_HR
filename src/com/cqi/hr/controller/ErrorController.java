package com.cqi.hr.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cqi.hr.service.CreateInfo;
import com.cqi.hr.service.SysFunctionService;
import com.cqi.hr.service.SysUserService;


@Controller
@RequestMapping("/error")
public class ErrorController extends AbstractController<CreateInfo> {	
	@Resource SysUserService sysUserService;
	@Resource SysFunctionService sysFunctionService;
	
	@RequestMapping(value="/404")
    public String handle404() {
    	return "/error/404";
    }
	
}
