package com.krillsson.sysapi.gui;

import com.krillsson.sysapi.MaintenanceApplication;
import com.krillsson.sysapi.MaintenanceConfiguration;
import com.krillsson.sysapi.gui.logback.TextAreaAppender;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.DropwizardTestSupport;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.slf4j.Logger;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.concurrent.*;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;

public class Controller {

    private final DropwizardTestSupport<MaintenanceConfiguration> MAINTENANCE_APPLICATION =
            new DropwizardTestSupport<MaintenanceConfiguration>(MaintenanceApplication.class, resourcePath());


    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Controller.class.getSimpleName());

    @FXML
    private ListView loggingTextArea;

    @FXML
    private Button playButton;
    @FXML
    private Button restartButton;
    @FXML
    private Button stopButton;

    private Future<Environment> applicationFuture;

    Environment environment;

    @FXML
    public void initialize() {
        initLogger();
        startDropwizard();
        initControlButtons();
    }

    private void startDropwizard() {
        MAINTENANCE_APPLICATION.before();
    }

    private void stopDropwizard() {
        MAINTENANCE_APPLICATION.after();
    }

    private void initControlButtons() {
        playButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            public void handle(javafx.event.ActionEvent event) {
                startDropwizard();
            }
        });
        restartButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            public void handle(javafx.event.ActionEvent event) {
                stopDropwizard();
                startDropwizard();
            }
        });
        stopButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            public void handle(javafx.event.ActionEvent event) {
                stopDropwizard();
            }
        });
    }

    private String resourcePath()
    {
        return new File(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile()).getParent()
                + System.getProperty("file.separator")
                + "configuration.yml").getPath();
    }
    private void initLogger() {
        TextAreaAppender.setList(loggingTextArea);
    }
}
