package com.small.advent2021.day2;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;


public class Submarine {
    private static final Logger LOGGER = Logger.getLogger(Submarine.class);

    private int horiz = 0;
    private int depth = 0;
    private int aim = 0;

    public static void main(String[] args) throws IOException {

        Submarine sub = new Submarine();
        List<Command> list = sub.getListFromFile("day2/input.txt");
        sub.processList(list);
        int position = sub.getPos();
        LOGGER.info("Final Position = " + position);
    }

    public int getHoriz() {
        return horiz;
    }

    public void forward(int distance) {
        horiz += distance;
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
        return horiz * depth;
    }

    public void processList(List<Command> cmds) {
        for (Command cmd : cmds) {
            move(cmd);
        }
    }

    public void processList(List<Command> cmds, int steps) {
        for (int step = 0; step < steps; ++step) {
            move(cmds.get(step));
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

        return Files
                .lines(path)
                .map(Command::new)
                .toList();
    }

    class Command {
        String action;
        Integer distance;

        Command(String s) {
            var a = s.split(" ");
            action = a[0];
            distance = Integer.parseInt(a[1]);
        }
    }

}
