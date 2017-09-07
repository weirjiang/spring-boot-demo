package org.weir.rpc.nioRpc.protocol;

import org.weir.rpc.nioRpc.protocol.model.RpcMessage;


/**
 * RPC codec factory, which provides static factory method to create {@link ProtocolEncoder} and {@link ProtocolDecoder} instance.
 * 
 * @author mindwind
 * @version 1.0, Aug 5, 2014
 */
public class RpcCodecFactory {
	
	
	public static ProtocolEncoder<RpcMessage> newRpcEncoder() {
		return new RpcEncoder();
	}
	
	public static ProtocolDecoder<RpcMessage> newRpcDecoder() {
		return new RpcDecoder();
	}
	
}
