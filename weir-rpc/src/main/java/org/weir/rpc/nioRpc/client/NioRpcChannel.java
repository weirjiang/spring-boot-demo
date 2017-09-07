package org.weir.rpc.nioRpc.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

import org.weir.rpc.kryoRpc.client.ReqMessage;


public class NioRpcChannel implements Channel<byte[]>{
	private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
	protected long                  id   ;
	private SocketChannel socketChannel;
	public NioRpcChannel(){
	}
	
	public NioRpcChannel(SocketChannel socketChannel){
		this.id = ID_GENERATOR.incrementAndGet();
		this.socketChannel = socketChannel;
	}
	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean write(byte[] data) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isClosing() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isClosed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPaused() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getAttribute(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object setAttribute(Object key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object removeAttribute(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsAttribute(Object key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SocketAddress getRemoteAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SocketAddress getLocalAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Queue<byte[]> getWriteQueue() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SocketChannel getSocketChannel() {
		return socketChannel;
	}
	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	@Override
	public void Write(ReqMessage reqMessage) {
		// TODO Auto-generated method stub
		
	}
}
