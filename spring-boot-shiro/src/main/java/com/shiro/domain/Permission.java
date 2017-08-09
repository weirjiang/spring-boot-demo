package com.shiro.domain;
/**
 * 权限控制
 * @author weir
 * 2017年7月31日下午6:06:35
 */
public class Permission {
	private String roleId;
	private String resourceId;
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
}
