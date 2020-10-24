# Burrows-Wheeler

Burrows-Wheeler is a compression algorithm that performs well and is fairly easy to implement. Its implementation comes in three stages. 
1. Burrows-Wheeler transform: this takes a typical English text file, and transforms it do that sequences of the same character occur near each other many times.
2. Move-to-front encoding: takes a text file in which sequences of the same character occur near each other many times (i.e. the output of the Burrows-Wheeler transform) and converts it into a file in which some characters occur much more frequently than others.
3. Huffman compression: apply Huffman compression to the output of move-to-front encoding. Huffman compression uses variable-length prefix-free code with shorter codes for characters that occur more frequently, and longer codes for characters that occur less frequently. It is effective on files in which some characters are much more frequent than others, hence it works well on the output of move-to-front encoding.

In *CircularSuffixArray.java* we implement the fundamental data structure known as circular suffix arrays, which will be used to implement the Burrows-Wheeler transform. This puts an array and its circular suffices (i.e. its characters shifted along the array modulo the array length) into a single data structure.

In *BurrowsWheeler.java* we implement the Burrows-Wheeler transform by using circular suffix arrays as follows. We sort all of the circular suffices, and then the Burrows-Wheeler transform is obtained by taking the last character of each circular suffix in the sorted order. For example, the Burrows-Wheeler transform applied to ABRACADABRA! is: ARD!RCAAAABB. We also implement the inverse transform, to allow for expansion of compressed code.

In *MoveToFront.java* we implement the move-to-front encoding. To do this, we keep an array of size 256 corresponding to the ASCII characters. Then we read the input one character at a time. We output the index of the ASCII array in which that character occurs, and then move the character to the front of the ASCII array. In a text file with repeated characters near each other, this will output many small and repeated indices. We also encode a decoding method to support compression expansion.

See the full specification at (https://coursera.cs.princeton.edu/algs4/assignments/burrows/specification.php)[https://coursera.cs.princeton.edu/algs4/assignments/burrows/specification.php]
