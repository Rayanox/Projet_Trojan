package Modele;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MessageListenner extends Thread{

	private BufferedReader BufferDEcoute;
	private ConnexionVictim Victim;
	private String message;
	
	public MessageListenner(ConnexionVictim sock) {
		this.Victim = sock;
		try {
			this.BufferDEcoute = new BufferedReader(new InputStreamReader(this.Victim.getSocketTrans().getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run(){
		System.out.println("OK");
		try {
			while((this.message = this.BufferDEcoute.readLine()) != null) {
				System.out.println("Message victime reçu : "+this.message);
				this.Victim.TraitementMesssageRecu(message);
			}							
		} catch (IOException e) {
		}
		
		this.Victim.disconnect();
	}
	
	public void close() {
		try {
			this.BufferDEcoute.close();
		} catch (IOException e) {
		}
	}
	
	
}
