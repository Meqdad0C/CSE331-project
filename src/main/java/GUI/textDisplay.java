package GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class textDisplay extends Application {

    // The TextArea that displays the contents of the text file
    private TextArea textArea;

    // The File that is currently open
    private File openFile;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Create a TextArea to display the text file contents
            textArea = new TextArea();
            textArea.setEditable(true);
            textArea.setPrefWidth(1000);
            textArea.setPrefHeight(1000);



            // Create buttons for opening, closing, and saving the text file
            Button openButton = new Button("Open");
            openButton.setOnAction(event -> openFile());
            Button closeButton = new Button("Clear");
            closeButton.setOnAction(event -> closeFile());
            Button saveButton = new Button("Save");
            saveButton.setOnAction(event -> saveText());

            // Add the buttons and the TextArea to the scene
            HBox buttonBox = new HBox(openButton, closeButton, saveButton);
            VBox root = new VBox(buttonBox, textArea);

            // Show the window
            Scene scene = new Scene(root, 1280, 720);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openFile() {
        try {
            // Prompt the user to select a text file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a text file");
            File textFile = fileChooser.showOpenDialog(textArea.getScene().getWindow());

            if (textFile != null) {
                // Save the File object so we can save it later
                openFile = textFile;

                // Read the text file and append its contents to the TextArea
                BufferedReader reader = new BufferedReader(new FileReader(textFile));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    textArea.appendText(line + "\n");
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeFile() {
        // Clear the TextArea
        textArea.clear();

        // Clear the openFile variable
        openFile = null;
    }

    private void saveText() {
        try {
            // Prompt the user to select a directory to save the file to
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select a directory to save the file to");
            File directory = directoryChooser.showDialog(textArea.getScene().getWindow());

            if (directory != null) {
                // Create a new file in the selected directory
                File saveFile = new File(directory, "guiOutput.txt");

                // Save the text to the new file
                BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
                writer.write(textArea.getText());
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

}
}
