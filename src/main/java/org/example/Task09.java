package org.example;

import org.example.utils.StreamUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Task09 implements Task {

    public long solve(String filePath) throws IOException {
        return Files.lines(Path.of(filePath))
                .mapToInt(this::interpolateLine)
                .sum();
    }

    private int interpolateLine(String line) {
        var numbers = Pattern.compile("-?\\d+").matcher(line).results()
                .map(MatchResult::group)
                .map(Integer::parseInt)
                .toList();

        return interpolateParsedLine(numbers, false);
    }

    private int interpolateParsedLine(List<Integer> numbers, boolean shouldNegateResult) {
        if (numbers.stream().allMatch(n -> n == 0)) return 0;

        var deltas = StreamUtils.createStreamWithIndex(numbers)
                .skip(1)
                .map(itemWithIndex -> itemWithIndex.item() - numbers.get(itemWithIndex.index() - 1))
                .toList();

        return interpolateParsedLine(deltas, !shouldNegateResult)
                + (shouldNegateResult ? -1 : 1) * numbers.get(0);
    }
}