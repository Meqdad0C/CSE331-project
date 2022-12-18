package HuffmanEncoding;

import HuffmanEncoding.BitInputStream;
import HuffmanEncoding.CodeTree;
import HuffmanEncoding.Node;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;


public final class HuffmanDecompress {

    // Command line main application function.
    public static void main1(File file1) throws IOException {

        File inputFile =file1;
        File outputFile = new File("decompressedoutput.txt");

        // Perform file decompression
        try (BitInputStream in = new BitInputStream(new BufferedInputStream(new FileInputStream(inputFile)))) {
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile))) {
                CodeTree.CanonicalCode canonCode = readCodeLengthTable(in);
                CodeTree code = canonCode.toCodeTree();
                decompress(code, in, out);
            }
        }
    }


    static CodeTree.CanonicalCode readCodeLengthTable(BitInputStream in) throws IOException {
        int[] codeLengths = new int[257];
        for (int i = 0; i < codeLengths.length; i++) {
            // For this file format, we read 8 bits in big endian
            int val = 0;
            for (int j = 0; j < 8; j++)
                val = (val << 1) | in.readNoEof();
            codeLengths[i] = val;
        }
        return new CodeTree.CanonicalCode(codeLengths);
    }


    static void decompress(CodeTree code, BitInputStream in, OutputStream out) throws IOException {
        HuffmanDecoder dec = new HuffmanDecoder(in);
        dec.codeTree = code;
        while (true) {
            int symbol = dec.read();
            if (symbol == 256)  // EOF symbol
                break;
            out.write(symbol);
        }
    }


    public static final class HuffmanDecoder {


        // The underlying bit input stream (not null).
        private BitInputStream input;


        public CodeTree codeTree;


        public HuffmanDecoder(BitInputStream in) {
            input = Objects.requireNonNull(in);
        }


        public int read() throws IOException {
            if (codeTree == null)
                throw new NullPointerException("Code tree is null");

            Node.InternalNode currentNode = codeTree.root;
            while (true) {
                int temp = input.readNoEof();
                Node nextNode;
                if (temp == 0) nextNode = currentNode.leftChild;
                else if (temp == 1) nextNode = currentNode.rightChild;
                else throw new AssertionError("Invalid value from readNoEof()");

                if (nextNode instanceof Node.Leaf)
                    return ((Node.Leaf) nextNode).symbol;
                else if (nextNode instanceof Node.InternalNode)
                    currentNode = (Node.InternalNode) nextNode;
                else
                    throw new AssertionError("Illegal node type");
            }
        }

    }
}
