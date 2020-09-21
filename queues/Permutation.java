/* *****************************************************************************
 *  Name: Salvastore Mercuri
 *  Date: May 14th 2020
 *  Description: Test client for RandomizedQueue. Takes a command-line argument
 *               k and reads strings from standard input. It prints out exactly
 *               k of them, chosen uniformly at random and at most once.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            rq.enqueue(s);
        }

        for (int i = 0; i < k; i++) {
            StdOut.println(rq.dequeue());
        }
    }
}
