/* *****************************************************************************
 *  Name: Salvatore Mercuri
 *  Date: June 22 2020
 *  Description: Finds the SAP (shortest ancestral path) between two vertices
 *               in a digraph. Also does this between two sets of
 *               vertices in a digraph.
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class SAP {
    private final Digraph G;
    private final int N;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.G = new Digraph(G);
        this.N = G.V();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= N || w < 0 || w >= N) throw new IllegalArgumentException();
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
        ArrayList<Integer> lengths = new ArrayList<Integer>();
        for (int u = 0; u < G.V(); u++) {
            if (bfsv.hasPathTo(u) && bfsw.hasPathTo(u)) {
                int length = bfsv.distTo(u) + bfsw.distTo(u);
                int n = lengths.size();
                if (n == 0) lengths.add(length);
                else if (length < lengths.get(n - 1)) lengths.add(length);
            }
        }
        if (lengths.isEmpty()) return -1;
        return lengths.get(lengths.size() - 1);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= N || w < 0 || w >= N) throw new IllegalArgumentException();
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
        ArrayList<Integer> lengths = new ArrayList<Integer>();
        int ancestor = -1;
        for (int u = 0; u < G.V(); u++) {
            if (bfsv.hasPathTo(u) && bfsw.hasPathTo(u)) {
                int length = bfsv.distTo(u) + bfsw.distTo(u);
                int n = lengths.size();
                if (n == 0) {
                    lengths.add(length);
                    ancestor = u;
                }
                else if (length < lengths.get(n - 1)) {
                    lengths.add(length);
                    ancestor = u;
                }
            }
        }
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
        ArrayList<Integer> lengths = new ArrayList<Integer>();
        for (int u = 0; u < G.V(); u++) {
            if (bfsv.hasPathTo(u) && bfsw.hasPathTo(u)) {
                int length = bfsv.distTo(u) + bfsw.distTo(u);
                int n = lengths.size();
                if (lengths.isEmpty()) lengths.add(length);
                else if (length < lengths.get(n - 1)) lengths.add(length);
            }
        }
        if (lengths.isEmpty()) return -1;
        return lengths.get(lengths.size() - 1);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
        ArrayList<Integer> lengths = new ArrayList<Integer>();
        int ancestor = -1;
        for (int u = 0; u < G.V(); u++) {
            if (bfsv.hasPathTo(u) && bfsw.hasPathTo(u)) {
                int length = bfsv.distTo(u) + bfsw.distTo(u);
                int n = lengths.size();
                if (n == 0) {
                    lengths.add(length);
                    ancestor = u;
                }
                else if (length < lengths.get(n - 1)) {
                    lengths.add(length);
                    ancestor = u;
                }
            }
        }
        return ancestor;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
