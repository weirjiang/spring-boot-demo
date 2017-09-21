package org.weir.socket.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class TestOkio {
	public static void main(String args[]){
		new ClientThread().start();
		new ServerThread().start();
	}
	
	static class ClientThread extends Thread{
		Socket socket = null;
		public void run(){
			try {
				socket = new Socket("127.0.0.1",8089);
				InputStream io = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				BufferedSource source = Okio.buffer(Okio.source(io));
				BufferedSink sink = Okio.buffer(Okio.sink(os));
//				sink.writeInt(888);
//				sink.flush();
				while(true){
					writeMsg(sink, "hello this is Okio jar test!!!");
					
				}
//				sink.writeString("hello this is Okio jar test!!!", Charset.forName("utf-8"));
//				sink.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		 private static void writeMsg(BufferedSink sink, String msg) {
		        try {
		            int msgLength = msg.getBytes().length;
		            sink.writeInt(msgLength);
//		            for(int i=0;i<30;i++){
		            	sink.writeString(msg, Charset.forName("utf-8"));
//		            }
		            sink.flush();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
	}
	
	static class ServerThread extends Thread{
		ServerSocket serverSocket = null;
		public void run(){
			try {
				serverSocket = new ServerSocket(8089);
				while(true){
					Socket socket = serverSocket.accept();
					handleRequest(socket);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		public  void handleRequest(Socket socket){
			final Socket clientSocket = socket;
			Thread handleThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true){
						try {
							BufferedSource source = Okio.buffer(Okio.source(clientSocket));
							int length = source.readInt();
							String str = source.readString( Charset.forName("utf-8"));
							System.out.println("server read:"+length);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
				}
			});
			handleThread.start();
		}
	}
}
