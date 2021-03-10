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

public abstract class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Spam master 3000");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.show();
    }
    public static void main(String[] args) { launch(args); }



}
