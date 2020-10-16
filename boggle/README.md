# Boggle

Boggle is a word game, where one has to find as many words using consecutive letters. The board is a 4-by-4 grid containing 16 letters, and a valid word on the board is one that is at least 3 letters, is obtained by following a sequence of *adjacent* letters, where two letters are adjacent if they are horizontal, vertical, or diagonal neighbours, and must belong to the dictionary. The score associated to a word depends on its length. The letter *Q* is replaced by *Qu*.

The file *BoggleBoard* was provided as part of the course, written by Robert Sedgewick and Kevin Wayne. We made no changes to it, but have uploaded it to allow the solvert to run. It is an immutable data type that represents a Boggle board using two-dimensional arrays.

In *BoggleSolver* we write code to find all valid words in a given Boggle board. It takes a dictionary as a command-line argument and builds a ternary search trie to represent all possible valid words. We then implement a depth-first search algorithm on the Boggle board to find all paths, checking if the corresponding words are valid (i.e. we search the ternary search trie for the word). Using a ternary search trie facilitates a critical optimization: if along the current path we are creating a prefix that is not a possible prefix of any word, then we can stop searching that path. For example, if the path is something like *THX*, then we can stop at the *X* because *THX* is not a prefix of any known word. This is the main benefit of the trie, and they are also called *prefix trees* for that reason.

See the full specification at (https://coursera.cs.princeton.edu/algs4/assignments/boggle/specification.php)[https://coursera.cs.princeton.edu/algs4/assignments/boggle/specification.php]
