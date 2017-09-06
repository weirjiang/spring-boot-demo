package org.weir.rpc.client.nioRpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.weir.rpc.client.kryoRpc.ReqMessage;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

public class NioRpcConnector {
	protected final ExecutorService executorService = Executors.newFixedThreadPool(5);
	private Map<Long, NioRpcChannel> channels;
	private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
	protected long id;
	private SocketChannel sc;
	private Selector selector;
	private static final String HOST = "localhost";
	private int port;
	protected volatile boolean            selectable      = false                                                                            ;
	protected volatile boolean            shutdown        = false                                                                            ;
	private static final Logger LOG = LoggerFactory.getLogger(NioRpcConnector.class);
	private SocketAddress                address               ;
	private final Queue<ConnectionCall>          connectQueue     = new ConcurrentLinkedQueue<ConnectionCall>();
	private final AtomicReference<ConnectThread> connectThreadRef = new AtomicReference<ConnectThread>()       ;
	private final Queue<ConnectionCall>          cancelQueue      = new ConcurrentLinkedQueue<ConnectionCall>();
	public NioRpcConnector(int port) {
		channels = new ConcurrentHashMap<Long, NioRpcChannel>();
		this.port = port;
		address = new InetSocketAddress(HOST, this.port);
		try {
			selectable =true;
			selector = Selector.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	private class ConnectThread implements Runnable {
		public void run() {
			int num = 0;
			while (selectable) {
				try {
					// the timeout for select shall be smaller of the connect timeout or 1 second
					int selected = selector.select(1000);

					// register new connect request
					num += register();

					// process connect event
					if (selected > 0) {
						num -= process();
					}

					// check connection timeout
					checkTimeout();

					// cancel
					num -= cancel();

					// last get a chance to exit infinite loop
					if (num == 0) {
						connectThreadRef.set(null);
						if (connectQueue.isEmpty()) {
							break;
						}

						if (!connectThreadRef.compareAndSet(null, this)) {
							break;
						}
					}
				} catch (Exception e) {
					LOG.warn("[CRAFT-ATOM-NIO] Connect exception", e);
				}
			}

			// if shutdown == true, shutdown the connector
			if (shutdown) {
				try {
//					shutdown0();
				} catch (Exception e) {
					LOG.error("[CRAFT-ATOM-NIO] Shutdown error", e);
				}
			}
		}
	}
	public void finishConnect(SelectionKey key) {
		System.out.println("client finish connect!");
		SocketChannel socketChannel = (SocketChannel) key.channel();
		try {
			socketChannel.finishConnect();
			synchronized (selector) {
				socketChannel.register(selector, SelectionKey.OP_WRITE);
//				key.interestOps(SelectionKey.OP_WRITE);

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void send(SelectionKey key, ReqMessage reqMessage) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();

		Kryo kryo = new Kryo();
		Output kryoOutput = new Output(sc.socket().getOutputStream());
		kryo.writeObject(kryoOutput, reqMessage);
		kryoOutput.flush();
		System.out.println("[NioRpcConnector] send reqMessage!");
		// key.interestOps(SelectionKey.OP_READ);
		try {
			synchronized (selector) {

				channel.register(selector, SelectionKey.OP_READ);
			}
		} catch (ClosedChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void read(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		int len = channel.read(byteBuffer);
		if (len > 0) {
			byteBuffer.flip();
			byte[] byteArray = new byte[byteBuffer.limit()];
			byteBuffer.get(byteArray);
			System.out.println("result" + new String(byteArray));
			len = channel.read(byteBuffer);
			byteBuffer.clear();

		}
		key.interestOps(SelectionKey.OP_READ);
	}

	public void sendReq(ReqMessage reqMessage) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(4096);
		long reqId = reqMessage.getId();
		SocketChannel channel = select(reqId).getSocketChannel();
		if (channel == null) {
			throw new NullPointerException("channel is null!");
		}
		Kryo kryo = new Kryo();
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    Output output = new Output(baos);
	    kryo.writeObject(output, reqMessage);
	    output.close();
	    byte[] bytes =  baos.toByteArray();
		System.out.println("[NioRpcConnector] send reqMessage!"+bytes.length);
		buffer.put(bytes);
		buffer.flip();
		channel.write(buffer);
		// key.interestOps(SelectionKey.OP_READ);
//		try {
//			synchronized (selector) {
//
//				channel.register(selector, SelectionKey.OP_READ);
//			}
//		} catch (ClosedChannelException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	public long connect() throws Exception   {
		try {
			Future<Channel<byte[]>> future = connect(address);
			Channel<byte[]> channel = future.get(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
			NioRpcChannel rpcChannel = new NioRpcChannel(channel.getSocketChannel());
			long id = ID_GENERATOR.incrementAndGet();
			channels.put(id,rpcChannel);
			System.out.println("connect finished");
			System.out.println("channels"+channels.values());
			LOG.debug("[CRAFT-ATOM-RPC] Rpc client connector established connection, |channel={}|.", rpcChannel);
			return id;
		} catch(Exception  e){
			e.printStackTrace();
			throw e;
		}
	}
	private NioRpcChannel select(long id) {
		Collection<NioRpcChannel> collection = channels.values();
		Object[] chs = collection.toArray();
		if (chs.length == 0)
			return null;
		int i = (int) (Math.abs(id) % chs.length);
		return (NioRpcChannel) chs[i];
	}
	
	public Future<Channel<byte[]>> connect(String ip, int port) throws IOException {
		SocketAddress remoteAddress = new InetSocketAddress(ip, port);
		return connect(remoteAddress);
	}
	
	public Future<Channel<byte[]>> connect(SocketAddress remoteAddress) throws IOException {
        return connect(remoteAddress, null);
    }
	
	public Future<Channel<byte[]>> connect(SocketAddress remoteAddress, SocketAddress localAddress) throws IOException {
		if (!this.selectable) {
			throw new IllegalStateException("The connector is already shutdown.");
		}

		if (remoteAddress == null) {
			throw new IllegalArgumentException("Remote address is null.");
		}
		
		return connectByProtocol(remoteAddress, localAddress);
	}
	
	protected Future<Channel<byte[]>> connectByProtocol(SocketAddress remoteAddress, SocketAddress localAddress) throws IOException {
		SocketChannel sc = null;
		boolean success = false;
		try {
            sc = newSocketChannel(localAddress);
            if (sc.connect(remoteAddress)) {
                // return true immediately, as established a local connection,
            	Future<Channel<byte[]>> future = executorService.submit(new ConnectionCall(sc));
            	success = true;
                return future;
            }
            success = true;
        } finally {
            if (!success && sc != null) {
                try {
                    close(sc);
                } catch (IOException e) {
                	LOG.warn("[CRAFT-ATOM-NIO] Close exception", e);
                }
            }
        }
        
        ConnectionCall cc = new ConnectionCall(sc);
        FutureTask<Channel<byte[]>> futureTask = new FutureTask<Channel<byte[]>>(cc);
        cc.setFutureTask(futureTask);
        connectQueue.add(cc);
        
        startup();
        selector.wakeup();
		return futureTask;
	}
	
	private SocketChannel newSocketChannel(SocketAddress localAddress) throws IOException {
		SocketChannel sc = SocketChannel.open();
		
        if (localAddress != null) {
            sc.socket().bind(localAddress);
        }
        sc.configureBlocking(false);
        return sc;
	}
	
	private class ConnectionCall implements Callable<Channel<byte[]>> {
		
		
		private FutureTask<Channel<byte[]>> futureTask;
		private SocketChannel socketChannel;
		private long deadline;

		
		public ConnectionCall(SocketChannel socketChannel) {
			super();
			this.socketChannel = socketChannel;
			this.deadline = System.currentTimeMillis() + 2000;
		}

		@Override
		public Channel<byte[]> call() throws Exception {
			NioRpcChannel channel = new NioRpcChannel(socketChannel);
			// finish connect, fire channel opened event
			return channel;
		}

		public SocketChannel getSocketChannel() {
			return socketChannel;
		}

		public long getDeadline() {
			return deadline;
		}

		public FutureTask<Channel<byte[]>> getFutureTask() {
			return futureTask;
		}

		public void setFutureTask(FutureTask<Channel<byte[]>> futureTask) {
			this.futureTask = futureTask;
		}
	}
	
	private void close(SocketChannel sc) throws IOException {

		SelectionKey key = sc.keyFor(selector);

		if (key != null) {
			key.cancel();
		}

		sc.close();
	}
	
	
	private void startup() {
		ConnectThread ct = connectThreadRef.get();
		if (ct == null) {
			ct = new ConnectThread();
			if (connectThreadRef.compareAndSet(null, ct)) {
				executorService.execute(ct);
			}
		}
	}
	
	
	private int register() throws IOException {
		int n = 0;
		for (;;) {
			ConnectionCall cc = connectQueue.poll();
			if (cc == null) {
				break;
			}

			SocketChannel sc = cc.getSocketChannel();
			try {
				sc.register(selector, SelectionKey.OP_CONNECT, cc);
				n++;
			} catch (Exception e) {
				close(sc);
				LOG.warn("[CRAFT-ATOM-NIO] Register connect event with exception", e);
			}
		}
		return n;
	}
	
	private int process() throws IOException {
		int n = 0;
		Iterator<SelectionKey> it = selector.selectedKeys().iterator();
		while (it.hasNext()) {
			SelectionKey key = it.next();
			ConnectionCall cc = (ConnectionCall) key.attachment();
			it.remove();

			boolean success = false;
			try {
				if (cc.getSocketChannel().finishConnect()) {
					// cancel finished key
					key.cancel();
					
					executorService.execute(cc.getFutureTask());
					n++;
				}
				success = true;
			} finally {
				if (!success) {
					// Connect failed, we have to cancel it.
					cancelQueue.offer(cc);
				}
			}
		}
		return n;
	}
	
	private void checkTimeout() {
		long now = System.currentTimeMillis();
		
		Iterator<SelectionKey> it = selector.keys().iterator();
		while (it.hasNext()) {
			ConnectionCall cc = (ConnectionCall) it.next().attachment();
			if (cc != null && now > cc.getDeadline()) {
				cancelQueue.offer(cc);
			}
		}
	}
	
	private int cancel() throws IOException {
		int n = 0;

		for (;;) {
			ConnectionCall cc = cancelQueue.poll();
			if (cc == null) {
				break;
			}

			SocketChannel sc = cc.getSocketChannel();

			try {
				close(sc);
			} finally {
				n++;
			}
		}

		if (n > 0) {
			selector.wakeup();
		}

		return n;
	}
}
