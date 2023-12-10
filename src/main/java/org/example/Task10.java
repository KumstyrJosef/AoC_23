package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.Task10.Direction.*;

public class Task10 implements Task {

    public long solve(String filePath) throws IOException {
        var maze = parseMaze(filePath);
        var startCoordinates = findStartCoordinates(maze);
        var loop = findLoop(maze, startCoordinates);
        removeTilesNotInMainLoop(maze, loop);
        return maze.stream().mapToInt(this::countTilesInsideMainLoop).sum();
    }

    private List<List<Character>> parseMaze(String filePath) throws IOException {
        return Files.lines(Path.of(filePath))
                .map(line -> line.chars().mapToObj(i -> ((char) i)).collect(Collectors.toList()))
                .toList();
    }

    private Coordinates findStartCoordinates(List<List<Character>> maze) {
        for (int i = 0; i < maze.size(); i++) {
            for (int j = 0; j < maze.get(i).size(); j++) {
                if (maze.get(i).get(j) == 'S') return new Coordinates(i, j);
            }
        }

        throw new RuntimeException("Start not found.");
    }

    private List<Coordinates> findLoop(List<List<Character>> maze, Coordinates startCoordinates) {
        for (var direction : Direction.values()) {
            List<Coordinates> loop = new ArrayList<>();
            loop.add(startCoordinates);

            if (findLoop(maze, startCoordinates, direction, loop)) return loop;
        }

        throw new RuntimeException("No loop found.");
    }

    private boolean findLoop(List<List<Character>> maze, Coordinates coordinates, Direction direction, List<Coordinates> loop) {
        while (true) {
            coordinates = updateCoordinates(coordinates, direction);
            if (coordinates.equals(loop.get(0))) return true;
            if (isCycle(coordinates, loop)) return false;
            loop.add(coordinates);
            direction = switch(getMazeTile(maze, coordinates)){
                case '|' -> direction == UP ? UP : DOWN;
                case '-' -> direction == LEFT ? LEFT : RIGHT;
                case 'L' -> direction == DOWN ? RIGHT : UP;
                case 'J' -> direction == DOWN ? LEFT : UP;
                case '7' -> direction == RIGHT ? DOWN : LEFT;
                case 'F' -> direction == UP ? RIGHT : DOWN;
                default -> null;
            };
            if (direction == null) return false;
        }
    }

    private static boolean isCycle(Coordinates coordinates, List<Coordinates> loop) {
        return loop.stream().skip(1).anyMatch(oldCoordinates -> oldCoordinates == coordinates);
    }

    private Character getMazeTile(List<List<Character>> maze, Coordinates startCoordinates) {
        return maze.get(startCoordinates.line).get(startCoordinates.column);
    }

    private Coordinates updateCoordinates(Coordinates lastCoordinates, Direction lastDirection) {
        return switch(lastDirection) {
            case LEFT -> new Coordinates(lastCoordinates.line, lastCoordinates.column - 1);
            case RIGHT -> new Coordinates(lastCoordinates.line, lastCoordinates.column + 1);
            case UP -> new Coordinates(lastCoordinates.line - 1, lastCoordinates.column);
            case DOWN -> new Coordinates(lastCoordinates.line + 1, lastCoordinates.column);
        };
    }

    private static void removeTilesNotInMainLoop(List<List<Character>> maze, List<Coordinates> loop) {
        for (int i = 0; i < maze.size(); i++) {
            for (int j = 0; j < maze.get(i).size(); j++) {
                if (!loop.contains(new Coordinates(i, j))) maze.get(i).set(j, '.');
            }
        }
    }

    private int countTilesInsideMainLoop(List<Character> characters) {
        int count = 0;
        int countTop = 0;

        for (Character character : characters) {
            if (character == '|' || character == 'L' || character == 'J') countTop++;
            if (character == '.' && countTop % 2 == 1) count++;
        }
        return count;
    }

    record Coordinates(int line, int column) { }

    enum Direction {RIGHT, LEFT, UP, DOWN}
}