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
