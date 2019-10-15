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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author rayanox
 */
public class Client {

	private InetAddress ipTarget;
    
    public Client(InetAddress ip) {
    	this.ipTarget = ip;
	}

	public void run() {
        try {
            Socket socket = new Socket(ipTarget, 2000);

			launchInputStreamReader(socket.getInputStream());
			
			PrintWriter bufferEnvoie = new PrintWriter(
					new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

			Scanner sc = new Scanner(System.in);
			System.out.print("(Client) : Message = ");

			String message = "";
			message = sc.nextLine();

			while (!message.equals("exit")) {
				bufferEnvoie.println(message);
				bufferEnvoie.flush();
				
				System.out.print("(Client) : Message = ");
				message = sc.nextLine();
			}                
                
            
        } catch (IOException ex) {
            System.out.println("(Client) : Connection avec socket serveur = PROBLEME");
            ex.printStackTrace();
        }
        System.out.println("(Client) : Fin d'execution");
    }
    
    private static void launchInputStreamReader(InputStream inputStream) {
    	StreamReader reader = new StreamReader(inputStream);
    	reader.start();
    }
}
