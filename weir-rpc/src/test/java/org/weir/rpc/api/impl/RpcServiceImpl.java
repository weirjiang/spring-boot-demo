package org.weir.rpc.api.impl;

import org.weir.rpc.api.RpcService;

public class RpcServiceImpl implements RpcService{

	@Override
	public String hello(String hi) {
		// TODO Auto-generated method stub
		System.out.println("[weir-Rpc]->hello"+hi);
		return "[weir-Rpc]->hello"+hi;
	}

}
