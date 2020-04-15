package com.cqi.hr.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * menu
 * */
@Entity
@Table(name = "sys_function")
public class SysFunction implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "SYS_FUNCTION_ID")
	private String functionId;
	
	@Column(name = "FUNCTION_URL")
	private String functionUrl;
	
	@Column(name = "MODULE_NAME")
	private String moduleName;
	
	@Column(name = "FUNCTION_NAME")
	private String functionName;

	@Column(name = "SUB_MODULE_NAME")
	private String subModuleName;
	
	@Column(name = "IS_VISIBLE")
	private String isVisible;
	
	public SysFunction() {}

	public String getFunctionId() {
		return functionId;
	}
	
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	
	public String getModuleName() {
		return moduleName;
	}
	
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	public String getSubModuleName() {
		return subModuleName;
	}
	
	public void setSubModuleName(String subModuleName) {
		this.subModuleName = subModuleName;
	}
	
	public String getFunctionName() {
		return functionName;
	}
	
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	
	public String getFunctionUrl() {
		return functionUrl;
	}
	
	public void setFunctionUrl(String functionUrl) {
		this.functionUrl = functionUrl;
	}
	
	public String getIsVisible() {
		return isVisible;
	}
	
	public void setIsVisible(String isVisible) {
		this.isVisible = isVisible;
	}
}
