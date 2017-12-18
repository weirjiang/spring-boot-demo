package org.weir.rpc.nioRpc.client;

public class NioRpcClient implements RpcClient{
	private int port;
	private String ip;
	

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void open() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refer(Class service) {
		// TODO Auto-generated method stub
		
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}


}
