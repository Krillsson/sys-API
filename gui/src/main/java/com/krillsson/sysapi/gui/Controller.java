package com.krillsson.sysapi.gui;

import com.krillsson.sysapi.MaintenanceApplication;
import com.krillsson.sysapi.gui.logback.TextAreaAppender;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Controller {

    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Controller.class.getSimpleName());

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    @FXML
    private ListView loggingTextArea;

    @FXML
    private Button playButton;
    @FXML
    private Button restartButton;
    @FXML
    private Button stopButton;
    private Future<?> applicationFuture;

    @FXML
    public void initialize() {
        initLogger();
        startDropwizard();
        initControlButtons();
    }

    private void initControlButtons() {
        playButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            public void handle(javafx.event.ActionEvent event) {
                startDropwizard();
            }
        });
        restartButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            public void handle(javafx.event.ActionEvent event) {
                stopApplicationFuture();
                startDropwizard();
            }
        });
        stopButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            public void handle(javafx.event.ActionEvent event) {
                stopApplicationFuture();
            }
        });
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        final long SHUTDOWN_TIME = 1000;
        stopApplicationFuture();
        executor.shutdownNow();
        try {
            if (!executor.awaitTermination(SHUTDOWN_TIME, TimeUnit.MILLISECONDS)) { //optional *
                LOGGER.error("Executor did not terminate in the specified time."); //optional *
                List<Runnable> droppedTasks = executor.shutdownNow(); //optional **
                LOGGER.error("Executor was abruptly shut down. " + droppedTasks.size() + " tasks will not be executed."); //optional **
            }
        } catch (InterruptedException e) {
            LOGGER.error("Exception occurred while shutting down", e);
        }
    }

    private void stopApplicationFuture() {
        if(applicationFuture != null){
            applicationFuture.cancel(true);
        }
    }

    private void startDropwizard() {
        applicationFuture = executor.submit(new Runnable() {
            public void run() {
                LOGGER.debug("initializing Dropwizard thread: {}", Thread.currentThread().getName());
                MaintenanceApplication application = new MaintenanceApplication();

                try {
                    File configuration = new File(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile()).getParent()
                            + System.getProperty("file.separator")
                            + "configuration.yml");
                    if (configuration.exists()) {
                        application.run("server", configuration.getAbsolutePath());
                    }
                } catch (Exception e) {
                    LOGGER.error("Exception occurred while starting", e);
                }
            }
        });
    }

    private void initLogger() {
        TextAreaAppender.setList(loggingTextArea);
    }
}
