package org.weir.socket.socketPackage.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioTcpServer {
	public static final int port = 6755;
	ServerSocketChannel serverSocketChannel = null;
	Selector selector = null;
	private static int number = 0;

	public static void main(String args[]) throws IOException {
		NioTcpServer server = new NioTcpServer();
		server.init();
		server.start();
	}

	public void init() {
		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket().bind(new InetSocketAddress(port));
			serverSocketChannel.configureBlocking(false);
			selector = Selector.open();
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("server start on port " + port + " ...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void start() {
		new ServerThread().run();
	}

	class ServerThread extends Thread {
		public void run() {
			try {

				while (true) {
					if (selector.select() > 0) {
						Set<SelectionKey> selectionKeys = selector.selectedKeys();
						Iterator<SelectionKey> iterator = selectionKeys.iterator();
						while (iterator.hasNext()) {
							SelectionKey key = iterator.next();
							if (!key.isValid()) {
								continue;
							}
							if (key.isAcceptable()) {
								handAccept(key);
							}
							if (key.isReadable()) {
								System.out.println("server isReadable");
								handleRead(key);
								System.out.println("server read complete");
							}
							if (key.isWritable()) {
								System.out.println("server isWritable");
								handleWrite(key);
							}
							iterator.remove();
						}

					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void handAccept(SelectionKey key) {
		try {
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
			SocketChannel socketChannel = serverSocketChannel.accept();
			System.out.println("is acceptable");
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class ReadThread extends Thread {
		SelectionKey selectionKey = null;

		public ReadThread(SelectionKey selectionKey) {
			this.selectionKey = selectionKey;
		}

		public void run() {
			SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			try {
				while (socketChannel.read(byteBuffer) > 0) {
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

					byte[] data = new byte[length];

					byteBuffer.get(data, 0, length);
					System.out.println(new String(data) + " <---> " + number++);
					byteBuffer.compact();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void handleRead(SelectionKey key) throws IOException {
		ReadThread readThread = new ReadThread(key);
		readThread.start();
	}

	public void handleWrite(SelectionKey selectionKey) {
		SendThread sendThread = new SendThread(selectionKey);
		sendThread.start();
	}

	class SendThread extends Thread {
		SelectionKey selectionKey = null;
		SocketChannel socketChannel = null;

		public SendThread(SelectionKey selectionKey) {
			this.selectionKey = selectionKey;
			socketChannel = (SocketChannel) selectionKey.channel();
		}

		public void run() {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("please enter response msg!");
			while (true) {
				try {
					String msg = bufferedReader.readLine();
					this.socketChannel.write(ByteBuffer.wrap(new PacketWrapper(msg).getBytes()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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

}
