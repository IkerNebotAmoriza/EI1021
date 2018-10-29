package cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import cliente.ClienteFlotaSockets;
import partida.Partida;
import cliente.ClienteFlotaSockets.ButtonListener;
import cliente.ClienteFlotaSockets.GuiTablero;
import cliente.ClienteFlotaSockets.MenuListener;

//Modifícala para que instancie un objeto de la clase AuxiliarClienteFlota en el método 'ejecuta'

	// Modifica todas las llamadas al objeto de la clase Partida
	// por llamadas al objeto de la clase AuxiliarClienteFlota.
	// Los métodos a llamar tendrán la misma signatura.

public class ClienteFlotaSockets {
	/**
	 * Implementa el juego 'Hundir la flota' mediante una interfaz grafica (GUI)
	 */

	/** Parametros por defecto de una partida */
	public static final int NUMFILAS=8, NUMCOLUMNAS=8, NUMBARCOS=6;

	private GuiTablero guiTablero = null;						// El juego se encarga de crear y modificar la interfaz grafica
	private AuxiliarClienteFlota partida = null;            	// Objeto auxiliar que llamará a los metodos del objeto partida del servidor
	
	/** Atributos de la partida guardados en el juego para simplificar su implementacion */
	private int quedan = NUMBARCOS, disparos = 0;

	/**
	 * Programa principal. Crea y lanza un nuevo juego
	 * @param args
	 */
	public static void main(String[] args) {
		ClienteFlotaSockets juego = new ClienteFlotaSockets();
		juego.ejecuta();
	} // end main

	/**
	 * Lanza una nueva hebra que crea la primera partida y dibuja la interfaz grafica: tablero
	 */
	private void ejecuta() {
		// Instancia la primera partida
		try {
			partida = new AuxiliarClienteFlota(InetAddress.getLocalHost().getHostName(), "8888"); //Creamos la nueva partida en el servidor
			partida.nuevaPartida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);								  //E iniciamos una nueva partida para poder jugar
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				guiTablero = new GuiTablero(NUMFILAS, NUMCOLUMNAS);
				guiTablero.dibujaTablero();
			}
		});
	} // end ejecuta

	/******************************************************************************************/
	/*********************  CLASE INTERNA GuiTablero   ****************************************/
	/******************************************************************************************/
	public class GuiTablero {

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
			// SOLUCIÃ“N
			String[] solucion;
			try {
				solucion = partida.getSolucion();	//El servidor nos devuelve la un vector con los barcos existentes.
				for (int i = 0; i < solucion.length; i++) { //Pintamos cada barco
					pintaBarcoHundido(solucion[i]);			
				}
			}catch(IOException ex) {
				ex.printStackTrace();
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
			
			// DecodificaciÃ³n cadenaBarco
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
		 * Limpia las casillas del tablero pintÃ¡ndolas del gris por defecto
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
	public class MenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String r = e.getActionCommand();
			
			switch(r) {
			
			case "Nueva Partida":
				guiTablero.limpiaTablero();
				try {
					partida.nuevaPartida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);	//Nueva partida si se selecciona la opcion en el menu, llamamos a la clase auxiliar
				}catch(IOException ex) {
					ex.printStackTrace();
				}
				break;
			case "Mostrar Solucion":
				guiTablero.muestraSolucion();	//Llamamos al metodo local muestraSolucion(), que llama a la clase auxiliar
				break;
			case "Salir":
				guiTablero.liberaRecursos();
				try {
					partida.fin();				//Llamamos a la clase auxiliar para acabar con el hilo de la sesión actual.
				}catch(IOException ex) {
					ex.printStackTrace();
				}
				
				System.exit(0);
				
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
	public class ButtonListener implements ActionListener {

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
		try {
			
			int resultado = partida.pruebaCasilla(f, c); //Llamamos al metodo de la clase auxiliar para probar casilla en el servidor
			if (resultado == Partida.AGUA) {
				guiTablero.pintaBoton(guiTablero.buttons[f][c], Color.BLUE);
				guiTablero.buttons[f][c].setEnabled(false);
			} else if (resultado == Partida.TOCADO) {
				guiTablero.pintaBoton(guiTablero.buttons[f][c], Color.ORANGE);
				guiTablero.buttons[f][c].setEnabled(false);
			} else if (resultado >= 0) {
				guiTablero.pintaBarcoHundido(partida.getBarco(resultado));
				quedan--;
				
			}
		}catch(IOException ex) {	//Si no recibimos nada o es erroneo, se lanza una excepcion.
			ex.printStackTrace();
		}
		
		if (quedan == 0) {
			guiTablero.cambiaEstado("FIN ... Intentos: " + disparos + "    Barcos restantes: " + quedan);
			guiTablero.desactivaTablero();
		} else {
			guiTablero.cambiaEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);
		}
	}

} // end class Juego
