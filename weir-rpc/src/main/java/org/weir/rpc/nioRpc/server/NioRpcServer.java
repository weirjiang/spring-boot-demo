package org.weir.rpc.nioRpc.server;

import java.io.IOException;

public interface NioRpcServer {
	public void export(Class<?>  rpcInterface,Class<?>  rpcInterfaceImpl);
	void open()throws IOException;
}