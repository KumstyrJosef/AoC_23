package org.example.utils;


import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamUtils {
    public static <T> Stream<ItemWithIndex<T>> createStreamWithIndex(List<T> input) {
        return IntStream.range(0, input.size())
                .mapToObj(index -> new ItemWithIndex<>(input.get(index), index));
    }
}

