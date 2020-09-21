/* *****************************************************************************
 *  Name: Salvatore Mercuri
 *  Date: May 14th 2020
 *  Description: Implements a Randomized Queue, a collection of objects in which
 *               items are removed uniformly at random.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q; // queue elements
    private int n; // number of elements on queue
    private int first; // index of first element of queue
    private int last; // index of next available slot


    // construct an empty randomized queue
    public RandomizedQueue() {
        q = (Item[]) new Object[2];
        n = 0;
        first = 0;
        last = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // resize the array
    private void resize(int capacity) {
        assert capacity >= n;
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = q[(first + i) % q.length];
        }
        q = copy;
        first = 0;
        last = n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (n == q.length) resize(2 * q.length);
        q[last++] = item;
        if (last == q.length) last = 0;
        n++;
    }


    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int r = StdRandom.uniform(n);
        Item item = q[(first + r) % q.length];
        q[(first + r) % q.length] = q[first];
        q[first] = null;
        n--;
        first++;
        if (n > 0 && n == q.length / 4) resize(q.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int r = StdRandom.uniform(n);
        Item item = q[(first + r) % q.length];
        return item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private int i;


        public boolean hasNext() {
            return i < n;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            int r = StdRandom.uniform(n);
            Item swap = q[(first + r) % q.length];
            q[(first + r) % q.length] = q[first];
            q[first] = swap;
            i++;
            return q[first];
        }

    }

    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        StdOut.println(rq.isEmpty());
        StdOut.println(rq.size());
        rq.enqueue("a");
        rq.enqueue("b");
        rq.enqueue("c");
        rq.enqueue("d");
        StdOut.println(rq.isEmpty());
        StdOut.println(rq.size());
        StdOut.println(rq.dequeue());
        StdOut.println(rq.size());
        StdOut.println(rq.sample());
        StdOut.println(rq.size());
        for (String s : rq) {
            StdOut.println(s);
        }
    }
}
