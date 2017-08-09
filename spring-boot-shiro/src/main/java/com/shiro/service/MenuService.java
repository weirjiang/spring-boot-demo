package com.shiro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shiro.dao.mapper.MenuMapper;
import com.shiro.domain.Menu;
public interface MenuService {
	
	public int count();
	
	public List<Menu> findByPage(int offset,int fetchSize);
	
	public void save(Menu menu);
	
	public void update(Menu menu);
	
	public void delete (int id);
	
	public Menu findById(int id);
	
	public List<Menu> findByPid(int parent);
	
	public List<Menu> findByUid(String uid);
	
	public List<Menu> findAll();
}
