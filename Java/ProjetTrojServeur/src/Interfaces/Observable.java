package Interfaces;

public interface Observable {

	
	public void addObservateur(Observateur observateur);
	public void delObservateur(Observateur observateur);
	public void updateConnectionObservateur();
	public void updateDisconnectionObservateur();
	
	public ObservableUser getUserConnecte();
	public void setUserConnecte(ObservableUser userConnecte);
	public void envoyerMessage(String m);
	public int getIdInstance();
	
	public void disconnect();
}
