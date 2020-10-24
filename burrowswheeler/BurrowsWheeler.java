/* *****************************************************************************
 *  Name: Salvatore Mercuri
 *  Date: July 20 2020
 *  Description: Implements Burrows-Wheeler transform, a key step in
 *               Burrows-Wheeler compression. Does this by taking the
 *               circular suffix array, sorting the circular suffices, and
 *               taking the last character of each circular suffix. Includes
 *               inverse transform to support Burrows-Wheeler expansion.
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray cs = new CircularSuffixArray(s);
        int n = cs.length();
        int originalIndex = 0;
        for (int i = 0; i < n; i++) {
            if (cs.index(i) == 0) originalIndex = i;
        }
        BinaryStdOut.write(originalIndex);
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(s.charAt((cs.index(i) + n - 1) % n));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform.
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        int N = t.length();

        // construct the next array with Key-indexed counting
        int[] count = new int[257], next = new int[N];
        for (int i = 0; i < N; i++)
            count[t.charAt(i) + 1]++;

        for (int r = 0; r < 256; r++)
            count[r + 1] += count[r];

        for (int i = 0; i < N; i++)
            next[count[t.charAt(i)]++] = i;

        // decode message
        for (int i = next[first], c = 0; c < N; i = next[i], c++)
            BinaryStdOut.write(t.charAt(i));
        BinaryStdOut.close();
    }

    // returns all occurrences of a character in an ArrayList of characters
    private static Queue<Integer> getAllIndices(char[] t, char c) {
        Queue<Integer> allIndices = new Queue<Integer>();
        for (int i = 0; i < t.length; i++) {
            if (t[i] == c) allIndices.enqueue(i);
        }
        return allIndices;
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        if (args[0].equals("+")) inverseTransform();
    }
}
