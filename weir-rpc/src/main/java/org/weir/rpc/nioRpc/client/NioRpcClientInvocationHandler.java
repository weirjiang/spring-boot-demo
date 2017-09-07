package org.weir.rpc.nioRpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.weir.rpc.kryoRpc.client.ReqMessage;

public class NioRpcClientInvocationHandler implements InvocationHandler{
	private NioRpcConnector nioRpcConnector;

	Class<?> rpcInterface = null;
	public NioRpcClientInvocationHandler(Class<?> rpcInterface,int port, NioRpcConnector nioRpcConnector){
		this.rpcInterface = rpcInterface;
		this.nioRpcConnector = nioRpcConnector;
	}

	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		ReqMessage reqMessage = RpcMessageFactory.newHbRequestRpcMessage();
		reqMessage.setServiceinterface(rpcInterface.getName());
		reqMessage.setMethod(method.getName());
		reqMessage.setParameterTypes(method.getParameterTypes());
		reqMessage.setArgs(args);
		nioRpcConnector.sendReq(reqMessage);
		return null;
	}

}
