package main;

import java.util.List;

public class User {
    private int id=0;
    private String name;
    private Post[] posts;
    private List<Integer> followers;

    public User() {
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPosts(Object[] posts) {
        this.posts = (Post[]) posts;
    }

    public void setFollowers(List<Integer> followers) {
        this.followers = followers;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Post[] getPosts() {
        return posts;
    }

    public List<Integer> getFollowers() {
        return followers;
    }
    public void print(){
        System.out.println(id+" "+name+" "+posts[0].body+posts[0].topic[0]+" "+" "+followers);
    }
}
class Post {
    String[] topic;
    String body;

    public void setTopic(String[] topic) {
        this.topic = topic;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
