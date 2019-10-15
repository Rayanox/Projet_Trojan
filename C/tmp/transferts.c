#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>


void main() {
/*
	FILE * f = fopen("fichierFake", "r");
	if(f == NULL) 
		printf("\n\nf est NULL\n\n");
	else {
		printf("\n\nf n'est pas NULL\n\n");
		fclose(f);
	}
*/

// Partie TRANSFERTS

	int soc = socket(AF_INET, SOCK_STREAM, 0);
		
	printf("\n\n");

	if (soc == -1)
		printf("Socket est en erreur ou n'est pas définie");
	else {
		printf("Socket existe (%d) et je vais vite la closer ^^", soc);
		int r;
		scanf("%d",&r);
		close(soc);
	}
	printf("\n\n");
}
