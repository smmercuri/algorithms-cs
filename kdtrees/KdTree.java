/* *****************************************************************************
 *  Name: Salvatore Mercuri
 *  Date: June 8 2020
 *  Description: Implements a 2D tree to perform an efficient range search and
 *               nearest neighbour search for points in the 2D plane.
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class KdTree {
    private static class Node {
        private Point2D p; // point
        private RectHV rect; // axis-aligned rectangle corrsponding to this node
        private Node lb; // left/bottom subtree (depends on alternating orientation)
        private Node rt; // right/top subtree
        private int count;
    }

    private Node root;
    private final ArrayList<Point2D> rangeList;

    // construct an empty set of points
    public KdTree() {
        this.rangeList = new ArrayList<Point2D>();
        this.root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    // size helper
    private int size(Node x) {
        if (x == null) return 0;
        return x.count;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        int orientation = 0;
        root = insert(root, p, orientation % 2);
    }

    // recursive routine for insert
    private Node insert(Node x, Point2D p, int orientation) {
        if (x == null) {
            Node y = new Node();
            y.p = p;
            y.rect = new RectHV(0, 0, 1, 1);
            y.count = 1;
            return y;
        }
        double cmp;
        double cmpx = p.x() - x.p.x();
        double cmpy = p.y() - x.p.y();
        RectHV lbRect;
        RectHV rtRect;
        if (orientation == 0) {
            cmp = cmpx;
            lbRect = new RectHV(0, x.rect.ymin(), x.p.x(), x.rect.ymax());
            rtRect = new RectHV(x.p.x(), x.rect.ymin(), 1, x.rect.ymax());
        }
        else {
            cmp = cmpy;
            lbRect = new RectHV(x.rect.xmin(), 0, x.rect.xmax(), x.p.y());
            rtRect = new RectHV(x.rect.xmin(), x.p.y(), x.rect.xmax(), 1);
        }
        if (cmp < 0) {
            orientation++;
            x.lb = insert(x.lb, p, orientation % 2);
            x.lb.rect = lbRect;
        }
        else if (cmpx != 0 || cmpy != 0) {
            orientation++;
            x.rt = insert(x.rt, p, orientation % 2);
            x.rt.rect = rtRect;
        }
        x.count = 1 + size(x.lb) + size(x.rt);
        return x;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return get(p) != null;
    }

    // helper function for contain
    private Point2D get(Point2D p) {
        int orientation = 0;
        return get(root, p, orientation);
    }

    // recursive routing for get
    private Point2D get(Node x, Point2D p, int orientation) {
        if (x == null) return null;
        if (x.p.equals(p)) return p;
        double cmp;
        double cmpx = p.x() - x.p.x();
        double cmpy = p.y() - x.p.y();
        if (orientation == 0) cmp = cmpx;
        else cmp = cmpy;
        if (cmp < 0) {
            orientation++;
            return get(x.lb, p, orientation % 2);
        }
        else {
            orientation++;
            return get(x.rt, p, orientation % 2);
        }

    }

    // draw all points to standard draw
    public void draw() {
        int orientation = 0;
        draw(root, orientation);
    }

    private void draw(Node x, int orientation) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.p.draw();
        StdDraw.setPenRadius();
        if (orientation % 2 == 0) StdDraw.setPenColor(StdDraw.RED);
        else StdDraw.setPenColor(StdDraw.BLUE);
        x.rect.draw();
        if (x.lb != null) {
            orientation++;
            draw(x.lb, orientation);
        }
        if (x.rt != null) {
            orientation++;
            draw(x.rt, orientation);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        range(root, rect);
        return rangeList;
    }

    // recursive helper function for range
    private void range(Node x, RectHV rect) {
        if (rect.contains(x.p)) rangeList.add(x.p);
        if (x.lb != null && rect.intersects(x.lb.rect)) range(x.lb, rect);
        if (x.rt != null && rect.intersects(x.rt.rect)) range(x.rt, rect);
    }


    // a nearest neighbour in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        int orientation = 0;
        return nearest(root, p, root.p, orientation);
    }

    // recursive helper function for nearest neighbour
    private Point2D nearest(Node x, Point2D p, Point2D minPt, int orientation) {
        if (x == null) return minPt;
        if (x.p.distanceSquaredTo(p) < minPt.distanceSquaredTo(p)) {
            minPt = x.p;
        }
        double cmp;
        if (orientation == 0) cmp = p.x() - x.p.x();
        else cmp = p.y() - x.p.y();
        orientation++;
        if (cmp < 0) {
            if (x.lb != null && (minPt.distanceSquaredTo(p) >= x.lb.rect.distanceSquaredTo(p))) {
                minPt = nearest(x.lb, p, minPt, orientation % 2);
            }
            if (x.rt != null && (minPt.distanceSquaredTo(p) >= x.rt.rect.distanceSquaredTo(p))) {
                minPt = nearest(x.rt, p, minPt, orientation);
            }
        }
        else {
            if (x.rt != null && (minPt.distanceSquaredTo(p) >= x.rt.rect.distanceSquaredTo(p))) {
                minPt = nearest(x.rt, p, minPt, orientation);
            }
            if (x.lb != null && (minPt.distanceSquaredTo(p) >= x.lb.rect.distanceSquaredTo(p))) {
                minPt = nearest(x.lb, p, minPt, orientation % 2);

            }
        }
        return minPt;
    }

    // unit testing
    public static void main(String[] args) {
        KdTree kd = new KdTree();
        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            Point2D p = new Point2D(x, y);
            kd.insert(p);
        }
        Point2D q = new Point2D(0.606, 0.23);
        StdOut.println(kd.nearest(q));
    }
}

