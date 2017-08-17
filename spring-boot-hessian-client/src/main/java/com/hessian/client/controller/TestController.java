package com.hessian.client.controller;

import org.spring.boot.hessian.server.service.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@Autowired
	HelloWorldService helloWorldService;

	@RequestMapping("/test")
	public String test() {
		 System.out.println(helloWorldService.sayHello("Spring boot with Hessian."));

		 return helloWorldService.sayHello("Spring boot with Hessian.");

	}
}
