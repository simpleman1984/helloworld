package com;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.registry.Registry;
import com.alibaba.dubbo.registry.RegistryFactory;

public class Provider {

	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "/remote-provider.xml" });
		context.start();

//		RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getAdaptiveExtension();
//		Registry registry = registryFactory.getRegistry(URL.valueOf("redis://127.0.0.1:6379"));
//		
//		URL url = URL.valueOf("xxx://0.0.0.0/com.DemoService?category=routers&dynamic=false&rule=222222");
//		registry.register(url);
		
		System.in.read(); // 按任意键退出
	}

}
