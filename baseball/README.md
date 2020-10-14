# Baseball Elimination using Flow Networks

A fairly unexpected application of finding the maximum flow of a flow network is in determining whether a baseball team has been mathematically eliminated. Consider the league table, where w[i], l[i], r[i] represents the number of wins, losses, remaining games respectively for team i, and g[i][j] represents the number of games remaining between team i and team i.

i|team| w[i] | l[i] | r[i] | g[i][0] |g[i][1] | g[i][2] |g[i][3]| 
-|----|------|------|------|---------|--------|---------|-------|
0|Atlanta|83|71|8| |1|6|1|
1|Philadelphia|80|79|3|1| |0|2|
2|New York|78|78|6|6|0| |0|
3|Montreal|77|82|3|1|2|0| |

Here Montreal is mathematically eliminated because it can only finish with at most 80 wins, but Atlanta already has 83. This is a trivial form of elimination that is easy to spot. More difficult to see is that Philadelphia is mathematically eliminated because, although it can finish with 83 wins, Atlanta and New York face each other 6 times, so at least one of them finishes with 84 or more wins. 

To determine whether a team x is mathematically eliminated, first we check for trivial elimination (i.e. if there is a team i such that w[x] + r[x] < w[i], then x is eliminated). Otherwise, we can formulate the rest of the league table (excluding team x) as a flow network. Because of the way the network is set up, properties of the maxflow of the network can tell us whether team x is mathematically eliminated.

Create an artificial source vertex s and sink vertex t. Create a game vertex corresponding to the game i-j, and an edge from s to game vertex i-j of capacity g[i][j]. If there are N teams, then there will be 'N-1 choose 2' such game vertices (we exclude team x). Next we create team vertices for each team except team x. Each game vertex i-j will be connected to team vertex i and team vertex j with two edges of infinite capacity. 

Now connect each team vertex to the sink vertex t with an edge. Note that team x can win as many as w[x] + r[x] games in total. To prevent team i from winning more than w[x] + r[x] many games in total, we set the capacity of the edge from team i to t to be w[x] + r[x] - w[i].

See the diagram in the full specification at [https://coursera.cs.princeton.edu/algs4/assignments/baseball/specification.php](https://coursera.cs.princeton.edu/algs4/assignments/baseball/specification.php)

Then we use the Ford-Fulkerson algorithm to find the maxflow. If all the edges from s to the game vertices are full in the maxflow, this means we have assigned winners to all the remaining games of the remaining teams in such a way that no team wins more games than x, because of the capacities used in the last edges. On the other hand, if one of the edges from s is not full, this means that such an assignment was not possible, i.e. team x is mathematically eliminated.

