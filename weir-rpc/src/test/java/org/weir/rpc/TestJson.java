package org.weir.rpc;

import java.lang.reflect.Method;

import org.weir.rpc.api.RpcService;
import org.weir.rpc.jsonRpc.client.JsonBean;
import org.weir.rpc.jsonRpc.client.typeAdapter.ClassTypeAdapter;
import org.weir.rpc.jsonRpc.client.typeAdapter.ClassTypeAdapterFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestJson {
	public static void main(String args[]) throws NoSuchMethodException, SecurityException{
		JsonBean jsonBean = new JsonBean();
		String serviceName = RpcService.class.getName();
		Method method = RpcService.class.getMethod("hello", String.class);
		jsonBean.setId("123435");
		jsonBean.setName("RpcService");
		jsonBean.setParameterTypes(new Class[]{RpcService.class,RpcService.class});
		jsonBean.setParamArgs(new Object[]{"hs","js"});
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapterFactory(new ClassTypeAdapterFactory());
		gsonBuilder.registerTypeAdapter(Class.class, new ClassTypeAdapter());
		Gson gson = gsonBuilder.create();
		String jsonStr = gson.toJson(jsonBean);
		System.out.println(jsonStr);
		JsonBean bean = gson.fromJson(jsonStr, JsonBean.class);
		System.out.println(bean.toString());
		Class[] paramTypes = bean.getParameterTypes();
		System.out.println(paramTypes[0].getName());
	}
}
