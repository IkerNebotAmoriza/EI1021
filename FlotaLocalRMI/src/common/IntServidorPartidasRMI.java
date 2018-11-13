package common;

public interface IntServidorPartidasRMI {
	
	/**
	 * 	dado el numero de filas y columnas del tablero y el numero de barcos,
	 *	instancia un nuevo objeto de la clase Partida.
	 */ 
	void nuevaPartida(int nf, int nc, int nb);
	
	/**
	 * 	dada la fila y columna de una casilla, llama a la funcion del mismo
	 *	nombre del objeto Partida y devuelve el resultado obtenido
	 */
	int pruebaCasilla( int nf, int nc);
	
	/**
	 * 	dado el identificador de un barco, llama a la funcion del mismo nombre del
	 *	objeto Partida y devuelve el resultado obtenido.
	 */
	String getBarco(int idBarco);
	
	/**
	 *	llama a la funcion del mismo nombre del objeto Partida y devuelve el
	 *	resultado obtenido
	 */
	String [] getSolucion();
	
}
