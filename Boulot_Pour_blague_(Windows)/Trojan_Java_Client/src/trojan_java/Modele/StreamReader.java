package trojan_java.Modele;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamReader extends Thread{

	private InputStream inputStream;

	public StreamReader(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public void run() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		try {
			while((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("End of program");
			System.exit(-1);
		}
	}
}
