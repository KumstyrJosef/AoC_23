package org.example;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.lang.Math.*;

public class Task05 implements Task  {

    public long solve(String filePath) throws IOException {
        var lines = Files.lines(Path.of(filePath)).toList();

        Set<Range> ranges = Pattern.compile("(\\d+) (\\d+)")
                .matcher(lines.get(0))
                .results()
                .map(r -> new Range(Long.parseLong(r.group(1)), Long.parseLong(r.group(1)) + Long.parseLong(r.group(2))))
                .collect(Collectors.toSet());

        List<Set<List<Long>>> maps = createMaps(lines.stream().skip(2));

        long i = 0L;
        while(true) {
            var seed = i;
            var step = Long.MAX_VALUE;

            for (var map : Lists.reverse(maps)) {
                var result = findNewSeedValue(seed, map);
                seed = result.newSeed();
                step = min(step, result.step());
            }

            var x = findLowestIntersectingSeed(ranges, seed, step);
            if (x.isPresent()) {
                return i + max(0, x.getAsLong() - seed);
            }

            i += step;
        }
    }

    private static OptionalLong findLowestIntersectingSeed(Set<Range> ranges, long seed, long step) {
        return ranges.stream()
                .filter(r -> max(seed, r.min()) <= min(seed + step, r.max()) - 1)
                .mapToLong(Range::min).min();
    }

    private Result findNewSeedValue(Long seed, Set<List<Long>> lists) {
        for(var list : lists) {
            if (seed >= list.get(0) && seed < list.get(0) + list.get(2))
                return new Result(seed - list.get(0) + list.get(1), list.get(0) + list.get(2) - seed);
        }

        return new Result(seed, lists.stream().mapToLong(list -> list.get(0)).filter(x -> x > seed).min().orElse(Long.MAX_VALUE) - seed);
    }

    private List<Set<List<Long>>> createMaps(Stream<String> input) {
        List<Set<List<Long>>> maps = new ArrayList<>();
        input.forEach(line -> parseLine(line, maps));
        return maps;
    }

    private void parseLine(String line, List<Set<List<Long>>> maps) {
        if (line.contains(":")) {
            maps.add(new HashSet<>());
            return;
        }

        if (line.isBlank()) return;

        maps.get(maps.size() - 1).add(Pattern.compile("\\d+").matcher(line).results().map(MatchResult::group).map(Long::parseLong).toList());
    }
}

record Range(Long min, Long max) {}
record Result(Long newSeed, Long step) {}