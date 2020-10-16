/* *****************************************************************************
 *  Name: Salvatore Mercuri
 *  Date: July 7 2020
 *  Description: Takes a dictionary as input file to represent all possible
 *               words. This is represented as a ternary search trie. We then
 *               try to find all possible words on the Boggle board using a
 *               depth-first search algorithm, checking if the word encountered
 *               belongs in the dictionary TST.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

import java.util.ArrayList;

public class BoggleSolver {
    private final TST<Integer> dictionaryTrie;

    // Initializes the data structure using the given array of strings as the dictionary.
    public BoggleSolver(String[] dictionary) {
        dictionaryTrie = new TST<Integer>();
        for (String word : dictionary) {
            int value;
            if (word.length() == 3 || word.length() == 4) value = 1;
            else if (word.length() == 5) value = 2;
            else if (word.length() == 6) value = 3;
            else if (word.length() == 7) value = 5;
            else value = 11;
            dictionaryTrie.put(word, value);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        // depth first search on boggle board
        SET<String> validWords = new SET<String>();
        int rows = board.rows();
        int cols = board.cols();
        for (int vertexRow = 0; vertexRow < rows; vertexRow++) {
            for (int vertexCol = 0; vertexCol < cols; vertexCol++) {
                boolean[][] marked = new boolean[rows][cols];
                String word = "";
                dfs(board, vertexRow, vertexCol, marked, word, validWords);
            }
        }
        return validWords;
    }

    // recursive depth-first-search based algorithm
    private void dfs(BoggleBoard board, int vertexRow, int vertexCol,
                     boolean[][] marked, String word, SET<String> validWords) {
        marked[vertexRow][vertexCol] = true;
        char letter = board.getLetter(vertexRow, vertexCol);
        if (letter == 'Q') word += "QU";
        else word += letter;
        if (isValidWord(word)) validWords.add(word);
        for (Point2D index : adjacentIndices(board, vertexRow, vertexCol)) {
            int x = (int) index.x(), y = (int) index.y();
            if (!marked[x][y]) {
                int i = 0;
                for (String key : dictionaryTrie.keysWithPrefix(word + board.getLetter(x, y))) i++;
                if (i != 0) dfs(board, x, y, marked, word, validWords);
            }
        }
        marked[vertexRow][vertexCol] = false;

    }

    // returns iterable with adjacent letters on boggle board
    private Iterable<Point2D> adjacentIndices(BoggleBoard board, int vertexRow,
                                              int vertexCol) {
        ArrayList<Point2D> adj = new ArrayList<Point2D>();
        int rows = board.rows();
        int cols = board.cols();
        for (int i = vertexRow - 1; i < vertexRow + 2; i++) {
            for (int j = vertexCol - 1; j < vertexCol + 2; j++) {
                Point2D pt = new Point2D(i, j);
                if (isValidVertex(board, vertexRow, vertexCol, pt)) adj.add(pt);
            }
        }
        return adj;
    }

    // checks if Point2D pt is distinct from (VertexRow, vertexCol) and actually lies on board
    private boolean isValidVertex(BoggleBoard board, int vertexRow, int vertexCol, Point2D pt) {
        Point2D originalPt = new Point2D(vertexRow, vertexCol);
        if (pt.equals(originalPt)) return false;
        int rows = board.rows();
        int cols = board.cols();
        if (pt.x() < 0 || pt.x() > rows - 1 || pt.y() < 0 || pt.y() > cols - 1) return false;
        return true;
    }

    // checks if a word is a valid boggle word
    private boolean isValidWord(String word) {
        return (word.length() > 2 && dictionaryTrie.contains(word));
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    public int scoreOf(String word) {
        return dictionaryTrie.get(word);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
