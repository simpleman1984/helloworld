package com;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Consumer {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "/remote-consumer.xml" });
		context.start();

		DemoService demoService = (DemoService) context.getBean("demoService"); // 获取远程服务代理
		String hello = demoService.sayHello("111111"); // 执行远程方法

		System.out.println("参数:111111 ========" + hello); // 显示调用结果

		demoService = (DemoService) context.getBean("demoService"); // 获取远程服务代理
		hello = demoService.sayHello("111111"); // 执行远程方法

		System.out.println("参数:222222 ========" + hello); // 显示调用结果
		
		demoService = (DemoService) context.getBean("demoService"); // 获取远程服务代理
		hello = demoService.sayHello("222222"); // 执行远程方法

		System.out.println("参数:333333 ========" + hello); // 显示调用结果
		
		demoService = (DemoService) context.getBean("demoService"); // 获取远程服务代理
		hello = demoService.sayHello("111111"); // 执行远程方法

		System.out.println("参数:111111 ========" + hello); // 显示调用结果
	}

}