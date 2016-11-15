package com;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.registry.Registry;
import com.alibaba.dubbo.registry.RegistryFactory;

public class RouterHelper {
	
	public RouterHelper(){
		registerRoutes();
	}

	public void registerRoutes(){
		RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getAdaptiveExtension();
		
		Registry registry = registryFactory.getRegistry(URL.valueOf("redis://127.0.0.1:6379"));
		
		registry.register(URL.valueOf("xxx://0.0.0.0/com.DemoService?category=routers"));
	}
	
}
