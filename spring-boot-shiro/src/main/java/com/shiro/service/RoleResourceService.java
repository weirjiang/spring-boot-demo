package com.shiro.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shiro.dao.mapper.RoleResourceMapper;
import com.shiro.domain.Permission;

@Service
public class RoleResourceService {
	@Resource
	private RoleResourceMapper roleResourceMapper;
	public List<String> findResourceByRoleId(String roleId){
		return roleResourceMapper.findResourceByRoleId(roleId);
	}
	
	public void save(List<Permission> list,String roleId){
		roleResourceMapper.deletePermissionByRoleId(roleId);
		for(Permission permission : list){
			roleResourceMapper.save(permission);
		}
	}
}
