/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trojan_java;

import trojan_java.Modele.Client;
import trojan_java.Modele.Serveur;

/**
 *
 * @author rayanox
 */
public class Trojan_Java {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Client client = new Client();
        Serveur serveur = new Serveur();
        
        serveur.start();
        client.start();
    }
}
