package com.shiro.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shiro.dao.mapper.UserMapper;
import com.shiro.domain.SystemUser;
@Service
public class UserService {
	@Autowired
	UserMapper userMapper;
	/**
	 * findAll
	 * 
	 * @return
	 */
	List<SystemUser> findAll(){
		return userMapper.findAll();
	};
	
	public List<SystemUser> findByPage(int offset,int fetchSize){
		Map<String,Integer> parmMap = new HashMap<String,Integer>();
		parmMap.put("offset", (offset-1)*fetchSize);
		parmMap.put("fetchSize", fetchSize);
		return userMapper.findByPage(parmMap);
	} 
	public SystemUser findOne(String id){
		return userMapper.findOne(id);
	}

	public void save(SystemUser user){
		userMapper.insert(user);
	}
	
	public int count(){
		return userMapper.count();
	}
	
	public void update(SystemUser user){
		userMapper.update(user);
	}
	
	public void delete(String id){
		userMapper.delete(Integer.valueOf(id));
	}
	
	public SystemUser findByUserName(String name){
		return userMapper.findByUserName(name);
	}
}
