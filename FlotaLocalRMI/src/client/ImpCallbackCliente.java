package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.IntCallbackCliente;

public class ImpCallbackCliente extends UnicastRemoteObject implements IntCallbackCliente {
	
	public ImpCallbackCliente() throws RemoteException {
		super();
	}

	// Implementacion del metodo de la interfaz IntCallbackCliente
	@Override
	public void notificame(String mensaje) throws RemoteException {
		 System.out.println("Mensaje recibido: " + mensaje);		
	}
	
	

}
