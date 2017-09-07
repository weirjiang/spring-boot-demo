package org.weir.rpc.jsonRpc.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.weir.rpc.jsonRpc.client.JsonMessage;
import org.weir.rpc.jsonRpc.client.typeAdapter.ClassTypeAdapter;
import org.weir.rpc.jsonRpc.client.typeAdapter.ClassTypeAdapterFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonRpcServerCenter implements JsonRpcServer{
	private static int port;
	private static final HashMap<String, Class> serviceRegistry = new HashMap<String, Class>();
	private static boolean isRunning = false;
	
	public JsonRpcServerCenter(int port) {
		this.port = port;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		isRunning = false;
	}

	@Override
	public void start() throws IOException {
		// TODO Auto-generated method stub
		ServerSocket serverSocket = new ServerSocket();
		serverSocket.bind(new InetSocketAddress(port));
		System.out.println("[weir-rpc]->start server!!!");
		ObjectOutputStream output = null;
		try {
			while (true) {
				Socket socket = serverSocket.accept();
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				String apiMessage = (String) ois.readObject();
				System.out.println("[weir-rpc]>jsonPrcServer"+apiMessage);
				GsonBuilder gsonBuilder = new GsonBuilder();
				gsonBuilder.registerTypeAdapterFactory(new ClassTypeAdapterFactory());
				gsonBuilder.registerTypeAdapter(Class.class, new ClassTypeAdapter());
				Gson gson = gsonBuilder.create();
				JsonMessage jsonMessage = gson.fromJson(apiMessage, JsonMessage.class); 
				
				String serviceName = jsonMessage.getServiceinterface();
				String methodName = jsonMessage.getMethod();
				Class<?>[] parameterTypes = jsonMessage.getParameterTypes();
                Object[] arguments = jsonMessage.getArgs();
				Class<?> serviceClass = serviceRegistry.get(serviceName);
				if (serviceClass == null) {
					throw new ClassNotFoundException("[weir-rpc>JsonRpc]" + serviceName + "not found");
				}	
				Method method = serviceClass.getMethod(methodName, parameterTypes);

				Object result = method.invoke(serviceClass.newInstance(), arguments);
				 // 3.将执行结果反序列化，通过socket发送给客户端
                output = new ObjectOutputStream(socket.getOutputStream());
                output.writeObject(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void register(Class serviceInterface, Class impl) {
		// TODO Auto-generated method stub
		serviceRegistry.put(serviceInterface.getName(), impl);
	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return isRunning;
	}

	@Override
	public int getPort() {
		// TODO Auto-generated method stub
		return port;
	}

}
