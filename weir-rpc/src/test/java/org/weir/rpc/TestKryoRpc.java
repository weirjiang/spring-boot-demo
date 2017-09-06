package org.weir.rpc;

import java.io.IOException;

import org.weir.rpc.api.RpcService;
import org.weir.rpc.api.impl.RpcServiceImpl;
import org.weir.rpc.client.kryoRpc.KryoRpcClient;
import org.weir.rpc.client.kryoRpc.KryoRpcClientCenter;
import org.weir.rpc.server.kryoRpc.KryoRpcServer;
import org.weir.rpc.server.kryoRpc.KryoRpcServerCenter;

public class TestKryoRpc {
	public static void main(String args[]) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					KryoRpcServer server = new KryoRpcServerCenter(8089);
					server.register(RpcService.class, RpcServiceImpl.class);
					server.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
		KryoRpcClient client = new KryoRpcClientCenter(8089);
		RpcService service = (RpcService) client.refer(RpcService.class);
		System.out.println(service.hello("kryo"));
	}
}
