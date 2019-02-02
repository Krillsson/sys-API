package com.krillsson.sysapi.util;

import javax.inject.Singleton;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Provider
@Singleton
public class ZonedDateTimeConverter implements ParamConverterProvider {

    @Override public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {

        if (!rawType.isAssignableFrom(ZonedDateTime.class)) {
            return null;
        }
        return new ParamConverter<T>() {
            @Override
            @SuppressWarnings("unchecked")
            public T fromString(final String value) {
                if (value == null) {
                    throw new IllegalArgumentException("value may not be null");
                }
                return (T) ZonedDateTime.parse(value);
            }

            @Override
            public String toString(final T value) {
                return ((ZonedDateTime) value).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            }
        };
    }

}