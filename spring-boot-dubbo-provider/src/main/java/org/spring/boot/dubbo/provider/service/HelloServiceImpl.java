package org.spring.boot.dubbo.provider.service;

import org.spring.boot.dubbo.api.HelloService;

public class HelloServiceImpl implements HelloService{

	 public String sayHello(String name) {
	        return "Hello " + name;
	    }

}
