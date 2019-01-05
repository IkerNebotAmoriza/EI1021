package controlador;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import modelo.Partida;

/**
 * Servlet implementation class SolucionPartidaServlet
 */
@WebServlet("/SolucionPartidaServlet")
public class SolucionPartidaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int numFilas = 8;
	private static final int numColumnas = 8;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SolucionPartidaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true); // Obtenemos la sesion, y si no existe el metodo "getSession" devuelve una nueva
		response.setContentType("text/html;charset=UTF-8"); 
		
		if (session != null) { // Comprobamos que la sesion NO sea nula para evitar errores
			Partida partida = (Partida) session.getAttribute("partida"); // Obtenemos la partida
			boolean [][] tableroSolucion = getTableroSolucion(partida.getSolucion()); // Obtenemos la solucion
			request.setAttribute("partida", partida); // Guardamos la partida en la peticion
			request.setAttribute("tableroSolucion", tableroSolucion);
			RequestDispatcher rd;
			rd = request.getRequestDispatcher("TableroSolucion.jsp");
			rd.forward(request, response); // Pasamos el trabajo al fichero jsp
		}
	}
	
	private boolean [][] getTableroSolucion(String [] solucion) {
		boolean [][] tablero = new boolean[numFilas][numColumnas];
		String [] datosBarco;

		for (int i = 0; i < solucion.length; i++) {
			datosBarco =  solucion[i].split("#");
			int f_ini = Integer.parseInt(datosBarco[0]);	
			int c_ini = Integer.parseInt(datosBarco[1]);
			String orientacion = datosBarco[2];
			int tam = Integer.parseInt(datosBarco[3]);

			switch (orientacion) {
			case "V":
				for (int j = f_ini ; j < f_ini + tam; j++){
					tablero[j][c_ini] = true;
				}
				break;
			case "H":
				for (int k = c_ini; k < c_ini + tam; k++){
					tablero[f_ini][k] = true;
				}	
				break;
			}
		}
		return tablero;
	}
}
