package org.weir.rpc.jsonRpc.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.weir.rpc.jsonRpc.client.typeAdapter.ClassTypeAdapter;
import org.weir.rpc.jsonRpc.client.typeAdapter.ClassTypeAdapterFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonRpcClientInvocationHandler implements InvocationHandler{
	Class<?> serviceinterface = null;
	int port;
	public JsonRpcClientInvocationHandler(Class<?> rpcInterface,int port){
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
			JsonMessage jsonMessage = new JsonMessage();
			jsonMessage.setServiceinterface(serviceinterface.getName());
			jsonMessage.setMethod(method.getName());
			jsonMessage.setParameterTypes(method.getParameterTypes());
			jsonMessage.setArgs(args);
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapterFactory(new ClassTypeAdapterFactory());
			gsonBuilder.registerTypeAdapter(Class.class, new ClassTypeAdapter());
			Gson gson = gsonBuilder.create();
			String jsonStr = gson.toJson(jsonMessage);
			System.out.println(jsonStr);
			output = new ObjectOutputStream(socket.getOutputStream());
			output.writeObject(jsonStr);

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
