package org.spring.boot.dubbo.provider;

import java.util.Scanner;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Provider {
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "provider.xml" });
		context.refresh();
		context.start();
		Scanner scan = new Scanner(System.in);
		System.out.println("service started");
		Boolean wait = true;
		while (wait) {
			String result = scan.nextLine();
			while (!result.equals("stop")) {
				System.out.println("service continue");
				result = scan.nextLine();
			}
			System.out.println("are you sure stop services?");
			result = scan.nextLine();
			if (!result.equals("y")) {
				System.out.println("service continue");
				continue;
			}
			wait = false;
		}
		System.out.println("service end");
		context.close();
		scan.close();
	}
}
