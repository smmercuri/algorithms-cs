/* *****************************************************************************
 *  Name: Salvatore Mercuri
 *  Date: June 30 2020
 *  Description: Create flow network based on baseball league to determine
 *               whether a team has been mathematically eliminated. Uses
 *               Ford-Fulkerson algorithm to find the maxflow to determine
 *               this. Corresponding mincut of that maxflow gives subset of
 *               teams that prove this mathematical elimination.
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BaseballElimination {
    private final int numberOfTeams;
    private final ArrayList<String> teams;
    private final int[] w; // wins
    private final int[] l; // losses
    private final int[] r; // remaining
    private final int[][] g; // games between teams

    // create a baseball division from given filename
    public BaseballElimination(String filename) {
        In division = new In(filename);
        this.numberOfTeams = division.readInt();
        this.teams = new ArrayList<String>();
        this.w = new int[numberOfTeams];
        this.l = new int[numberOfTeams];
        this.r = new int[numberOfTeams];
        this.g = new int[numberOfTeams][numberOfTeams];
        int i = 0;
        while (!division.isEmpty()) {
            teams.add(division.readString());
            w[i] = division.readInt();
            l[i] = division.readInt();
            r[i] = division.readInt();
            for (int j = 0; j < numberOfTeams; j++) {
                g[i][j] = division.readInt();
            }
            i++;
        }
        //int V = 1 + numberOfTeams + (numberOfTeams - 1) * (numberOfTeams - 2) / 2;
        //this.teamNetwork = new FlowNetwork(numberOfTeams);

        // construct graph from rest!!
    }

    // number of teams
    public int numberOfTeams() {
        return numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return teams;
    }

    // number of wins for given team
    public int wins(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException();
        int teamIndex = teams.indexOf(team);
        return w[teamIndex];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException();
        int teamIndex = teams.indexOf(team);
        return l[teamIndex];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException();
        int teamIndex = teams.indexOf(team);
        return r[teamIndex];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teams.contains(team1) || !teams.contains(team2)) throw new IllegalArgumentException();
        int team1Index = teams.indexOf(team1);
        int team2Index = teams.indexOf(team2);
        return g[team1Index][team2Index];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException();
        if (isTriviallyEliminated(team)) return true;
        FlowNetwork divisionNetwork = divisionNetwork(team);
        int s = teams.indexOf(team);
        FordFulkerson maxFlow = maxFlow(team);
        double maxValue = 0.0;
        for (FlowEdge e : divisionNetwork.adj(s)) maxValue += e.capacity();
        return maxFlow.value() != maxValue;
    }

    // is given team trivially eliminated?
    private boolean isTriviallyEliminated(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException();
        int teamIndex = teams.indexOf(team);
        for (int i = 0; i < numberOfTeams; i++) {
            if (w[teamIndex] + r[teamIndex] < w[i]) return true;
        }
        return false;
    }

    // Ford Fulkerson
    private FordFulkerson maxFlow(String team) {
        FlowNetwork divisionNetwork = divisionNetwork(team);
        int s = teams.indexOf(team);
        int numberOfRemainingPairedGames = (numberOfTeams - 1) * (numberOfTeams - 2) / 2;
        int t = numberOfTeams + numberOfRemainingPairedGames;
        FordFulkerson maxFlow = new FordFulkerson(divisionNetwork, s, t);
        return maxFlow;
    }

    // set up flow network representation of division
    private FlowNetwork divisionNetwork(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException();
        int numberOfRemainingPairedGames = (numberOfTeams - 1) * (numberOfTeams - 2) / 2;
        int V = 1 + numberOfTeams + numberOfRemainingPairedGames;
        FlowNetwork divisionNetwork = new FlowNetwork(V);
        int s = teams.indexOf(team);
        int t = V - 1;
        int vertexCounter = numberOfTeams;
        for (int i = 0; i < numberOfTeams; i++) {
            if (i != s) {
                FlowEdge e = new FlowEdge(i, t, w[s] + r[s] - w[i]);
                divisionNetwork.addEdge(e);

                for (int j = i + 1; j < numberOfTeams; j++) {
                    if (j != s) {
                        FlowEdge h = new FlowEdge(s, vertexCounter, g[i][j]);
                        divisionNetwork.addEdge(h);
                        FlowEdge h1 = new FlowEdge(vertexCounter, i, Double.POSITIVE_INFINITY);
                        FlowEdge h2 = new FlowEdge(vertexCounter, j, Double.POSITIVE_INFINITY);
                        divisionNetwork.addEdge(h1);
                        divisionNetwork.addEdge(h2);
                        vertexCounter++;
                    }
                }
            }
        }
        return divisionNetwork;

    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException();
        Queue<String> certificateOfElimination = new Queue<String>();
        if (isTriviallyEliminated(team)) {
            int maxWinIndex = 0;
            for (int i = 1; i < numberOfTeams; i++) {
                if (w[i] > w[maxWinIndex]) maxWinIndex = i;
            }
            certificateOfElimination.enqueue(teams.get(maxWinIndex));
            return certificateOfElimination;
        }
        int s = teams.indexOf(team);
        FordFulkerson maxFlow = maxFlow(team);
        for (int i = 0; i < numberOfTeams; i++) {
            if (i != s && maxFlow.inCut(i)) {
                certificateOfElimination.enqueue(teams.get(i));
            }
        }
        if (certificateOfElimination.size() == 0) return null;
        return certificateOfElimination;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
