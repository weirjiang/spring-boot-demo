package org.weir.rpc.nioRpc.server;

public class NioRpcApi {
	 private String       key         ;
	 private String       name        ;
	 private String       rpcId       ;
	 private Class<?>     rpcInterface;
	 private Class<?>     rpcObject;
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

}
