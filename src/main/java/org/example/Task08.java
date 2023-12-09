package org.example;

import org.example.utils.MathUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Task08 implements Task {

    public long solve(String filePath) throws IOException {
        var lines = Files.lines(Path.of(filePath)).toList();
        var path = lines.get(0).chars().boxed().toList();
        var nodes = lines.stream()
                .skip(2)
                .map(Node::fromString)
                .collect(Collectors.toMap(n -> n.name, n -> n));

        var startingNodes = nodes.values().stream().filter(n -> n.name.endsWith("A")).toList();
        var cycleLengths = new ArrayList<Long>();

        for (var startingNode: startingNodes) {
            var currentNode = startingNode;
            int index = 0;

            while (!currentNode.name.endsWith("Z")) {
                var direction = path.get(index++ % path.size());
                currentNode = (direction == 'L')
                        ? nodes.get(currentNode.left())
                        : nodes.get(currentNode.right());
            }

            cycleLengths.add((long) index);
        }

        return cycleLengths.stream().reduce(MathUtils::lcm).orElseThrow();
    }

    record Node(String name, String left, String right) {
        public static Node fromString(String s) {
            var matches = Pattern.compile("\\w+").matcher(s).results().map(MatchResult::group).toList();
            return new Node(matches.get(0), matches.get(1), matches.get(2));
        }
    }
}