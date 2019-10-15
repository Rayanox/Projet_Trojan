#include "../Headers/ecritureScriptFTP.h"

void EcritureScriptFTP(int os, char * nomScriptFtp) {

	

	if(os > -1) {// SURTOUT A REMETTRE A 1
// tester si le fichier existe deja (ou voir si le programme est deja automatise mais je pense pas)

		FILE * file = fopen(nomScriptFtp, "r");
		if(f == NULL) {
			fclose(file);

			file = fopen(nomScriptFtp, "w");
				
			fprintf(file, "@echo off\n");
			fprintf(file, "if \"%%1%%\" == \"/?\" (");
			fprintf(file, "\tset filename=%%0%%\n");
			fprintf(file, "\techo Arguments: \n");
			fprintf(file, "\techo       Argument 1 = ftp command. Example = 'put' or 'get'\n");
			fprintf(file, "\techo       Argument 2 = file path.\n");
			fprintf(file, "\techo.\n");
			fprintf(file, "\techo  Example : \n");
			fprintf(file, "\techo       %%filename%% get testFile\n");
			fprintf(file, "\texit /B\n");
			fprintf(file, ")\n");
			fprintf(file, "if \"%%1%%\" == "" (\n");
			fprintf(file, "\techo \"Error: argument 1 is missing => 'put' or 'get' command\"\n");
			fprintf(file, "\texit /B\n");
			fprintf(file, ")\n");
			fprintf(file, "if \"%%2%%\" == \"\" (\n");
			fprintf(file, "\techo \"Error: argument 2 is missing => absolute path of the file you want to import or to export.\"\n");
			fprintf(file, "\texit /B\n");
			fprintf(file, ")\n");
			fprintf(file, "\n");
			fprintf(file, "echo \"DEBUG:\"\n");
			fprintf(file, "set command=%%1%%\n");
			fprintf(file, "set path_absolu=%%2%%\n");
			fprintf(file, "\n");
			fprintf(file, "if not \"%%1%%\" == \"put\" (\n");
			fprintf(file, "\tif not \"%%1%%\" == \"get\" (\n");
			fprintf(file, "\t\techo \"Error: argument 1 should equals 'put' or 'get'\"\n");
			fprintf(file, "\t\texit /B\n");
			fprintf(file, "\t)\n");
			fprintf(file, ")\n");
			fprintf(file, "\n");
			fprintf(file, "echo user rayanox> scriptFTP.ftp\n");
			fprintf(file, "echo 300393>> scriptFTP.ftp\n");
			fprintf(file, "echo bin>> scriptFTP.ftp\n");
			fprintf(file, "echo %%command%% %%path_absolu%%>> scriptFTP.ftp\n");
			fprintf(file, "echo bye>> scriptFTP.ftp\n");
			fprintf(file, "\n");
			fprintf(file, "ftp -n -s:scriptFTP.ftp 92.222.29.95\n");
			fprintf(file, "del scriptFTP.ftp\n");
			fprintf(file, "\n");
			fprintf(file, "echo.\n");
			fprintf(file, "echo \"Traitement FTP termine\"\n");
			fprintf(file, "echo.\n");		
		}
		fclose(file);
/*	
		char * test = ajouteString(NULL, nomScriptFtp);
		printf("\n\n%s\n\n", test);
*/	}

}
