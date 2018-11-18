package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import common.IntCallbackCliente;
import common.IntServidorJuegoRMI;
import common.IntServidorPartidasRMI;
import server.ImplServidorPartidasRMI;
import tablero.Partida;


//Modif�cala para que instancie un objeto de la clase AuxiliarClienteFlota en el m�todo 'ejecuta'

	// Modifica todas las llamadas al objeto de la clase Partida
	// por llamadas al objeto de la clase AuxiliarClienteFlota.
	// Los m�todos a llamar tendr�n la misma signatura.

public class ClienteFlotaRMI {
	
	/**
	 * Implementa el juego 'Hundir la flota' mediante una interfaz grafica (GUI)
	 */

	/** Parametros por defecto de una partida */
	public static final int NUMFILAS=8, NUMCOLUMNAS=8, NUMBARCOS=6;

	private GuiTablero guiTablero = null;		// El juego se encarga de crear y modificar la interfaz grafica
	private IntServidorPartidasRMI partida = null;            	// Objeto con los datos de la partida en juego
	
	/** Atributos de la partida guardados en el juego para simplificar su implementacion */
	private int quedan = NUMBARCOS, disparos = 0;
	
	private String nombreJugador;
	IntServidorJuegoRMI juego;

	/**
	 * Programa principal. Crea y lanza un nuevo juego
	 * @param args
	 */
	public static void main(String[] args) {
		ClienteFlotaRMI juego = new ClienteFlotaRMI();
		juego.obtenObjeto();
		juego.ejecuta();
	} // end main

