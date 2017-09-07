package org.weir.rpc.nioRpc.client;

public class NioRpcClientBuilder {
	private int port;
	private NioRpcConnector nioRpcConnector;

	public NioRpcClientBuilder port(int port) {
		this.port = port;
		return this;
	}

	public NioRpcClient build() {
		DefaultNioRpcClient nioRpcClient = new DefaultNioRpcClient(port);
		nioRpcConnector = new NioRpcConnector(port);
		nioRpcClient.setNioRpcConnector(nioRpcConnector);
		return nioRpcClient;
	}
}
