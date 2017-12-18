package org.weir.rpc.nioRpc.client;

public interface RpcClient {
	public void connect();
	public void open();
	public void refer(Class service);
}
