package org.spring.boot.dubbo.consumer;

import org.spring.boot.dubbo.api.HelloService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ZookeeperConsumer {
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "zookeeper-consumer.xml" });
		context.start();

		HelloService demoService = (HelloService) context.getBean("helloService"); // 获取远程服务代理
		String hello = demoService.sayHello("world"); // 执行远程方法

		System.out.println(hello); // 显示调用结果
	}
}
