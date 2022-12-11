package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Users {
     ArrayList<User> users = new ArrayList<>();
     // User reference to store the user with most followers
        User mostFollowedUser;
        // user reference to store the user who follows the most users
        User mostFollowingUser;

    public  Users(List<String> tagData,List<String> tagsQueue) {
        int counter=0;
        for(int i=0;i<tagsQueue.size()-1;i++){
            User user=new User();
            while(!tagsQueue.get(i).equals("</user>")){
                if(tagsQueue.get(i).equals("<id>")&&tagsQueue.get(i+1).equals("</id>")){
                    user.setId(Integer.parseInt(tagData.get(counter)));
                    counter++;
                }
                if(tagsQueue.get(i).equals("<name>")&&tagsQueue.get(i+1).equals("</name>")){
                    user.setName(tagData.get(counter));
                    counter++;
                }
                if(tagsQueue.get(i).equals("<posts>")){
                    List<Post> posts=new ArrayList<>();
                    while(!tagsQueue.get(i).equals("</posts>")){
                        Post post=new Post();
                        while(!tagsQueue.get(i).equals("</post>")){
                            if(tagsQueue.get(i).equals("<topics>")){
                                List<String> topics=new ArrayList<>();
                                while(!tagsQueue.get(i).equals("</topics>")){
                                    if(tagsQueue.get(i).equals("<topic>")&&tagsQueue.get(i+1).equals("</topic>")){
                                        topics.add(tagData.get(counter));
                                        counter++;
                                    }
                                    i++;
                                }
                                post.setTopic(topics.toArray(new String[0]));
                            }

                            if(tagsQueue.get(i).equals("<body>")){
                                post.setBody(tagData.get(counter));
                                counter++;
                            }
                            i++;
                        }
                        i++;
                        posts.add(post);
                    }
                    Post[] posts1=new Post[posts.size()];
                    user.setPosts(posts.toArray(posts1));
                }
                if(tagsQueue.get(i).equals("<followers>")){
                    List<Integer> followers=new ArrayList<>();
                    while(!tagsQueue.get(i).equals("</followers>")){
                        if(tagsQueue.get(i).equals("<id>")&&tagsQueue.get(i+1).equals("</id>")){
                            followers.add(Integer.parseInt(tagData.get(counter)));
                            counter++;
                        }
                        i++;
                    }
                    user.setFollowers(followers);
                }
                i++;
            }
            users.add(user);
        }

    }

    // Method to find the user with most followers
    public User getMostFollowedUser() {
        // Initialize the mostFollowedUser with the first user
        mostFollowedUser = users.get(0);
        // Loop through the users
        for (User user : users) {
            // If the current user has more followers than the mostFollowedUser
            if (user.getFollowers().size() > mostFollowedUser.getFollowers().size()) {
                // Set the mostFollowedUser to the current user
                mostFollowedUser = user;
            }
        }
        // Return the mostFollowedUser
        return mostFollowedUser;
    }

    // Method to find the user who is following the most users
    // initialize A mapping of the followers list to this user
    // that they follow him
    // Loop through the users
    // return the user who follows the most users
    // TODO: Implement this method

    // Static Method to find mutual followers between two users
    public static List<Integer> getMutualFollowers(User user1, User user2) {
        // Initialize a list to store the mutual followers
        List<Integer> mutualFollowers = new ArrayList<>();
        // Loop through the followers of the first user
        for (Integer follower : user1.getFollowers()) {
            // If the second user is following the current follower
            if (user2.getFollowers().contains(follower)) {
                // Add the follower to the mutualFollowers list
                mutualFollowers.add(follower);
            }
        }
        // Return the mutualFollowers list
        return mutualFollowers;
    }


    @Override
    public String toString() {
        return "Users{ " +
                "users" + users +
                '}';
    }
    // function to call the JSON function of each user
    public String toJson() {
        String json="{\"users\":[";
        for(int i=0;i<users.size();i++){
            json+=users.get(i).toJson();
            if(i!=users.size()-1){
                json+=",\n";
            }
        }
        json+="]}";
        return json;
    }


    public List<String> toXML(){
        List<String> XML=new ArrayList<>();
        XML.add("<users>");
        for(User user: users){
            for(String line: user.XML())
            XML.add(line);
        }
        XML.add("</users>");
        return XML;
    }
    // prettyXML function to make the XML file more readable
    public List<String> prettyXML(){
        List<String> XML=new ArrayList<>();
        XML.add("<users>\n");
        for(User user: users){
                XML.add(user.prettyXML());
        }
        XML.add("</users>");
        return XML;
    }
    // prettyXML using StringBuilder
    public String prettyXML2(){
        StringBuilder XML=new StringBuilder();
        XML.append("<users>\n");
        for(User user: users){
            XML.append(user.prettyXML());
        }
        XML.append("</users>");
        return XML.toString();
    }

    // prettyJSON function to make the JSON file more readable
    public String prettyJSON(){
        String json="{\n\t\"users\":[\n";
        for(int i=0;i<users.size();i++){
            json+=users.get(i).prettyJSON();
            if(i!=users.size()-1){
                json+=",\n";
            }
        }
        json+="\n\t]\n}";
        return json;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
