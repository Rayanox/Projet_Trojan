package Modele.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

public class MoteurLog {

	private File logFile;
	private File logFileTentativeConnexion;
	private PrintWriter BufferDEcritureLog;
	String date = new SimpleDateFormat("dd-MM-yy").format(new java.util.Date());
	
	public MoteurLog(String pathLog){
		this.logFile = new File(pathLog);
		this.logFileTentativeConnexion = new File("C:\\Users\\rben-hmidane\\Desktop\\logProjTrojTentatives.txt");
		try {
			
			this.BufferDEcritureLog = new PrintWriter(new FileWriter(this.logFile, true), true);
			//this.connexion();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			System.err.println("error exception log");
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
	
	
	
	public void EcrireSautsDeLignes(PrintWriter p, int nbLignes) {
		for(int i=0; i<nbLignes; i++) {
			p.println();
		}
	}
	
}
