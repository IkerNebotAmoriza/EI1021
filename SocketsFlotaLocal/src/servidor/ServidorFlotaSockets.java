package servidor;
import java.net.InetAddress;
import java.net.ServerSocket;

import comun.MyStreamSocket;

/**
 * Este modulo contiene la logica de aplicacion del servidor del juego Hundir la flota
 * Utiliza sockets en modo stream para llevar a cabo la comunicacion entre procesos.
 * Puede servir a varios clientes de modo concurrente lanzando una hebra para atender a cada uno de ellos.
 * Se le puede indicar el puerto del servidor en linea de ordenes.
 */


public class ServidorFlotaSockets {
   
   public static void main(String[] args) {
	   int puertoServidor = 8888;	//Puerto del servidor por defecto
	   
	   if ( args.length == 1 ) {	//Si la llamada al servidor tiene argumento
		   try {
			   puertoServidor = Integer.parseInt(args[0]);	//Lo usamos como puerto
		   }catch(IllegalArgumentException ex){				//Si no es un un argumento valido lanzamos una excepcion
			   ex.printStackTrace();
		   }
	   }
	   
	   try {
		
		   ServerSocket socketServidor = new ServerSocket(puertoServidor);	//Creamos socket de conexion
		   System.out.println("Servidor listo.");
		   
		   while(true) {	//Bucle infinito
			   
			   System.out.println("A la espera de una conexion.");
			   MyStreamSocket socketDatos = new MyStreamSocket(socketServidor.accept());	//Creamos socket de datos
			   System.out.println("Conexion aceptada.");
			   HiloServidorFlota h = new HiloServidorFlota(socketDatos);	//Creamos un objeto runnable
			   
			   Thread hilo = new Thread(h);									//Creamos un hilo con el
			   hilo.start();												//Y lo iniciamos
			   
		   }
		   
	   }catch(Exception ex) {												//Cazamos cualquier excepcion que pueda saltar en la ejecucion del bucle
		   ex.printStackTrace();
	   }
	   
	  // Acepta conexiones vía socket de distintos clientes.
	  // Por cada conexión establecida lanza una hebra de la clase HiloServidorFlota.
	   

	  // Revisad el apartado 5.5 del libro de Liu
 
   } //fin main
} // fin class
