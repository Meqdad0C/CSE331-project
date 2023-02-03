package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Users {
     ArrayList<User> users = new ArrayList<>();
     // User reference to store the user with most followers
        User mostFollowedUser;
        // user reference to store the user who follows the most users
        User mostActiveUser;
    // user reference to store the user who has the most connections

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
                        post.setPublisher(user);
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
        this.mostFollowedUser=getMostFollowedUser();
        this.mostActiveUser=getMostActiveUser();

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

    public User getMostActiveUser() {
        Integer[] connections = new Integer[users.size()];
        // Loop through the users
        for (User user : users) {
            connections[user.getId()-1] = 0;
            for (User user1 : users) {
                if (user1.getFollowers().contains(user.getId())) connections[user.getId()-1]++;
            }

        }
        int max = connections[0];
        int index=0;

        for (int i=1;i<connections.length;i++) {
           if(connections[i]>max)
               index=i;
        }
        return users.get(index);
    }
    // Method to find the user who is following the most users
    // initialize A mapping of the followers list to this user
    // that they follow him
    // Loop through the users
    // return the user who follows the most users

    // Static Method to find mutual followers between two users
    public static List<Integer> getMutualFollowers(User user1, User user2){
            // Initialize a list to store the mutual followers
            List<Integer> mutualFollowers = new ArrayList<>();
            // Loop through the followers of the first user
            for (Integer follower : user1.getFollowers()) {
                // If the second user is following the current follower
                if (user2.getFollowers().contains(follower)) {
                    // Add the follower to the mutualFollowers list
                    mutualFollowers.add(follower);
                }

                // Return the mutualFollowers list

            }
        return mutualFollowers;
        }
    //Post search: given a specific word or topic, get the posts where this word or topic was mentioned
    public List<Post> searchPosts(String word){
        List<Post> posts=new ArrayList<>();
        for(User user:users){
            for(Post post:user.getPosts()){
                if(post.getBody().toLowerCase().contains(word.toLowerCase())){
                    if(posts.contains(post))
                        continue;
                        posts.add(post);
                }
                for(String topic:post.getTopic()){
                    if(topic.toLowerCase().contains(word.toLowerCase())){
                        if(posts.contains(post))
                            continue;
                        posts.add(post);
                    }
                }
            }
        }
        return posts;
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
    public List<User> SuggestedUsers(User user){
        List<User> users1=new ArrayList<User>();
        for(int i=0;i<user.getFollowers().size();i++){
            for (int id:users.get(user.getFollowers().get(i)-1).getFollowers()){
                if(!user.getFollowers().contains(id)&&id!=user.getId()){

                   if(users1.contains(users.get(id-1)))
                       continue;
                       users1.add(users.get(id-1));
                }

            }
        }
        return users1;
    }
    public ArrayList<User> getUsers() {
        return users;
    }
}
