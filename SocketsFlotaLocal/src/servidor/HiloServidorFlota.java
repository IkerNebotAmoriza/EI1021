package servidor;


import java.io.IOException;
import java.net.SocketException;

import partida.*;
import comun.MyStreamSocket;

/**
 * Clase ejecutada por cada hebra encargada de servir a un cliente del juego Hundir la flota.
 * El metodo run contiene la logica para gestionar una sesion con un cliente.
 */

 // Revisar el apartado 5.5. del libro de Liu

class HiloServidorFlota implements Runnable {
   MyStreamSocket myDataSocket;
   private Partida partida = null;
   int nf, nc, nb;

	/**
	 * Construye el objeto a ejecutar por la hebra para servir a un cliente
	 * @param	myDataSocket	socket stream para comunicarse con el cliente
	 */
   HiloServidorFlota(MyStreamSocket myDataSocket, int nf, int nc, int nb) {
	   this.myDataSocket = myDataSocket;
	   this.nf = nf;
	   this.nc = nc;
	   this.nb = nb;
	   this.partida = new Partida(nf,nc,nb);
	   
   }
 
   /**
	* Gestiona una sesion con un cliente	
   */
   public void run( ) {
      boolean done = false;
      int operacion = 0;
      // ...
      try {
         while (!done) {
        	 // Recibe una peticion del cliente
        	 // Extrae la operación y los argumentos
        	 String [] mensaje = myDataSocket.receiveMessage().split("#");
        	 int [] argumentos = new int[mensaje.length];
        	 
        	 for(int i = 0; i < mensaje.length; i++) {
        		 argumentos[i] = Integer.parseInt(mensaje[i]);
        	 }
        	 
        	 operacion = argumentos[0];
        	 
             switch (operacion) {
             case 0:  // fin de conexión con el cliente
            	 myDataSocket.close();
            	 break;

             case 1: { // Crea nueva partida
            	 partida = new Partida(argumentos[1], argumentos [2], argumentos[3]);
            	 break;
             }             
             case 2: { // Prueba una casilla y devuelve el resultado al cliente
            	 String res = String.valueOf(partida.pruebaCasilla(argumentos[1], argumentos[2]));
            	 myDataSocket.sendMessage(res);
                 break;
             }
             case 3: { // Obtiene los datos de un barco y se los devuelve al cliente
            	 String res = String.valueOf(partida.getBarco(argumentos[1]));
            	 myDataSocket.sendMessage(res);
                 break;
             }
             case 4: { // Devuelve al cliente la solucion en forma de vector de cadenas
        	   // Primero envia el numero de barcos 
               // Despues envia una cadena por cada barco
            	 String [] datosBarcos = partida.getSolucion();
            	 myDataSocket.sendMessage(String.valueOf(datosBarcos.length));
            	 
            	 for(int i = 0; i < datosBarcos.length; i++) {
            		 myDataSocket.sendMessage(datosBarcos[i]);
            	 }
            	 
               break;
             }
         } // fin switch
       } // fin while   
     } // fin try
     catch (Exception ex) {
        System.out.println("Exception caught in thread: " + ex);
     } // fin catch
   } //fin run
   
} //fin class 
