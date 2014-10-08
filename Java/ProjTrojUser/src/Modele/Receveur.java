package Modele;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import Vues.Fenetre;

import com.sun.imageio.plugins.common.InputStreamAdapter;


public class Receveur extends Thread{
	
	private BufferedReader reader;
	private Connexion connexion;
	private Fenetre fenetre;
	
	public Receveur(Socket s, Fenetre f, Connexion c) throws IOException {
		this.reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
		this.connexion = c;
		this.fenetre = f;
	}
	
	public void run() {
		int length = 0;
		char [] message = new char[10000];
		
		try {
			while((length = this.reader.read(message)) > 0) {
				System.out.println(message);
				this.fenetre.addInOutputBox(new String(message, 0, length));
			}
		} catch (IOException e) {
		}
		
		this.connexion.deconnexion();
		
		
	}
	
	public void closeConnexions() {
		try {
			this.reader.close();
		}catch (Exception e) {
			
		}
	}
	
}
