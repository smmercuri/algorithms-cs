# Kd Trees

Given a set of points in the 2D plane, the task here is to implement range search (find the points that lie within a given search rectange) and nearest neighbour search (find a closest point to a given search point). 

We make use of *Point2D* and *RectHV* data types (part of algs4.jar library) to represent points on the plane and axis-aligned plane rectangles respectively.

In *PointSET* a brute-force implementation of range search and nearest neighbour are given by using a red-black BST to represent the points and simply searching through all the points in range search and nearest neighbour search.

In *KdTree* we implement a 2d tree to provide an efficient algorithm for range search and nearest neighbour search. This uses each point to recursively split up the plane into two half-planes, and represents the points and half-planes in a tree structure. Thus to search for a particular point, we only need to search the half-planes that that point lies in. In particular, for range search, we can easily avoid searching half-planes that do not intersect with the query rectangle. Likewise, for nearest neighbour search we can exclude certain half-planes if points in them are guaranteed to be further away from the query point than points already checked.

See the full specification at [https://coursera.cs.princeton.edu/algs4/assignments/kdtree/specification.php](https://coursera.cs.princeton.edu/algs4/assignments/kdtree/specification.php)
