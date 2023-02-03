package GUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import main.Post;
import main.Reader;
import main.User;
import main.Users;

import java.io.File;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Random;

public class graphDisplay {

//    public static void main(String[] args) {
//        launch(args);
//    }


    public static Stage getPrimaryStage(File file) {
        Reader reader = new main.Reader(file.getAbsolutePath());
        Users users=new Users(reader.getTagData(),reader.getTagsQueue());
        List<User> usersList = users.getUsers();
//        System.out.println(usersList.get(0).getId());
        // Create a Group object
        Group graph = new Group();

        // convert the list of users to a list of circles
        Circle[] circles = new Circle[usersList.size()];
        ArrayList<Circle> smallCircles=new ArrayList<Circle>();


        //predefine the coordinates of the circles as a polygon
        double[] x = new double[usersList.size()];
        double[] y = new double[usersList.size()];
        double radius = 200;
        double angle = 0;
        double angleIncrement = 2 * Math.PI / usersList.size();
        for (int i = 0; i < usersList.size(); i++) {
            x[i] = 600 + radius * Math.cos(angle);
            y[i] = 350 + radius * Math.sin(angle);
            angle += angleIncrement;
        }
        Random random = new Random();
        for (int i = 0; i < usersList.size(); i++) {
            // Generate random x and y coordinates
/*            double x = random.nextDouble() * 1000+200; // Random x coordinate
            double y = random.nextDouble() * 400+200; // Random y coordinate*/


            // Create the circle with the random coordinates
            circles[i] = new Circle(x[i], y[i], 50); // Circle with radius 100 at (x[i, y[i)

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
        ArrayList<Line> lines=new ArrayList<Line>();
        for (int i = 0; i < usersList.size(); i++) {
            User user = usersList.get(i);
            for (int j = 0; j < user.getFollowers().size(); j++) {
                int followerID = user.getFollowers().get(j)-1;
                double X1 = circles[i].getCenterX();
                double Y1 = circles[i].getCenterY();
                double X2 = circles[followerID].getCenterX();
                double Y2 = circles[followerID].getCenterY();

                double lengthCase1=Math.sqrt(Math.pow(Math.abs(X2-X1)-50,2)+Math.pow(Y2-Y1,2));
                double lengthCase2=Math.sqrt(Math.pow(X2-X1,2)+Math.pow(Math.abs(Y2-Y1)-50,2));
                double lengthCase3=Math.sqrt(Math.pow(Math.abs(X2-X1)+50,2)+Math.pow(Y2-Y1,2));
                double lengthCase4=Math.sqrt(Math.pow(X2-X1,2)+Math.pow(Math.abs(Y2-Y1)+50,2));
                Line line;// Set the stroke width to 2

                if(lengthCase1<=lengthCase2&&lengthCase1<=lengthCase3&&lengthCase1<=lengthCase4){
                    if(X1>X2) {
                        line = new Line(X1, Y1, X2 + 50, Y2);

                    }
                    else
                        line = new Line(X1, Y1, X2 - 50, Y2);
                    // make the line color the same as the circle
                }
                else if(lengthCase2<=lengthCase1&&lengthCase2<=lengthCase3&&lengthCase2<=lengthCase4) {
                    if(Y1>Y2)
                        line = new Line(X1, Y1, X2, Y2+50);
                    else
                        line = new Line(X1, Y1, X2, Y2-50);
                    // make the line color the same as the circle
                }
                else if(lengthCase3<=lengthCase1&&lengthCase3<=lengthCase2&&lengthCase3<=lengthCase4) {

                    if(X1>X2)
                        line = new Line(X1, Y1, X2-50, Y2);
                    else
                        line = new Line(X1, Y1, X2+50, Y2);
                    // make the line color the same as the circle
                }
                else{
                    if(Y1>Y2)
                        line = new Line(X1, Y1, X2, Y2 -50);
                    else
                        line = new Line(X1, Y1, X2, Y2 +50);

                    // make the line color the same as the circle
                }
                line.setStrokeWidth(2); // Set the stroke width to 2
                line.setStroke(circles[i].getStroke());

                for(Line line1 :lines){
                    if( Math.abs(line1.getEndY()-line1.getStartY())<1&&Math.abs(line.getStartY()-line.getEndY())<1&&Math.abs(line.getStartY()-line1.getStartY())<1){
                        line.setEndY(line.getEndY() - 15);
                    }
                    else if( Math.abs(line1.getEndX()-line1.getStartX())<1&&Math.abs(line.getStartX()-line.getEndX())<1&&Math.abs(line.getStartX()-line1.getStartX())<1){
                        line.setEndX(line.getEndX() - 15); // Shift the end of the line to the left
                    }
                }

                Circle smallCircle=new Circle(line.getEndX(),line.getEndY(),5);

                lines.add(line);
                // Shift the end of the line to the left
                smallCircle.setFill(circles[i].getStroke());

                for (Circle circle:smallCircles){
                    if(circle.getCenterX()==smallCircle.getCenterX()&&circle.getCenterY()==smallCircle.getCenterY()){
                        line.setEndX(line.getEndX() -10);

                        smallCircle.setCenterX(line.getEndX());

                    }
                }
                graph.getChildren().add(line);

                 graph.getChildren().add(smallCircle);

                smallCircles.add(smallCircle);


            }

        }

        ChoiceBox choiceBox1=new ChoiceBox<String>();
        ChoiceBox choiceBox=new ChoiceBox<String>();
        for(User user:usersList){
            choiceBox.getItems().add("Name: "+user.getName() +" ID:"+user.getId());
            choiceBox1.getItems().add("Name: "+user.getName() +" ID:"+user.getId());
        }

        TabPane tabPane = new TabPane();
//        graph.getChildren().add(new Circle());
        Circle Emptycircle=new Circle(30);
        Circle Emptycircle1=new Circle(30);
        Emptycircle.setFill(Color.TRANSPARENT);
        Emptycircle1.setFill(Color.TRANSPARENT);
        VBox vBox1=new VBox();
        Button button2=new Button("Show most followed user");
        Button button3=new Button("Show most active user");

        HBox buttonBox=new HBox();
        button2.setOnAction(event -> {
            Alert a=new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Here!");
            a.setHeaderText("Name: "+users.getMostFollowedUser().getName()+" ID: "+users.getMostFollowedUser().getId());

            a.showAndWait();
        });
        button3.setOnAction(event -> {
            Alert a=new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Here!");
            a.setHeaderText("Name: "+users.getMostActiveUser().getName()+" ID: "+users.getMostActiveUser().getId());
            a.showAndWait();
        });

        for (int i=0; i<circles.length;i++){
            int finalI = i;
            circles[i].setOnMouseClicked(e -> {
                Alert a=new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Suggested Followers!");
                String s="";
                for(User user:users.SuggestedUsers(usersList.get(finalI))){
                    if(s.contains(user.getName()))
                        continue;
                    s+= user.getName()+",";
                }
                if(s.equals(""))
                    s="No suggestion! ";

                a.setHeaderText(s.substring(0,s.length()-1));
                a.showAndWait();
            });
        }

        Label label2=new Label("Press on any node to show the sugessted friends");
        vBox1.getChildren().add(label2);
        buttonBox.getChildren().add(button2);
        buttonBox.getChildren().add(button3);

        buttonBox.alignmentProperty().set(Pos.CENTER);
        buttonBox.setSpacing(20);
        buttonBox.setPadding(new Insets(10, 10, 10, 10));
        vBox1.getChildren().add(buttonBox);
        vBox1.getChildren().add(graph);
        vBox1.alignmentProperty().set(Pos.CENTER);

        Tab tab1 = new Tab("Graph", vBox1);
        HBox hBox=new HBox(choiceBox,choiceBox1);
        hBox.alignmentProperty().set(Pos.TOP_CENTER);
        Button button=new Button("Submit");
        VBox vBox2=new VBox();
        vBox2.alignmentProperty().set(Pos.CENTER);
        vBox2.getChildren().add(Emptycircle1);
        vBox2.getChildren().add(new Label("Select the two users that you want to find the mutual followers between."));
        vBox2.getChildren().add(hBox);
        vBox2.getChildren().add(button);

//        Label label=new Label();
        Label label=new Label("the mutual followers: ");
        vBox2.getChildren().add(label);
        TextArea text=new TextArea();
        vBox2.getChildren().add(text);

        button.setOnAction(event -> {

           List<Integer> ids=Users.getMutualFollowers(usersList.get(Integer.valueOf(choiceBox.getValue().toString().split("ID:")[1])-1),usersList.get(Integer.valueOf(choiceBox1.getValue().toString().split("ID:")[1])-1));
            text.clear();
           String s="";
           int counter=1;
           for(int id:ids){
                s+=counter+"-"+" Name:"+usersList.get(id-1).getName()+"   --    ID:"+usersList.get(id-1).getId()+"\n";
                counter++;
//               vBox2.getChildren().add(new Label("Name:"+usersList.get(id).getName()+" ID:"+usersList.get(id).getId()));
           }

           text.setFont(Font.font("Verdana", 20));
          text.setText(s);
       });
        VBox vBox3=new VBox();
        vBox3.alignmentProperty().set(Pos.TOP_CENTER);
        Circle Emptycircle2=new Circle(30);

        Emptycircle2.setFill(Color.TRANSPARENT);

        vBox3.setPadding(new Insets(50, 10, 10, 10));
        TextField searchField=new TextField();
        searchField.setMaxSize(400,20);
        searchField.setPadding(new Insets(10, 10, 10, 10));
        searchField.setPromptText("Search for a post");
        vBox3.getChildren().add(searchField);
        Button button1=new Button("Search");

        TextArea textArea=new TextArea();
        textArea.setWrapText(true);
        vBox2.alignmentProperty().set(Pos.TOP_CENTER);
        vBox3.getChildren().add(button1);
        vBox3.getChildren().add(Emptycircle2);
        vBox3.getChildren().add(textArea);
        textArea.setPrefHeight(500);
        textArea.setFont(Font.font("Verdana", 15));
        button1.setOnAction(event -> {
            textArea.clear();
            List<Post> posts= users.searchPosts(searchField.getText());
            for (Post post:posts){
                textArea.appendText(post.getPublisher().getName()+"(");
                for(String topic:post.getTopic()){
                    textArea.appendText(topic);
                    if(topic.equals(post.getTopic()[post.getTopic().length-1]))
                        continue;
                    textArea.appendText(",");
                }
                textArea.appendText(")"+": \n");
                textArea.appendText(post.getBody()+"\n\n\n\n\n");



            }
        });
        Tab tab2 = new Tab("Mutual Followers"  , vBox2);
        Tab tab3 = new Tab("Search" ,vBox3);

        tabPane.getTabs().add(tab1);
        tabPane.getTabs().add(tab2);
        tabPane.getTabs().add(tab3);

        VBox vBox = new VBox(tabPane);

        Scene scene = new Scene(vBox ,1280, 720);


        // Create a Scene with the graph as the root node


        // Set the title of the primary stage
        Stage primaryStage=new Stage();
        primaryStage.setTitle("Social Analysis");

        // Set the scene on the primary stage
        primaryStage.setScene(scene);

        // Show the primary stage
        return primaryStage;

    }

}
