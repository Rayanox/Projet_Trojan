import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ThreadBufferProcess extends Thread{

	private ClientVictim threadPere ;
	
	private BufferedReader readerProcess;
	private int lengthDonneesRecues;
	private char [] donneesProcess;
	private String nomthread;
	
	public ThreadBufferProcess(ClientVictim c, BufferedReader buffer, String nom) {
		this.threadPere = c;
		this.readerProcess = buffer;
		this.donneesProcess = new char [1000];
		this.nomthread = nom;
	}
	
	public void run() {
		String resultFinal;
		try {
			while((lengthDonneesRecues = readerProcess.read(donneesProcess)) > 0)	{
					//System.setProperty("file.encoding", Main.encodingTerminal);
					resultFinal = new String(donneesProcess, 0, lengthDonneesRecues);
					this.threadPere.setResultFinal(this.threadPere.getResultFinal()+resultFinal);
					//System.out.println(resultFinal);
			}
			
			
			
			synchronized (threadPere) {
				threadPere.notify();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
