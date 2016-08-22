package com.krillsson.sysapi.gui;

import com.krillsson.sysapi.MaintenanceApplication;
import com.krillsson.sysapi.gui.logback.TextAreaAppender;
import io.dropwizard.setup.Environment;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.slf4j.Logger;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.*;

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

    private Future<Environment> applicationFuture;

    Environment environment;

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

    @FXML
    public void exitApplication(ActionEvent event) {
        final long SHUTDOWN_TIME = 1000;
        stopDropwizard();
        executor.shutdownNow();
        try {
            if (!executor.awaitTermination(SHUTDOWN_TIME, TimeUnit.MILLISECONDS)) {
                LOGGER.error("Executor did not terminate in the specified time.");
                List<Runnable> droppedTasks = executor.shutdownNow();
                LOGGER.error("Executor was abruptly shut down. " + droppedTasks.size() + " tasks will not be executed.");
            }
        } catch (InterruptedException e) {
            LOGGER.error("Exception occurred while shutting down", e);
        }
    }

    private void stopDropwizard() {
        if(applicationFuture != null){
            try {
                Environment environment = applicationFuture.get();
                environment.getApplicationContext().stop();
                environment.getAdminContext().stop();
                environment.jersey().
            } catch (InterruptedException e) {
                LOGGER.error("Interrupt", e);
            } catch (ExecutionException e) {
                LOGGER.error("Execution exception", e);
            } catch (Exception e) {
                LOGGER.error("Execution exception", e);
            }
        }
    }

    private void startDropwizard() {
        if(applicationFuture != null){
            try {
                Environment environment = applicationFuture.get();
                environment.getApplicationContext().start();
                environment.getAdminContext().start();
            } catch (InterruptedException e) {
                LOGGER.error("Interrupt", e);
            } catch (ExecutionException e) {
                LOGGER.error("Execution exception", e);
            } catch (Exception e) {
                LOGGER.error("Execution exception", e);
            }
        }
        else {
            applicationFuture = executor.submit(new Callable<Environment>() {
                public Environment call() throws Exception {
                    LOGGER.debug("initializing Dropwizard thread: {}", Thread.currentThread().getName());
                    MaintenanceApplication application = new MaintenanceApplication();

                    try {
                        File configuration = new File(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile()).getParent()
                                + System.getProperty("file.separator")
                                + "configuration.yml");
                        if (configuration.exists()) {
                            application.run("server", configuration.getAbsolutePath());
                            return application.getEnvironment();
                        }
                    } catch (Exception e) {
                        LOGGER.error("Exception occurred while starting", e);
                    }
                    return null;
                }
            });
        }
    }

    private void initLogger() {
        TextAreaAppender.setList(loggingTextArea);
    }
}
