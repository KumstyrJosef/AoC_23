package org.example;

import org.example.utils.StreamUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Task11 implements Task {

    public long solve(String filePath) throws IOException {
        var galaxies = StreamUtils.createStreamWithIndex(Files.readAllLines(Path.of(filePath)))
                .flatMap(lineWithIndex -> parseLine(lineWithIndex.item(), lineWithIndex.index()))
                .toList();

        var emptyColumns = IntStream.range(0, galaxies.stream().mapToInt(g -> g.column).max().orElseThrow())
                .filter(column -> galaxies.stream().allMatch(galaxy -> galaxy.column != column))
                .boxed().toList();

        var emptyLines = IntStream.range(0, galaxies.stream().mapToInt(g -> g.line).max().orElseThrow())
                .filter(line -> galaxies.stream().allMatch(galaxy -> galaxy.line != line))
                .boxed().toList();

        return galaxies.stream()
                .flatMap(galaxy1 -> galaxies.stream().map(galaxy2 -> new AbstractMap.SimpleEntry<>(galaxy1, galaxy2)))
                .mapToLong(galaxyPair -> countDistance(galaxyPair, emptyColumns, emptyLines))
                .sum() / 2;
    }

    private long countDistance(AbstractMap.SimpleEntry<Galaxy, Galaxy> galaxyPair, List<Integer> emptyColumns, List<Integer> emptyLines) {
        var columnMin = Math.min(galaxyPair.getKey().column, galaxyPair.getValue().column);
        var columnMax = Math.max(galaxyPair.getKey().column, galaxyPair.getValue().column);
        var lineMin = Math.min(galaxyPair.getKey().line, galaxyPair.getValue().line);
        var lineMax = Math.max(galaxyPair.getKey().line, galaxyPair.getValue().line);

        return columnMax - columnMin + lineMax - lineMin + 999_999 * countNumberOfEmpties(emptyColumns, columnMin, columnMax) + 999_999 * countNumberOfEmpties(emptyLines, lineMin, lineMax);
    }

    private long countNumberOfEmpties(List<Integer> empties, int min, int max) {
        return empties.stream().filter(c -> c > min && c < max).mapToInt(i -> i).count();
    }

    private Stream<Galaxy> parseLine(String line, Integer lineNumber) {
        return StreamUtils.createStreamWithIndex(line.chars().mapToObj(c -> (char)c).toList())
                .filter(charWithIndex -> charWithIndex.item().equals('#'))
                .map(charWithIndex -> new Galaxy(lineNumber, charWithIndex.index()));
    }

    record Galaxy (int line, int column) { }
}