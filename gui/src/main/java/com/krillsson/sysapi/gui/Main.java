package com.krillsson.sysapi.gui;

import com.krillsson.sysapi.MaintenanceApplication;
import com.krillsson.sysapi.gui.logback.TextAreaAppenderFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.apache.log4j.spi.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        URL resource = getClass().getResource("/fxml/main.fxml");
        Parent root = FXMLLoader.load(resource);
        primaryStage.setTitle("Sys-Api Server");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }




    public static void main(String[] args) {
        launch(args);
    }
}
