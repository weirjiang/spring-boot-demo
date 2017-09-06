package org.weir.rpc.server.nioRpc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.weir.rpc.client.kryoRpc.ReqMessage;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

public class NioProcessor {
	private NioRpcRegistry serviceRegistry;
	public NioProcessor(NioRpcRegistry nioRpcRegistry){
		this.serviceRegistry = nioRpcRegistry;
	}
	public void process(SocketChannel socketChannel) throws NoSuchMethodException, SecurityException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
		ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
		int len;
		try {
			len = socketChannel.read(byteBuffer);
			System.out.println("[NioProcessor length ]"+len);
			if (len > 0) {
				byteBuffer.flip();
				byte[] byteArray =byteBuffer.array();;
				Kryo kryo = new Kryo();
				Input input = new Input(byteArray );
				ReqMessage reqMessage = kryo.readObject(input, ReqMessage.class);
				System.out.println("[NioProcessor]"+reqMessage);
				String serviceName = reqMessage.getServiceinterface();
				String methodName = reqMessage.getMethod();
				Class<?>[] parameterTypes = reqMessage.getParameterTypes();
                Object[] arguments = reqMessage.getArgs();
                NioRpcApi api = new NioRpcApi();
                api.setKey(serviceName);
				Class<?> serviceClass = serviceRegistry.lookup(api).getRpcObject();
				if (serviceClass == null) {
					throw new ClassNotFoundException("[weir-rpc>JsonRpc]" + serviceName + "not found");
				}	
				Method method = serviceClass.getMethod(methodName, parameterTypes);

				Object result = method.invoke(serviceClass.newInstance(), arguments);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
