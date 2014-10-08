package Interfaces;

import Modele.*;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

public interface Observateur {

	public void updateConnection(Observable o);
	public void updateDisconnection(Observable o);
	public void updateConnectionUser(ObservableUser User);
	public void updateDisconnectionUser(ObservableUser User);
	public ArrayList<Observable> getlistVictimsConnected();
	public ArrayList<Observable> getlistVictimsTransaction();
	public ArrayList<ObservableUser> getListUsersConnected();
	public void PasserVictimeEnListeTransaction(Observable victim);
	public void PasserVictimeEnListeConnectee(Observable victim);
	public ArrayList<Observable> getListIdVictimsEnTransaction();
	public ArrayList<ConnexionEnvoiFichier> getListConnexionEnvoiFichier();
	
	public String redemarrageDuServeur(String pathJar);
	public ServerSocket getServeurSocketFichier();
	
}
