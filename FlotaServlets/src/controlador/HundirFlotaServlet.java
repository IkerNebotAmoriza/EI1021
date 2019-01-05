package controlador;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import modelo.Partida;

/**
 * Servlet implementation class HundirFlotaServlet
 */
// Se ha eliminado la notacion de los servlets 3.0 para introducir manualmente el servlet en web.xml
public class HundirFlotaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int numFilas = 8;
	private static final int numColumnas = 8;
	private static final int numBarcos = 6;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HundirFlotaServlet() {
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
			Partida partida = (Partida) session.getAttribute("partida"); // Obtenemos la partida y si esta no existe la creamos
			if (partida == null) {
				partida = new Partida(numFilas, numColumnas, numBarcos);
			}
			String casilla = request.getParameter("casilla"); // Obtenemos la cadena de texto que indica la casilla pulsada
			if (casilla != null) { // Comprobamos que la casilla NO sea nula para evitar errores
				String [] coordenadas = casilla.split("#");
				int fila = Integer.parseInt(coordenadas[0]);
				int col = Integer.parseInt(coordenadas[1]);
				partida.pruebaCasilla(fila, col); // Probamos la casilla de la partida
				int [] posicion = {fila, col};
				request.setAttribute("posicion", posicion);
			}
			session.setAttribute("partida", partida); // Guardamos la partida en la session
			request.setAttribute("partida", partida); // Guardamos la partida en la peticion
			RequestDispatcher rd;
			rd = request.getRequestDispatcher("TableroActual.jsp");
			rd.forward(request, response); // Pasamos el trabajo al fichero jsp
		}
	}
}
