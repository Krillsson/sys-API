package com.krillsson.sysapi.gui.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class TextAreaAppender extends AppenderBase<ILoggingEvent> {
    private static volatile TextArea textArea = null;

    public static void setTextArea(final TextArea textArea) {
        TextAreaAppender.textArea = textArea;
    }

    @Override
    protected void append(ILoggingEvent eventObject) {

        final String message = eventObject.getFormattedMessage();

        // Append formatted message to text area using the Thread.
        try {
            Platform.runLater(new Runnable() {
                public void run() {
                    try {
                        if (textArea != null) {
                            if (textArea.getText().length() == 0) {
                                textArea.setText(message);
                            } else {
                                textArea.selectEnd();
                                textArea.insertText(textArea.getText().length(),
                                        message);
                            }
                        }
                    } catch (final Throwable t) {
                        System.out.println("Unable to append log to text area: "
                                + t.getMessage());
                    }
                }
            });
        } catch (final IllegalStateException e) {
            // ignore case when the platform hasn't yet been iniitialized
        }
    }
}
