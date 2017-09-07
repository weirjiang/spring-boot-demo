package org.weir.rpc;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.weir.rpc.api.RpcService;
import org.weir.rpc.api.impl.RpcServiceImpl;
import org.weir.rpc.jsonRpc.client.JsonClient;
import org.weir.rpc.jsonRpc.client.JsonRpcClient;
import org.weir.rpc.jsonRpc.client.JsonRpcClientCenter;
import org.weir.rpc.jsonRpc.server.JsonRpcServer;
import org.weir.rpc.jsonRpc.server.JsonRpcServerCenter;

public class TestJsonRpc {
	static JsonRpcClient jsonRpcClient = null;

	@Before
	public void init() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					JsonRpcServer server = new JsonRpcServerCenter(8088);
					server.register(RpcService.class, RpcServiceImpl.class);
					server.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
		jsonRpcClient = new JsonRpcClientCenter(8088);
	}

	@Test
	public void testBasic() {
		RpcService rpcService = jsonRpcClient.refer(RpcService.class);
		System.out.println(rpcService.hello("weir"));
	}

	public static void main(String args[]) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					JsonRpcServer server = new JsonRpcServerCenter(8089);
					server.register(RpcService.class, RpcServiceImpl.class);
					server.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
		JsonClient client = new JsonClient(8089);
		RpcService service = (RpcService) client.refer(RpcService.class);
		System.out.println(service.hello("weir"));
	}
}
