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
package com.krillsson.sysapi.gui.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextAreaAppender extends AppenderBase<ILoggingEvent> {

    private static volatile ListView list = null;
    static ObservableList<String> values = FXCollections.observableArrayList();

    public static void setList(final ListView list) {
        TextAreaAppender.list = list;
        list.setItems(values);
    }

    @Override
    protected void append(ILoggingEvent eventObject) {

        final String message = eventObject.getFormattedMessage();

        // Append formatted message to text area using the Thread.
        try {
            Platform.runLater(new Runnable() {
                public void run() {
                    try {
                        if (list != null) {
                            values.add(message);
                        }
                    } catch (final Throwable t) {
                        System.out.println("Unable to append log to text area: "
                                + t.getMessage());
                    }
                }
            });
        } catch (final IllegalStateException e) {
            // ignore case when the platform hasn't yet been initialized
        }
    }
}
