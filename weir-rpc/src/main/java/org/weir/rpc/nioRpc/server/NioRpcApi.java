package org.weir.rpc.nioRpc.server;

import org.weir.rpc.nioRpc.protocol.model.RpcMethod;

public class NioRpcApi {
	private String key;
	private String name;
	private String rpcId;
	private Class<?> rpcInterface;
	private Class<?> rpcObject;
	private RpcMethod rpcMethod;

	public NioRpcApi(String rpcId, Class<?> rpcInterface, RpcMethod rpcMethod) {
		this.key = rpcInterface.getName();
		this.rpcId = rpcId;
		this.rpcInterface = rpcInterface;
		this.name = rpcInterface.getName();
		this.setRpcMethod(rpcMethod);
	}
	public NioRpcApi(){}
	public Class<?> getRpcObject() {
		return rpcObject;
	}

	public void setRpcObject(Class<?> rpcObject) {
		this.rpcObject = rpcObject;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRpcId() {
		return rpcId;
	}

	public void setRpcId(String rpcId) {
		this.rpcId = rpcId;
	}

	public Class<?> getRpcInterface() {
		return rpcInterface;
	}

	public void setRpcInterface(Class<?> rpcInterface) {
		this.rpcInterface = rpcInterface;
	}

	public RpcMethod getRpcMethod() {
		return rpcMethod;
	}

	public void setRpcMethod(RpcMethod rpcMethod) {
		this.rpcMethod = rpcMethod;
	}

}
