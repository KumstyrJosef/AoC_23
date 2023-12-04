package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.IntStream;

public class Task03 implements Task  {
    private final List<Gear> gears = new ArrayList<>();
    private final Map<Integer, Map<Integer, List<Integer>>> numbers = new HashMap<>();

    public long solve(String filePath) throws IOException {
        var lines = Files.readAllLines(Path.of(filePath));

        IntStream.range(0, lines.size())
                .forEach(i -> Pattern.compile("\\d+|[*]").matcher(lines.get(i)).results().forEach(result -> parseResult(result, i)));

        return gears.stream()
                .filter(gear -> numbers.containsKey(gear.line()) && numbers.get(gear.line()).containsKey(gear.column()))
                .map(gear -> numbers.get(gear.line()).get(gear.column()))
                .filter(list -> list.size() == 2)
                .mapToInt(list -> list.get(0) * list.get(1))
                .sum();
    }

    private void parseResult(MatchResult result, int line) {
        if (!result.group().equals("*")) {
            for (int i = line - 1; i <= line + 1; i++)
                for (int j = result.start() - 1; j <= result.end(); j++) {
                    numbers.computeIfAbsent(i, key -> new HashMap<>());
                    numbers.get(i).computeIfAbsent(j, key -> new ArrayList<>());
                    numbers.get(i).get(j).add(Integer.parseInt(result.group()));
                }
        } else gears.add(new Gear(line, result.start()));
    }
}

record Gear(int line, int column) { }