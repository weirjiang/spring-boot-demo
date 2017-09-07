package org.weir.rpc.jsonRpc.client;

import java.lang.reflect.Proxy;

public class JsonClient {
	public int port;
	public JsonClient(int port){
		this.port = port;
	}
	public <T> T refer(Class<T> rpcInterface) {
		// TODO Auto-generated method stub
		return getProxy(rpcInterface);
	}
	public <T> T getProxy(Class<T> rpcInterface) {
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[] { rpcInterface },
				new JsonRpcClientInvocationHandler(rpcInterface,port));
	}
}
