package HuffmanEncoding;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;


public final class BitOutputStream implements AutoCloseable {


    // The underlying byte stream to write to (not null).
    private OutputStream output;

    // The accumulated bits for the current byte, always in the range [0x00, 0xFF].
    private int currentByte;

    // Number of accumulated bits in the current byte, always between 0 and 7 (inclusive).
    private int numBitsFilled;


    public BitOutputStream(OutputStream out) {
        output = Objects.requireNonNull(out);
        currentByte = 0;
        numBitsFilled = 0;
    }


    public void write(int b) throws IOException {
        if (b != 0 && b != 1)
            throw new IllegalArgumentException("Argument must be 0 or 1");
        currentByte = (currentByte << 1) | b;
        numBitsFilled++;
        if (numBitsFilled == 8) {
            output.write(currentByte);
            currentByte = 0;
            numBitsFilled = 0;
        }
    }


    public void close() throws IOException {
        while (numBitsFilled != 0)
            write(0);
        output.close();
    }

}
