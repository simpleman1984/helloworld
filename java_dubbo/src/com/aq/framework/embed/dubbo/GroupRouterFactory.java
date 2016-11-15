package com.aq.framework.embed.dubbo;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.cluster.Router;
import com.alibaba.dubbo.rpc.cluster.RouterFactory;

public class GroupRouterFactory implements RouterFactory {

	public static final String NAME = "script";
	 
	@Override
	public Router getRouter(URL url) {
		return new GroupRouter(url);
	}

}
