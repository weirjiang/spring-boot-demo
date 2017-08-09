package com.shiro.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.shiro.domain.Permission;
@Mapper
public interface RoleResourceMapper {
	@Select("select resourceid from role_resources where roleid=#{roleId}")
	public List<String> findResourceByRoleId(String roleId);
	@Insert("insert into role_resources(roleId,resourceId) values(#{roleId},#{resourceId})")
	public void save(Permission permission);
	@Delete("delete from role_resources where roleId=#{roleId}")
	public void deletePermissionByRoleId(String roleId);
}
