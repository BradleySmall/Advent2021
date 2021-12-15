package com.small.advent2021;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.String.format;

@Slf4j
public class Origami {

    private List<String> dotList;
    private List<String> cmdList;
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;
    private List<Pair> pairList;

    Origami(String fileName) {
        processFile(fileName);
    }

    public static void main(String[] args) {
        log.info("Done!");
    }

    private void processFile(String fileName) {
        Path path = Path.of(fileName);

        try (Stream<String> s = Files.lines(path)) {
            List<String> stringList = s
                    .toList();
            dotList = new ArrayList<>();
            cmdList = new ArrayList<>();
            boolean cmds = false;
            for (String str : stringList) {
                if (cmds) {
                    cmdList.add(str);
                } else {
                    if (str.equals("")) {
                        cmds = true;
                    } else {
                        dotList.add(str);
                    }
                }
            }
            log.info("Cmds" + cmdList);
            log.info("Dots" + dotList);
            populatePageExtents();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPairsCount() {
        return pairList.size();
    }
    public int getDotsSize() {
        return dotList.size();
    }

    public int getCmdsSize() {
        return cmdList.size();
    }

    public Pair getMinXY() {
        return new Pair(minX, minY);
    }

    public Pair getMaxXY() {
        return new Pair(maxX, maxY);
    }

    private void populatePageExtents() {
        pairList = dotList.stream()
                .map(e -> {
                    var p = e.split(",");
                    return new Pair(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
                })
                .toList();
        log.info(String.valueOf(pairList));

        minX = 0;
        minY = 0;
        maxX = 0;
        maxY = 0;

        for (var p : pairList) {
            minX = min(minX, p.x);
            minY = min(minY, p.y);
            maxX = max(maxX, p.x);
            maxY = max(maxY, p.y);
        }
        log.info(format("%d %d %d %d",minX, minY, maxX, maxY));
    }

    public Pair yFoldTranslation(int foldLine, Pair pair) {
        if (pair.y > foldLine) {
            return new Pair(pair.x, foldLine - (pair.y - foldLine));
        }
        return pair;
    }

    public Pair xFoldTranslation(int foldLine, Pair pair) {
        if (pair.x > foldLine) {
            return new Pair(foldLine - (pair.x - foldLine), pair.y);
        }
        return pair;
    }

    public void performYFold(int foldLine) {
        Set<Pair> pairs = new HashSet<>();

        for (Pair pair : pairList) {
            pairs.add(yFoldTranslation(foldLine, pair));
        }
        log.info("Pairs = " + pairs);

        pairList = new ArrayList<>(pairs);
    }

    public void performXFold(int foldLine) {

        Set<Pair> pairs = new HashSet<>();

        for (Pair pair : pairList) {
            pairs.add(xFoldTranslation(foldLine, pair));
        }
        log.info("Pairs = " + pairs);

        pairList = new ArrayList<>(pairs);
    }

    private static class Pair {
        int x;
        int y;
        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public String toString() {
            return format("%d,%d",x, y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return x == pair.x && y == pair.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
