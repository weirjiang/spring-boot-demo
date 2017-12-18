package org.weir.rpc.nioRpc.client;

public interface RpcConnector {
	long connect();

	boolean disConnect();

	void close();
}
