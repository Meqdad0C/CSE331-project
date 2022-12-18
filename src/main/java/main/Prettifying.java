package main;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Prettifying {
    public static ArrayList prettify(Reader fileName) {
        Reader reader=fileName;
        String tabs="";
        ArrayList<String> Lines=new ArrayList<>();
        int counter=0;
        int j=0;
        Pattern pattern = Pattern.compile("<[^?!>]*>");
        Matcher matcher ;
        List<String> AllLines=reader.getAllLines();



            for (int i = 0; i < reader.getAllLines().size() - 1; i++) {
                matcher = pattern.matcher(AllLines.get(i));
                if (matcher.find()) {
                    if ((reader.getIsOpenClose().get(j)) && (!reader.getIsOpenClose().get(j + 1))) {



                            Lines.add(tabs + "<" + reader.getTagNames().get(j) + ">" + reader.getTagData().get(counter) + "</" + reader.getTagNames().get(j + 1) + ">");
                            counter++;
                            j++;

                    } else {

                        if(reader.getIsOpenClose().get(j)){
                            Lines.add(tabs + "<" + reader.getTagNames().get(j) + ">");
                        }
                        else {
                            Lines.add(tabs + "</" + reader.getTagNames().get(j) + ">");
                        }

                    }
                    if (reader.getIsOpenClose().get(j) && reader.getIsOpenClose().get(j + 1)) {
                        tabs += "\t";
                    } else if ((!reader.getIsOpenClose().get(j)) && (!reader.getIsOpenClose().get(j + 1))) {
                        tabs = tabs.substring(0, tabs.length() - 1);
                    }
                    j++;
                }
                else{
                    Lines.add(reader.getAllLines().get(i));
                }
            }

        Lines.add("<"+reader.getTagNames().get(j)+">");
        return Lines;
    }


}
