package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
// Metodo que notifica al cliente el mensaje recibido por parte del Servidor
public interface IntCallbackCliente extends Remote {
	public void notificame(String mensaje) throws RemoteException;
}
