package main;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", posts=" + Arrays.toString(posts) +
                ", followers=" + followers +
                '}';
    }
    // function to print the user in json format
    public String toJson(){
        String json="{";
        json+="\"id\":"+id+",\n";
        json+="\"name\":\""+name+"\",\n";
        json+="\"posts\":[";
        for (Post post : posts) {
            json += post.toJson();
        }
        json+="],\n";
        json+="\"followers\":[";
        for(int i=0;i<followers.size();i++){
            json+="{\"id\":"+followers.get(i)+"}";
            if(i!=followers.size()-1){
                json+=",";
            }
        }
        json+="]}";
        return json;
    }

    public String[] XML() {
        List<String> XML=new ArrayList<>();
        XML.add("\t<user>");
        XML.add("\t\t<id>"+this.id+"</id>");
        XML.add("\t\t<name>"+this.name+"</name>");
        XML.add("\t\t<posts>");
        for(Post post: posts){
            XML.add("\t\t\t<post>");
            for(String line : post.XML())
                 XML.add(line);
            XML.add("\t\t\t</post>");
        }
        XML.add("\t\t</posts>");
        XML.add("\t\t<followers>");
        for(Post post: posts){

            for(int id : followers){
                XML.add("\t\t\t<follower>");
                XML.add("\t\t\t\t<id>".concat(String.valueOf(id))+"</id>");
                XML.add("\t\t\t</follower>");
            }


        }
        XML.add("\t\t</followers>");
        XML.add("\t</user>");

        return XML.toArray(new String[0]);
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

    @Override
    public String toString() {
        return "Post{" +
                "topic=" + Arrays.toString(topic) +
                ", body='" + body + '\'' +
                '}';
    }

    public String[] XML() {
        List<String> XML=new ArrayList<>();
        XML.add("\t\t\t\t<body>".concat(body)+"</body>");
        XML.add("\t\t\t\t<topics>");
        for(String topic : topic){
            XML.add("\t\t\t\t\t<topic>"+topic+"</topic>");
        }
        XML.add("\t\t\t\t</topics>");
        return XML.toArray(new String[0]);
    }
    // function to print the post in json format
    public String toJson(){
        String json="{";
        json+="\"post\":{";
        json+="\"body\":\""+body+"\",\n";
        json+="\"topics\":[";
        for(int i=0;i<topic.length;i++){
            json+="\""+topic[i]+"\"";
            if(i!=topic.length-1){
                json+=",";
            }
        }

        json+="]}\n";
        return json;
    }

}
