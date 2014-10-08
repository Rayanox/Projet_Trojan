package Interfaces;

public interface ObservableUser {

	public int getId();
	
	public void updateConnectionUserInCentralServer();
	public void updateDisconnectionUserInCentralServer();
	
	public void envoyerMessage(String m);
	public void setConnecteAvecVictime(boolean connecteAvecVictime);
	public void setVictimeConnecte (Observable victim);
	public boolean getConnecteAvecVictime();
	public Observable getVictimeConnectee();
	
	public void disconnect();
	
}
