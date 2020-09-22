/* *****************************************************************************
 *  Name: Salvatore Mercuri
 *  Date: May 29 2020
 *  Description: Board data type to represent the n-by-n grid with sliding tiles
 *               for the 8 puzzle problem.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public final class Board {
    private final int n; // size of board
    private final int[][] board;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.board = new int[n][n];
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                this.board[row][col] = tiles[row][col];
            }
        }
    }

    // string representation of this board
    public String toString() {
        String rep = Integer.toString(n) + "\n";
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                rep = rep + String.format("%5d", board[row][col]) + " ";
            }
            rep = rep + "\n";
        }
        return rep;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (board[row][col] != (row * n + col + 1) % (n * n) && board[row][col] != 0)
                    hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (board[row][col] != (row * n + col + 1) % (n * n) && board[row][col] != 0) {
                    int goalRow = (board[row][col] - 1) / n;
                    int goalCol = (board[row][col] - 1) % n;
                    manhattan = manhattan + Math.abs(goalRow - row) + Math.abs(goalCol - col);
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (this.n != that.n) return false;
        return Arrays.deepEquals(this.board, that.board);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighbours = new Stack<Board>();
        int emptyRow = 0;
        int emptyCol = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (board[row][col] == 0) {
                    emptyRow = row;
                    emptyCol = col;
                }
            }
        }
        if (indexInBound(board, emptyRow - 1, emptyCol)) {
            Board nb1 = new Board(board);
            exch(nb1.board, emptyRow, emptyCol, emptyRow - 1, emptyCol);
            neighbours.push(nb1);
        }
        if (indexInBound(board, emptyRow + 1, emptyCol)) {
            Board nb2 = new Board(board);
            exch(nb2.board, emptyRow, emptyCol, emptyRow + 1, emptyCol);
            neighbours.push(nb2);
        }
        if (indexInBound(board, emptyRow, emptyCol - 1)) {
            Board nb3 = new Board(board);
            exch(nb3.board, emptyRow, emptyCol, emptyRow, emptyCol - 1);
            neighbours.push(nb3);
        }
        if (indexInBound(board, emptyRow, emptyCol + 1)) {
            Board nb4 = new Board(board);
            exch(nb4.board, emptyRow, emptyCol, emptyRow, emptyCol + 1);
            neighbours.push(nb4);
        }
        return neighbours;
    }

    // determines whether an index is valid for the board
    private static boolean indexInBound(int[][] board, int row, int col) {
        return (row >= 0 && row < board.length && col >= 0 && col < board.length);
    }


    // exchanges two tiles
    private static void exch(int[][] board, int row1, int col1, int row2, int col2) {
        int a = board[row1][col1];
        board[row1][col1] = board[row2][col2];
        board[row2][col2] = a;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = new Board(board);
        int emptyRow = 0;
        int emptyCol = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (board[row][col] == 0) {
                    emptyRow = row;
                    emptyCol = col;
                }
            }
        }
        int row1 = 0;
        int col1 = 0;
        if (row1 == emptyRow && col1 == emptyCol) {
            row1++;
        }
        int row2 = n - 1;
        int col2 = n - 1;
        if (row2 == emptyRow && col2 == emptyCol) {
            col2--;
        }
        exch(twin.board, row1, col1, row2, col2);
        return twin;
    }

    // unit testing
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        StdOut.println(initial);
        for (Board s : initial.neighbors()) {
            StdOut.println(s);
        }
    }
}
