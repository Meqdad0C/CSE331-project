package main;

import java.util.ArrayList;
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
        List<String> closedTagsOnly = reader.getClosedTagsOnly();
        List<String> allLines = reader.getAllLines();

        // check general consistency
        System.out.println("Checking general consistency...");
        checkTagsBalance(openTagsOnly,closedTagsOnly, allLines);

        fixClosingTags(openTagsOnly,closedTagsOnly,isOpenClose);

        swapTagsInAllLines(openTagsOnly,closedTagsOnly,isOpenClose,allLines,lineNumbers);

        // print all lines
        for (String line : allLines) {
            System.out.println(line);
        }
    }
    public void checkTagsBalance(List<String> openTagsOnly, List<String> closedTagsOnly, List<String> allLines) {
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
            // compare openTagsOnly and closedTagsOnly and make new list of the missing open tag
            List<String> OpenTagsClone = reader.clone().getOpenTagsOnly();
            List<String> ClosedTagsClone = reader.clone().getClosedTagsOnly();
            List<String> missingOpenTags = new ArrayList<>();


            // closedTags LineNumbers
            List<Integer> closedTagsLineNumbers = new ArrayList<>();
            for (int i = 0; i < reader.getIsOpenClose().size(); i++) {
                if (!reader.getIsOpenClose().get(i)){
                    closedTagsLineNumbers.add(reader.getLineNumbers().get(i));
                }
            }

            // openTags LineNumbers
            List<Integer> openTagsLineNumbers = new ArrayList<>();
            for (int i = 0; i < reader.getIsOpenClose().size(); i++) {
                if (reader.getIsOpenClose().get(i)){
                    openTagsLineNumbers.add(reader.getLineNumbers().get(i));
                }
            }

            for (int i = 0; i < ClosedTagsClone.size(); i++) {
                String closedTag = ClosedTagsClone.get(i);
                if (!OpenTagsClone.remove(closedTag)) {
                    missingOpenTags.add(closedTag);
                }
            }

            for (int i = 0; i < missingOpenTags.size(); i++) {
                addOpenTag(reader.getIsOpenClose(),reader.getOpenTagsOnly(),reader.getClosedTagsOnly(),closedTagsLineNumbers, allLines);
            }

            System.out.println("dada");

        }
    }

    private void addOpenTag(List<Boolean> isOpenClose, List<String> openTagsOnly, List<String> closedTagsOnly, List<Integer> closedTagsLineNumbers, List<String> allLines) {
        // create a stack of open tags
        Stack<String> openTagsStack = new Stack<>();
        int openTagsCount = -1;
        int closedTagsCount = 0;
        // iterate over isOpenClose list and add open tags to the stack and remove them when a closed tag is found
        for (int i = 0; i < isOpenClose.size(); i++) {
            if (isOpenClose.get(i)) {
                openTagsStack.push(openTagsOnly.get(++openTagsCount));
//                openTagsCount++;
            } else {
                // if the popped tag is not the same as the closed tag then add the missing open tag
                String poppedTag = openTagsStack.pop();
                if (!poppedTag.equals(closedTagsOnly.get(closedTagsCount))) {
                    openTagsOnly.add(openTagsCount+1,closedTagsOnly.get(closedTagsCount));
                    // add it to the queue
                    reader.getTagsQueue().add(i,"<"+closedTagsOnly.get(closedTagsCount)+">");
                    reader.getLineNumbers().add(closedTagsCount,closedTagsLineNumbers.get(closedTagsCount));
                    isOpenClose.add(i,true);
                    String amendedLine =  "<" + closedTagsOnly.get(closedTagsCount)+">" + "</" + closedTagsOnly.get(closedTagsCount)+">";
                    String target = "</" + closedTagsOnly.get(closedTagsCount)+">";
                    int index = closedTagsLineNumbers.get(closedTagsCount);
                    allLines.set(index, allLines.get(index).replace(target, amendedLine));
                    return;
                }
                closedTagsCount++;
            }

        }
    }



    // check if open tags matches closed tags with two pointers
    public void fixClosingTags(List<String> openTagsOnly, List<String> closedTagsOnly, List<Boolean> isOpenClose) {
        // copy openTagsOnly into a new list
        List<String> openTagsOnlyClone = new ArrayList<>(openTagsOnly);


        int openTagsPointer = -1;
        int closedTagsPointer = 0;
        int openClosedCounter = 0;
        while (closedTagsPointer < closedTagsOnly.size()){
            if(isOpenClose.get(openClosedCounter)){
                openTagsPointer++;
            }
            else {
                if (!openTagsOnlyClone.get(openTagsPointer).equals(closedTagsOnly.get(closedTagsPointer))) {
                    // replace the closed tag with the open tag
                    closedTagsOnly.set(closedTagsPointer, openTagsOnlyClone.get(openTagsPointer));
                }
                openTagsOnlyClone.remove(openTagsPointer);
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
