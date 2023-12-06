package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Task06 implements Task  {

    public long solve(String filePath) throws IOException {
        var lines = Files.lines(Path.of(filePath)).map(s -> s.replace(" ", "")).toList();

        long time = parseLine(lines.get(0));
        long record = parseLine(lines.get(1));

        return LongStream.range(0, time)
                .map(waitTime -> waitTime * (time - waitTime))
                .filter(distance -> distance > record)
                .count();
    }

    private static Long parseLine(String line) {
        return Pattern.compile("\\d+")
                .matcher(line)
                .results()
                .map(MatchResult::group)
                .map(Long::parseLong)
                .findAny().orElseThrow();
    }
}