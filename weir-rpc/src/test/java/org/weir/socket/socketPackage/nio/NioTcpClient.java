package org.weir.socket.socketPackage.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * packet=包体长度(4byte)+包体内容
 * 
 * @author weir 2017年9月21日上午10:45:35
 */
public class NioTcpClient {
	public static final int port = 6755;
	Selector selector = null;
	SocketChannel socketChannel = null;

	public static void main(String args[]) {
		NioTcpClient nioTcpClient = new NioTcpClient();
		nioTcpClient.connect();
		nioTcpClient.start();
	}

	public void start() {
		new ConnectThread().start();
	}

	public void connect() {
		try {
			selector = Selector.open();
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.connect(new InetSocketAddress(port));
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	class ConnectThread extends Thread {
		public void run() {
			while (true) {
				try {
					int select = selector.select();
					if (select > 0) {
						Set<SelectionKey> ketSet = selector.selectedKeys();
						Iterator<SelectionKey> keyIterator = ketSet.iterator();
						while (keyIterator.hasNext()) {
							SelectionKey key = keyIterator.next();
							if (key.isConnectable()) {
								finishConnect(key);
							}
							if (key.isWritable()) {
								System.out.println("client isWritable!");
								System.out.println("Type to send message:");
								SocketChannel socketChannel = (SocketChannel) key.channel();
								// String msg =
								// "Hello World！this is messge from NioTcpClient!";
								BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
								String msg = bufferedReader.readLine();
								sendPacket(socketChannel, msg);
								socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
							}
							if (key.isReadable()) {
								System.out.println("client isReadable!");
								processByHead(key);
							}
							keyIterator.remove();
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		public void sendPacket(SocketChannel socketChannel, String content) {
			byte[] contentBytes = new PacketWrapper(content).getBytes();
			try {
				socketChannel.write(ByteBuffer.wrap(contentBytes));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void finishConnect(SelectionKey key) {
		SocketChannel channel = (SocketChannel) key.channel();
		try {
			channel.finishConnect();
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_WRITE);
			System.out.println("client finish connect!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class PacketWrapper {
		private int length;
		private byte[] content;

		public PacketWrapper(String content) {
			this.content = content.getBytes();
			this.length = this.content.length;
		}

		public byte[] getBytes() {
			ByteBuffer buffer = ByteBuffer.allocate(4 + length);
			buffer.putInt(length);
			buffer.put(content);
			return buffer.array();
		}
	}

	public void processByHead(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		int readLength = socketChannel.read(byteBuffer);
		System.out.println("server readLength is:" + readLength);
		while (byteBuffer.remaining() > 0) {
			int position = byteBuffer.position();
			int limit = byteBuffer.limit();
			byteBuffer.flip();
			if (byteBuffer.remaining() < 4) {
				byteBuffer.position(position);
				byteBuffer.limit(limit);
				continue;
			}

			int length = byteBuffer.getInt();
			if (byteBuffer.remaining() < length) {
				byteBuffer.position(position);
				byteBuffer.limit(limit);
				continue;
			}
			// 拿到实际数据包
			byte[] data = new byte[length];

			byteBuffer.get(data, 0, length);
			System.out.println(new String(data));
			byteBuffer.compact();
			socketChannel.register(selector, SelectionKey.OP_READ);
		}
		// ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		// System.out.println("receive data from client:");
		// int len;
		// try {
		// len = socketChannel.read(byteBuffer);
		// if (len > 0) {
		// byteBuffer.flip();
		// byte[] byteArray = new byte[byteBuffer.limit()];
		// byteBuffer.get(byteArray);
		// System.out.println(new String(byteArray));
		// byteBuffer.clear();
		// len = socketChannel.read(byteBuffer);
		// }
		// socketChannel.register(selector, SelectionKey.OP_WRITE);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// try {
		// serverSocketChannel.close();
		// // selectionKey.cancel();
		// } catch (IOException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// e.printStackTrace();
		// }

	}
}
