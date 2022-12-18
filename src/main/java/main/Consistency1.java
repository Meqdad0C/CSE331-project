package main;

import java.io.File;
import java.util.Stack;

public class Consistency1 {
    public static int errors=0;
    public static Reader reader;
    public static void fixConsistency(File file){

        reader=new Reader(file.getName());

        Stack<String> stack=new Stack<>();
        for(int i=0;i<reader.getTagNames().size();i++){
            if(reader.getIsOpenClose().get(i)){

                stack.push(reader.getTagNames().get(i)+"-"+i);
            }
            else{
                if(!stack.peek().split("-")[0].equals(reader.getTagNames().get(i))){

                    reader.getTagNames().add(Integer.valueOf(stack.peek().split("-")[1])+1,reader.getAllLines().get(reader.getLineNumbers().get(Integer.valueOf(stack.peek().split("-")[1])))+"<"+stack.peek().split("-")[0]+"/>"+"~1");
                    reader.getIsOpenClose().add(Integer.valueOf(stack.peek().split("-")[1])+1,true);

                    errors++;
                }
                stack.pop();

            }

        }

    }
}
