package com.cqi.hr.service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.LocaleResolver;

import com.cqi.hr.config.LogInInfo;
import com.cqi.hr.config.WebConfigBean;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.SysLogDao;
import com.cqi.hr.entity.SysLog;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UpdateInfo;


public abstract class AbstractService<T> {
	protected abstract AbstractDAO<T> getDAO();
	protected Logger logger = Logger.getLogger(getClass());
	@Autowired protected MessageSource    messageSource;
	@Autowired protected LocaleResolver   localeResolver;
	@Autowired protected WebConfigBean	  webConfigBean;
	@Autowired private SysLogDao sysLogDAO;
	/** 紀錄系統操作紀錄 */
	public void writeLog(String functionId, String operationDesc, SysUser operator) {
		try {
			SysLog sysLog = new SysLog();
			sysLog.setFunctionId(functionId);
			sysLog.setCreateUser(operator.getSysUserId());
			sysLog.setOperationDesc(operationDesc);
			sysLog.setCreateDate(new Date());
			sysLogDAO.saveOrUpdate(sysLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void printLogWithBean(String desc, Object bean) {
		try {
			logger.info(desc + BeanUtils.describe(bean).toString());			
		} catch (IllegalAccessException | InvocationTargetException	| NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	public List<T> getList() throws Exception {
		return getDAO().get();
	}
	
	@Transactional
	public void saveOrUpdate(T t) throws Exception {
		getDAO().saveOrUpdate(t);
	}
	
	@Transactional
	public void persist(T t) throws Exception {
		getDAO().persist(t);
	}
	
	@Transactional
	public void update(T t) throws  Exception {
		getDAO().update(t);
	}
	
	@Transactional
	public void delete(T t) throws Exception {
		getDAO().delete(t);
	}
	
	@Transactional
	public void saveOrUpdate(T t, LogInInfo logInInfo) throws Exception {
		if(t instanceof UpdateInfo) {
			UpdateInfo updateInfo = (UpdateInfo)t;
			updateInfo.setUpdateUser(logInInfo.getUserId());
			updateInfo.setUpdateDate(new Date());
		}
		getDAO().saveOrUpdate(t);
	}
	
	@Transactional
	public void persist(T t, LogInInfo logInInfo) throws Exception {
		if(t instanceof CreateInfo) {
			CreateInfo createInfo = (CreateInfo)t;
			createInfo.setCreateUser(logInInfo.getUserId());
			createInfo.setCreateDate(new Date());
		}
		if(t instanceof UpdateInfo) {
			UpdateInfo updateInfo = (UpdateInfo)t;
			updateInfo.setUpdateUser(logInInfo.getUserId());
			updateInfo.setUpdateDate(new Date());
		}
		getDAO().persist(t);
	}
	
	@Transactional
	public void update(T t, LogInInfo logInInfo) throws  Exception {
		if(t instanceof UpdateInfo) {
			UpdateInfo updateInfo = (UpdateInfo)t;
			updateInfo.setUpdateUser(logInInfo.getUserId());
			updateInfo.setUpdateDate(new Date());
		}
		getDAO().update(t);
	}
	
	@Transactional
	public void delete(T t, LogInInfo logInInfo) throws Exception {
		getDAO().delete(t);
	}
		
	@Transactional
	public T get(Serializable id) throws Exception {
		return getDAO().get(id);
	}
	/**
	 * 取得多語系字串。
	 * @param key
	 * @param args
	 * @return
	 */
	@Transactional
	protected String getMessage(String key, Object...args) {
		return messageSource.getMessage(key, args, Locale.getDefault());
	}
	
	/**
	 * 取得多語系字串。
	 * @param request
	 * @param key
	 * @param args
	 * @return
	 */
	@Transactional
	protected String getMessage(HttpServletRequest request, String key, Object...args) {
		return messageSource.getMessage(key, args, localeResolver.resolveLocale(request));
	}
	
	/**
	 * 取得多語系字串。
	 * @param request
	 * @param key
	 * @param args
	 * @return
	 */
	@Transactional
	protected String getMessage(Locale local, String key, Object...args) {
		return messageSource.getMessage(key, args, local);
	}
	@ModelAttribute("webConfigBean")
	public WebConfigBean getWebConfigBean() {
		return webConfigBean;
	}
}
