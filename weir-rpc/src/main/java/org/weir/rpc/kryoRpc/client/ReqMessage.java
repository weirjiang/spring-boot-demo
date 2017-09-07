package org.weir.rpc.kryoRpc.client;

import java.util.Arrays;

public class ReqMessage {
	private long id;
	private String serviceinterface;
	private String method;
	private Class[] parameterTypes;
	private Object[] args;
	public String getServiceinterface() {
		return serviceinterface;
	}
	@Override
	public String toString() {
		return "ReqMessage [serviceinterface=" + serviceinterface + ", method=" + method + ", parameterTypes=" + Arrays.toString(parameterTypes)
				+ ", args=" + Arrays.toString(args) + "]";
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
