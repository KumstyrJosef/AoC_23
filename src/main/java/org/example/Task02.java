package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToLongFunction;

public class Task02 implements Task {
    public long solve(String filePath) throws IOException {
        return Files.lines(Path.of(filePath))
                .map(GameLog::createFromText)
                .mapToLong(this::findPowerOfMinimumSet)
                .sum();
    }

    private long findPowerOfMinimumSet(GameLog gameLog) {
        var minimumOfRed = findMinimumForColor(gameLog, GameRound::red);
        var minimumOfGreen = findMinimumForColor(gameLog, GameRound::green);
        var minimumOfBlue = findMinimumForColor(gameLog, GameRound::blue);

        return minimumOfRed * minimumOfGreen * minimumOfBlue;
    }

    private static long findMinimumForColor(GameLog gameLog, ToLongFunction<GameRound> red) {
        return gameLog.gameRound().stream()
                .mapToLong(red)
                .max()
                .getAsLong();
    }
}

record GameLog(int id, List<GameRound> gameRound) {
    public static GameLog createFromText(String text) {
        var parts = text.split("Game |:|;");

        var id = Integer.parseInt(parts[1]);
        var gameRound = createGameRounds(parts);

        return new GameLog(id, gameRound);
    }

    private static List<GameRound> createGameRounds(String[] parts) {
        return Arrays.stream(parts)
                .skip(2)
                .map(GameRound::createFromText)
                .toList();
    }
}

record GameRound(int red, int green, int blue) {
    public static GameRound createFromText(String text) {
        var parts = text.split(",");

        var red = findAndAssignColor(parts, "red");
        var green = findAndAssignColor(parts, "green");
        var blue = findAndAssignColor(parts, "blue");

        return new GameRound(red, green, blue);
    }

    private static int findAndAssignColor(String[] parts, String color) {
        return Arrays.stream(parts)
                .filter(p -> p.contains(color))
                .map(p -> p.trim().split(" ")[0])
                .map(Integer::parseInt)
                .findFirst()
                .orElse(0);
    }
}
