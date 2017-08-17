package org.spring.boot.hessian.server.service;

import org.springframework.stereotype.Service;

@Service("HelloWorldService")
public class HelloWorldServiceImpl implements HelloWorldService {

	@Override
	public String sayHello(String name) {
		return "Hello World! " + name;

	}

}
