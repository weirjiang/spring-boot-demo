package org.weir.rpc.kryoRpc.server;

import java.io.IOException;

public interface KryoRpcServer {
	public void stop();

	public void start() throws IOException;

	public void register(Class<?> serviceInterface, Class<?> impl);

	public boolean isRunning();

	public int getPort();
}
