package HuffmanEncoding;

import java.util.Objects;


public abstract class Node {

    Node() {
    }

    /**
     * An internal node in a code tree. It has two nodes as children.
     */
    public static final class InternalNode extends Node {

        public final Node leftChild;

        public final Node rightChild;


        public InternalNode(Node left, Node right) {
            leftChild = Objects.requireNonNull(left);
            rightChild = Objects.requireNonNull(right);
        }

    }

    /**
     * A leaf node in a code tree. It has a symbol value.
     */
    public static final class Leaf extends Node {

        public final int symbol;


        public Leaf(int sym) {
            if (sym < 0)
                throw new IllegalArgumentException("Symbol value must be non-negative");
            symbol = sym;
        }

    }
}
