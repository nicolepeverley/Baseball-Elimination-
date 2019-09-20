/* BaseballElimination.java
   Spring 2019
   Nicole Peverley
	
    The input consists of an integer representing the number of teams in the division and then
    for each team, the team name (no whitespace), number of wins, number of losses, and a list
    of integers represnting the number of games remaining against each team (in order from the first
    team to the last). That is, the text file looks like:
   
	<number of teams in division>
	<team1_name wins losses games_vs_team1 games_vs_team2 ... games_vs_teamn>
	...
	<teamn_name wins losses games_vs_team1 games_vs_team2 ... games_vs_teamn>

    An input file can contain an unlimited number of divisions but all team names are unique, i.e.
    no team can be in more than one division.

    Given an input stream connected to a collection of baseball division
	standings we determine for each division which teams have been eliminated 
	from the playoffs. For each team in each division we create a flow network
	and determine the maxflow in that network. If the maxflow exceeds the number
	of inter-divisional games between all other teams in the division, the current
	team is eliminated.
*/

import edu.princeton.cs.algs4.*;
import java.util.*;
import java.io.File;

public class BaseballElimination{
	
	// use an ArrayList to keep track of the eliminated teams.
	public ArrayList<String> eliminated = new ArrayList<String>();

	public BaseballElimination(Scanner s){
		int n = s.nextInt();
        String[] teams = new String[n]; // team names
        int[] wins = new int[n]; // number of wins for each team
        int[] gamestoplay = new int[n]; // games left to play for each team
        int[][] games = new int[n][n]; // games between each team
        for(int i =0; i < n; i++){
            teams[i] = s.next();
            wins[i] = s.nextInt();
            gamestoplay[i] = s.nextInt();
            for(int j = 0; j < n; j++){
                games[i][j] = s.nextInt();
            }
        }
        // if automatically eliminated based on games left to play
        for(int i = n-1; i >= 0; i--){
            int W = wins[i] + gamestoplay[i];
            if(W < wins[0]){
                eliminated.add(teams[i]);
            }
        }
        // set up flow network vertices
        int st = 0;
        int t = 0;
        int pos = 1;
        int sum = 0;
        int gamevertices[][] = new int[n][n]; // keep track of vertex used for each game
        int teamvertices[] = new int[n]; // keep track of vertex used for each team
        boolean possible = true; 
        for(int j = 0; j < n; j++){
            for(int k = 0; k < n; k++){
                if(j == k){
                    break;
                }
                if(games[j][k] != 0 && j != k){
                    gamevertices[j][k] = pos;
                    pos++;
                }
            }
        }
        for(int i = 0; i < n; i++){
            teamvertices[i] = pos;
            pos++;
        }
        t = pos;
        // build flow networks of games and teams for each possible elimination
        for(int i = 0; i < n; i++){
            FlowNetwork flow = new FlowNetwork(pos+1);
            if(!eliminated.contains(teams[i])){
                for(int j = 0; j < n; j++){
                    for(int k = 0; k < n; k++){
                        if(j == k){
                              break; 
                        }
                        if(j != i && i != k && games[j][k] != 0){
                            sum = sum + games[j][k];
                            FlowEdge e = new FlowEdge(st, gamevertices[j][k], games[j][k]);
                            FlowEdge f = new FlowEdge(gamevertices[j][k], teamvertices[j], Double.POSITIVE_INFINITY);
                            FlowEdge d = new FlowEdge(gamevertices[j][k], teamvertices[k], Double.POSITIVE_INFINITY);
                            flow.addEdge(e);
                            flow.addEdge(f);
                            flow.addEdge(d);
                        }
                    }
                }
                // set edges from teams to the end
                int W = wins[i]+gamestoplay[i];
                    for(int x = 0; x < n; x++){
                        if(i != x){
                            if(W>=wins[x]){
                                FlowEdge e = new FlowEdge(teamvertices[x],t,W-wins[x]);
                                flow.addEdge(e);
                            }
                            else{
                                possible = false;
                            }
                        }
                    }
                // run FordFulkerson to find maxflow on flow network
                FordFulkerson maxflow = new FordFulkerson(flow, st, t);
                // check if team is eliminated
                if(!possible){
                    eliminated.add(teams[i]);
                }
                else if(maxflow.value() != sum){
                    eliminated.add(teams[i]);
                }
                possible = true;
                sum = 0;
            }
        }
	}
		
	/* main()
	   code to test the BaseballElimantion function. 
	*/
	public static void main(String[] args){
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		BaseballElimination be = new BaseballElimination(s);		
		
		if (be.eliminated.size() == 0)
			System.out.println("No teams have been eliminated.");
		else
			System.out.println("Teams eliminated: " + be.eliminated);
	}
}