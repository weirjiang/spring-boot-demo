package org.weir.rpc;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;

import org.weir.rpc.api.RpcService;
import org.weir.rpc.jsonRpc.client.JsonBean;
import org.weir.rpc.jsonRpc.client.typeAdapter.ClassTypeAdapter;
import org.weir.rpc.jsonRpc.client.typeAdapter.ClassTypeAdapterFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestJson {
	public static void main(String args[]) throws NoSuchMethodException, SecurityException, UnsupportedEncodingException{
//		JsonBean jsonBean = new JsonBean();
//		String serviceName = RpcService.class.getName();
//		Method method = RpcService.class.getMethod("hello", String.class);
//		jsonBean.setId("123435");
//		jsonBean.setName("RpcService");
//		jsonBean.setParameterTypes(new Class[]{RpcService.class,RpcService.class});
//		jsonBean.setParamArgs(new Object[]{"hs","js"});
//		GsonBuilder gsonBuilder = new GsonBuilder();
//		gsonBuilder.registerTypeAdapterFactory(new ClassTypeAdapterFactory());
//		gsonBuilder.registerTypeAdapter(Class.class, new ClassTypeAdapter());
//		Gson gson = gsonBuilder.create();
//		String jsonStr = gson.toJson(jsonBean);
//		System.out.println(jsonStr);
//		JsonBean bean = gson.fromJson(jsonStr, JsonBean.class);
//		System.out.println(bean.toString());
//		Class[] paramTypes = bean.getParameterTypes();
//		System.out.println(paramTypes[0].getName());
		System.out.println(URLDecoder.decode("https%3A%2F%2Fg.10086.cn%3A5443%2Fh5pay%2Fapi%2FygPay%3FchannelCode%3D42960000%26failRedirectURL%3Dwww.jiayuan.com%26monthStatus%3D1%26productDescribe%3D%25E5%2592%25AA%25E5%2592%2595%25E4%25BD%25B3%25E7%25BC%2598%25E6%25B8%25B8%25E6%2588%258F%26redirectURL%3Dwww.jiayuan.com%26serviceID%3D705663246700%26spCode%3D795165%26webId%3DY138766598","UTF-8"));
	}
}
