package org.weir.rpc.client.jsonRpc;


public interface JsonRpcClient {

	public <T> T refer(Class<T> rpcInterface) ;


	@SuppressWarnings("unchecked")
	public <T> T getProxy(Class<T> rpcInterface) ;
}
