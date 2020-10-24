/* *****************************************************************************
 *  Name: Salvatore Mercuri
 *  Date: July 20 2020
 *  Description: Move-to-front encoding. Takes a file in which sequences of the
 *               same character occurring near each other many times and creates
 *               an encoding where some characters occur much more frequently than
 *               others.
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] ASCII = new char[256];
        for (int i = 0; i < 256; i++) {
            ASCII[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar(8);
            int index = findIndex(ASCII, c);
            BinaryStdOut.write(index, 8);
            moveToFront(ASCII, c, index);
        }
        BinaryStdOut.close();
    }

    private static int findIndex(char[] a, char c) {
        int index = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] == c) {
                index = i;
                break;
            }
        }
        return index;
    }

    private static void moveToFront(char[] a, char c, int index) {
        for (int i = index - 1; i >= 0; i--) {
            a[i + 1] = a[i];
        }
        a[0] = c;
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] ASCII = new char[256];
        for (int i = 0; i < 256; i++) {
            ASCII[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            int i = BinaryStdIn.readInt(8);
            char c = ASCII[i];
            BinaryStdOut.write(c, 8);
            moveToFront(ASCII, c, i);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }
}
