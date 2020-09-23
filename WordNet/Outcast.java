/* *****************************************************************************
 *  Name: Salvatore Mercuri
 *  Date: June 22 2020
 *  Description: Outcast detection in WordNet digraph. Given a list of words
 *               this will return the word that is least related to the others.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of wordnet nouns, return an outcast
    public String outcast(String[] nouns) {
        String outcast = nouns[0];
        int dist = 0;
        for (String s : nouns) {
            int d = 0;
            for (String t : nouns) {
                d += wordnet.distance(s, t);
            }
            if (d > dist) {
                outcast = s;
                dist = d;
            }
        }
        return outcast;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
