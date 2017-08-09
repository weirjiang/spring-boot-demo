package com.shiro.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.shiro.service.UserService;

@Controller
public class IndexController {
	@Autowired
	UserService userService;
	@RequestMapping(value="/user")
	@ResponseBody
	public String findUserById(@RequestParam String id){
		return JSON.toJSONString(userService.findOne(id));
	}
	@RequestMapping(value="/index")
	public String index(){
		System.out.println("-------------index-------------");
		return "index";
	}
}
