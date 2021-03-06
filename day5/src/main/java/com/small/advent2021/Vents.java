package com.small.advent2021;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Slf4j
public class Vents {
    private List<LineSegment> lineList;
    private Map<Point, Long> matrix;

    @SuppressWarnings("SameParameterValue")
    Vents(String fileName) {
        processFile(fileName);
        putCountedPointsOnMatrix();
    }

    public static void main(String[] args) {
        Vents vents = new Vents("input.txt");
        log.info("Overlapping points =" + vents.countOverlappingPoints());
    }

    private void putCountedPointsOnMatrix() {
        List<Point> list = new ArrayList<>();
        for (LineSegment line : lineList) {
            List<Point> points = line.toPoints();
            list.addAll(points);
        }
        matrix = list.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    long countOverlappingPoints() {
        return matrix.values().stream().filter(v -> v > 1).count();
    }

    long countVertical() {
        return lineList.stream().filter(LineSegment::isVertical).count();
    }

    long countHorizontal() {
        return lineList.stream().filter(LineSegment::isHorizontal).count();
    }

    private void processFile(String fileName) {
        Path path = Path.of(fileName);
        log.info("Path =  " + path.toAbsolutePath());
        try (Stream<String> s = Files.lines(path)) {
            lineList = s.map(l -> new LineSegment(
                    l.replace(" -> ", ","))).toList();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static class LineSegment {
        final String[] pointArray;

        LineSegment(String input) {
            pointArray = input.split(",");
        }

        public boolean isHorizontal() {
            return (Objects.equals(pointArray[0], pointArray[2]));
        }

        public boolean isVertical() {
            return (Objects.equals(pointArray[1], pointArray[3]));
        }

        List<Point> toPoints() {
            int fromX = Integer.parseInt(pointArray[0]);
            int fromY = Integer.parseInt(pointArray[1]);
            int toX = Integer.parseInt(pointArray[2]);
            int toY = Integer.parseInt(pointArray[3]);

            List<Point> list = new ArrayList<>();
            if (isHorizontal()) {
                for (int y = min(fromY, toY); y <= max(fromY, toY); ++y) {
                    list.add(new Point(fromX, y));
                }
            } else if (isVertical()) {
                for (int x = min(fromX, toX); x <= max(fromX, toX); ++x) {
                    list.add(new Point(x, fromY));
                }
            } else {
                int xAddend = (fromX < toX) ? 1 : -1;
                int yAddend = (fromY < toY) ? 1 : -1;

                for (int x = fromX, y = fromY;
                        x != (toX + xAddend) && y != (toY + yAddend);
                        x += xAddend, y += yAddend) {
                    list.add(new Point(x, y));
                }

            }
            return list;
        }

        public String toString() {
            return Arrays.toString(pointArray);
        }

    }

    @SuppressWarnings("ClassCanBeRecord")
    private static class Point {
        final int x;
        final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

    }
}
