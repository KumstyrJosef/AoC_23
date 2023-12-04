package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Task04 implements Task  {

    public long solve(String filePath) throws IOException {
        var cards = Files.lines(Path.of(filePath))
                .map(Card::createFromText)
                .toList();

        for (int i = 0; i < cards.size(); i++) {
            var card = cards.get(i);
            IntStream.range(i + 1, i + 1 + card.numberOfMatchingNumbers)
                    .filter(x -> x < cards.size())
                    .forEach(x -> cards.get(x).count += card.count);
        }

        return cards.stream().mapToInt(c -> c.count).sum();
    }
}

class Card {
    public int numberOfMatchingNumbers;
    public int count = 1;

    public static Card createFromText(String text) {
        var parts = text.split("[:|]");
        var winningNumbers = createList(parts[1]).collect(Collectors.toSet());

        return new Card() {{
            numberOfMatchingNumbers = (int) createList(parts[2]).filter(winningNumbers::contains).count();
        }};
    }

    private static Stream<Integer> createList(String s) {
        return Pattern.compile("\\d+").matcher(s).results()
                .map(MatchResult::group)
                .map(Integer::parseInt);
    }
}