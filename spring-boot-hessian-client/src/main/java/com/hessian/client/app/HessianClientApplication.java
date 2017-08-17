package com.hessian.client.app;

import org.spring.boot.hessian.server.service.HelloWorldService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;





@SpringBootApplication
@ComponentScan(basePackages="com.hessian.client.controller")
public class HessianClientApplication {
	@Bean
	public HessianProxyFactoryBean helloClient() {
		HessianProxyFactoryBean factory = new HessianProxyFactoryBean();
		factory.setServiceUrl("http://localhost:8090/HelloWorldService");
		factory.setServiceInterface(HelloWorldService.class);
		return factory;
	}


	public static void main(String[] args) {
		SpringApplication.run(HessianClientApplication.class, args);
	}
}