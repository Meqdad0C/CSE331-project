package GUI;
import HuffmanEncoding.HuffmanCompress;
import HuffmanEncoding.HuffmanDecompress;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.*;
import main.Reader;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class textDisplay extends Application {

    // The TextArea that displays the contents of the text file
    private TextArea textArea;
    private ArrayList<String> XMLLines;

    // The File that is currently open
    private File openFile;
    private String ext=".xml";
    private File outputFile;

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
            AtomicInteger counter= new AtomicInteger();
            primaryStage.setTitle("Editor");

            // Create buttons for opening, closing, and saving the text file
            Button openButton = new Button("Open");
            openButton.setOnAction(event -> openFile());
            Button closeButton = new Button("Clear");
            closeButton.setOnAction(event -> closeFile());
            Button saveButton = new Button("Save");
            saveButton.setOnAction(event -> {
                saveText(counter.get());
                counter.getAndIncrement();
            }
            );
            Button consistencyButton = new Button("Check Consistency");
            consistencyButton.setOnAction(event -> {
                Alert a=new Alert(Alert.AlertType.CONFIRMATION);
                Consistency1.fixConsistency(openFile);
                if(Consistency1.errors==1){
                    a.setHeaderText("the file has 1 error in consistency");
                }
                else{
                    a.setHeaderText("the file has "+Consistency1.errors+" errors in consistency");
                }


                a.setContentText("press OK to fix the errors");
                Optional<ButtonType> result = a.showAndWait();
                if(!result.isPresent());
                // alert is exited, no button has been pressed.
                else if(result.get() == ButtonType.OK){
                    prettify1(Consistency1.reader);
                }
                //oke button is pressed
                else if(result.get() == ButtonType.CANCEL);
                // cancel button is pressed
                Consistency1.errors=0;
            });
            Button compressButton = new Button("Compress");
            compressButton.setOnAction(event -> {
                try {
                    compressFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            Button decompressButton = new Button("Decompress");
            decompressButton.setOnAction(event -> {
                try {
                    decompressText();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            Button jsonButton= new Button("Convert to JSON");
            jsonButton.setOnAction(event -> {
                Reader reader=new Reader(openFile.getName());
                Users users=new Users(reader.getTagData(),reader.getTagsQueue());
                String s=users.prettyJSON();
                textArea.clear();

                textArea.appendText(s);
                ext="json";



            });
            CheckBox checkBox=new CheckBox("Prettified");
            boolean b = checkBox.isSelected();
            checkBox.setPadding(new Insets(5));
            checkBox.setSelected(false);
            checkBox.setOnAction(actionEvent ->{
                if(checkBox.isSelected()){
                    prettify1(new Reader(openFile.getAbsolutePath()));
                }
                else {
                    try {
                        openFile1();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            } );



            // Add the buttons and the TextArea to the scene
            HBox buttonBox = new HBox(openButton, closeButton, saveButton,compressButton,decompressButton,jsonButton,consistencyButton,checkBox);
            VBox root = new VBox(buttonBox, textArea);

            // Show the window
            Scene scene = new Scene(root, 1280, 720);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openFile1() throws IOException {
        textArea.clear();
        if (openFile != null) {


            BufferedReader reader = new BufferedReader(new FileReader(openFile));
            String line = null;
            while ((line = reader.readLine()) != null) {
                textArea.appendText(line + "\n");
            }
            reader.close();
        }
    }
    private void prettify1(Reader reader) {
        XMLLines=Prettifying.prettify(reader);
        textArea.clear();
        for (String line:XMLLines){
            textArea.appendText(line + "\n");
        }
    }

    private void decompressText() throws IOException {
        FileChooser fileChooser = new FileChooser();
        File File1 = fileChooser.showOpenDialog(textArea.getScene().getWindow());

        HuffmanDecompress.main1(File1);
    }

    private void compressFile() throws IOException {
        HuffmanCompress.main1(openFile);
    }
    private void openFile() {
        try {
            // Prompt the user to select a text file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a text file");
            File textFile = fileChooser.showOpenDialog(textArea.getScene().getWindow());
            openFile=textFile;
            ext=openFile.getName().split("\\.")[1];
            textArea.clear();

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

    private void saveText(int num) {
        try {

            // Prompt the user to select a directory to save the file to
            DirectoryChooser directoryChooser = new DirectoryChooser();

            directoryChooser.setTitle("Select a directory to save the file to");
            File directory = directoryChooser.showDialog(textArea.getScene().getWindow());

            if (directory != null) {
                Integer num1=num;
                // Create a new file in the selected directory
                File saveFile = new File(directory, openFile.getName());

                // Save the text to the new file

                BufferedWriter writer = new BufferedWriter(new FileWriter(openFile.getName().split("\\.")[0]+"."+ext));
                Alert a=new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Done!");
                a.setHeaderText("Saved as: "+openFile.getName().split("\\.")[0]+"."+ext);
//                a.setContentText("Done! Saved as: "+"guiOutput"+num1.toString()+".txt");
                a.show();
                writer.write(textArea.getText());
                writer.close();
            }
        } catch (Exception e) {
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setTitle("File Cannot Be Saved");
            a.show();
            e.printStackTrace();
        }

}
}
