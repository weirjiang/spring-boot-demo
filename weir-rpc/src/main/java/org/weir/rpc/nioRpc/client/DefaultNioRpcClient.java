package org.weir.rpc.nioRpc.client;

import java.lang.reflect.Proxy;

public class DefaultNioRpcClient implements NioRpcClient{
	private int port;
	private NioRpcConnector nioRpcConnector;
	public DefaultNioRpcClient(int port){
		this.port = port;
	}
	public void setNioRpcConnector(NioRpcConnector nioRpcConnector) {
		this.nioRpcConnector = nioRpcConnector;
	}
	@Override
	public void open() {
		// TODO Auto-generated method stub
		try {
			nioRpcConnector.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int getPort() {
		// TODO Auto-generated method stub
		return port;
	}

	@Override
	public <T> T refer(Class<T> rpcInterface) {
		// TODO Auto-generated method stub
		return getProxy(rpcInterface);
	}
	@SuppressWarnings("unchecked")
	public <T> T getProxy(Class<T> rpcInterface) {
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[] { rpcInterface },
				new NioRpcClientInvocationHandler(rpcInterface, port,nioRpcConnector));
	}
}
