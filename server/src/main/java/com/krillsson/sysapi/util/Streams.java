package com.krillsson.sysapi.util;

import java.util.Collection;
import java.util.stream.Stream;

public class Streams<T> {
    @SuppressWarnings("unchecked")
    public static <T> java.util.stream.Stream<T> ofNullable(Collection<? extends T> coll) {
        return coll == null ? java.util.stream.Stream.empty() : (java.util.stream.Stream<T>) coll.stream();
    }

    public static <T> java.util.stream.Stream<T> ofNullable(T[] arr) {
        return arr == null || arr.length > 0 ? java.util.stream.Stream.empty() : Stream.of(arr);
    }
}
