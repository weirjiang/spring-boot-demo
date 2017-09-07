package org.weir.rpc.nioRpc.server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NioServerAcceptor {
	private static final Logger LOG = LoggerFactory.getLogger(NioServerAcceptor.class);
	private int port;
	private NioRpcRegistry nioRpcRegistry;
	public NioServerAcceptor(int port,NioRpcRegistry nioRpcRegistry) {
		this.port = port;
		this.nioRpcRegistry = nioRpcRegistry;
	}

	private Selector selector;
	ServerSocketChannel serverSocketChannel = null;
	private boolean selectable = false;

	public void init() throws IOException {
		selector = Selector.open();
		selectable = true;
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.socket().bind(new InetSocketAddress("localhost",port));
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("[NioRpc server start!]");
		new AcceptThread().start();
	}
	public void listen() throws IOException{
		while (true) {  
            // 选择一组键，并且相应的通道已经打开  
            selector.select();  
            // 返回此选择器的已选择键集。  
            Set<SelectionKey> selectionKeys = selector.selectedKeys();  
            Iterator<SelectionKey> iterator = selectionKeys.iterator();  
            while (iterator.hasNext()) {          
                SelectionKey selectionKey = iterator.next();  
                iterator.remove();  
                acceptByProtocol(selectionKey);  
            }  
        }  
	}
	private class AcceptThread extends Thread {
		@Override
		public void run() {
			while (selectable) {
				try {
					
					
					 int selected =selector.select();
					
					 if (selected > 0) {
						 handle();
					 }

				} catch (ClosedSelectorException e) {
					LOG.error("[CRAFT-ATOM-NIO] Closed selector exception", e);
					break;
				} catch (Exception e) {
					LOG.error("[CRAFT-ATOM-NIO] Unexpected exception", e);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ie) {
					}
				}
			}

		}
	}

	private void handle() throws IOException {
		Iterator<SelectionKey> it = selector.selectedKeys().iterator();
		while (it.hasNext()) {
			SelectionKey key = it.next();
			it.remove();
			if(key.isAcceptable()){
				acceptByProtocol(key);
			}
			if (key.isReadable()) {
				NioProcessor processor = new NioProcessor(nioRpcRegistry);
				SocketChannel ssc = (SocketChannel) key.channel();
//				SocketChannel sc = ssc.accept();
				try {
					processor.process(ssc);
				} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	protected SocketChannel acceptByProtocol(SelectionKey key) throws IOException {
		System.out.println("acceptByProtocol");
		ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
		SocketChannel sc = null;
		try {
			sc = ssc.accept();
			sc.configureBlocking(false);
			sc.register(selector, SelectionKey.OP_READ);
			System.out.println("a new client connected"); 
			return sc;
		} catch (IOException e) {
			throw e;
		}
	}
}
