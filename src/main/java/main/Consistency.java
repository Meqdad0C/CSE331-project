package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Consistency {
    private List<String> tags;
    List<Boolean> isOpenClose;

    private Stack<String> tagsStack = new Stack<>();

    public Consistency(List<String> tags,List<Boolean> isOpenClose ) {
        this.tags = tags;
        this.isOpenClose=isOpenClose;
    }
    public void correctTags(){
        for(int i=0;i<tags.size();i++){
            if(isOpenClose.get(i)==true){
                tagsStack.push(tags.get(i));
            }
            else{
                if(tagsStack.pop().equals(tags.get(i)));
                else{
                    correctTag(x)
                }
            }
        }
    }
}
