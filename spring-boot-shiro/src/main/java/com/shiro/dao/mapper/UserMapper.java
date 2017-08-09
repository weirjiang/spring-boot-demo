package com.shiro.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.shiro.domain.SystemUser;

@Mapper
public interface UserMapper {
	
	/**
	 * findOne
	 * @param id
	 * @return
	 */
	@Select(value="select * from users where id=#{id}")
	SystemUser findOne(String id);

	/**
	 * findAll
	 * @return
	 */
	@Select(value="select * from user")
	List<SystemUser> findAll();
	
	void insert(SystemUser user);
	//#方式能够很大程度防止sql注入。
	List<SystemUser> findByPage(Map<String,Integer> paramMap);
	@Select(value="select count(*) from users")
	int count();
	@Update(value="update users set name=#{name},password=#{password} where id = #{id}")
	public void update(SystemUser user);
	@Delete(value="delete from users where id=#{id}")
	public void delete(int id);
	@Select(value="select * from users where name=#{userName}")
	public SystemUser findByUserName(String userName);
}
