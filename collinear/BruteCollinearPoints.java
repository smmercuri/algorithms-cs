/* *****************************************************************************
 *  Name: Salvatore Mercuri
 *  Date: May 25 2020
 *  Description: Implement a brute-force algorithm for finding all subsets of 4
 *               or more collinear points. This checks all possible 4-tuples.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private int numberOfSegments;
    private LineSegment[] segmentArr;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        else if (hasNull(points)) throw new IllegalArgumentException();
        else if (repeated(points)) throw new IllegalArgumentException();
        int n = points.length;
        segmentArr = new LineSegment[n];
        Arrays.sort(points);
        for (int i = 0; i < n; i++) {
            Point p = points[i];
            for (int j = i + 1; j < n; j++) {
                Point q = points[j];
                for (int k = j + 1; k < n; k++) {
                    Point r = points[k];
                    if (p.slopeTo(q) == p.slopeTo(r)) {
                        for (int ell = k + 1; ell < n; ell++) {
                            Point s = points[ell];
                            if (p.slopeTo(q) == p.slopeTo(s)) {
                                LineSegment segment = new LineSegment(p, s);
                                segmentArr[numberOfSegments++] = segment;
                            }
                        }
                    }
                }
            }

        }
    }

    private boolean hasNull(Point[] points) {
        boolean hasNull = false;
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                hasNull = true;
                break;
            }
        }
        return hasNull;
    }

    private boolean repeated(Point[] points) {
        boolean repeated = false;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    repeated = true;
                    break;
                }
            }
        }
        return repeated;
    }

    public int numberOfSegments() {
        return numberOfSegments;
    }

    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[numberOfSegments];
        for (int i = 0; i < numberOfSegments; i++) {
            segments[i] = segmentArr[i];
        }
        return segments;
    }

    public static void main(String[] args) {
        /** Point[] points = new Point[7];
         int i = 0;
         while (!StdIn.isEmpty()) {
         int x = StdIn.readInt();
         int y = StdIn.readInt();
         points[i] = new Point(x, y);
         i++;
         }
         */
        /**
         BruteCollinearPoints bcp = new BruteCollinearPoints(points);
         StdOut.println(bcp.numberOfSegments());
         StdOut.println(bcp.segments()[0]);
         StdOut.println(bcp.segments()[1]);
         */

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
