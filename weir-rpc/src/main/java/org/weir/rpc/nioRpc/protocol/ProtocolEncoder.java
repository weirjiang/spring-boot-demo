package org.weir.rpc.nioRpc.protocol;

/**
 * Encodes higher-level protocol objects into binary data, implementor should be thread safe.
 * @author weir
 * 2017年9月7日上午10:22:23
 */
public interface ProtocolEncoder<P> {
	/**
	 * Encodes higher-level protocol objects into binary data.
	 * If input <tt>null</tt> output <tt>null</tt>
	 * 
	 * @param protocolObject
	 * @return byte array
	 * @throws ProtocolException
	 */
	byte[] encode(P protocolObject) throws ProtocolException;
}
