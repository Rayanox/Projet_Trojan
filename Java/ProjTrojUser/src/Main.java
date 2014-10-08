import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Modele.Connexion;
import Vues.Fenetre;


//Rajouter identification



public class Main {

	
	private static String IP = "127.0.0.1";
	private static int port = 3000;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		InetAddress IP = null;
		
		
		if(args.length < 1) { //sans arguments -> Lancement du programme par click executable
			JFrame fenPopUp = new JFrame();
			fenPopUp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			recupIpParDialog(fenPopUp);
			
			
			
			try {
				IP = InetAddress.getByName(Main.IP);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(-1);
			}
			
			popup(fenPopUp, "IP = "+Main.IP+"\nPort = "+Main.port);
		}else { //avec arguments
			IP = getIPArguments(args[0]);
			port = getPortArguments(args[1]);
			
		}
			
		Fenetre f = new Fenetre();
		
		Connexion connexion = new Connexion(IP, port, f);			
		f.setConnexion(connexion);
		
		connexion.Lancement();	
		
	}

	private static void recupIpParDialog(JFrame j) {
		JOptionPane pop = new JOptionPane();
		String IP = pop.showInputDialog(j, "Entrez une IP sure laquelle vous connecter (ou rien pour utiliser l'IP par d�faut = "+Main.IP+")");
		try {
			int port = Integer.parseInt(pop.showInputDialog(j, "Entrez un port sur lequel vous connecter (ou rien pour utiliser le port par d�faut = "+Main.port+")"));
		}catch (Exception e) {			
		} // Port par d�faut
		try {
			InetAddress.getByName(IP);
			if(IP.equals("")) throw new Exception();
			Main.IP = IP;
		} catch (Exception e) {
		}
		
		System.out.println("PORT = "+port+"\nIP = "+IP);
	}

	private static void popup(JFrame j, String message) {
		JOptionPane pop = new JOptionPane();
		pop.showMessageDialog(j, message);
	}

	private static InetAddress getIPArguments(String ip) {
		InetAddress IP;
		try {
			IP = InetAddress.getByName(ip);
			
		}catch (Exception e) {
			System.out.println("Erreur => Mauvais port entr� -> le deuxieme argument doit etre un port valide (entier)");
			System.exit(-1);
			return null;
		}
		return IP;
		
	}

	private static int getPortArguments(String port) {
		int Port;
		try {
			Port = Integer.parseInt(port);
			
		}catch (Exception e) {
			System.out.println("Erreur => Mauvais port entr� -> le deuxieme argument doit etre un port valide (entier)");
			System.exit(-1);
			return -1;
		}
		return Port;
	}

}
