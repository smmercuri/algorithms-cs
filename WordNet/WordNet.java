/* *****************************************************************************
 *  Name: Salvatore Mercuri
 *  Date: June 22 2020
 *  Description: Creates a digraph based on the WordNet lexicon. Vertices are
 *               synsets and edges are defined by the hypernym relation, i.e.
 *               v -> w means w is a hypernym of v. Various methods for the
 *               WordNet digraph are implemented, most notable sap, which gives
 *               the shortest ancestral path between two WordNet nouns.
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Topological;

import java.util.ArrayList;

public class WordNet {
    private final ST<String, Integer> synsetNumbers;
    private final ST<String, ArrayList<Integer>> nouns;
    private final Digraph G;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        synsetNumbers = new ST<String, Integer>();
        nouns = new ST<String, ArrayList<Integer>>();
        readSynsets(synsets);
        int V = findMax(synsetNumbers) + 1;
        G = new Digraph(V);
        readHypernyms(hypernyms);
        Topological top = new Topological(G);
        if (!top.hasOrder()) throw new IllegalArgumentException();
        sap = new SAP(G);
    }

    // helper function to read in the synsets.txt file
    private void readSynsets(String synsets) {
        In synsetsFile = new In(synsets);
        while (synsetsFile.hasNextLine()) {
            String synset = synsetsFile.readLine();
            int synsetNumber = Integer.parseInt(synset.split(",")[0]);
            String synsetName = synset.split(",")[1];
            synsetNumbers.put(synsetName, synsetNumber);
            for (String noun : synsetName.split(" ")) {
                ArrayList<Integer> synsetList;
                if (nouns.get(noun) == null) synsetList = new ArrayList<Integer>();
                else synsetList = nouns.get(noun);
                synsetList.add(synsetNumbers.get(synsetName));
                nouns.put(noun, synsetList);
            }
        }
    }

    // helper function to read in the hypernyms.txt file
    private void readHypernyms(String hypernyms) {
        In hypernymsFile = new In(hypernyms);
        while (hypernymsFile.hasNextLine()) {
            String hypernym = hypernymsFile.readLine();
            String[] hypernymSplit = hypernym.split(",");
            for (int i = 1; i < hypernymSplit.length; i++) {
                G.addEdge(Integer.parseInt(hypernymSplit[0]), Integer.parseInt(hypernymSplit[i]));
            }
        }
    }

    // finds maximum synset number
    private int findMax(ST<String, Integer> st) {
        int max = 0;
        for (String s : st.keys()) {
            if (st.get(s) > max) max = st.get(s);
        }
        return max;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nouns.contains(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        return sap.length(nouns.get(nounA), nouns.get(nounB));
    }

    // a synset that is the common ancestor of nounA and nounB
    // in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        int ancestorId = sap.ancestor(nouns.get(nounA), nouns.get(nounB));
        return getWord(synsetNumbers, ancestorId);
    }


    // finds word associated to synsetId in the symbol table
    private String getWord(ST<String, Integer> st, int id) {
        if (id < 0 || id > findMax(st)) throw new IllegalArgumentException();
        String word = "";
        for (String s : st.keys()) {
            if (st.get(s) == id) {
                word = s;
                break;
            }
        }
        return word;
    }

    public static void main(String[] args) {

    }
}
