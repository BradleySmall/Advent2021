package com.small.advent2021.day5;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Vents {

    private static final Logger log = Logger.getLogger(Vents.class);
    private List<LineSegment> lineList;
    private Map<Point, Long> matrix;

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

    private class LineSegment {
        String[] pointArray;

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
                int x = fromX;
                for (int y = min(fromY, toY); y <= max(fromY, toY); ++y) {
                    list.add(new Point(x, y));
                }
            } else if (isVertical()) {
                int y = fromY;
                for (int x = min(fromX, toX); x <= max(fromX, toX); ++x) {
                    list.add(new Point(x, y));
                }
            } else {
                int x = fromX;
                int y = fromY;
                if (fromX < toX) {
                    if (fromY < toY) {
                        for(; x <= toX && y <= toY; ++x, ++y) {
                            list.add(new Point(x, y));
                        }
                    } else {
                        for(; x <= toX && y >= toY; ++x, --y) {
                            list.add(new Point(x, y));
                        }
                    }
                } else {
                    if (fromY < toY) {
                        for(; x >= toX && y <= toY; --x, ++y) {
                            list.add(new Point(x, y));
                        }
                    } else {
                        for(; x >= toX && y >= toY; --x, --y) {
                            list.add(new Point(x, y));
                        }
                    }
                }
            }
            return list;
        }

        public String toString() {
            return Arrays.toString(pointArray);
        }

    }

    private class Point {
        int x;
        int y;

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
