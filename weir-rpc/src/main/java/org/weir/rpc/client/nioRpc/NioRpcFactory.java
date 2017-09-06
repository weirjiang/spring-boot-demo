package org.weir.rpc.client.nioRpc;

import org.weir.rpc.server.nioRpc.NioRpcServer;
import org.weir.rpc.server.nioRpc.NioRpcServerBuilder;



public class NioRpcFactory {
	public static NioRpcClient newNioRpcClient(int port){
		return new NioRpcClientBuilder().port(port).build();
	}
	
	public static NioRpcServer newNioRpcServer(int port){
		return new NioRpcServerBuilder().port(port).build();
	}
}
