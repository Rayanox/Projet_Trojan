/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trojan_java.Modele;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author rayanox
 */
public class Client extends Thread{
    
    
    @Override
    public void run() {
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), 2000);
            
            
                
                
                
                
                
                BufferedReader bufferReception = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                PrintWriter bufferEnvoie = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);

                
                String message = "";   
                
                System.out.print("(Client) : Message = ");
                Scanner sc = new Scanner(System.in);
                message = sc.nextLine();
                
                while(!message.equals("fin")){
                    
                    //bufferEnvoie.print(message);
                    bufferEnvoie.println(message);
                    bufferReception.readLine();
                    System.out.print("(Client) : Message = ");
                    message = sc.nextLine();
                } 
                
                
            
        } catch (IOException ex) {
            System.out.println("(Client) : Connection avec socket serveur = PROBLEME");
            ex.printStackTrace();
        }
        System.out.println("(Client) : Fin d'execution");
    }
}