	/**
	 * Lanza una nueva hebra que crea la primera partida y dibuja la interfaz grafica: tablero
	 */
	private void ejecuta() {
		// Instancia la primera partida
		try {
			partida.nuevaPartida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				guiTablero = new GuiTablero(NUMFILAS, NUMCOLUMNAS);
				guiTablero.dibujaTablero();
			}
		});
	} // end ejecuta
	
	private void obtenObjeto() {
		try {
            int RMIPort;
            String hostName;
            InputStreamReader is = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(is);
            System.out.println("Enter the RMIRegistry host name:");
            hostName = br.readLine();
            System.out.println("Enter the RMIregistry port number:");
            String portNum = br.readLine();
            RMIPort = Integer.parseInt(portNum);

            // start a security manager - this is needed if stub
            // downloading is in use for this application.
            // The following sentence avoids the need to use
            // the option -DJava.security.policy=..." when launching the client
            //System.setProperty("java.security.policy", "HelloWorldRMI/src/client/java.policy");
            //System.setSecurityManager(new SecurityManager());

            String registryURL = "rmi://localhost:" + portNum + "/JuegoRMI";
            // find the remote object and cast it to an interface object
            juego = (IntServidorJuegoRMI) Naming.lookup(registryURL);

            System.out.println("Lookup completed ");
            // invoke the remote method
            partida = juego.nuevoServidorPartidas();
        } // end try
        catch (Exception e) {
            System.out.println(
                    "Exception in JuegoRMI: " + e);
        }
	}

	/******************************************************************************************/
	/*********************  CLASE INTERNA GuiTablero   ****************************************/
	/******************************************************************************************/
	private class GuiTablero {

		private int numFilas, numColumnas;

		private JFrame frame = null;        // Tablero de juego
		private JLabel estado = null;       // Texto en el panel de estado
		private JButton buttons[][] = null; // Botones asociados a las casillas de la partida

		/**
         * Constructor de una tablero dadas sus dimensiones
         */
		GuiTablero(int numFilas, int numColumnas) {
			this.numFilas = numFilas;
			this.numColumnas = numColumnas;
			frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		}

		/**
		 * Dibuja el tablero de juego y crea la partida inicial
		 */
		public void dibujaTablero() {
			anyadeMenu();
			anyadeGrid(numFilas, numColumnas);		
			anyadePanelEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);		
			frame.setSize(300, 300);
			frame.setVisible(true);
			while (nombreJugador == null || nombreJugador.length() == 0) {
				nombreJugador =	JOptionPane.showInputDialog(frame, "Introduce un nombre de usuario:", null);
				if (nombreJugador == null) {
	       			frame.dispose();   
	       			break;
	       		}
			}
			frame.setTitle(nombreJugador);
		} // end dibujaTablero

		/**
		 * Anyade el menu de opciones del juego y le asocia un escuchador
		 */
		private void anyadeMenu() {			
			MenuListener m = new MenuListener();
			
			JMenuBar menuBar = new JMenuBar();
			JMenu menu = new JMenu("Opciones");
			
			JMenuItem m1 = new JMenuItem("Nueva Partida");
			JMenuItem m2 = new JMenuItem("Mostrar Solucion");
			JMenuItem m3 = new JMenuItem("Salir");
			
			m1.addActionListener(m);
			m2.addActionListener(m);
			m3.addActionListener(m);
			
			menu.add(m1);
			menu.add(m2);
			menu.add(m3);
			
			menuBar.add(menu);
			
			menu = new JMenu("Multijugador");
			m1 = new JMenuItem("Proponer Partida");
			m2 = new JMenuItem("Borrar Partida");
			m3 = new JMenuItem("Listar Partidas");
			JMenuItem m4 = new JMenuItem("Aceptar Partida");
			
			m1.addActionListener(m);
			m2.addActionListener(m);
			m3.addActionListener(m);
			m4.addActionListener(m);
			
			menu.add(m1);
			menu.add(m2);
			menu.add(m3);
			menu.add(m4);
			
			menuBar.add(menu);
			
			frame.setJMenuBar(menuBar);
			
		} // end anyadeMenu

		/**
		 * Anyade el panel con las casillas del mar y sus etiquetas.
		 * Cada casilla sera un boton con su correspondiente escuchador
		 * @param nf	numero de filas
		 * @param nc	numero de columnas
		 */
		private void anyadeGrid(int nf, int nc) {
			this.buttons = new JButton [nf+1] [nc+2];
			ButtonListener b = new ButtonListener();
			
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(nf+1, nc+2));
			
			panel.add(new JLabel(" ", SwingConstants.CENTER));
			for (int c = 1; c < nc+1; c++) {
				panel.add(new JLabel(String.valueOf(c), SwingConstants.CENTER));
			}
			panel.add(new JLabel(" ", SwingConstants.CENTER));
			
			char letter = 'A';
			for (int f = 0; f < nf; f++) {
				panel.add(new JLabel(String.valueOf(letter), SwingConstants.CENTER));
				for ( int c = 0; c < nc; c++) {
					buttons[f][c] = new JButton();
					buttons[f][c].putClientProperty("ROW", f);
					buttons[f][c].putClientProperty("COLUMN", c);
					buttons[f][c].addActionListener(b);
					buttons[f][c].putClientProperty("f", f);
					buttons[f][c].putClientProperty("c", c);
					panel.add(buttons[f][c]);
				}
				panel.add(new JLabel(String.valueOf(letter), SwingConstants.CENTER));
				letter++;
			}
			
			frame.add(panel);	
		} // end anyadeGrid


		/**
		 * Anyade el panel de estado al tablero
		 * @param cadena	cadena inicial del panel de estado
		 */
		private void anyadePanelEstado(String cadena) {	
			JPanel panelEstado = new JPanel();
			estado = new JLabel(cadena);
			panelEstado.add(estado);
			// El panel de estado queda en la posicion SOUTH del frame
			frame.getContentPane().add(panelEstado, BorderLayout.SOUTH);
		} // end anyadePanel Estado

		/**
		 * Cambia la cadena mostrada en el panel de estado
		 * @param cadenaEstado	nuevo estado
		 */
		public void cambiaEstado(String cadenaEstado) {
			estado.setText(cadenaEstado);
		} // end cambiaEstado

		/**
		 * Muestra la solucion de la partida y marca la partida como finalizada
		 */
		public void muestraSolucion() {
			// AGUA
			for (int i = 0; i < buttons.length - 1; i++) {
				for (int j = 0; j < buttons[0].length - 2; j++) {
					guiTablero.pintaBoton(guiTablero.buttons[i][j], Color.BLUE);
					buttons[i][j].setEnabled(false);
				}
			}
			// SOLUCIÓN
			String[] solucion = null;
			try {
				solucion = partida.getSolucion();
			} catch (RemoteException e) {
				e.printStackTrace();
			}	
			for (int i = 0; i < solucion.length; i++) {				 
				pintaBarcoHundido(solucion[i]);			
			}
			
			guiTablero.cambiaEstado("FIN ... Intentos: " + disparos + "    Barcos restantes: " + quedan);
			
		} // end muestraSolucion


		/**
		 * Pinta un barco como hundido en el tablero
		 * @param cadenaBarco	cadena con los datos del barco codifificados como
		 *                      "filaInicial#columnaInicial#orientacion#tamanyo"
		 */
		public void pintaBarcoHundido(String cadenaBarco) {
			int filaInicial, columnaInicial, tamanyo;
			char orientacion;
			
			// Decodificación cadenaBarco
			String [] cadena = cadenaBarco.split("#");
			filaInicial = Integer.parseInt(cadena[0]);
			columnaInicial = Integer.parseInt(cadena[1]);
			orientacion = cadena[2].toUpperCase().charAt(0);
			tamanyo = Integer.parseInt(cadena[3]);
			
			// pintar
			for (int i = 0; i < tamanyo; i++) {
				if (orientacion == 'H') {
					guiTablero.pintaBoton(guiTablero.buttons[filaInicial][columnaInicial+i], Color.RED);
					guiTablero.buttons[filaInicial][columnaInicial+i].setEnabled(false);
				} else  {
					guiTablero.pintaBoton(guiTablero.buttons[filaInicial+i][columnaInicial], Color.RED);
					guiTablero.buttons[filaInicial+i][columnaInicial].setEnabled(false);
				}
			}
			
		} // end pintaBarcoHundido

		/**
		 * Pinta un boton de un color dado
		 * @param b			boton a pintar
		 * @param color		color a usar
		 */
		public void pintaBoton(JButton b, Color color) {
			b.setBackground(color);
			// El siguiente codigo solo es necesario en Mac OS X
			b.setOpaque(true);
			b.setBorderPainted(false);
		} // end pintaBoton

		/**
		 * Limpia las casillas del tablero pintándolas del gris por defecto
		 */
		public void limpiaTablero() {
			for (int i = 0; i < numFilas; i++) {
				for (int j = 0; j < numColumnas; j++) {
					buttons[i][j].setBackground(null);
					buttons[i][j].setOpaque(true);
					buttons[i][j].setBorderPainted(true);
					buttons[i][j].setEnabled(true);
				}
			}
			disparos = 0;
			quedan = NUMBARCOS;
			guiTablero.cambiaEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);
		} // end limpiaTablero
		
		public void desactivaTablero() {
			for (int i = 0; i < numFilas; i++) {
				for (int j = 0; j < numColumnas; j++) {
					buttons[i][j].setEnabled(false);
				}
			}
		}

		/**
		 * 	Destruye y libera la memoria de todos los componentes del frame
		 */
		public void liberaRecursos() {
			frame.dispose();
		} // end liberaRecursos
		
	} // end class GuiTablero

	/******************************************************************************************/
	/*********************  CLASE INTERNA MenuListener ****************************************/
	/******************************************************************************************/

	/**
	 * Clase interna que escucha el menu de Opciones del tablero
	 * 
	 */
	private class MenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String r = e.getActionCommand();
			
			switch(r) {
			
			case "Nueva Partida":
				guiTablero.limpiaTablero();
				try {
					partida.nuevaPartida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				break;
			case "Mostrar Solucion":
				guiTablero.muestraSolucion();
				break;
			case "Salir":
				guiTablero.liberaRecursos();
				try {
					juego.borraPartida(nombreJugador);
				} catch (RemoteException e2) {
					e2.printStackTrace();
				}
				System.exit(0);
				break;
			case "Proponer Partida":
				try {
					IntCallbackCliente cbk = new ImpCallbackCliente();
					if (juego.proponPartida(nombreJugador, cbk))
						guiTablero.cambiaEstado("Nueva partida propuesta.");
					else
						guiTablero.cambiaEstado("Ya hay una partida propuesta.");
					
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				break;
			case "Borrar Partida":
				try {
					if (juego.borraPartida(nombreJugador))
						guiTablero.cambiaEstado("Se ha borrado la partida.");
					else
						guiTablero.cambiaEstado("No hay partida propuesta.");
					break;
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			case "Listar Partidas":
				String[] listaPartidas;
				try {
					String texto = "";
					listaPartidas = juego.listaPartidas();					
					for (int i = 0; i < listaPartidas.length; i++) {
						System.out.println(i + ": " + listaPartidas[i]);
						texto += i + ": " + listaPartidas[i] + "\n";
					}
					if(!texto.isEmpty())
						JOptionPane.showMessageDialog(guiTablero.frame, texto);
					else {
						JOptionPane.showMessageDialog(guiTablero.frame, "No hay ninguna partida.");
						guiTablero.cambiaEstado("No hay ninguna partida.");
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}	
				break;
			case "Aceptar Partida":
				String nombreRival = JOptionPane.showInputDialog("Introduce el nombre de tu rival:"); 		       	
		       	if (nombreRival != null) {
		       		try {
		       			if(juego.aceptaPartida(nombreJugador, nombreRival))
		       				guiTablero.cambiaEstado("Se ha empezado la partida.");
		       			else
		       				guiTablero.cambiaEstado("No se ha podido empezar la partida.");
		       		} catch (RemoteException e1) {
		       			e1.printStackTrace();
		       		}
		       	}
			}
		} // end actionPerformed

	} // end class MenuListener



	/******************************************************************************************/
	/*********************  CLASE INTERNA ButtonListener **************************************/
	/******************************************************************************************/
	/**
	 * Clase interna que escucha cada uno de los botones del tablero
	 * Para poder identificar el boton que ha generado el evento se pueden usar las propiedades
	 * de los componentes, apoyandose en los metodos putClientProperty y getClientProperty
	 */
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton) e.getSource();
			int f = (int) button.getClientProperty("f");
			int c = (int) button.getClientProperty("c");
			pruebaCasilla(f,c);
        } // end actionPerformed

	} // end class ButtonListener



	private void pruebaCasilla(int f, int c) {
		disparos++;
		int resultado = 0;
		try {
			resultado = partida.pruebaCasilla(f, c);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resultado == Partida.AGUA) {
			guiTablero.pintaBoton(guiTablero.buttons[f][c], Color.BLUE);
			guiTablero.buttons[f][c].setEnabled(false);
		} else if (resultado == Partida.TOCADO) {
			guiTablero.pintaBoton(guiTablero.buttons[f][c], Color.ORANGE);
			guiTablero.buttons[f][c].setEnabled(false);
		} else if (resultado >= 0) {
			try {
				guiTablero.pintaBarcoHundido(partida.getBarco(resultado));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			quedan--;
		}
		
		if (quedan == 0) {
			guiTablero.cambiaEstado("FIN ... Intentos: " + disparos + "    Barcos restantes: " + quedan);
			guiTablero.desactivaTablero();
		} else {
			guiTablero.cambiaEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);
		}
	}

} // end class Juego
