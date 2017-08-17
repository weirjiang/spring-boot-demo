package com.hessian.client.app;

public class MainTest {
	public static void main(String args[]){
		System.out.println("mainThread id is:"+Thread.currentThread().getId());
		MyThread thread = new MyThread();
		thread.start();
	}
	
	static	class MyThread extends Thread{
		@Override
		public void run(){
			System.out.println("myThread id id:"+Thread.currentThread().getId());
		}
	}
}
