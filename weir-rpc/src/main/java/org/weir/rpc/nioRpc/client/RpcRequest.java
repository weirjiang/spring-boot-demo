package org.weir.rpc.nioRpc.client;

public class RpcRequest {
	private String serviceinterface;
	private String method;
	private Class[] parameterTypes;
	private Object[] args;
	public String getServiceinterface() {
		return serviceinterface;
	}
	public void setServiceinterface(String serviceinterface) {
		this.serviceinterface = serviceinterface;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Class[] getParameterTypes() {
		return parameterTypes;
	}
	public void setParameterTypes(Class[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
}
