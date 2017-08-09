package com.shiro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shiro.dao.mapper.UserRoleMapper;
import com.shiro.domain.Role;
import com.shiro.domain.UserRole;

@Service
public class UserRoleService {
	@Autowired
	UserRoleMapper userRoleMapper;
	public void save(List<UserRole> userRoleList){
		for(UserRole userRole : userRoleList){
			userRoleMapper.save(userRole);
		}
	}
	
	public List<String> getRoleIdByUserId(String userId){
		return userRoleMapper.getRoleIdsByUserId(userId);
	}
	
	public void deleteByUserId(String userId){
		userRoleMapper.deleteByUserId(userId);
	}
	
	public List<Role> findRoleByUserId(String userId){
		return userRoleMapper.findRoleByUserId(userId);
	}
}
