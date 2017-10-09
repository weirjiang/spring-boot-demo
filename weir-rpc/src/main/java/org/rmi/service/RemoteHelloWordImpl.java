package org.rmi.service;

import java.rmi.RemoteException;

public class RemoteHelloWordImpl implements RemoteHelloWord{

	@Override
	public String sayHello() throws RemoteException {
		// TODO Auto-generated method stub
		return "Hello World";
	}

}
