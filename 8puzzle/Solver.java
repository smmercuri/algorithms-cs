/* *****************************************************************************
 *  Name: Salvatore Mercuri
 *  Date: May 29 2020
 *  Description: Implements an A*-search algorithm using priority queues to
 *               give an efficient way of solving the 8 puzzle problem.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final SearchNode firstNode;
    private SearchNode finalNode;
    private Stack<Board> boards;
    private SearchNode current;

    private class SearchNode implements Comparable<SearchNode> {
        private final int priority;
        private final Board board;
        private final SearchNode prev;
        private final int moves;

        public SearchNode(Board board, int moves, SearchNode prev) {
            this.board = board;
            this.priority = moves + board.manhattan();
            this.prev = prev;
            this.moves = moves;
        }

        public int compareTo(SearchNode that) {
            if (this.priority == that.priority) {
                return this.board.manhattan() - that.board.manhattan();
            }
            return this.priority - that.priority;
        }

    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        MinPQ<SearchNode> pqTwin = new MinPQ<SearchNode>();
        firstNode = new SearchNode(initial, 0, null);
        SearchNode twin = new SearchNode(initial.twin(), 0, null);
        pq.insert(firstNode);
        pqTwin.insert(twin);
        SearchNode deq = pq.delMin();
        SearchNode deqTwin = pqTwin.delMin();
        while ((!deq.board.isGoal()) && (!deqTwin.board.isGoal())) {
            for (Board s : deq.board.neighbors()) {
                if (deq.prev == null || !s.equals(deq.prev.board)) {
                    SearchNode sn = new SearchNode(s, deq.moves + 1, deq);
                    pq.insert(sn);
                }
            }
            deq = pq.delMin();
            for (Board r : deqTwin.board.neighbors()) {
                if (deqTwin.prev == null || !r.equals(deqTwin.prev.board)) {
                    SearchNode sn = new SearchNode(r, deqTwin.moves + 1, deqTwin);
                    pqTwin.insert(sn);
                }
            }
            deqTwin = pqTwin.delMin();
        }
        if (deq.board.isGoal()) finalNode = deq;
        if (deqTwin.board.isGoal()) finalNode = deqTwin;
    }


    // is the initial board solvable?
    public boolean isSolvable() {
        solution();
        return boards.peek().equals(firstNode.board);
    }

    // min number of moves to solve initial board
    public int moves() {
        return finalNode.moves;

    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        boards = new Stack<Board>();
        SearchNode pointer = finalNode;
        while (pointer != null) {
            boards.push(pointer.board);
            pointer = pointer.prev;
        }
        return boards;
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

