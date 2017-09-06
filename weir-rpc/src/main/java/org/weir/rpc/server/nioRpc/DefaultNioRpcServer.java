package org.weir.rpc.server.nioRpc;

import java.io.IOException;


public class DefaultNioRpcServer implements NioRpcServer{
	int port;
	NioRpcRegistry registry;
	public DefaultNioRpcServer(int port){
//		registry = new NioRpcRegistry();
		this.port = port;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public NioRpcRegistry getRegistry() {
		return registry;
	}
	public void setRegistry(NioRpcRegistry registry) {
		this.registry = registry;
	}
	@Override
	public void export(Class<?> rpcInterface, Class<?> rpcInterfaceImpl) {
		// TODO Auto-generated method stub
		NioRpcApi api = new NioRpcApi();
		api.setKey(rpcInterface.getName());
		api.setRpcObject(rpcInterfaceImpl);
		registry.register(api);
	}

	@Override
	public void open() throws IOException {
		NioServerAcceptor acceptor = new NioServerAcceptor(port,registry);
		// TODO Auto-generated method stub
		acceptor.init();
	}

}
