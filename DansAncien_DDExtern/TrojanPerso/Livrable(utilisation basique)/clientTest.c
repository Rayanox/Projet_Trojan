#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>

//prototypes
int scanConnection(int soc, struct sockaddr_in config);


void main() {





	int soc, connection;
	struct sockaddr_in config;

	soc = socket(AF_INET, SOCK_STREAM,0);

	config.sin_family = AF_INET;
	config.sin_port = htons(2000);


	connection = scanConnection(soc, config);
	if(connection != -1) printf("\n\nUne cible est connectée : Connection Réussie\n\n");
	else printf("\n\nAucune cible connectée.\n\n");
}

//renvoie -1 si aucune connection n'a fonctionné, ou une autre valeur sinon.
int scanConnection(int soc, struct sockaddr_in config) {

	int nb=1, connection, u, d, c, decalage=0; //u=unité, d= dizaine, c=centaine
        char ip[15] = "192.168.30.1";
        ip[12] = '\0';

        for(nb=1, u=1, d=0, c=0 ; nb<255; nb++, u++) {

		if(u>9) {
                        u=0;
                        d++;
                        if(d>9) {
                                d=0;
                                c++;
                                if(decalage == 1) decalage++;
                        }
                        if(decalage==0) decalage++;
                }
                if(decalage == 0){
                        ip[11+decalage] = (char)u+48;
                }else if(decalage== 1 ) {
                        ip[11] = (char)d+48;
                        ip[11+decalage] = (char)u+48;
                } else if(decalage == 2) {
                        ip[11] = (char)c+48;
                        ip[11+decalage-1] = (char)d+48;
                        ip[11+decalage] = (char)u+48;
                }

		printf("ip = %s\n", ip);

                config.sin_addr.s_addr = inet_addr(ip);
                connection = connect(soc, (struct sockaddr *)&config, sizeof(struct sockaddr *));
                printf("Connexion = %d\n", connection);
		if(connection != -1) break;

        }

	return connection;
}
