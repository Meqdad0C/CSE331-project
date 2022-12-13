package HuffmanEncoding;

import HuffmanEncoding.BitOutputStream;
import HuffmanEncoding.CodeTree;
import HuffmanEncoding.FrequencyTable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;


public final class HuffmanCompress {


    public static void main(String[] args) throws IOException {

        File inputFile = new File("input.xml");
        File outputFile = new File("output.txt");

        // Read input file once to compute symbol frequencies.
        FrequencyTable freqs = getFrequencies(inputFile);
        freqs.increment(256);  // EOF symbol gets a frequency of 1
        CodeTree code = freqs.buildCodeTree();
        CodeTree.CanonicalCode canonCode = new CodeTree.CanonicalCode(code, freqs.getSymbolLimit());
        // Replace code tree with canonical one. For each symbol,
        // the code value may change but the code length stays the same.
        code = canonCode.toCodeTree();

        // Read input file again, compress with Huffman coding, and write output file
        try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile))) {
            try (BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
                writeCodeLengthTable(out, canonCode);
                compress(code, in, out);
            }
        }
    }


    // Returns a frequency table based on the bytes in the given file.
    private static FrequencyTable getFrequencies(File file) throws IOException {
        FrequencyTable freqs = new FrequencyTable(new int[257]);
        try (InputStream input = new BufferedInputStream(new FileInputStream(file))) {
            while (true) {
                int b = input.read();
                if (b == -1)
                    break;
                freqs.increment(b);
            }
        }
        return freqs;
    }


    static void writeCodeLengthTable(BitOutputStream out, CodeTree.CanonicalCode canonCode) throws IOException {
        for (int i = 0; i < canonCode.getSymbolLimit(); i++) {
            int val = canonCode.getCodeLength(i);
            if (val >= 256)
                throw new RuntimeException("The code for a symbol is too long");

            // Write value as 8 bits in big endian
            for (int j = 7; j >= 0; j--)
                out.write((val >>> j) & 1);
        }
    }


    static void compress(CodeTree code, InputStream in, BitOutputStream out) throws IOException {
        HuffmanEncoder enc = new HuffmanEncoder(out);
        enc.codeTree = code;
        while (true) {
            int b = in.read();
            if (b == -1)
                break;
            enc.write(b);
        }
        enc.write(256);  // EOF
    }


    public static final class HuffmanEncoder {


        // The underlying bit output stream (not null).
        private BitOutputStream output;
        public CodeTree codeTree;


        public HuffmanEncoder(BitOutputStream out) {
            output = Objects.requireNonNull(out);
        }


        /**
         * Encodes the specified symbol and writes to the Huffman-coded output stream.
         */
        public void write(int symbol) throws IOException {
            if (codeTree == null)
                throw new NullPointerException("Code tree is null");
            List<Integer> bits = codeTree.getCode(symbol);
            for (int b : bits)
                output.write(b);
        }

    }
}
