package main;

import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        Reader reader=new Reader("input.xml");
        Users users=new Users(reader.getTagData(),reader.getTagsQueue());
        //System.out.println(users.users.get(0));
        System.out.println();
        System.out.println();
        System.out.println(users.users.get(0).toJson());
    }
}
