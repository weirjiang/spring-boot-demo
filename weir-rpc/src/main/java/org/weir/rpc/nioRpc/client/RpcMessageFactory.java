package org.weir.rpc.nioRpc.client;

import java.util.concurrent.atomic.AtomicLong;

import org.weir.rpc.kryoRpc.client.ReqMessage;

public class RpcMessageFactory {
	private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
	
	public static ReqMessage newHbRequestRpcMessage(){
		ReqMessage reqMessage = new ReqMessage();
		reqMessage.setId(ID_GENERATOR.incrementAndGet());
		return reqMessage;
	}
}
