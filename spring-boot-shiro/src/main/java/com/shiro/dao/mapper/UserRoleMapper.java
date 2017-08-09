package com.shiro.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.shiro.domain.Role;
import com.shiro.domain.UserRole;

@Mapper
public interface UserRoleMapper {
	@Insert("insert into user_role(userId,roleId) values(#{userId},#{roleId})")
	public void save(UserRole userRole);
	
	@Select("select roleId from user_role where userId=#{userId}")
	public List<String> getRoleIdsByUserId(String userId);
	
	@Delete("delete from user_role where userId=#{userId}")
	public void deleteByUserId(String userId);
	
	@Select("select t1.id,t1.name,t1.description from roles t1,user_role t2 where t1.id = t2.roleId and t2.userId=#{userId}")
	public List<Role> findRoleByUserId(String userId);
}
