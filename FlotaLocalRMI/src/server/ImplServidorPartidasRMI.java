package server;

import tablero.*;
import common.IntServidorPartidasRMI;

public class ImplServidorPartidasRMI implements IntServidorPartidasRMI{
	Partida partida;

	@Override
	public void nuevaPartida(int nf, int nc, int nb) {
		partida = new Partida(nf, nc, nb);
	}

	@Override
	public int pruebaCasilla(int nf, int nc) {
		return pruebaCasilla(nf,nc);
	}

	@Override
	public String getBarco(int idBarco) {
		return getBarco(idBarco);
	}

	@Override
	public String[] getSolucion() {
		return getSolucion();
	}
}
