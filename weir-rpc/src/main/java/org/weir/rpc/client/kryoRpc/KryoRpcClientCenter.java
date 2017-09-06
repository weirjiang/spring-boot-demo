package org.weir.rpc.client.kryoRpc;

import java.lang.reflect.Proxy;

public class KryoRpcClientCenter implements KryoRpcClient {
	public int port;
	public KryoRpcClientCenter(int port){
		this.port = port;
	}
	@Override
	public <T> T refer(Class<T> rpcInterface) {
		// TODO Auto-generated method stub
		return getProxy(rpcInterface);
	}

	@Override
	public <T> T getProxy(Class<T> rpcInterface) {
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[] { rpcInterface },
				new KryoRpcInvocationHandler(rpcInterface, port));
	}
}
