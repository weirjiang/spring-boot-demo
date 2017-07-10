package com.redis.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/redis")
public class RedisController {
	@RequestMapping(value="/index")
	public String index(){
		System.out.println("index-----------");
		return "index";
	}
}
