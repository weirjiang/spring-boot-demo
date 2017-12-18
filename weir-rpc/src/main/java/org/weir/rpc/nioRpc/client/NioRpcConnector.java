package org.weir.rpc.nioRpc.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioRpcConnector implements RpcConnector {
	private String ip;
	private int port;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public long connect() {
		// TODO Auto-generated method stub
		try {
			Selector selector = Selector.open();
			SocketChannel socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			InetSocketAddress address = new InetSocketAddress(ip, port);
			socketChannel.bind(address);
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	class Process extends Thread {
		Selector selector = null;

		public Process(Selector selector) {
			this.selector = selector;
		}

		public void run() {
			while (true) {
				int key;
				try {
					key = selector.select();
					if (key > 0) {
						Set<SelectionKey> keySet = selector.selectedKeys();
						Iterator<SelectionKey> it = keySet.iterator();
						while(it.hasNext()){
							SelectionKey selectionKey = it.next();
							
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}
		}
	}

	@Override
	public boolean disConnect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
