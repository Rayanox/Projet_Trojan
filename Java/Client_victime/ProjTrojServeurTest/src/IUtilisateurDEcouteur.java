import java.net.Socket;


public interface IUtilisateurDEcouteur {

	public void TraitementMessage(String message);
	public Socket getSocket();
	public void connexion();
	public void Disconnection();
}
