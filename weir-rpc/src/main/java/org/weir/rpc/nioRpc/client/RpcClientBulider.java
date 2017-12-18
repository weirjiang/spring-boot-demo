package org.weir.rpc.nioRpc.client;

public class RpcClientBulider {
	public int port;
	public String ip;
	public RpcClientBulider port(int port){
		this.port = port;
		return this;
	}
	
	public RpcClientBulider ip(String ip){
		this.ip = ip;
		return this;
	}
	
	public RpcClient build(){
		NioRpcClient rpcClient = new NioRpcClient();
		rpcClient.setPort(this.port);
		rpcClient.setIp(this.ip);
		return rpcClient;
	}
}
