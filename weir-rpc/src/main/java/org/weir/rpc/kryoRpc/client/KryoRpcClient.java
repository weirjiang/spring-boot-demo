package org.weir.rpc.kryoRpc.client;

public interface KryoRpcClient {
	public <T> T refer(Class<T> rpcInterface) ;


	@SuppressWarnings("unchecked")
	public <T> T getProxy(Class<T> rpcInterface) ;
}
