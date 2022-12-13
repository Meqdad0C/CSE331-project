package main;

import java.util.List;
import java.util.Stack;

public class Consistency {
    private Reader reader;


    public Consistency(Reader reader) {
        this.reader = reader;
    }

    public void isConsistent() {
        Stack<String> tagsStack = reader.getTagsStack();
        List<String> tagsQueue = reader.getTagsQueue();
        List<Boolean> isOpenClose = reader.getIsOpenClose();
        List<String> tagNames = reader.getTagNames();
        List<List<Integer>> tagsIndex = reader.getTagsIndex();
        List<List<Integer>> openTagsIndices = reader.getOpenTagsIndices();
        List<List<Integer>> closedTagsIndices = reader.getClosedTagsIndices();
        List<Integer> lineNumbers = reader.getLineNumbers();
        List<String> openTagsOnly = reader.getOpenTagsOnly();
        List<String> closedTagsOnly = reader.getClosedTagsOnly();

        // check the first open tag is the same as the last closed tag


    }


    // Main method
    public static void main(String[] args) {
        String fileName = "test.xml";
        Reader reader = new Reader(fileName);
        // Create a new instance of the class
        Consistency validator = new Consistency(reader);
        validator.isConsistent();

    }
}
