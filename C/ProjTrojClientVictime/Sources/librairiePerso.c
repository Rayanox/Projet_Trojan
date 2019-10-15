#include "../Headers/librairiePerso.h"

char * ajouteString(const char * str1, const char * str2) {

	if(str1 == NULL)
		if(str2 == NULL)
			return "";
		else
			return (char *) str2;
	if(str2 == NULL)
		return (char *) str1;

	char * final = (char *) malloc (strlen(str1)+strlen(str2));
	strcpy(final, str1);
	strcat(final, str2);
	return final;
}