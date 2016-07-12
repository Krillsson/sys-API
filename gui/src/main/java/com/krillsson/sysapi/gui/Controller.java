package com.krillsson.sysapi.gui;

import com.krillsson.sysapi.MaintenanceApplication;
import com.krillsson.sysapi.gui.logback.TextAreaAppender;
import io.dropwizard.logging.DefaultLoggingFactory;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.slf4j.Logger;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller {

    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Controller.class.getSimpleName());

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    @FXML
    private TextArea loggingTextArea;

    @FXML
    public void initialize() {
        initLogger();
        startDropwizard();
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        //executor.shutdown();
    }

    private void startDropwizard() {
        executor.submit(new Runnable() {
            public void run() {
                LOGGER.debug("initializing");
                MaintenanceApplication application = new MaintenanceApplication();

                try {
                    File configuration = new File(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile()).getParent()
                            + System.getProperty("file.separator")
                            + "configuration.yml");
                    if (configuration.exists()) {
                        application.run("server", configuration.getAbsolutePath());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initLogger() {
        TextAreaAppender.setTextArea(loggingTextArea);
    }
}
