package com.krillsson.sysapi.gui;

import com.krillsson.sysapi.MaintenanceApplication;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    @FXML
    private TextArea loggingTextArea;

    @FXML
    public void initialize() {

        startDropwizard();
        initLogger();
    }

    private void startDropwizard() {
        executor.submit(new Runnable() {
            public void run() {
                MaintenanceApplication application = new MaintenanceApplication();
                try {
                    application.run("server", "C:/Users/Christian/StudioProjects/sys-api/server/dev.yml");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initLogger() {
        loggingTextArea.appendText("Initializing...");
    }
}
