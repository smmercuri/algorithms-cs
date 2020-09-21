# Queues

A queue is a standard data structure representing a collection of objects with a FIFO (first in first out) removal system, i.e. when we remove an item we remove the item that was *least* recently added. A stack, on the other hand, represents a collection of objects with a LIFO (last in first out) removal system which removes the item *most* recently added.

In *Deque* we generalise these objects to produce a Deque data structure, which represents a collection of objects in which we may remove either the least recently or most recently added item. 

We also implement a Randomized Queue in *RandomizedQueue* where the item removed is selected uniformly at random from the collection of objects.

The client code, *Permutation* takes a command-line argument *k*, an integer, and reads in strings from standard input. It then prints out *k* of these strings, selected at most once and uniformly at random.

See the full specification at [https://coursera.cs.princeton.edu/algs4/assignments/queues/specification.php](https://coursera.cs.princeton.edu/algs4/assignments/queues/specification.php)
