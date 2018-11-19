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

	// PROPONEMOS UNA PARTIDA PARA QUE EL RESTO DE CLIENTES PUEDAN ACEPTARLA
	@Override
	public boolean proponPartida(String nombreJugador, IntCallbackCliente callbackClientObject) throws RemoteException {
		if ( !map.containsKey(nombreJugador) ) { // SI EL CLIENTE NO HA PROPUESTO NINGUNA PARTIDA
			map.put(nombreJugador, callbackClientObject); // LA PARTIDA SE ANYADE AL MAPA DE PARTIDAS
			System.out.println("Nueva partida propuesta por " + nombreJugador + ".");
			return true;
		} 
		else {
			System.out.println("ERROR: Ya hay una partida propuesta por " + nombreJugador + "."); // SI YA EXISTE UNA PARTIDA DEL CLIENTE SE MUESTRA EL MENSAJE DE ERROR
			return false;
		}
	}

	// BORRAMOS LA PARTIDA QUE HEMOS PROPUESTO
	@Override
	public boolean borraPartida(String nombreJugador) throws RemoteException {
		if (map.containsKey(nombreJugador)) { //SI EL MAPA DE PARTIDAS CONTIENE UNA PARTIDA DEL CLIENTE
			map.remove(nombreJugador); // LA BORRA
			System.out.println("Se ha borrado la partida propuesta por " + nombreJugador);
			return true;
		} else {
			System.out.println("ERROR: No hay ninguna partida propuesta por " + nombreJugador + "."); // SI NO LA CONTIENE MUESTRA EL MENSAJE DE ERROR
			return false;
		}
	}

	// LISTAMOS TODAS LAS PARTIDAS QUE ESTAN DISPONIBLES
	@Override
	public String[] listaPartidas() throws RemoteException {
		Set<String> nombres = map.keySet();
		return nombres.toArray(new String[0]);
	}

	// ACEPTAMOS LA PARTIDA QUE HA PROPUESTO OTRO CLIENTE
	@Override
	public boolean aceptaPartida(String nombreJugador, String nombreRival) throws RemoteException {
		if (nombreJugador.equals(nombreRival)) {
			System.out.println("ERROR: un jugador no puede aceptar su propia partida.");
			return false;
		}
		if (map.containsKey(nombreRival)) { // SI EL CLIENTE ESPECIFICADO HA PROPUESTO UNA PARTIDA
			IntCallbackCliente callbackClientObject = (IntCallbackCliente) map.get(nombreRival); // TODO: probar sin cast
			map.remove(nombreRival);
			try {
				callbackClientObject.notificame("Tu partida ha sido aceptada por " + nombreJugador + ".");
				System.out.println("Partida propuesta por " + nombreRival + " ha sido aceptada por " + nombreJugador);
				return true;
			}
			catch (RemoteException e) {	// EN CASO DE QUE LAS COMUNICACIONES FALLEN
				System.out.println("ERROR al notificar a " + nombreRival + ".");
				return false;
			}

		} else {
			System.out.println("ERROR: No hay ninguna partida propuesta por " + nombreRival + "."); // SI EL CLIENTE ESPECIFICADO NO HA PROPUESTO LA PARTIDA
			return false;
		}
	}

}
