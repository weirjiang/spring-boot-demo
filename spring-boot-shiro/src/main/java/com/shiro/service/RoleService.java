package com.shiro.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shiro.dao.mapper.RoleMapper;
import com.shiro.domain.Role;
import com.shiro.domain.SystemUser;

@Service
public class RoleService {
	@Autowired
	RoleMapper roleMapper;
	/**
	 * findAll
	 * 
	 * @return
	 */
	public List<Role> findAll(){
		return roleMapper.findAll();
	};
	
	public List<Role> findByPage(int offset,int fetchSize){
		Map<String,Integer> parmMap = new HashMap<String,Integer>();
		parmMap.put("offset", (offset-1)*fetchSize);
		parmMap.put("fetchSize", fetchSize);
		return roleMapper.findByPage(parmMap);
	} 
	public Role findOne(String id){
		return roleMapper.findOne(id);
	}

	public void save(Role role){
		roleMapper.insert(role);
	}
	
	public int count(){
		return roleMapper.count();
	}
	
	public void update(Role role){
		roleMapper.update(role);
	}
	
	public void delete(String id){
		roleMapper.delete(Integer.valueOf(id));
	}
}
