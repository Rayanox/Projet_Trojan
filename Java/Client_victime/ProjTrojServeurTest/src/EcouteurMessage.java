import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class EcouteurMessage extends Thread{

	private IUtilisateurDEcouteur client;
	private BufferedReader BufferDEcoute;
	
	public EcouteurMessage(IUtilisateurDEcouteur c) {
		this.client = c;
		try {
			this.BufferDEcoute = new BufferedReader(new InputStreamReader(this.client.getSocket().getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void run() {
		
			String message;
			
			try {
				while(!(message = this.BufferDEcoute.readLine()).equals("fin")) {
					
					this.client.TraitementMessage(message);
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("this.bufferDEcoute = null !!!!");
				//e.printStackTrace();
				this.client.Disconnection();
				
				this.client.connexion();
			}
			
		
	}
	
	public void close() {
		try {
			this.BufferDEcoute.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("erreur lors du close de bufferDEcoute");
		}
	}
	
	
	
	
	
}
