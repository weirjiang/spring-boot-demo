package org.weir.rpc.jsonRpc.client;

import java.util.Arrays;

public class JsonBean {
	public String id;
	public String name;
	private  Class[] parameterTypes;
	private Object[] paramArgs;
	public Object[] getParamArgs() {
		return paramArgs;
	}
	@Override
	public String toString() {
		return "JsonBean [id=" + id + ", name=" + name + ", parameterTypes=" + Arrays.toString(parameterTypes) + ", paramArgs="
				+ Arrays.toString(paramArgs) + "]";
	}
	public void setParamArgs(Object[] paramArgs) {
		this.paramArgs = paramArgs;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class[] getParameterTypes() {
		return parameterTypes;
	}
	public void setParameterTypes(Class[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
	
}
