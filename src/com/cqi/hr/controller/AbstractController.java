package com.cqi.hr.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;

import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.util.SessionUtils;

public abstract class AbstractController<T> {
	protected Logger logger = Logger.getLogger(this.getClass());
	
	public SysUser getLoginInfo(HttpServletRequest req){
		SysUser operator = SessionUtils.getLoginInfo(req);
		
		return operator;
	}
	
	public void returnJsonMap(HttpServletRequest req, HttpServletResponse resp, Map<Object, Object> map) {
		resp.setHeader("ContentType", "text/html");
		resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("utf-8"); 
		PrintWriter out = null;
		JSONObject o = null;
		o = JSONObject.fromObject(map);
		
		try {
			out = resp.getWriter();
			out.print(o);
			
			out.flush();
			o.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			out.close();
		}
	}
	
	public void returnJsonArray(HttpServletRequest req, HttpServletResponse resp, JSONArray jsonArray) {
		resp.setHeader("ContentType", "text/html");
		resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("utf-8"); 
		PrintWriter out = null;
		
		try {
			out = resp.getWriter();
			out.print(jsonArray);
			
			out.flush();
			jsonArray.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			out.close();
		}
	}
	
	public Map<Object, Object> createResponseMsg(boolean success, Object successObject, Object errorObject) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("success", success);
		if (success)	map.put("message", successObject);
		else	map.put("message", errorObject);
		
		return map;
	}
	
	public ModelMap createPagingInfo(ModelMap model, PagingList<T> pageList){
		model.addAttribute("totalRecord", pageList.getTotalRecords());
		model.addAttribute("totalPage", pageList.getTotalPages());
		model.addAttribute("totalRecords", pageList.getTotalRecords());
		model.addAttribute("dataList", pageList.getDatas());
		model.addAttribute("currentPage", pageList.getCurrentPage());
		Integer startPage = 0;
		if((pageList.getCurrentPage() % 10) == 1){
			startPage = pageList.getCurrentPage();
		}else if((pageList.getCurrentPage() % 10) == 0){
			startPage = ((int)(pageList.getCurrentPage() / 10) - 1) * 10;
		}else{
			startPage = ((int)(pageList.getCurrentPage() / 10) * 10) + 1;
		}
		if(startPage == 0){
			startPage = 1;
		}
		int endPage = ((int)(pageList.getCurrentPage() / 10) * 10) + 10;
		if(pageList.getCurrentPage() % 10 == 0){
			endPage = pageList.getCurrentPage();
		}else if(endPage > pageList.getTotalPages()){
			endPage = pageList.getTotalPages();
		}
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		return model;
	}
	
	
}
