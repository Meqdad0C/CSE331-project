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
                    try {
                        if (reader.getIsOpenClose().get(j) && reader.getIsOpenClose().get(j + 1)) {
                            tabs += "\t";
                        } else if ((!reader.getIsOpenClose().get(j)) && (!reader.getIsOpenClose().get(j + 1))) {
                            tabs = tabs.substring(0, tabs.length() - 1);

                        }
                        j++;
                    }catch (Exception e){}


                }
                else{
                    Lines.add(reader.getAllLines().get(i));
                }
            }
            try {
                if(!Lines.get(Lines.size()-1).equals("</"+reader.getTagNames().get(j)+">")){
                    Lines.add("</"+reader.getTagNames().get(j)+">");
                }
            }catch (Exception e){}


        return Lines;
    }


}
