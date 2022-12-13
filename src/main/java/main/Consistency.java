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
        List<String> allLines = reader.getAllLines();

        // check general consistency
        System.out.println("Checking general consistency...");
        System.out.println(checkXmlTagConsistency(tagNames,isOpenClose));


    }

    public boolean checkXmlTagConsistency(List<String> tagNames, List<Boolean> isOpenClose) {
        Stack<String> tagsStack = new Stack<>();

        for (int i = 0; i < tagNames.size(); i++) {
            String tag = tagNames.get(i);
            boolean isOpenTag = isOpenClose.get(i);

            if (isOpenTag) {
                // Push open tag onto stack
                tagsStack.push(tag);
            } else {
                // Check if the top element on the stack is the matching open tag for this closed tag
                String openTag = tagsStack.pop();
                if (!openTag.equals(tag)) {
                    // Open and closed tags do not match, so XML tags are not consistent
                    return false;
                }
            }
        }

        // Check if the stack is empty, which indicates that all open tags have been closed
        if (!tagsStack.isEmpty()) {
            // Stack is not empty, so there are unclosed open tags, which means XML tags are not consistent
            return false;
        }

        // If we reach this point, then all open tags have been matched with closed tags, so XML tags are consistent
        return true;
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
