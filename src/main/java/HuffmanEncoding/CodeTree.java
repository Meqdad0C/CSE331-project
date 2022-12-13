package HuffmanEncoding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public final class CodeTree {


    public final Node.InternalNode root;

    // Stores the code for each symbol, or null if the symbol has no code.

    private List<List<Integer>> codes;


    public CodeTree(Node.InternalNode root, int symbolLimit) {
        this.root = Objects.requireNonNull(root);
        if (symbolLimit < 2)
            throw new IllegalArgumentException("At least 2 symbols needed");

        codes = new ArrayList<List<Integer>>();  // Initially all null
        for (int i = 0; i < symbolLimit; i++)
            codes.add(null);
        buildCodeList(root, new ArrayList<Integer>());  // Fill 'codes' with appropriate data
    }


    // Recursive helper function for the constructor
    private void buildCodeList(Node node, List<Integer> prefix) {
        if (node instanceof Node.InternalNode) {
            Node.InternalNode internalNode = (Node.InternalNode) node;

            prefix.add(0);
            buildCodeList(internalNode.leftChild, prefix);
            prefix.remove(prefix.size() - 1);

            prefix.add(1);
            buildCodeList(internalNode.rightChild, prefix);
            prefix.remove(prefix.size() - 1);

        } else if (node instanceof Node.Leaf) {
            Node.Leaf leaf = (Node.Leaf) node;
            if (leaf.symbol >= codes.size())
                throw new IllegalArgumentException("Symbol exceeds symbol limit");
            if (codes.get(leaf.symbol) != null)
                throw new IllegalArgumentException("Symbol has more than one code");
            codes.set(leaf.symbol, new ArrayList<Integer>(prefix));

        } else {
            throw new AssertionError("Illegal node type");
        }
    }


    public List<Integer> getCode(int symbol) {
        if (symbol < 0)
            throw new IllegalArgumentException("Illegal symbol");
        else if (codes.get(symbol) == null)
            throw new IllegalArgumentException("No code for given symbol");
        else
            return codes.get(symbol);
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString("", root, sb);
        return sb.toString();
    }


    private static void toString(String prefix, Node node, StringBuilder sb) {
        if (node instanceof Node.InternalNode) {
            Node.InternalNode internalNode = (Node.InternalNode) node;
            toString(prefix + "0", internalNode.leftChild, sb);
            toString(prefix + "1", internalNode.rightChild, sb);
        } else if (node instanceof Node.Leaf) {
            sb.append(String.format("Code %s: Symbol %d%n", prefix, ((Node.Leaf) node).symbol));
        } else {
            throw new AssertionError("Illegal node type");
        }
    }


    public static final class CanonicalCode {


        private int[] codeLengths;


        public CanonicalCode(int[] codeLens) {
            // Check basic validity
            Objects.requireNonNull(codeLens);
            if (codeLens.length < 2)
                throw new IllegalArgumentException("At least 2 symbols needed");
            for (int cl : codeLens) {
                if (cl < 0)
                    throw new IllegalArgumentException("Illegal code length");
            }

            // Copy once and check for tree validity
            codeLengths = codeLens.clone();
            Arrays.sort(codeLengths);
            int currentLevel = codeLengths[codeLengths.length - 1];
            int numNodesAtLevel = 0;
            for (int i = codeLengths.length - 1; i >= 0 && codeLengths[i] > 0; i--) {
                int cl = codeLengths[i];
                while (cl < currentLevel) {
                    if (numNodesAtLevel % 2 != 0)
                        throw new IllegalArgumentException("Under-full Huffman code tree");
                    numNodesAtLevel /= 2;
                    currentLevel--;
                }
                numNodesAtLevel++;
            }
            while (currentLevel > 0) {
                if (numNodesAtLevel % 2 != 0)
                    throw new IllegalArgumentException("Under-full Huffman code tree");
                numNodesAtLevel /= 2;
                currentLevel--;
            }
            if (numNodesAtLevel < 1)
                throw new IllegalArgumentException("Under-full Huffman code tree");
            if (numNodesAtLevel > 1)
                throw new IllegalArgumentException("Over-full Huffman code tree");

            // Copy again
            System.arraycopy(codeLens, 0, codeLengths, 0, codeLens.length);
        }


        public CanonicalCode(CodeTree tree, int symbolLimit) {
            Objects.requireNonNull(tree);
            if (symbolLimit < 2)
                throw new IllegalArgumentException("At least 2 symbols needed");
            codeLengths = new int[symbolLimit];
            buildCodeLengths(tree.root, 0);
        }


        // Recursive helper method for the above constructor.
        private void buildCodeLengths(Node node, int depth) {
            if (node instanceof Node.InternalNode) {
                Node.InternalNode internalNode = (Node.InternalNode) node;
                buildCodeLengths(internalNode.leftChild, depth + 1);
                buildCodeLengths(internalNode.rightChild, depth + 1);
            } else if (node instanceof Node.Leaf) {
                int symbol = ((Node.Leaf) node).symbol;
                if (symbol >= codeLengths.length)
                    throw new IllegalArgumentException("Symbol exceeds symbol limit");
                // Note: HuffmanEncoding.CodeTree already has a checked constraint that disallows a symbol in multiple leaves
                if (codeLengths[symbol] != 0)
                    throw new AssertionError("Symbol has more than one code");
                codeLengths[symbol] = depth;
            } else {
                throw new AssertionError("Illegal node type");
            }
        }


        public int getSymbolLimit() {
            return codeLengths.length;
        }


        public int getCodeLength(int symbol) {
            if (symbol < 0 || symbol >= codeLengths.length)
                throw new IllegalArgumentException("Symbol out of range");
            return codeLengths[symbol];
        }


        /**
         * Returns the canonical code tree for this canonical Huffman code.
         */
        public CodeTree toCodeTree() {
            List<Node> nodes = new ArrayList<Node>();
            for (int i = max(codeLengths); i >= 0; i--) {  // Descend through code lengths
                if (nodes.size() % 2 != 0)
                    throw new AssertionError("Violation of canonical code invariants");
                List<Node> newNodes = new ArrayList<Node>();

                // Add leaves for symbols with positive code length i
                if (i > 0) {
                    for (int j = 0; j < codeLengths.length; j++) {
                        if (codeLengths[j] == i)
                            newNodes.add(new Node.Leaf(j));
                    }
                }

                // Merge pairs of nodes from the previous deeper layer
                for (int j = 0; j < nodes.size(); j += 2)
                    newNodes.add(new Node.InternalNode(nodes.get(j), nodes.get(j + 1)));
                nodes = newNodes;
            }

            if (nodes.size() != 1)
                throw new AssertionError("Violation of canonical code invariants");
            return new CodeTree((Node.InternalNode) nodes.get(0), codeLengths.length);
        }


        // Returns the maximum value in the given array, which must have at least 1 element.
        private static int max(int[] array) {
            int result = array[0];
            for (int x : array)
                result = Math.max(x, result);
            return result;
        }

    }
}
