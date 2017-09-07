package org.weir.rpc.nioRpc.server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

import org.weir.rpc.nioRpc.protocol.ProtocolDecoder;
import org.weir.rpc.nioRpc.protocol.RpcCodecFactory;
import org.weir.rpc.nioRpc.protocol.model.RpcMessage;
import org.weir.rpc.nioRpc.protocol.model.RpcMethod;

public class NioProcessor {
	private NioRpcRegistry serviceRegistry;

	public NioProcessor(NioRpcRegistry nioRpcRegistry) {
		this.serviceRegistry = nioRpcRegistry;
	}

	public void process(SocketChannel socketChannel) throws NoSuchMethodException, SecurityException, ClassNotFoundException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, InstantiationException {
		ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
		int length;
		try {
			// len = socketChannel.read(byteBuffer);
			// System.out.println("[NioProcessor length ]"+len);
			// if (len > 0) {
			// byteBuffer.flip();
			// byte[] byteArray =byteBuffer.array();;
			// Kryo kryo = new Kryo();
			// Input input = new Input(byteArray );
			// ReqMessage reqMessage = kryo.readObject(input, ReqMessage.class);
			// System.out.println("[NioProcessor]"+reqMessage);
			// String serviceName = reqMessage.getServiceinterface();
			// String methodName = reqMessage.getMethod();
			// Class<?>[] parameterTypes = reqMessage.getParameterTypes();
			// Object[] arguments = reqMessage.getArgs();
			// NioRpcApi api = new NioRpcApi();
			// api.setKey(serviceName);
			// Class<?> serviceClass =
			// serviceRegistry.lookup(api).getRpcObject();
			// if (serviceClass == null) {
			// throw new ClassNotFoundException("[weir-rpc>JsonRpc]" +
			// serviceName + "not found");
			// }
			// Method method = serviceClass.getMethod(methodName,
			// parameterTypes);
			//
			// Object result = method.invoke(serviceClass.newInstance(),
			// arguments);
			// }
//			byte[] barr = new byte[length];
//			System.arraycopy(buf.array(), 0, barr, 0, length);
			length = socketChannel.read(byteBuffer);
			if(length>0){
				System.out.println("[NioProcessor length ]"+length);
				byte[] barr = new byte[length];
				System.arraycopy(byteBuffer.array(), 0, barr, 0, length);
				ProtocolDecoder<RpcMessage> decoder = RpcCodecFactory.newRpcDecoder();
				List<RpcMessage> msgs = decoder.decode(barr);
				for (RpcMessage msg : msgs) {
					NioRpcApi api = api(msg);
					Class<?> serviceClass = serviceRegistry.lookup(api).getRpcObject();
					Method method = serviceClass.getMethod(api.getRpcMethod().getName(), api.getRpcMethod().getParameterTypes());
					method.invoke(serviceClass.newInstance(),api.getRpcMethod().getParameters());
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private NioRpcApi api(RpcMessage msg) {
		String rpcId = msg.getBody().getRpcId();
		RpcMethod rpcMethod = msg.getBody().getRpcMethod();
		Class<?> rpcInterface = msg.getBody().getRpcInterface();
		NioRpcApi api = new NioRpcApi(rpcId, rpcInterface, rpcMethod);
		return api;
	}
}
