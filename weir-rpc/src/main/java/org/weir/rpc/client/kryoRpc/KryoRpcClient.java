package org.weir.rpc.client.kryoRpc;

public interface KryoRpcClient {
	public <T> T refer(Class<T> rpcInterface) ;


	@SuppressWarnings("unchecked")
	public <T> T getProxy(Class<T> rpcInterface) ;
}
