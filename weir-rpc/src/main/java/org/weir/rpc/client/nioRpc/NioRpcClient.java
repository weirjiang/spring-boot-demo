package org.weir.rpc.client.nioRpc;

public interface NioRpcClient {
	public void open();
	public int getPort();
	public <T> T refer(Class<T> rpcInterface) ;
	
}
