package com.example.one;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Reader;
import main.User;
import main.Users;

import java.util.List;
import java.util.Random;

public class graphDisplay extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Reader reader = new main.Reader("input.xml");
        Users users=new Users(reader.getTagData(),reader.getTagsQueue());
        List<User> usersList = users.getUsers();
        System.out.println(usersList.get(0).getId());
        // Create a Group object
        Group graph = new Group();

        // convert the list of users to a list of circles
        Circle[] circles = new Circle[usersList.size()];

        //predefine the coordinates of the circles as a pentagon
        double[] x = new double[usersList.size()];
        double[] y = new double[usersList.size()];
        double radius = 150;
        double angle = 0;
        double angleIncrement = 2 * Math.PI / usersList.size();
        for (int i = 0; i < usersList.size(); i++) {
            x[i] = radius * Math.cos(angle) + 300;
            y[i] = radius * Math.sin(angle) + 300;
            angle += angleIncrement;
        }




        Random random = new Random();
        for (int i = 0; i < usersList.size(); i++) {
            // Generate random x and y coordinates
/*            double x = random.nextDouble() * 1000+200; // Random x coordinate
            double y = random.nextDouble() * 400+200; // Random y coordinate*/


            // Create the circle with the random coordinates
            circles[i] = new Circle(x[i], y[i], 100); // Circle with radius 100 at (x[i, y[i)

            // Add the user's name as a text label to the circle
            Text text = new Text(usersList.get(i).getName());
            // add sting value of id text to the circle
            Text idText = new Text("id : "+String.valueOf(usersList.get(i).getId()));
            // display[i the id text on the circle under the name
            idText.setY(y[i]+20);
            idText.setX(x[i]);

            // Set the text's x[i and y[i coordinates to the circle's x[i and y[i coordinates
            text.setTranslateX(x[i]);
            text.setTranslateY(y[i]);
            // make the text appear inside the circle
            text.setTranslateX(text.getTranslateX() - text.getLayoutBounds().getWidth() / 2);
            // make the circle behind the text

            graph.getChildren().add(text);
            graph.getChildren().add(idText);
        }

        // Add the circles to the group
        graph.getChildren().addAll(circles);

        // make the circles completely transparent and with Stroke only
        for (int i = 0; i < circles.length; i++) {
            circles[i].setFill(Color.TRANSPARENT);
            // Random color for the stroke
            circles[i].setStroke(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            // increase the stroke width to make the lines more visible
            circles[i].setStrokeWidth(5);

        }
        // Create a Line for each follower relationship in your array
        for (int i = 0; i < usersList.size(); i++) {
            User user = usersList.get(i);
            for (int j = 0; j < user.getFollowers().size(); j++) {
                int followerID = user.getFollowers().get(j)-1;
                double X1 = circles[i].getCenterX();
                double Y1 = circles[i].getCenterY();
                double X2 = circles[followerID].getCenterX();
                double Y2 = circles[followerID].getCenterY();
                Line line = new Line(X1, Y1, X2, Y2);
                line.setStrokeWidth(2); // Set the stroke width to 2
                // make the line color the same as the circle
                line.setStroke(circles[i].getStroke());


                line.setEndX(line.getEndX() - 20); // Shift the end of the line to the left
                line.setEndY(line.getEndY() - 20); // Shift the end of the line upwards
                graph.getChildren().add(line);
            }
        }




        // Create a Scene with the graph as the root node
        Scene scene = new Scene(graph, 1280, 720);

        // Set the title of the primary stage
        primaryStage.setTitle("My Graph");

        // Set the scene on the primary stage
        primaryStage.setScene(scene);

        // Show the primary stage
        primaryStage.show();
    }

}
