package Modele;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Interfaces.Observable;
import Interfaces.ObservableUser;

public class AttenteConnexionUser extends Thread{
	
	private ServerStart serveurStart;
	private int Port;
	private InetAddress IP;
	private ServerSocket socketServeur;
	
	
	public AttenteConnexionUser(ServerStart s, InetAddress ip, int port) {
		this.serveurStart = s;
		this.IP = ip;
		this.Port = port;
	}
	
	public void run() {
		try {
			this.socketServeur = new ServerSocket(this.Port, 5, this.IP);
			
			while(true) {
				Socket soc;
				try {
					soc = socketServeur.accept();
					ConnexionUser c = new ConnexionUser(soc, this.serveurStart, getNewID());
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
	
	public void arretDecoute() {
		this.interrupt();/*
		try {
			this.socketServeur.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	//Permet de déterminer le nouvel ID du nouvel utilisateur en prenant le plus petit id disponible possible
		private int getNewID() {
			int id = 0;
			while(true) {
				if(!contient(this.serveurStart.getListUsersConnected(), id)) return id;
				id++;
			}
		}

		private boolean contient(ArrayList<ObservableUser> liste,
				int id) {
			for (ObservableUser user : liste) {
				if(user.getId() == id) return true;
			}
			return false;
		}
}
