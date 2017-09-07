package org.weir.rpc.nioRpc.protocol.model;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * A <code>RpcOption</code> provides optional information about rpc invocation.
 * 
 * @author mindwind
 * @version 1.0, Aug 8, 2014
 */
public class RpcOption implements Serializable {

	private static final long serialVersionUID = -9035174922898235358L;

	transient private InetSocketAddress serverAddress;
	transient private InetSocketAddress clientAddress;
	private int rpcTimeoutInMillis = Integer.MAX_VALUE;
	public InetSocketAddress getServerAddress() {
		return serverAddress;
	}
	public void setServerAddress(InetSocketAddress serverAddress) {
		this.serverAddress = serverAddress;
	}
	public InetSocketAddress getClientAddress() {
		return clientAddress;
	}
	public void setClientAddress(InetSocketAddress clientAddress) {
		this.clientAddress = clientAddress;
	}
	public int getRpcTimeoutInMillis() {
		return rpcTimeoutInMillis;
	}
	public void setRpcTimeoutInMillis(int rpcTimeoutInMillis) {
		this.rpcTimeoutInMillis = rpcTimeoutInMillis;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
