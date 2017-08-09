package com.shiro.service.impl;

import org.springframework.stereotype.Service;

import com.shiro.service.ServiceTest;
@Service("serviceTest")
public class ServiceTestImpl implements ServiceTest{

	public void test() {
		System.out.println("this is test service");
		
	}

}
