package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Post {
    String[] topic;
    String body;
    User publisher;
    public void setTopic(String[] topic) {
        this.topic = topic;
    }

    public User getPublisher() {
        return publisher;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "Post{" +
                "topic=" + Arrays.toString(topic) +
                ", body='" + body + '\'' +
                '}';
    }

    public String[] XML() {
        List<String> XML = new ArrayList<>();
        XML.add("\t\t\t\t<body>".concat(body) + "</body>");
        XML.add("\t\t\t\t<topics>");
        for (String topic : topic) {
            XML.add("\t\t\t\t\t<topic>" + topic + "</topic>");
        }
        XML.add("\t\t\t\t</topics>");
        return XML.toArray(new String[0]);
    }

    // function to print the post in json format
    public String toJson() {
        String json = "{";
        json += "\"post\":{";
        json += "\"body\":\"" + body + "\",\n";
        json += "\"topics\":[";
        for (int i = 0; i < topic.length; i++) {
            json += "\"" + topic[i] + "\"";
            if (i != topic.length - 1) {
                json += ",";
            }
        }

        json += "]}";
        return json;
    }

    // function to print the post in xml format
    public String prettyXML() {
        String xml = "<post>\n";
        xml += "\t<body>" + body + "</body>\n";
        xml += "\t<topics>\n";
        for (int i = 0; i < topic.length; i++) {
            xml += "\t\t<topic>" + topic[i] + "</topic>\n";
        }
        xml += "\t</topics>\n";
        xml += "</post>\n";
        return xml;
    }

    // pretty JSON function
    public String prettyJSON() {
        String json = "\t\t{\n";
        json += "\t\t\t\"body\":\"" + body + "\",\n";
        json += "\t\t\t\"topics\":[\n";
        for (int i = 0; i < topic.length; i++) {
            json += "\t\t\t\t\"" + topic[i] + "\"";
            if (i != topic.length - 1) {
                json += ",\n";
            }
        }
        json += "\n\t\t\t]\n";
        json += "\t\t}";
        return json;
    }

    public String[] getTopic() {
        return topic;
    }

    public String getBody() {
        return body;
    }

}
