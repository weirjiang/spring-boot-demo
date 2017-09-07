package org.weir.rpc.nioRpc.client;

public interface NioRpcClient {
	public void open();
	public int getPort();
	public <T> T refer(Class<T> rpcInterface) ;
	
}
