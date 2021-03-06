package com.small.advent2021;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class Submarine {

    private int horizontalPosition = 0;
    private int depth = 0;
    private int aim = 0;

    public static void main(String[] args) throws IOException {

        Submarine sub = new Submarine();
        List<Command> list = sub.getListFromFile("input.txt");
        sub.processList(list);
        int position = sub.getPos();
        log.info("Final Position = " + position);
    }

    public int getHorizontalPosition() {
        return horizontalPosition;
    }

    public void forward(int distance) {
        horizontalPosition += distance;
        depth += aim * distance;
    }

    public int getDepth() {
        return depth;
    }

    public void down(int distance) {
        aim += distance;
    }

    public void up(int distance) {
        aim -= distance;
    }

    public int getPos() {
        return horizontalPosition * depth;
    }

    public void processList(List<Command> commands) {
        for (Command cmd : commands) {
            move(cmd);
        }
    }

    public void processList(List<Command> commands, int steps) {
        for (int step = 0; step < steps; ++step) {
            move(commands.get(step));
        }
    }

    private void move(Command cmd) {
        switch (cmd.action) {
            case "up" -> up(cmd.distance);
            case "down" -> down(cmd.distance);
            case "forward" -> forward(cmd.distance);
            default -> throw new IllegalStateException("Unexpected value: " + cmd.action);
        }
    }

    public List<Command> getListFromFile(String fileName) throws IOException {
        Path path = Path.of(fileName);

        try(Stream<String> lines = Files.lines(path)) {
            return lines
                    .map(Command::new)
                    .toList();
        }
    }

    static class Command {
        final String action;
        final Integer distance;

        Command(String s) {
            var a = s.split(" ");
            action = a[0];
            distance = Integer.parseInt(a[1]);
        }
    }

}
