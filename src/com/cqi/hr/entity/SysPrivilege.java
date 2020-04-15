package com.cqi.hr.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sys_privilege")
public class SysPrivilege implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "P_ID")
	private Integer sysPrivilegeId;
	
	@Column(name = "FUNCTION_ID")
	private String functionId;

	@Column(name = "ROLE_ID")
	private String roleId;
	
	public SysPrivilege() {}

	public String getFunctionId() {
		return functionId;
	}
	
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	
	public Integer getSysPrivilegeId() {
		return sysPrivilegeId;
	}
	
	public void setSysPrivilegeId(Integer sysPrivilegeId) {
		this.sysPrivilegeId = sysPrivilegeId;
	}
	
	public String getRoleId() {
		return roleId;
	}
	
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
}
