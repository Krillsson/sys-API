package com.krillsson.sysapi.gui.logback;


import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("TextAreaAppenderFactory")
public class TextAreaAppenderFactory extends io.dropwizard.logging.AbstractAppenderFactory {
    public Appender<ILoggingEvent> build(LoggerContext loggerContext, String s, Layout<ILoggingEvent> layout) {
            return null;
    }

    class TextAreaAppender extends AppenderBase<ILoggingEvent> {
        @Override
        protected void append(ILoggingEvent eventObject) {
            System.err.print("got event");
        }
    }
}
