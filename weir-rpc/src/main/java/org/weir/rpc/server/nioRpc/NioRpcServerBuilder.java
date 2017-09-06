package org.weir.rpc.server.nioRpc;

public class NioRpcServerBuilder {
	int port;
	NioRpcRegistry nioRpcRegistry;

	public NioRpcServerBuilder port(int port) {
		this.port = port;
		return this;
	};
	
	public NioRpcServer build() {
		DefaultNioRpcServer server = new DefaultNioRpcServer(port);
		nioRpcRegistry = new NioRpcRegistry();
		server.setRegistry(nioRpcRegistry);
		return server;
	}
}
