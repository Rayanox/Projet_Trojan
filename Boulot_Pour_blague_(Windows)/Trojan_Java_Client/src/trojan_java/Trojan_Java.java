/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trojan_java;

import java.net.InetAddress;
import java.net.UnknownHostException;

import trojan_java.Modele.Client;

/**
 * All my implementation has been lost, so the minimal code has been recoded and has to be improved..
 *
 * @author rayanox
 */
public class Trojan_Java {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	
    	if(args.length < 1) {
    		System.err.println("You must specify the IP address to reach in first argument ! End  of program.");
    		System.exit(1);
    	}
    	String ipString = args[0];
    	
    	InetAddress ip;
    	if((ip = checkAndExtractDestinationIP(ipString)) == null) {
    		System.err.println("The ip Address in not in a correct format at the first argument position. Please retry with the correct value as first argument. Enf of program.");
    		System.exit(2);
    	}
    	
        Client client = new Client(ip);
        client.run();
    }
    
    public static InetAddress checkAndExtractDestinationIP(String ipString) {
			try {
				InetAddress ip = InetAddress.getByName(ipString);
				return ip;
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
    }
    
    public static InetAddress checkAndExtractDestinationIPRange(String startingRangeIP, String EndingRangeIP) throws Exception {
    	throw new Exception("Not implemented. Must return the first connection succeeded between the two ranges of IPs");
    }
}
