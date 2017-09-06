package org.weir.rpc;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.weir.rpc.api.RpcService;
import org.weir.rpc.api.impl.RpcServiceImpl;
import org.weir.rpc.client.nioRpc.NioRpcClient;
import org.weir.rpc.client.nioRpc.NioRpcFactory;
import org.weir.rpc.server.nioRpc.NioRpcServer;

public class TestNioRpc {
	private static NioRpcClient nioRpcClient;
	private static NioRpcServer nioRpcServer;
	private static RpcService rpcService;
	@Before
	public void init() throws IOException{
		nioRpcServer = NioRpcFactory.newNioRpcServer(8090);
		nioRpcServer.open();
		nioRpcServer.export(RpcService.class, RpcServiceImpl.class);
		nioRpcClient = NioRpcFactory.newNioRpcClient(8090);
		nioRpcClient.open();
		rpcService = nioRpcClient.refer(RpcService.class);
		rpcService.hello("nioRpc");
	}
	
	@Test
	public void testBasic(){
		rpcService.hello("nioRpc");
	}
	
	public static void main(String args[]) throws IOException{
		nioRpcServer = NioRpcFactory.newNioRpcServer(8090);
		nioRpcServer.export(RpcService.class, RpcServiceImpl.class);
		nioRpcServer.open();
		nioRpcClient = NioRpcFactory.newNioRpcClient(8090);
		nioRpcClient.open();
		rpcService = nioRpcClient.refer(RpcService.class);
		rpcService.hello("nioRpc");	
	}
	
}
