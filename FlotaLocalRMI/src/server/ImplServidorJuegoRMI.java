package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import common.IntCallbackCliente;
import common.IntServidorJuegoRMI;
import common.IntServidorPartidasRMI;

public class ImplServidorJuegoRMI extends UnicastRemoteObject implements IntServidorJuegoRMI {
	private Map<String, IntCallbackCliente> map = null;

	protected ImplServidorJuegoRMI() throws RemoteException {
		super();
		this.map = new ConcurrentHashMap<String, IntCallbackCliente>();
		System.out.println("Se ha creado una nueva instancia de ImplServidorJuegoRMI");
	}

	@Override
	public IntServidorPartidasRMI nuevoServidorPartidas() throws RemoteException {
		return new ImplServidorPartidasRMI();
	}

	@Override
	public boolean proponPartida(String nombreJugador, IntCallbackCliente callbackClientObject) throws RemoteException {
		if ( !map.containsKey(nombreJugador) ) {
			map.put(nombreJugador, callbackClientObject);
			System.out.println("Nueva partida propuesta por " + nombreJugador + ".");
			return true;
		} 
		else {
			System.out.println("ERROR: Ya hay una partida propuesta por " + nombreJugador + ".");
			return false;
		}
	}

	@Override
	public boolean borraPartida(String nombreJugador) throws RemoteException {
		if (map.containsKey(nombreJugador)) {
			map.remove(nombreJugador);
			System.out.println("Se ha borrado la partida propuesta por " + nombreJugador);
			return true;
		} else {
			System.out.println("ERROR: No hay ninguna partida propuesta por " + nombreJugador + ".");
			return false;
		}
	}

	@Override
	public String[] listaPartidas() throws RemoteException {
		Set<String> nombres = map.keySet();
		return nombres.toArray(new String[0]);
	}

	@Override
	public boolean aceptaPartida(String nombreJugador, String nombreRival) throws RemoteException {
		if (nombreJugador.equals(nombreRival)) {
			System.out.println("ERROR: un jugador no puede aceptar su propia partida.");
			return false;
		}
		if (map.containsKey(nombreRival)) {
			IntCallbackCliente callbackClientObject = (IntCallbackCliente) map.get(nombreRival); // TODO: probar sin cast
			map.remove(nombreRival);
			try {
				callbackClientObject.notificame("Tu partida ha sido aceptada por " + nombreJugador + ".");
				System.out.println("Partida propuesta por " + nombreRival + " ha sido aceptada por " + nombreJugador);
				return true;
			}
			catch (RemoteException e) {
				System.out.println("ERROR al notificar a " + nombreRival + ".");
				return false;
			}

		} else {
			System.out.println("ERROR: No hay ninguna partida propuesta por " + nombreRival + ".");
			return false;
		}
	}

}
