# SIBDex
Pokedex Java/psql dans le cadre de l'électif sibd
Version actuelle :
  Access control is done with java.
TODO : 
  build some database access
  implement a init() class for installation of database (this one should parse .csv)
  
  
  # Run the project
  You have 2 choices :
    Run "run" class in Test package with Netbeans
    Run from cli : cli.java T <usertype> U <username> P <password>
    >see access control
  
  # Access control configuration
  To make th DEVMODE work properly, go to "ctrl" class. Find DEVMODEUSN and DEVMODEPWD. Put your postgres superuser id instead.
