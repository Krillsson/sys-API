package com.krillsson.sysapi.util;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Streams {
    @SuppressWarnings("unchecked")
    public static <T> java.util.stream.Stream<T> ofNullable(Collection<? extends T> coll) {
        return coll == null ? java.util.stream.Stream.empty() : (java.util.stream.Stream<T>) coll.stream();
    }

    public static <T> java.util.stream.Stream<T> ofNullable(T[] arr) {
        return arr == null || arr.length > 0 ? java.util.stream.Stream.empty() : Stream.of(arr);
    }

    public static <T, K, U> Collector<T, ?, Map<K, U>> toLinkedMap(
            Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends U> valueMapper) {
        return Collectors.toMap(keyMapper, valueMapper,
                (u, v) -> {
                    throw new IllegalStateException(String.format("Duplicate key %s", u));
                },
                LinkedHashMap::new);
    }
}
