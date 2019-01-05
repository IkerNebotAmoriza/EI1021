<%@ page import="modelo.*" language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Hundir la Flota</title>
</head>
<body>
<%
		Partida partida = (Partida) request.getAttribute("partida"); // Obtenemos la partida de la peticion
		boolean [][] tableroSolucion = (boolean [][]) request.getAttribute("tableroSolucion");
		int quedan = partida.getAliveBoats();
		int disparos = partida.getDisparos();
		int numBarcos = partida.getNumBarcos();
		
		out.println("<a>Solucion PARTIDA</a>");
		out.println("<br>");
		
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
						if (tableroSolucion[i-1][j-1]) { 			
							color = "red";		// BARCO			
						} else {						
							color = "blue"; 	// AGUA
						}							
						out.println("<td bgcolor=\"" + color + "\"></td>");
					}
				}
			}
			out.println("</tr>");				
		}					
		out.println("</table></td>");
		out.println("</form>");

		// enlaces
		out.println("<a href=\"NuevaPartidaServlet\">Nueva partida</a><br />");
		out.println("<a href=\"SalirPartidaServlet\">Salir</a></td></tr></table>");
	%>
</body>
</html>