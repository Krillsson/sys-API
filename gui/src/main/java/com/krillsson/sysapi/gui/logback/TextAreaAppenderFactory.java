package com.krillsson.sysapi.gui.logback;


import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Layout;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.logging.AbstractAppenderFactory;

@JsonTypeName("textarea")
public class TextAreaAppenderFactory extends AbstractAppenderFactory {
    public Appender<ILoggingEvent> build(LoggerContext loggerContext, String s, Layout<ILoggingEvent> layout) {
        final TextAreaAppender appender = new TextAreaAppender();
        appender.setName("textarea-appender");
        appender.setContext(loggerContext);
        appender.start();
        return wrapAsync(appender);
    }

}
