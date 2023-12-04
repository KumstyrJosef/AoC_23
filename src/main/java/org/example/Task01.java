package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task01 {
    public long countCalibrationValueForFile(String filePath) throws IOException {
        return Files.lines(Path.of(filePath))
                .mapToLong(this::countCalibrationValueForLine)
                .sum();
    }

    private int countCalibrationValueForLine(String line) {
        var firstValue = countSingleCalibrationValue(line, false);
        var secondValue = countSingleCalibrationValue(line, true);

        return 10 * firstValue + secondValue;
    }

    private int countSingleCalibrationValue(String line, boolean shouldReverse) {
        return findAndReplaceFirstStringNumber(line, shouldReverse)
                .chars()
                .filter(this::isNumber)
                .map(c -> (c - '0'))
                .findFirst()
                .getAsInt();
    }

    private String findAndReplaceFirstStringNumber(String line, boolean shouldReverse) {
        Pattern p = Pattern.compile(reverseIfShould("one|two|three|four|five|six|seven|eight|nine", shouldReverse));
        Matcher m = p.matcher(reverseIfShould(line, shouldReverse));

        return m.replaceFirst(matchResult -> replaceStringNumber(matchResult, shouldReverse));
    }

    private String replaceStringNumber(MatchResult matchResult, boolean shouldReverse) {
        return switch(reverseIfShould(matchResult.group(), shouldReverse)) {
            case "one" -> "1";
            case "two" -> "2";
            case "three" -> "3";
            case "four" -> "4";
            case "five" -> "5";
            case "six" -> "6";
            case "seven" -> "7";
            case "eight" -> "8";
            case "nine" -> "9";
            default -> throw new RuntimeException(":( " + matchResult.group());
        };
    }

    private String reverseIfShould(String s, boolean shouldReverse) {
        return shouldReverse
                ? reverse(s)
                : s;
    }
    private String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    private boolean isNumber(int i) {
        return i >= '0' && i <= '9';
    }
}
