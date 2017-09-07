package org.weir.rpc.nioRpc.io;


import java.io.IOException;
import java.util.List;

import org.weir.rpc.nioRpc.protocol.model.RpcMessage;

public interface RpcChannel {
	/**
	 * Write rpc message and encode it to bytes to remote peer of the channel.
	 * 
	 * @param msg
	 * @throws RpcException if any other error occurs
	 * @throws IOException 
	 */
	void write(RpcMessage msg) throws RpcException, IOException;
	
	/**
	 * Read bytes and decode it to rcp messages from remote peer of the channel.
	 * 
	 * @param bytes
	 * @return rpc message list
	 */
	List<RpcMessage> read(byte[] bytes);
}
