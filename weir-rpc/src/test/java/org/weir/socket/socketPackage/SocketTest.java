package org.weir.socket.socketPackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTest {
	public static void main(String[] args) throws IOException, InterruptedException{
		new SocketServer().start();
		new SocketClient().start();
	}
	
	static class SocketClient extends Thread{
		Socket clientSocket = new Socket();
		public SocketClient() throws IOException{
			clientSocket.connect(new InetSocketAddress(8089));
		}
		public void run(){
			String reqMessage = "HelloWorld！ from clientsocket this is test half packages!";
			try {
				for( int i=0;i<10;i++){
					OutputStream os = clientSocket.getOutputStream();
					os.write(reqMessage.getBytes());
					System.out.println("send message "+i+" "+reqMessage);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if (clientSocket != null) {
					try {
						clientSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
	}
	
	static class SocketServer extends Thread{
		ServerSocket serverSocket;
		public SocketServer(){
			try {
				serverSocket = new ServerSocket();
				serverSocket.bind(new InetSocketAddress(8089));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void run(){
			int count = 0;
			Socket socket = null;
			try {
				socket = serverSocket.accept();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while(true){
				try {
					byte[] byteBuffer = new byte[100];
					StringBuffer receivBuffer = new StringBuffer();
					InputStream reader = socket.getInputStream();
					count = reader.read(byteBuffer);
					if(count>0){
						receivBuffer.append(new String(byteBuffer,0,count));
						System.out.println("receive data from client:"+receivBuffer.toString());
					}
					count = 0;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
