package org.weir.rpc.jsonRpc.client;


public interface JsonRpcClient {

	public <T> T refer(Class<T> rpcInterface) ;


	@SuppressWarnings("unchecked")
	public <T> T getProxy(Class<T> rpcInterface) ;
}
