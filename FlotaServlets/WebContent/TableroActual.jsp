<%@ page import="modelo.*" language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%> <!-- Importamos el modelo -->
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Hundir la Flota</title>
</head>
<body>
	<header>
		<h1>Hundir La Flota</h1>
	</header>
	<br>
	<%
		Partida partida = (Partida) request.getAttribute("partida"); // Obtenemos la partida de la peticion
		int [] posicion = (int []) request.getAttribute("posicion"); // La posicion del ultimo disparo efectuado
		int quedan = partida.getAliveBoats();
		int disparos = partida.getDisparos();
		int numBarcos = partida.getNumBarcos();
		
		if (disparos == 0) {
			out.println("<a>NUEVA PARTIDA</a>");
			out.println("<br>");
		}
		else if (posicion == null) {
			out.println("<a>No se ha seleccionado ninguna casilla...</a>");
			out.println("<br>");
		}
		else if (quedan > 0) { // Indicamos el resultado de realizar el ultimo disparo mientras queden barcos
			out.println("<a>Pagina de resultado del disparo en (" + (posicion[0] + 1) + "," + Character.toString((char) ('A' + posicion[1])) + "): Ok! </a>");
			out.println("<br>");
		}
		else {
			out.println("<a>GAME OVER</a>");
			out.println("<br>");
		}
		
		out.println("<a>Barcos Navegando: "+quedan+" Barcos Hundidos: "+ (numBarcos - quedan)+"</a>");
		out.println("<br>");
		out.println("Numero de disparos efectuados: " + disparos);
		
		out.println("<form><table border=\"0\" align=center width=300>");					
		String color;
		for (int i = 0; i <= partida.getNumFilas(); i++) {
			out.println("<tr>");
			for (int j = 0; j <= partida.getNumColumnas(); j++) {
				if(i == 0 && j > 0) {
					out.println("<td><center>" + Character.toString((char) ('A' + (j - 1))) + "</center></td>"); // Letras arriba
				} else {
					if(j == 0) {
						if (i != 0) {
							out.println("<td><center>" + i + "</center></td>"); // Numeros a la izquierda
						}
						else {
							out.println("<td></td>");
						}
					} else {
						int casilla = 0;
						if(partida.casillaDisparada(i-1, j-1)) {
							casilla = partida.getCasilla(i-1, j-1);
						}
						// Se comprueba donde ha dado el disparo
						if( casilla == -1 ) { 			
							color = "blue";			// AGUA
						} else if (casilla == -2) { 	
							color = "yellow";		// TOCADO
						}  else if (casilla == -3) { 	
							color = "red";			// HUNDIDO
						} else {						
							color = "white";
						}
						// Aqui se introduce el radio button
						if(quedan > 0) {
							out.println("<td bgcolor=\"" + color + "\"><center><input type=radio name=\"casilla\" value=\"" + (i-1) + "#" + (j-1) + "\"/></center></td>");
						}
						// Si ya no quedan barcos se desactiva el radio button
						else {
							out.println("<td bgcolor=\"" + color + "\"><center><input type=radio name=\"casilla\" value=\"" + (i-1) + "#" + (j-1) + "\" disabled/></center></td>");
						}
					}
				}
			}
			out.println("</tr>");				
		}					
		out.println("</table></td>");
		if (quedan > 0) {
			out.println("<tr><td><br><center><input type=submit name=\"HundirFlotaServlet\" value=submit></center></td></tr>");
		}
		out.println("</form>");

		// enlaces
		out.println("<tr><td><br><a HREF=\"SolucionPartidaServlet\">Muestra solucion</a><br/>");
		out.println("<a href=\"NuevaPartidaServlet\">Nueva partida</a><br />");
		out.println("<a href=\"SalirPartidaServlet\">Salir</a></td></tr></table>");
	%>	
</body>
</html>