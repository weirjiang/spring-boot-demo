package com.quartz.web;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	Logger logger = Logger.getLogger(IndexController.class);
	@RequestMapping(value = "/index")
	public String index(){
		logger.info("mapper /index");
		return "index";
	}
}
