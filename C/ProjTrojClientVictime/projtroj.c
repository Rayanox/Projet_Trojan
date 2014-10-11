#include <stdio.h>
#include <stdlib.h>
#include <string.h>



void AutomatisationProgramme();
int VerificationExecutionProgramme();
int VerificationDejaAutomatise();
char* AppelSysteme(char* commande);

int system_OS = 0; // 1 = linux; 2 = windows7, windows8 et windowsVISTA; 3 = windowsXP.
char * cheminAbsoluProgramme;
char * COMMANDE_crontab_Linux = "*/5 * * * * "; // On concatene ensuite dans le programme le chemin absolu




void ReglageCheminAbsoluEtCommandes(char* nomProgramme) {

// Récupération du chemin absolu
	int i;
	int taille = strlen(nomProgramme)-1;
	char * nom = (char *) malloc(taille);
	for(i=0; i<taille; i++)
		nom[i] = nomProgramme[i+1];

	char* c = AppelSysteme("pwd");
	
	char* tmp = (char*) malloc(strlen(c)-1);
	for(i=0; i<strlen(c)-1; i++)
		tmp[i] = c[i];
	free(c);
	c = tmp;

	int nbe = ((int)strlen(c))-1;
	strcat(c, nom);
	cheminAbsoluProgramme = c;
	//printf("\n\nChemin = %s\n\n", cheminAbsoluProgramme);

//Creation des commandes
	tmp = (char*) malloc(strlen(COMMANDE_crontab_Linux)+strlen(cheminAbsoluProgramme));
	strcpy(tmp,COMMANDE_crontab_Linux);
	strcat(tmp, cheminAbsoluProgramme);
	COMMANDE_crontab_Linux = tmp;
	printf("\n\nCommande = %s\n\n", COMMANDE_crontab_Linux);
}

void main(int argc, char* argv[]) {
	
	if(VerificationExecutionProgramme())
		exit(0);

	ReglageCheminAbsoluEtCommandes(argv[0]);

	AutomatisationProgramme();

	fflush(stdout);
	pause();
}

void AutomatisationProgramme(){

	char tmp [] = "crontab -l | grep '";
	char * commande = (char*) malloc(strlen(tmp)+strlen(cheminAbsoluProgramme)+1);
	strcpy(commande, tmp);
	strcat(commande, cheminAbsoluProgramme);
	strcat(commande,"'");

printf("\n\nTMP1 = %s", commande);

	char* result = AppelSysteme(commande);
printf("\n\nTEMPORAIRE = %s\n\n", result);
	if(strstr(result, COMMANDE_crontab_Linux) == NULL) { // Si il n'y a pas la commande dans le crontab

		printf("\n\nLa commande n'est pas présente dans le fichier crontab ! \n\n");	

		char tmp2 [] = "' >> fich";
		free(commande);
		commande = (char*) malloc(strlen("echo '") + strlen(tmp2) + strlen(COMMANDE_crontab_Linux));
		strcpy(commande, "echo '");
		strcat(commande, COMMANDE_crontab_Linux);
		strcat(commande, tmp2);

		system("crontab -l > fich");
		system(commande);
		system("crontab fich");
		system("rm fich");
	}
}


// renvoie vrai si le programme est déjà en cours d'execution
int VerificationExecutionProgramme(){
	char* texte = AppelSysteme("ps aux | grep projtroj | wc -l");
	int nbProcess = (((int)texte[0]) - 48);

	printf("\n\nNombre de process = %d\n\n", nbProcess);
	return nbProcess > 3;
}

char* AppelSysteme(char* commande){

	char buffer [1024];
	char *texte = (char*) malloc(1024) ;

	FILE* fcmd = popen(commande, "r");

	while(fgets(buffer, 1024 , fcmd) != NULL){
		strcat(texte, buffer);
	}

	fclose(fcmd);

	return texte;
}






