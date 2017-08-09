package com.shiro.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shiro.dao.mapper.MenuMapper;
import com.shiro.domain.Menu;
import com.shiro.service.MenuService;
@Service("menuService")
public class MenuServiceImpl implements MenuService{
	@Autowired
	MenuMapper menuMapper;
	
	public int count(){
		return menuMapper.count();
	}
	
	public List<Menu> findByPage(int offset,int fetchSize){
		return menuMapper.findByPage((offset-1)*fetchSize, fetchSize);
	} 
	
	public void save(Menu menu){
		menuMapper.save(menu);
	}
	
	public void update(Menu menu){
		menuMapper.update(menu);
	}
	
	public void delete (int id){
		menuMapper.delete(id);
	}
	
	public Menu findById(int id){
		return menuMapper.findById(id);
	}
	
	public List<Menu> findByPid(int parent){
		return menuMapper.findByPid(parent);
	}
	
	public List<Menu> findByUid(String uid){
		return menuMapper.findByUid(uid);
	}
	
	public List<Menu> findAll(){
		return menuMapper.findAll();
	}
}
