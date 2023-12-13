package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Task12 implements Task {

    public long solve(String filePath) throws IOException {
        return Files.lines(Path.of(filePath))
                .map(Line::fromString)
                .mapToLong(Line::countOptions)
                .sum();
    }

    record Line (String conditions, List<Integer> groups) {
        public static Line fromString(String s) {
            var parts = s.split("\\s");
            var multipliedInput = String.join("?", Stream.generate(() -> parts[0]).limit(5).toList())
                    + " " + String.join(",", Stream.generate(() -> parts[1]).limit(5).toList());

            var result = Pattern.compile("[?.#]+|\\d+").matcher(multipliedInput).results().toList();

            var conditions = result.get(0).group();
            var groups = result.stream().skip(1).map(MatchResult::group).map(Integer::parseInt).toList();

            return new Line(conditions, groups);
        }

        record MapIndex (String remainingConditions, int groupIndex) { }
        static Map<MapIndex, Long> cache;

        public long countOptions() {
            cache = new HashMap<>();
            return countOptionsWithCache(conditions, 0);
        }

        private long countOptionsWithCache(String remainingConditions, int groupIndex) {
            var index = new MapIndex(remainingConditions, groupIndex);

            if (!cache.containsKey(index)) {
                var result = countOptions(remainingConditions, groupIndex);
                cache.put(index, result);
            }
            return cache.get(index);
        }

        private long countOptions(String remainingConditions, int groupIndex) {
            // Skip dots
            while (remainingConditions.startsWith(".")) remainingConditions = remainingConditions.substring(1);

            // Handle recursion end
            if (groupIndex == groups.size()) return remainingConditions.contains("#") ? 0 : 1;
            if (remainingConditions.isEmpty()) return 0;

            int currentGroupSize = groups.get(groupIndex);

            // Handle when close to the end
            if (currentGroupSize > remainingConditions.length()) return 0;
            if (currentGroupSize == remainingConditions.length()) {
                return remainingConditions.contains(".") ? 0 : countOptionsWithCache("", groupIndex + 1);
            }

            // Handle general case
            var cannotSkipNextChar = remainingConditions.startsWith("#");
            var cannotReplaceWholeGroup = remainingConditions.substring(0, currentGroupSize).contains(".") || remainingConditions.charAt(currentGroupSize) == '#'; // try stream??

            return (cannotReplaceWholeGroup ? 0 : countOptionsWithCache(remainingConditions.substring(currentGroupSize + 1), groupIndex + 1))
                    + (cannotSkipNextChar ? 0 : countOptionsWithCache(remainingConditions.substring(1), groupIndex));
        }
    }
}