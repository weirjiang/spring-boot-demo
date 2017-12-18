package org.weir.socket.socketPackage;

import java.nio.ByteBuffer;

public class NioTest {
	public static void main(String args[]){
		ByteBuffer byteBuffer = ByteBuffer.allocate(10);
		byteBuffer.put((byte)'H').put((byte)'E').put((byte)'L').put((byte)'L').put((byte)'O');
		byteBuffer.flip();
		System.out.println((char)byteBuffer.get());
		System.out.println((char)byteBuffer.get());
		System.out.println(byteBuffer.mark());
	}
}
