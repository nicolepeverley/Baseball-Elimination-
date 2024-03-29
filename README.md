# Baseball-Elimination-
Algorithm used to determine whether a baseball team is eliminated from the playoffs


Given the standings in a sports league at some point during the season, determine which teams have been mathematically eliminated from winning their division.


Given an input of baseball division standings we determine for each division which teams have been eliminated from the playoffs. For each team in each division we create a flow network and determine the maxflow in that network. If the maxflow exceeds the number of inter-divisional games between all other teams in the division, the current team is eliminated.


The input consists of an integer representing the number of teams in the division and then for each team, the team name (no spaces), number of wins, number of losses, and a list of integers representing the number of games remaining against each team (from the first team to the last). That is, the text file looks like:


number of teams in division


team1_name wins losses games_vs_team1 games_vs_team2 ... games_vs_teamn


...


teamn_name wins losses games_vs_team1 games_vs_team2 ... games_vs_teamn


An input file can contain an unlimited number of divisions but all team names are unique, i.e. no team can be in more than one division.

To test the algorithm, create a text file with one or more test divisions (in the designated format) and run the program with:


java -cp .;algs4.jar BaseballElimination testfile.txt (Windows) or java -cp .:algs4.jar BaseballElimination testfile.txt (Linux or Mac)

Algorithms textbook jar file found below:


https://algs4.cs.princeton.edu/code/
