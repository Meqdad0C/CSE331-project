package main;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Reader reader=new Reader("input.xml");
        Users users=new Users(reader.getTagData(),reader.getTagsQueue());
        List<User> usersList=users.getUsers();
        //System.out.println(users.users.get(0));
        System.out.println();
        System.out.println();
        System.out.println(users.toJson());
        System.out.println(users.prettyXML());
        System.out.println(users.prettyJSON());
        System.out.println(users.prettyXML2());
        System.out.println(users.getMostFollowedUser());
        System.out.println(users.searchPosts("spOrts"));

        System.out.println(Users.getMutualFollowers(usersList.get(3),usersList.get(0)));
    }
}
