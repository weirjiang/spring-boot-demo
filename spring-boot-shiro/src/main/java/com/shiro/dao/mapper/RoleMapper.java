package com.shiro.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.shiro.domain.Role;

@Mapper
public interface RoleMapper {
	/**
	 * findOne
	 * @param id
	 * @return
	 */
	@Select(value="select * from roles where id=#{id}")
	Role findOne(String id);

	/**
	 * findAll
	 * @return
	 */
	@Select(value="select * from roles")
	List<Role> findAll();
	
	void insert(Role role);
	//#方式能够很大程度防止sql注入。
	List<Role> findByPage(Map<String,Integer> paramMap);
	@Select(value="select count(*) from roles")
	int count();
	@Update(value="update roles set name=#{name},description=#{description} where id = #{id}")
	public void update(Role role);
	@Delete(value="delete from roles where id=#{id}")
	public void delete(int id);
}
