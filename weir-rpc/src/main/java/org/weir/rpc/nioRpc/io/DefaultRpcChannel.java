package org.weir.rpc.nioRpc.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.weir.rpc.nioRpc.client.Channel;
import org.weir.rpc.nioRpc.protocol.ProtocolDecoder;
import org.weir.rpc.nioRpc.protocol.ProtocolEncoder;
import org.weir.rpc.nioRpc.protocol.model.RpcMessage;

public class DefaultRpcChannel implements RpcChannel {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultRpcChannel.class);

	private ProtocolEncoder<RpcMessage> encoder;
	private ProtocolDecoder<RpcMessage> decoder;
	private Channel<byte[]> channel;
	public ProtocolEncoder<RpcMessage> getEncoder() {
		return encoder;
	}

	public void setEncoder(ProtocolEncoder<RpcMessage> encoder) {
		this.encoder = encoder;
	}

	public ProtocolDecoder<RpcMessage> getDecoder() {
		return decoder;
	}

	public void setDecoder(ProtocolDecoder<RpcMessage> decoder) {
		this.decoder = decoder;
	}

	public Channel<byte[]> getChannel() {
		return channel;
	}

	public void setChannel(Channel<byte[]> channel) {
		this.channel = channel;
	}

	private Map<Long, RpcFuture<?>> futures;

	// ~
	// -------------------------------------------------------------------------------------------------------------

	public DefaultRpcChannel() {
	}

	DefaultRpcChannel(Channel<byte[]> channel, ProtocolEncoder<RpcMessage> encoder, ProtocolDecoder<RpcMessage> decoder) {
		this.channel = channel;
		this.encoder = encoder;
		this.decoder = decoder;
	}

	// ~
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public void write(RpcMessage msg) throws RpcException, IOException {
		try {
			byte[] bytes = encoder.encode(msg);
			LOG.debug("[WEIR-NIO-RPC] Rpc channel write bytes, |length={}, bytes={}, channel={}|", bytes.length, bytes, channel);
			ByteBuffer buffer = ByteBuffer.allocate(4096);
			buffer.put(bytes);
			buffer.flip();
			channel.getSocketChannel().write(buffer);
			System.out.println("[WEIR-NIO-RPC] Rpc channel write bytes, |length={}"+bytes.length);
		} catch (IllegalChannelStateException e) {
			throw new RpcException(RpcException.NETWORK, "broken connection");
		}
	}

	@Override
	public List<RpcMessage> read(byte[] bytes) {
		LOG.debug("[WEIR-NIO-RPC] Rpc channel read bytes, |length={}, bytes={}, channel={}|", bytes.length, bytes, channel);
		List<RpcMessage> msgs = decoder.decode(bytes);
		return msgs;
	}

	void close() {
		channel.close();
	}

	boolean isOpen() {
		return channel.isOpen();
	}

	long getId() {
		return channel.getId();
	}

	void setRpcFuture(long mid, RpcFuture<?> future) {
		futures.put(mid, future);
	}

	void notifyRpcMessage(RpcMessage msg) {
		RpcFuture<?> future = futures.remove(msg.getId());
		if (future == null)
			return;
		future.setResponse(msg);
	}

	void notifyRpcException(Exception e) {
		for (RpcFuture<?> future : futures.values()) {
			future.setException(e);
		}
	}

	int waitCount() {
		return channel.getWriteQueue().size();
	}

}
