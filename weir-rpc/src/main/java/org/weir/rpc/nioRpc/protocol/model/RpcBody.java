package org.weir.rpc.nioRpc.protocol.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents a RPC body field.
 * <p>
 * A body that can be sent or received with a rpc message, 
 * but not all messages contain a body, it is optional.
 * The body contains a block of arbitrary data and can be serialized by specific serializer.
 * 
 * @author mindwind
 * @version 1.0, Jul 18, 2014
 */
public class RpcBody implements Serializable {

	
	private static final long serialVersionUID = 5138100956693144357L;
	
	
	  private String              rpcId       ;
	  private Class<?>            rpcInterface;
	  private RpcMethod           rpcMethod   ;
	  private RpcOption           rpcOption   ;
	  private Map<String, String> attachments ;
	  private Object              returnObject;
	  private Exception           exception   ;
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
	public RpcOption getRpcOption() {
		return rpcOption;
	}
	public void setRpcOption(RpcOption rpcOption) {
		this.rpcOption = rpcOption;
	}
	public Map<String, String> getAttachments() {
		return attachments;
	}
	public void setAttachments(Map<String, String> attachments) {
		this.attachments = attachments;
	}
	public Object getReturnObject() {
		return returnObject;
	}
	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}
	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
