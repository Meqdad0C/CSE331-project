package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reader {
   private Stack<String> tagsStack = new Stack<>();

   private List<String> tagsQueue = new LinkedList<>();
    // true = open, false = closed
   private List<Boolean> isOpenClose = new LinkedList<>();
    // data structure to hold tag names
   private List<String> tagNames = new LinkedList<>();

   private List<String> tagData = new LinkedList<>();
    // data structure to hold tags Index
   private List<List<Integer>> tagsIndex = new LinkedList<>();
    // data structure to hold two-dimensional array of open tags and start and end indexes
   private List<List<Integer>> openTagsIndices = new LinkedList<>();
    // data structure to hold two-dimensional array of closed tags and start and end indexes
   private List<List<Integer>> closedTagsIndices = new LinkedList<>();
   private List<Integer> lineNumbers = new LinkedList<>();
     // data structure to hold open tags only
   private List<String> openTagsOnly = new LinkedList<>();
        // data structure to hold closed tags only
   private List<String> closedTagsOnly = new LinkedList<>();

    public Reader(String xml) {
        int counter1=0;
        Pattern pattern = Pattern.compile("<[^?!>]*>");
        Pattern pattern1 = Pattern.compile(">([^<]*)<");
        Matcher matcher;


        // read all lines from file
        Path file = Paths.get(xml);
        List<String> allLines = null;
        try {
            allLines = Files.readAllLines(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String line : allLines) {
            System.out.println(line);
            matcher = pattern.matcher(line);
            while (matcher.find()) {
                // Print starting and ending indexes
                // of the pattern in the text
                // using this functionality of this class
                System.out.println("Pattern found from "
                        + matcher.start() + " to "
                        + (matcher.end() - 1));

                // add match to tagsQueue
                tagsQueue.add(matcher.group());
                tagsStack.add(matcher.group());

                // Fill tagsIndex with start and end indexes
                tagsIndex.add(List.of(matcher.start(), matcher.end()));


                // add open or close to list
                if (matcher.group().contains("/")) {
                    isOpenClose.add(false);
                    // add start and end indexes to list of start and end indexes
                    closedTagsIndices.add(List.of(matcher.start(), matcher.end()));

                } else {
                    isOpenClose.add(true);
                    // add start and end indexes to list of start and end indexes
                    openTagsIndices.add(List.of(matcher.start(), matcher.end()));
                }

                // add tag name to list
                tagNames.add(matcher.group().replaceAll("[^a-zA-Z]", ""));
                lineNumbers.add(counter1);
            }
            counter1++;
            matcher = pattern1.matcher(line);
            while (matcher.find()) {
                tagData.add(matcher.group().substring(1,matcher.group().length()-1));
            }
        }
        for (int i = 0; i < isOpenClose.size(); i++) {
            if (isOpenClose.get(i)) {
                openTagsOnly.add(tagNames.get(i));
            }
        }
        System.out.println("Open tags only: " + openTagsOnly);

        // add closed tags to list
        for (int i = 0; i < isOpenClose.size(); i++) {
            if (!isOpenClose.get(i)) {
                closedTagsOnly.add(tagNames.get(i));
            }
        }
        System.out.println("Closed tags only: " + closedTagsOnly);
        System.out.println(lineNumbers);
        System.out.println(tagData);

    }

    public Stack<String> getTagsStack() {
        return tagsStack;
    }

    public List<String> getTagsQueue() {
        return tagsQueue;
    }

    public List<Boolean> getIsOpenClose() {
        return isOpenClose;
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public List<String> getTagData() {
        return tagData;
    }

    public List<List<Integer>> getTagsIndex() {
        return tagsIndex;
    }

    public List<List<Integer>> getOpenTagsIndices() {
        return openTagsIndices;
    }

    public List<List<Integer>> getClosedTagsIndices() {
        return closedTagsIndices;
    }

    public List<Integer> getLineNumbers() {
        return lineNumbers;
    }

    public List<String> getOpenTagsOnly() {
        return openTagsOnly;
    }

    public List<String> getClosedTagsOnly() {
        return closedTagsOnly;
    }
}
