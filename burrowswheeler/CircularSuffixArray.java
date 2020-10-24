/* *****************************************************************************
 *  Name: Salvatore Mercuri
 *  Date: July 20 2020
 *  Description: Circular suffix array data type to be used in Burrows-Wheeler
 *               transform. Takes a sequence of characters and shifts one at
 *               a time modulo the character length. Sort and index methods
 *               to support Burrows-Wheeler compression and expansion.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;

public class CircularSuffixArray {
    private final int n; // length of string
    private final ArrayList<CircularSuffix> csArray; // array of circular suffices


    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        this.n = s.length();
        csArray = new ArrayList<CircularSuffix>();
        for (int i = 0; i < n; i++) {
            CircularSuffix cs = new CircularSuffix();
            cs.s = s;
            cs.charIndex = i;
            csArray.add(cs);
        }
        Collections.sort(csArray);
    }

    private static class CircularSuffix implements Comparable<CircularSuffix> {
        private String s;
        private int charIndex;

        public int compareTo(CircularSuffix that) {
            if (this.equals(that)) return 0;
            int thisIndex = this.charIndex;
            int thatIndex = that.charIndex;
            while (s.charAt(thisIndex) == s.charAt(thatIndex)) {
                thisIndex = (thisIndex + 1) % s.length();
                thatIndex = (thatIndex + 1) % s.length();
            }
            return (s.charAt(thisIndex) - s.charAt(thatIndex));
        }

    }


    // length of s
    public int length() {
        return n;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > n - 1) throw new IllegalArgumentException();
        return csArray.get(i).charIndex;
    }

    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray cs = new CircularSuffixArray(s);
        StdOut.println(cs.length());
        for (int i = 0; i < cs.length(); i++) {
            StdOut.println(cs.index(i));
        }
    }
}
