/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trojan_java.Modele;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rayanox
 */
public class Serveur extends Thread{
    
    
    public Serveur() {
        
        
        
    }
    
    @Override
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
                
                //Echanges
                do {
                
                    BufferedReader bufferReception = new BufferedReader(new InputStreamReader(socketClientServeur.getInputStream()));
                    PrintWriter bufferEnvoie = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketClientServeur.getOutputStream())));

                    System.out.println("(Serveur) : Ecoute de messages...");
                    messageRecu = bufferReception.readLine();
                    
                    System.out.println("(Serveur) : Message = "+messageRecu);
                    bufferEnvoie.println(); // accusé de réception
                }while(!messageRecu.equals("fin"));
                
                
                
                
                } catch (IOException ex) {
                    System.out.println("(Serveur) : Connection avec socket client = PROBLEME");
                }
            
            }
            
            
        } catch (IOException ex) {
            System.out.println("(Serveur) : Création de la socket = PROBLEME");
            ex.printStackTrace();
        }
        
        
        System.out.println("(Serveur) : Fin d'execution");
        
    }
}
