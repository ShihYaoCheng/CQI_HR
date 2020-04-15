package com.cqi.hr.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * menu
 * */
@Entity
@Table(name = "sys_role")
public class SysRole implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "SYS_ROLE_ID")
	private String sysRoleId;
	
	@Column(name = "role_name")
	private String roleName;
	
	@Column(name = "role_desc")
	private String roleDesc;
	
	@Column(name = "create_user")
	private String createUser;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="sys_privilege"
	, joinColumns={@JoinColumn(name="ROLE_ID", unique=true)}
	, inverseJoinColumns={@JoinColumn(name="FUNCTION_ID")})
	@OrderBy("functionId")
	@Fetch (FetchMode.SELECT)
	private Set<SysFunction> sysPrivilegeSet;

	public SysRole() {}

	public String getSysRoleId() {
		return sysRoleId;
	}
	
	public void setSysRoleId(String sysRoleId) {
		this.sysRoleId = sysRoleId;
	}
	
	public String getRoleName() {
		return roleName;
	}
	
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public Set<SysFunction> getSysPrivilegeSet() {
		return sysPrivilegeSet;
	}
	
	public void setSysPrivilegeSet(Set<SysFunction> sysPrivilegeSet) {
		this.sysPrivilegeSet = sysPrivilegeSet;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

}
