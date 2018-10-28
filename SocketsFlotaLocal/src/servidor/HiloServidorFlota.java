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

	/**
	 * Construye el objeto a ejecutar por la hebra para servir a un cliente
	 * @param	myDataSocket	socket stream para comunicarse con el cliente
	 */
   HiloServidorFlota(MyStreamSocket myDataSocket) {
	   this.myDataSocket = myDataSocket;
	   
   }
 
   /**
	* Gestiona una sesion con un cliente	
   */
   public void run( ) {
      boolean done = false;
      int operacion = 0;
      String [] mensaje;
      int [] argumentos;
      
      try {
         while (!done) {
        	 // Recibe una peticion del cliente
        	 // Extrae la operación y los argumentos
        	 mensaje = myDataSocket.receiveMessage().split("#");
        	 argumentos = new int[mensaje.length];
        	 
        	 for(int i = 0; i < mensaje.length; i++) {
        		 argumentos[i] = Integer.parseInt(mensaje[i]);
        	 }
        	 
        	 operacion = argumentos[0];
        	 
             switch (operacion) {
             case 0:  // fin de conexión con el cliente
            	 done = true;
            	 myDataSocket.close();
            	 break;

             case 1: { // Crea nueva partida
            	 partida = new Partida(argumentos[1], argumentos [2], argumentos[3]);
            	 break;
            	 
             }             
             case 2: { // Prueba una casilla y devuelve el resultado al cliente
            	 myDataSocket.sendMessage(String.valueOf(partida.pruebaCasilla(argumentos[1], argumentos[2])));
                 break;
                 
             }
             case 3: { // Obtiene los datos de un barco y se los devuelve al cliente
            	 myDataSocket.sendMessage(String.valueOf(partida.getBarco(argumentos[1])));
                 break;
                 
             }
             case 4: { // Devuelve al cliente la solucion en forma de vector de cadenas
        	   // Primero envia el numero de barcos 
               // Despues envia una cadena por cada barco
            	 String [] datosBarcos = partida.getSolucion();
            	 myDataSocket.sendMessage(String.valueOf(datosBarcos.length));
            	 
            	 for(String s: datosBarcos) {
            		 myDataSocket.sendMessage(s);
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
