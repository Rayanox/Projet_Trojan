@echo off

::Le fichier envoyé par le script est trouvé au path absolu et est envoyé à la racine FTP 
::/ Le fichier recu est recu à coté du script et est trouvé à la racine du serveur FTP.


if "%1%" == "/?" (
	set filename=%0%
	
	echo Arguments: 
	echo       Argument 1 = ftp command. Example = 'put' or 'get'
	echo       Argument 2 = file path.
	echo.
	echo Example : 
	echo       %filename% get testFile
	exit /B
)
if "%1%" == "" (
	echo "Error: argument 1 is missing => 'put' or 'get' command"
	exit /B
)
if "%2%" == "" (
	echo "Error: argument 2 is missing => absolute path of the file you want to import or to export."
	exit /B
)

echo "DEBUG:"
set command=%1%
set path_absolu=%2%

if not "%1%" == "put" (
	if not "%1%" == "get" (
		echo "Error: argument 1 should equals 'put' or 'get'"
		exit /B
	)
)

::echo "Le traitement FTP peut commencer"
echo user rayanox> scriptFTP.ftp
echo 300393>> scriptFTP.ftp
echo bin>> scriptFTP.ftp
echo %command% %path_absolu%>> scriptFTP.ftp
echo bye>> scriptFTP.ftp

ftp -n -s:scriptFTP.ftp 92.222.29.95
del scriptFTP.ftp

echo.
echo "Traitement FTP termine"
echo. 

