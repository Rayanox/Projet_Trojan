package Modele;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class MessageListennerUser extends Thread{

	
	private BufferedReader BufferDEcoute;
	private ConnexionUser User;
	//C'EST ICI QUE L'UTILISATEUR POURRA FAIRE SES CHOIX
	// C'EST AUSSI ICIQUE L'UTILISATEUR EFFECTUERA LES ECHANGES APRES CONNEXION AVEC LA VICTIME
	// *> RECEPTION DE L'UTILISATEUR PROVENANT DE LUI MEME ET DE LA VICTIME
	// Utiliser un boolean de mode pour réguler les flux d'echanges, gérer les déconnexions
	
	
	public MessageListennerUser(ConnexionUser user) {
		this.User = user;
		try {
			this.BufferDEcoute = new BufferedReader(new InputStreamReader(this.User.getSocketTrans().getInputStream()));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run(){
		try {
			String message;
			System.out.println("En ecoute user messages...");
			while((message = this.BufferDEcoute.readLine()) != null) {
				
				this.User.TraitementMesssageRecu(message);
			}				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
		}
		this.User.disconnection();
		
	}
	
	public void close() {
		try {
			this.interrupt();
			this.BufferDEcoute.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
