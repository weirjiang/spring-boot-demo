package org.weir.rpc.nioRpc.protocol.model;

import java.io.Serializable;

/**
 * A <code>RpcMethod</code> provides information about a RPC interface exposed
 * method.
 * 
 * @author mindwind
 * @version 1.0, Aug 8, 2014
 */
public class RpcMethod implements Serializable {

	private static final long serialVersionUID = -4302065109637231162L;

	private String name;

	private Class<?>[] parameterTypes;
	private Object[] parameters;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public Object[] getParameters() {
		return parameters;
	}

	// ~
	// -------------------------------------------------------------------------------------------------------------

	public RpcMethod() {
	}

	public RpcMethod(String name, Class<?>[] parameterTypes) {
		this.name = name;
		this.parameterTypes = parameterTypes;
	}

	public RpcMethod(String name, Class<?>[] parameterTypes, Object[] parameters) {
		this(name, parameterTypes);
		this.parameters = parameters;
	}

	// ~
	// -------------------------------------------------------------------------------------------------------------

	public void setParameterTypes(Class<?>... parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public void setParameters(Object... parameters) {
		this.parameters = parameters;
	}

}
