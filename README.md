# SIBDex
Pokedex Java/psql dans le cadre de l'électif sibd
Version actuelle :
  Access control is done with java. Can be optimised.
TODO : 
  build some database access
  add all the init operation into init() (also csv parsing)
  fix init() unknown error / put property file in dist
  finish to implement actions
  finish to implement CLI set of actions and control
  (modify login combo box list)
  
# Run the project
Simply press play. See cli main class to change inline arguement (batch argements simulation)
Btach argements : T <usertype> U <username> P <password>

# Access control configuration
To make th DEVMODE work properly, go to "ctrl" class. Find DEVMODEUSN and DEVMODEPWD. Put your postgres superuser id instead.
