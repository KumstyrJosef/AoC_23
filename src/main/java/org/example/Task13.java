package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Task13 implements Task {

    public long solve(String filePath) throws IOException {
        var input = Files.readAllLines(Path.of(filePath));

        return createMaps(input).stream()
                .mapToLong(this::countReflectionValue)
                .sum();
    }

    private List<List<String>> createMaps(List<String> strings) {
        List<List<String>> maps = new ArrayList<>();
        maps.add(new ArrayList<>());

        for (String s : strings) {
            if (s.isEmpty()) {
                maps.add(new ArrayList<>());
            } else maps.get(maps.size() - 1).add(s);
        }

        return maps;
    }

    private long countReflectionValue(List<String> map) {
        return 100 * countLineReflectionValue(map) + countLineReflectionValue(transpose(map));
    }

    private static long countLineReflectionValue(List<String> map) {
        return IntStream.range(1, map.size())
                .map(i -> countReflectionValueAtLine(map, i))
                .sum();
    }

    private static int countReflectionValueAtLine(List<String> map, int line) {
        int step = 0;
        int smudges = 0;

        while(true) {
            if (line - step - 1 < 0 || line + step >= map.size()) return smudges == 1 ? line : 0;
            smudges += countDifferencesBetweenStrings(map.get(line - step - 1), map.get(line + step));
            step++;
        }
    }

    private static int countDifferencesBetweenStrings(String s1, String s2) {
        return (int)IntStream.range(0, s1.length())
                .filter(i -> s1.charAt(i) != s2.charAt(i))
                .count();
    }

    private static List<String> transpose(List<String> map) {
        return IntStream.range(0, map.get(0).length())
                .mapToObj(i -> map.stream().map(s -> s.charAt(i)).map(Objects::toString).collect(Collectors.joining()))
                .toList();
    }
}