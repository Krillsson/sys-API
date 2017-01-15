/*
 * Sys-Api (https://github.com/Krillsson/sys-api)
 *
 * Copyright 2017 Christian Jensen / Krillsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Maintainers:
 * contact[at]christian-jensen[dot]se
 */
package com.krillsson.sysapi.gui;

import com.krillsson.sysapi.SystemApiApplication;
import com.krillsson.sysapi.config.SystemApiConfiguration;
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

    private final DropwizardTestSupport<SystemApiConfiguration> MAINTENANCE_APPLICATION =
            new DropwizardTestSupport<SystemApiConfiguration>(SystemApiApplication.class, resourcePath());


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
