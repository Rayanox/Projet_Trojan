import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;


public class ServeurTest {
	
	
	
	
	
	public static void main(String [] args) {
		
		System.out.println("Coté Serveur \n\n");
		InstanceServeur ins = new InstanceServeur();
		ins.start();
		
	}
	
}
