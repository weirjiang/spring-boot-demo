package com.shiro.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.shiro.domain.Menu;
@Mapper
public interface MenuMapper {
	public List<Menu> findByPage(int offset,int fetchSize);
	public void save(Menu menu);
	public int count();
	public void update(Menu menu);
	public void delete(int id);
	@Select("select * from menu where id=#{id}")
	public Menu findById(int id);
	@Select("select * from menu where parent=#{pid}")
	public List<Menu> findByPid(int pid);
	
	@Select("select t1.* from menu t1,user_role t2,role_resources t3 where t2.roleId= t3.roleId and t1.id=t3.resourceid and  t2.userid=#{uid}")
	public List<Menu> findByUid(String uid);
	@Select("select * from menu")
	public List<Menu> findAll();
}
