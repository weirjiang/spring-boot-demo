package org.weir.rpc.jsonRpc.client;

import java.lang.reflect.Proxy;

public class JsonRpcClientCenter<T>  implements JsonRpcClient{
	public int port;
	public JsonRpcClientCenter(int port){
		this.port = port;
	}
	@SuppressWarnings("hiding")
	@Override
	public <T> T refer(Class<T> rpcInterface) {
		// TODO Auto-generated method stub
		return getProxy(rpcInterface);
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	@Override
	public <T> T getProxy(Class<T> rpcInterface) {
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[] { rpcInterface },
				new JsonRpcClientInvocationHandler(rpcInterface,port));
	}

}
