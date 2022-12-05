package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Users {
     ArrayList<User> users = new ArrayList<>();

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

    @Override
    public String toString() {
        return "Users{ " +
                "users" + users +
                '}';
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
}
