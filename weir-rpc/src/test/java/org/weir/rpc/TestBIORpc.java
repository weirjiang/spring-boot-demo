package org.weir.rpc;

import java.io.IOException;

import org.weir.rpc.BioRpc.client.BIOClient;
import org.weir.rpc.BioRpc.server.BIOServer;
import org.weir.rpc.BioRpc.server.BIOServiceCenter;
import org.weir.rpc.api.RpcService;
import org.weir.rpc.api.impl.RpcServiceImpl;

public class TestBIORpc {
	public static void main(String[] args) throws IOException {
		new Thread(new Runnable() {
			public void run() {
				try {
					BIOServer serviceServer = new BIOServiceCenter(8088);
					serviceServer.register(RpcService.class, RpcServiceImpl.class);
					serviceServer.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		BIOClient bioClient = new BIOClient(8089);
		
		RpcService service = (RpcService) bioClient.getProxy(RpcService.class);
		System.out.println(service.hello("hello"));
	}
}
