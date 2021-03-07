package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

import java.io.*;
import java.util.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("spam master 3000");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();

        /*
         When the program starts it will ask the user to pick a a directory using the
         directoryChooser function.
         */
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(primaryStage);

        File path = new File(String.valueOf(mainDirectory));
        WordCounter wordCounter = new WordCounter();

        File[] files = path.listFiles();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) { //this line weeds out other directories/folders
                try {
                    wordCounter.parseFile(files[i]);

                } catch (FileNotFoundException e) {
                    System.err.println("Invalid input dir: " + String.valueOf(mainDirectory));
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) { launch(args); }



}
