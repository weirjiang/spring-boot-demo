package org.weir.rpc.nioRpc.client;

import org.weir.rpc.nioRpc.server.NioRpcServer;
import org.weir.rpc.nioRpc.server.NioRpcServerBuilder;



public class NioRpcFactory {
	public static NioRpcClient newNioRpcClient(int port){
		return new NioRpcClientBuilder().port(port).build();
	}
	
	public static NioRpcServer newNioRpcServer(int port){
		return new NioRpcServerBuilder().port(port).build();
	}
}
