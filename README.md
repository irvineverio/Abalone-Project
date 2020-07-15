# Abalone Game Project
Networked Abalone Game Board for 2-4 players and can be played within the same network

## Test run:
The game files have been zipped and it doesn’t need any installation. However, it still has to be imported into an IDE and to a project.\
Importing The Project To Eclipse:\
File: Import...\
Select Projects from Folder Or Archive under General. And click next.\
Click on archive and locate the zip file. Choose the folder with /Abalone, it should say import as Eclipse project.\
Click Finish, the project should now be visible in the package explorer.\
The project requires JUnit 5 and JRE System Library java 11.

### Starting a local game:
Open the Abalone class from the package Abalone\
Run the class with Java compiler\
The console will ask to input between 2-4 players to play the game mode in\
After inputting the number of players, it would ask an input whether the user wants to play with a computer player or a human player\
If the user gives an input cp, it would initialize the Better Strategy (which only randomize it moves)\
If the user gives an input sp, it would initialize the Mini Max Strategy (which are the smart computer player)\
For the other input, it would initialize a human player to play the game\
Play the game\
After the game ends, an option to play again would be given for the user to input

### Starting the server:
Open the AbaloneServer class from the Abalone Server package\
Run the class with Java compiler\
The console will ask to input a port number\
Input an empty port number\
Wait for the clients to connect\
The game would be started once a lobby has been created and filled with a specific number of players

### Connecting as a client:
Open the AbaloneClient class from the Abalone Client package\
Run the class with Java compiler\
The console will ask to input an IP address\
Input the server’s IP address\
The console will ask to input a port number\
Input the server’s port number\
The console will ask to input a username\
Input the username\
Input command to send to the server\
To see the list of commands, input help and the commands list will be printed\
The main command is to make a lobby with its name and the size of the lobby by CREATE_LOBBY;LOB;2;\
A lobby can be joined by JOIN_LOBBY;LOB;\
If a lobby created with 3 or 4 players, the remaining clients would be waited by the server before the server starts the game.\
A user can tell that it’s ready by inputting READY_LOBBY;\
Once the lobby is full, the game will start\
Play the game


### Making moves:
In local games, the player at the bottom starts and turns go clockwise.\
In games on the server, the player at the top starts and turns go clockwise.\
Moves should be in the format A0,C2,1 where A is the row, 0 is the column and 1 is the direction. 0 is top left and goes clockwise.\
Directions:\
0: top left\
1: top right\
2: right\
3: bottom right\
4: bottom left\
5: left\
Board coordinates:\
<pre>
      I 4 5 6 7 8 \
   H 3 4 5 6 7 8 \
  G 2 3 4 5 6 7 8 \
 F 1 2 3 4 5 6 7 8 \
E 0 1 2 3 4 5 6 7 8 \
 D 0 1 2 3 4 5 6 7 \
  C 0 1 2 3 4 5 6 \
   B 0 1 2 3 4 5 \
    A 0 1 2 3 4 
</pre>

## Documentation:
https://docs.google.com/document/d/1DUP6UV7ShwyCfPVU_gCsKjU6GIbiOyfq-DJcjgXQpkI/edit?usp=sharing
