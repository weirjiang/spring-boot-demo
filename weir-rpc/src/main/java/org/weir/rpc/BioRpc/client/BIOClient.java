package org.weir.rpc.BioRpc.client;

import java.lang.reflect.Proxy;

public class BIOClient<T> {
	public int port;

	public <T> T refer(Class<T> rpcInterface) {
		return getProxy(rpcInterface);
	}

	public BIOClient(int port) {
		this.port = port;
	}

	@SuppressWarnings("unchecked")
	public <T> T getProxy(Class<T> rpcInterface) {
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[] { rpcInterface },
				new BioClientInvocationHandler(rpcInterface,port));
	}

	
}
