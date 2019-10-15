#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <arpa/inet.h>
#include <netinet/in.h>


void envoyerDonnee(int sock) {

	char buffer[2024];
	int envoie;

	//scanf("Veuillez entrer votre message : %s", &buffer);
	printf("Veuillez entrer votre message : ");
	gets(buffer);
	envoie = send(sock, buffer, sizeof(buffer), 0);
	printf("\n");


}


void main(int nb,char *arg) {

	int soc, connection, config, envoie ;
int e = 0;
for(e=0; e<nb; e++) printf("argument n°%d = %s\n", e, (char)arg[e]);

soc = socket(AF_INET,SOCK_STREAM, 0 );

struct sockaddr_in configuration;


configuration.sin_port = htons(2000);
configuration.sin_family = AF_INET;
configuration.sin_addr.s_addr = inet_addr("192.168.1.60");

connection = connect(soc, (struct sockaddr *) &configuration, sizeof(configuration));

if(connection != -1) printf("\n\nVous etes connecté\n\n");
else {printf("erreur de connexion"); exit(0); }


//DANS LA CONNECTION
char buffer[2024] ;


while(1) {
//	scanf("Entrez un message : %c", &buffer);
//	envoie = send(soc, buffer, sizeof(buffer), 0);
	envoyerDonnee(soc);
	recv(soc, buffer, sizeof(buffer), 0);
	printf("\n\nRéponse : %s\n\n", buffer);


}


if(envoie != -1) printf("\n\nEnvoie réussi\n\n");
else exit(0);


printf("\n\nFermeture de la connection\n\n");


}
