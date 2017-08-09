package com.shiro.test;

import java.util.List;

public class MainTest {
	public static void main(String args[]) {
		System.out.println("ClassLoaderText类的加载器的名称:" + MainTest.class.getClassLoader().getClass().getName());
		System.out.println("System类的加载器的名称:" + System.class.getClassLoader());
		System.out.println("List类的加载器的名称:" + List.class.getClassLoader());

		ClassLoader cl = MainTest.class.getClassLoader();
		while (cl != null) {
			System.out.print(cl.getClass().getName() + "->");
			cl = cl.getParent();
		}
		System.out.println(cl);
	}
}
