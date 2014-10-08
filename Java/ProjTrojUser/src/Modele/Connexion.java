package Modele;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Vues.Fenetre;


public class Connexion {

	private Socket socket;
	private BufferedWriter writer;
	private Receveur receveur;
	private int PORT;
	private InetAddress IP;
	private boolean DeconnectionDuServeur;
	private Fenetre fenetre; 
	
	public Connexion(InetAddress ip, int port, Fenetre f) {
		this.fenetre = f;
		this.IP = ip;
		this.PORT = port;
	}
	
	
	public void Lancement() {
		
		
		try {
			this.socket = new Socket(this.IP, this.PORT);
			this.receveur = new Receveur(this.socket, this.fenetre, this);
			this.receveur.start();
			this.writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
			
			this.DeconnectionDuServeur = false;
			
			// Pour l'éventuel mode console uniquement
			
		/*	Scanner sc = new Scanner(System.in);
			String message = "";
			
			System.out.println("Entrez votre message : ");
			message = sc.nextLine();
			
			try{
				while(!DeconnectionDuServeur) {
					envoyerMessage(message);
					System.out.println("Entrez votre message : ");
					message = sc.nextLine();
				}
			}catch(IOException e) {
				System.out.println("Arret de connexion avec le serveur : fin des envois\n");	
				this.deconnexion();
			}
		*/	
		} catch (IOException e1) {
			String messageErreur = "Probleme de connexion avec le serveur :";
			System.out.println(messageErreur);
			this.popup(fenetre, messageErreur+" \nServeur éteint ou mauvaises IP et port entrés.\nFin du programme.");
			this.deconnexion();
		}
		/*
		
		
		
		
		System.out.println("Fin des echanges et du programme...");
		*/
		
	}	
	
	
	public void envoyerMessage(String message) throws IOException {
		writer.write(message+"\n");
		writer.flush();		
	}

	public InetAddress getIP() {
		return IP;
	}
	
	public int getPORT() {
		return PORT;
	}

	public void deconnexion() {
		try{
			this.receveur.closeConnexions();
		}catch(Exception e) {
		}
		
		try {
			this.writer.close();
		}catch (Exception e) {			
		}		
		try {
			this.socket.close();
		}catch (Exception e) {			
		}
		this.DeconnectionDuServeur = true;
		System.out.println("Deconnection du serveur : fin de reception des messages");
		System.out.println("Fin des echanges et du programme...");
		System.exit(0);
	}
	
	private static void popup(JFrame j, String message) {
		JOptionPane pop = new JOptionPane();
		pop.showMessageDialog(j, message);
	}
	
}
