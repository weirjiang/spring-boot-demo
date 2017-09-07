package org.weir.rpc.jsonRpc.server;

import java.io.IOException;

public interface JsonRpcServer {
	public void stop();

	public void start() throws IOException;

	public void register(Class<?> serviceInterface, Class<?> impl);

	public boolean isRunning();

	public int getPort();
}
