import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class ConnexionFichier extends Thread{

	private String message;
	private Socket socket;
	
	public ConnexionFichier(String m) {
		this.message = m;
	}
	
	
	public void run() {
		//Connexion
		boolean connecte = false;
		while(!connecte) {
			try {
				this.socket = new  Socket(Main.IP, Main.PORT_Fichiers);
				connecte = true;
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//Tests
		
	}
	
	
	
	
	private boolean testerExistenceFichier(String message) {
		return this;
	}
	
}
