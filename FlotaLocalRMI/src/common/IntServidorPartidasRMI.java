package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IntServidorPartidasRMI extends Remote {
	
	/**
	 * 	dado el numero de filas y columnas del tablero y el numero de barcos,
	 *	instancia un nuevo objeto de la clase Partida.
	 */ 
	void nuevaPartida(int nf, int nc, int nb) throws RemoteException;
	
	/**
	 * 	dada la fila y columna de una casilla, llama a la funcion del mismo
	 *	nombre del objeto Partida y devuelve el resultado obtenido
	 */
	int pruebaCasilla( int nf, int nc) throws RemoteException;
	
	/**
	 * 	dado el identificador de un barco, llama a la funcion del mismo nombre del
	 *	objeto Partida y devuelve el resultado obtenido.
	 */
	String getBarco(int idBarco) throws RemoteException;
	
	/**
	 *	llama a la funcion del mismo nombre del objeto Partida y devuelve el
	 *	resultado obtenido
	 */
	String [] getSolucion() throws RemoteException;
	
}
