package org.rmi.server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.rmi.service.RemoteHelloWord;
import org.rmi.service.RemoteHelloWordImpl;

public class RMIServer {
	public static void main(String[] args) {
	    try {
	        RemoteHelloWord hello=new RemoteHelloWordImpl();
	        RemoteHelloWord stub=(RemoteHelloWord)UnicastRemoteObject.exportObject(hello, 9999);
	        LocateRegistry.createRegistry(1099);
	        Registry registry=LocateRegistry.getRegistry();
	        registry.bind("helloword", stub);
	        System.out.println("绑定成功!");
	    } catch (RemoteException e) {
	        e.printStackTrace();
	    } catch (AlreadyBoundException e) {
	        e.printStackTrace();
	    }
	}
}
