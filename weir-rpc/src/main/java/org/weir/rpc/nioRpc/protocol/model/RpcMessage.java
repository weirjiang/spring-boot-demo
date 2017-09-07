package org.weir.rpc.nioRpc.protocol.model;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Map;

/**
 * RPC messages use generic message format for transferring data.
 * <pre>
 *      rpc-message = rpc-header + [ rpc-body ]
 * </pre>
 * 
 * @author mindwind
 * @version 1.0, Jul 17, 2014
 */
public class RpcMessage implements Serializable {

	
	private static final long serialVersionUID = 5138100956693144357L;
	
	
	  private RpcHeader  header;
	  private RpcBody    body  ;
	
	
	// ~ ---------------------------------------------------------------------------------------------------------

	
	public boolean isOneway() {
		return header.isOw();
	}
	
	public void setOneway(boolean oneway) {
		if (oneway) { header.setOw(); }
	}
	
	public boolean isResponse() {
		return header.isRp();
	}
	
	public void setResponse(boolean response) {
		if (response) { header.setRp(); }
	}
	
	public boolean isHeartbeat() {
		return header.isHb();
	}
	
	public void setHeartbeat(boolean heartbeat) {
		if (heartbeat) { header.setHb(); }
	}
	
	public long getId() {
		return header.getId();
	}
	
	public void setId(long id) {
		header.setId(id);
	}
	
	public RpcHeader getHeader() {
		return header;
	}

	public void setHeader(RpcHeader header) {
		this.header = header;
	}

	public RpcBody getBody() {
		return body;
	}

	public void setBody(RpcBody body) {
		this.body = body;
	}

	public Exception getException() {
		return body.getException();
	}
	
	public void setException(Exception e) {
		body.setException(e);
	}
	
	public int getRpcTimeoutInMillis() {
		return body.getRpcOption().getRpcTimeoutInMillis();
	}
	
	public void setRpcTimeoutInMillis(int rpcTimeoutInMillis) {
		body.getRpcOption().setRpcTimeoutInMillis(rpcTimeoutInMillis);
	}
	
	public void setServerAddress(InetSocketAddress serverAddress) {
		body.getRpcOption().setServerAddress(serverAddress);
	}
	
	public InetSocketAddress getServerAddress() {
		return body.getRpcOption().getServerAddress();
	}
	
	public void setClientAddress(InetSocketAddress clientAddress) {
		body.getRpcOption().setClientAddress(clientAddress);
	}
	
	public InetSocketAddress getClientAddress() {
		return body.getRpcOption().getClientAddress();
	}
	
	public Object getReturnObject() {
		return body.getReturnObject();
	}
	
	public void setReturnObject(Object returnObject) {
		body.setReturnObject(returnObject);
	}
	
	public void setAttachments(Map<String, String> attachments) {
		body.setAttachments(attachments);
	}
	
	public Map<String, String> getAttachments() {
		return body.getAttachments();
	}
	
	public void setRpcId(String rpcId) {
		body.setRpcId(rpcId);
	}
	
	public String getRpcId() {
		return body.getRpcId();
	}
	
}
