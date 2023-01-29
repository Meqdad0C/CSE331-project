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
    // check if open tags matches closed tags with two pointers
    public void fixClosingTags(List<String> openTagsOnly, List<String> closedTagsOnly, List<Boolean> isOpenClose) {
        int openTagsPointer = -1;
        int closedTagsPointer = 0;
        int openClosedCounter = 0;
        while (closedTagsPointer < closedTagsOnly.size()){
            if(isOpenClose.get(openClosedCounter)){
                openTagsPointer++;
            }
            else {
                if (!openTagsOnly.get(openTagsPointer).equals(closedTagsOnly.get(closedTagsPointer))) {
                    // replace the closed tag with the open tag
                    closedTagsOnly.set(closedTagsPointer, openTagsOnly.get(openTagsPointer));
                }
                openTagsOnly.remove(openTagsPointer);
                if(openTagsPointer > 0){
                    openTagsPointer--;
                }
                closedTagsPointer++;
            }
            openClosedCounter++;
        }
    }
    // function to swap tags in all lines
    public void swapTagsInAllLines(List<String> openTagsOnly, List<String> closedTagsOnly, List<Boolean> isOpenClose, List<String> allLines, List<Integer> lineNumbers){
        for (int i = 0; i<lineNumbers.size(); i++){
            // if the tag is open
            // replace the open tag in alllines with the open tag in the openTagsOnly list
            if (isOpenClose.get(i)){
                // if the open tag is replace_me
                // replace the open tag in allLines with the closed tag in the closedTagsOnly list
                if (openTagsOnly.get(0).equals("REPLACE_ME")){
                    allLines.set(lineNumbers.get(i),closedTagsOnly.get(0));
                    closedTagsOnly.remove(0);
                }
                else {
                    allLines.set(lineNumbers.get(i),openTagsOnly.get(0));
                    openTagsOnly.remove(0);
                }

                allLines.set(lineNumbers.get(i),allLines.get(lineNumbers.get(i)).replace(reader.getTagsQueue().remove(0),"<" + openTagsOnly.get(0) + ">"));
                openTagsOnly.remove(0);
            }
            else {
                allLines.set(lineNumbers.get(i),allLines.get(lineNumbers.get(i)).replace(reader.getTagsQueue().remove(0),"</" + closedTagsOnly.get(0) + ">"));
                closedTagsOnly.remove(0);
            }
        }
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
        String fileName = "input.xml";
        Reader reader = new Reader(fileName);
        // Create a new instance of the class
        Consistency validator = new Consistency(reader);
        validator.isConsistent();

    }

}
