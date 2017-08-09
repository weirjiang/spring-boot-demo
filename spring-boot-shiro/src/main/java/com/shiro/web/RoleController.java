package com.shiro.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.shiro.domain.Role;
import com.shiro.domain.SystemUser;
import com.shiro.json.ResultInfo;
import com.shiro.service.RoleService;

@Controller
@RequestMapping("/role")
public class RoleController {
	@Autowired
	RoleService roleService;

	@RequestMapping(value="/home")
	public String home(){
		return "roleManage";
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public String list(@RequestParam String page,String rows) {
		List<Role> list = roleService.findByPage(Integer.valueOf(page), Integer.valueOf(rows));
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("total", roleService.count());
		map.put("rows", list);
		
		return JSON.toJSONString(map);
	}
	
	@RequestMapping(value="/save",method= RequestMethod.POST)
	@ResponseBody
	public String save(Role role){
		if(role.getId()>0){
			roleService.update(role);
		}else{
			roleService.save(role);
		}
		return ResultInfo.success();
	}
	@RequestMapping(value="/delete/{id}",method=RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable String id){
		roleService.delete(id);
		return ResultInfo.success();
	}
}
