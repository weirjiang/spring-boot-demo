package com.weir.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.weir.Application;

@Controller
public class IndexController {
	static Logger logger = LoggerFactory.getLogger(Application.class);  
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String index(){
		logger.error("this is index info");
		return "redirect:countries";
	}
}
