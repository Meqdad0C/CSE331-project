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
        // clone openTagsOnly
        List<String> openTagsOnlyClone = reader.clone().getOpenTagsOnly();
        List<String> closedTagsOnly = reader.getClosedTagsOnly();
        List<String> allLines = reader.getAllLines();

        // check general consistency
        System.out.println("Checking general consistency...");
        checkTagsBalance(openTagsOnly,closedTagsOnly);
        System.out.println(checkXmlTagConsistency(tagNames,isOpenClose));
        fixClosingTags(openTagsOnly,closedTagsOnly,isOpenClose);
        System.out.println("Checking general consistency... Done");
        System.out.println("Open Tags After Fixing " + openTagsOnly);
        System.out.println("Closed Tags After Fixing" + closedTagsOnly);
        swapTagsInAllLines(openTagsOnlyClone,closedTagsOnly,isOpenClose,allLines,lineNumbers);

        // print all lines
        for (String line : allLines) {
            System.out.println(line);
        }
    }
    public void checkTagsBalance(List<String> openTagsOnly, List<String> closedTagsOnly) {
        if (openTagsOnly.size() == closedTagsOnly.size()){
            return;
        }
        if (openTagsOnly.size() > closedTagsOnly.size()){
            System.out.println("There are more open tags than closed tags");
            // append the missing closed tags
            for (int i = 0; i < openTagsOnly.size() - closedTagsOnly.size(); i++) {
                closedTagsOnly.add("REPLACE_ME");
                reader.getIsOpenClose().add(false);
                reader.getAllLines().add("</REPLACE_ME>");
                reader.getTagsQueue().add("</REPLACE_ME>");
                reader.getLineNumbers().add(reader.getAllLines().size()-1);

            }
        }
        else {
            System.out.println("There are more closed tags than open tags");
            // append the missing open tags
            for (int i = 0; i < closedTagsOnly.size() - openTagsOnly.size(); i++) {
                openTagsOnly.add("REPLACE_ME");
                reader.getIsOpenClose().add(true);
                reader.getAllLines().add("<REPLACE_ME>");
                reader.getTagsQueue().add("<REPLACE_ME>");
                reader.getLineNumbers().add(reader.getAllLines().size()-1);
            }
        }
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
