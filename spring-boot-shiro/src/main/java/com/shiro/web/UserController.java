package com.shiro.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.shiro.domain.SystemUser;
import com.shiro.domain.UserRole;
import com.shiro.json.ResultInfo;
import com.shiro.service.UserRoleService;
import com.shiro.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;
	@Autowired
	UserRoleService userRoleService;
	@RequestMapping("/toRegister")
	public String toRegister() {
		return "register";
	}

	@RequestMapping("/toLogin")
	public String toLogin() {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@Valid SystemUser user) {
		userService.save(user);
		return "login";
	}
	@RequestMapping(value="/home")
	public String home(){
		return "userManage";
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public String list(@RequestParam String page,String rows) {
		List<SystemUser> list = userService.findByPage(Integer.valueOf(page), Integer.valueOf(rows));
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("total", userService.count());
		map.put("rows", list);
		
		return JSON.toJSONString(map);
	}
	
	@RequestMapping(value="/save",method= RequestMethod.POST)
	@ResponseBody
	public String save(SystemUser user){
		if(user.getId()>0){
			userService.update(user);
		}else{
			userService.save(user);
		}
		return ResultInfo.success();
	}
	@RequestMapping(value="/delete/{id}",method=RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable String id){
		userService.delete(id);
		return ResultInfo.success();
	}
	@RequestMapping(value="/userRoleManage",method = RequestMethod.GET)
	public String userRoleManage(String userId,ModelMap map){
		map.addAttribute("userId", userId);
		return "userRoleManage";
	}
	
	
	@RequestMapping(value="/userRoleSave",method = RequestMethod.POST)
	@ResponseBody
	public String userRoleSave(String userId,String roleIds,ModelMap map){
		String roleId[] = roleIds.split(",");
		List<UserRole> userRoleList = new ArrayList<UserRole>();
		for(String id:roleId){
			if(StringUtils.hasText(id)){
				UserRole userRole = new UserRole();
				userRole.setUserId(userId);
				userRole.setRoleId(id);
				userRoleList.add(userRole);
			}
		}
		userRoleService.deleteByUserId(userId);
		userRoleService.save(userRoleList);
		map.addAttribute("userId", userId);
		return  ResultInfo.success();
	}
	@RequestMapping(value="/getRoleIdByUserId",method=RequestMethod.POST)
	@ResponseBody
	public String getRoleIdsByUserId(String userId){
		List<String> roleIds = userRoleService.getRoleIdByUserId(userId);
		return ResultInfo.success(roleIds);
	}
}
