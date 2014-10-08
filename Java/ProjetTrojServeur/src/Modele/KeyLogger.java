package Modele;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class KeyLogger extends KeyAdapter{
	
	private File file;
	private BufferedWriter bufferDEcriture;
	
	public KeyLogger() {
		
		try {
			this.file = new File("C:\\Users\\rben-hmidane\\Desktop\\logKey.txt");
	 		this.bufferDEcriture = new BufferedWriter(new FileWriter(file));
	 		this.bufferDEcriture.write("D�but d'enregistrement");
	 		this.bufferDEcriture.flush();
		} catch (IOException e1) { 
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	public void closeBuffer() {
		try {
			this.bufferDEcriture.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("JE PASSE ...");
		try {
			
			bufferDEcriture.write(e.toString());
			this.bufferDEcriture.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
