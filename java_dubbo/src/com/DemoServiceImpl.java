package com;

import com.alibaba.dubbo.config.annotation.Service;

public class DemoServiceImpl implements DemoService {

	@Override
	public String sayHello(String name) {
		System.out.println(name + "                1111111111111111111被调用到了啊！！！！！！！！！");
		return "Hello " + name + "        消息提供者为111111";
	}

}
