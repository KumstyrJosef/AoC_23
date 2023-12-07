package org.example;

import org.example.utils.StreamUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Task07 implements Task  {

    public long solve(String filePath) throws IOException {
        var orderedCards = Files.lines(Path.of(filePath))
                .map(s -> s.split(" "))
                .map(Card::fromStringArray)
                .sorted(Comparator.comparing(c -> c.cards))
                .sorted(Comparator.comparingLong(Card::value))
                .toList();

        return StreamUtils.createStreamWithIndex(orderedCards)
                .mapToLong(cardWithIndex -> cardWithIndex.item().bid * (cardWithIndex.index() + 1))
                .sum();
    }

    record Card(long value, String cards, long bid) {
        public static Card fromStringArray(String[] array) {
            var value = Stream.of('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')
                    .map(c -> array[0].replace('J', c))
                    .mapToLong(s -> s.chars().mapToLong(c -> s.chars().filter(arrayChar -> arrayChar == c).count()).sum())
                    .max()
                    .orElseThrow();

            var cards = array[0] // huh
                    .replace("A", "Z")
                    .replace("2", "B")
                    .replace("3", "C")
                    .replace("4", "D")
                    .replace("5", "E")
                    .replace("6", "F")
                    .replace("7", "G")
                    .replace("8", "H")
                    .replace("9", "I")
                    .replace("J", "A")
                    .replace("Q", "X")
                    .replace("K", "Y");

            return new Card(value, cards, Integer.parseInt(array[1]));
        }
    }
}