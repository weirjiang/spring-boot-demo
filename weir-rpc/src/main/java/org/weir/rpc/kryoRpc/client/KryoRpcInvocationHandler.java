package org.weir.rpc.kryoRpc.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

public class KryoRpcInvocationHandler implements InvocationHandler{

	Class<?> serviceinterface = null;
	int port;
	public KryoRpcInvocationHandler(Class<?> rpcInterface,int port){
		this.serviceinterface = rpcInterface;
		this.port = port;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		Socket socket = null;
		ObjectOutputStream output = null;
		ObjectInputStream input = null;
		try {
			// 2.创建Socket客户端，根据指定地址连接远程服务提供者
			socket = new Socket();
			socket.connect(new InetSocketAddress(port));

			// 3.将远程服务调用所需的接口类、方法名、参数列表等编码后发送给服务提供者
			ReqMessage reqMessage = new ReqMessage();
			reqMessage.setServiceinterface(serviceinterface.getName());
			reqMessage.setMethod(method.getName());
			reqMessage.setParameterTypes(method.getParameterTypes());
			reqMessage.setArgs(args);
			Kryo kryo = new Kryo();
//			output.writeObject(jsonStr);
			Output kryoOutPut = new Output(socket.getOutputStream());
			kryo.writeObject(kryoOutPut, reqMessage);
			kryoOutPut.flush();
			// 4.同步阻塞等待服务器返回应答，获取应答后返回
			input = new ObjectInputStream(socket.getInputStream());
			return input.readObject();
		} finally {
			if (socket != null)
				socket.close();
			if (output != null)
				output.close();
			if (input != null)
				input.close();
		}
	}

}
