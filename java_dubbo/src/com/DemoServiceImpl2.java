package com;

public class DemoServiceImpl2 implements DemoService {

	@Override
	public String sayHello(String name) {
		System.out.println(name + "                2222222222被调用到了啊！！！！！！！！！");
		return "Hello " + name + "                 消息提供者为222222";
	}

}
