package server;

import tablero.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.IntServidorPartidasRMI;

public class ImplServidorPartidasRMI extends UnicastRemoteObject implements IntServidorPartidasRMI{
	Partida partida;
	
	protected ImplServidorPartidasRMI() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void nuevaPartida(int nf, int nc, int nb) {
		this.partida = new Partida(nf, nc, nb);
	}

	@Override
	public int pruebaCasilla(int nf, int nc) {
		return partida.pruebaCasilla(nf, nc);
	}

	@Override
	public String getBarco(int idBarco) {
		return partida.getBarco(idBarco);
	}

	@Override
	public String[] getSolucion() {
		return partida.getSolucion();
	}
}
