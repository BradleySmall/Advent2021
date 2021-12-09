package com.small.advent2001;

import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

@Log4j
public class SmokeBasin {

    private int[][] matrix;
    private int riskLevel;
    private final List<Integer> basinList = new ArrayList<>();

    SmokeBasin(String fileName) {
        processFile(fileName);
    }

    public static void main(String[] args) {
        SmokeBasin smokeBasin = new SmokeBasin("day9/input.txt");
        log.info(smokeBasin.prettyPrintMatrix(smokeBasin.getMatrix()));
        smokeBasin.countLowPoints();
        log.info("Risk Level = " + smokeBasin.getRiskLevel());
        long assessment = smokeBasin.getBasinListAssessment();
        log.info("Assessment = " + assessment);
    }

    public int getRiskLevel() {
        return riskLevel;
    }

    private void processFile(String fileName) {
        Path path = Path.of(fileName);

        try (Stream<String> s = Files.lines(path)) {
            matrix = s
                    .map(e -> e.chars()
                            .map(c -> c - '0')
                            .toArray())
                    .toArray(int[][]::new);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public String prettyPrintMatrix(int[][] matrix) {
        return Arrays.deepToString(matrix)
                .replace("[[", "\n [")
                .replace("],", "]\n")
                .replace("]]", "]\n");
    }

    public int countLowPoints() {
        int lowPoints = 0;
        riskLevel = 0;

        for (int row = 0; row < getMatrix().length; ++row) {
            for (int column = 0; column < getMatrix()[row].length; ++column) {
                if (isLowPoint(row, column)) {
                    ++lowPoints;
                    int increase = getMatrix()[row][column] + 1;
                    riskLevel += getMatrix()[row][column] + 1;
                    log.info("Low point at (" + row + ", " + column + ") Risk Increase=" + increase + " RiskLevel =" + riskLevel);
                    int basinSize = getBasinSize(row, column);
                    log.info(String.format("Matrix basin Size (%d) for (%d, %d)", basinSize, row, column));
                    basinList.add(basinSize);
                }
            }
        }
        log.info("LowPoint Count = " + lowPoints + " RiskLevel =" + riskLevel);
        log.info(basinList.stream().sorted().toList());
        return lowPoints;

    }

    public long getBasinListAssessment() {
        Collections.sort(basinList);
        long product = 1;

        for (Integer integer : basinList
                .subList(basinList.size() -3, basinList.size())) {
            product *= integer;
        }
        log.info("Product="+product);
        return product;
    }

    private int getBasinSize(int row, int column) {
        if (row == -1 || row == getMatrix().length) return 0;
        if (column == -1 || column == getMatrix()[0].length) return 0;
        if (getMatrix()[row][column] == 9) return 0;

        Set<Pair> pairs = inThePool(row, column);
        Set<Pair> newPairs = new HashSet<>();
        for (Pair p : pairs) {
            newPairs.addAll(inThePool(p.row, p.column));
        }

        for (Pair p : newPairs) {
            pairs.addAll(inThePool(p.row, p.column));
        }
        for (Pair p : pairs) {
            newPairs.addAll(inThePool(p.row, p.column));
        }
        return pairs.size();
    }

    private Set<Pair> inThePool(int row, int column) {
        Set<Pair> pairs = new HashSet<>();
        pairs.add(new Pair(row, column));

        int x = column - 1;
        while (x != -1 && getMatrix()[row][x] != 9) {
            pairs.add(new Pair(row, x));
            --x;
        }
        x = column + 1;
        while (x != getMatrix()[0].length && getMatrix()[row][x] != 9) {
            pairs.add(new Pair(row, x));
            ++x;
        }

        int y = row - 1;
        while (y != -1 && getMatrix()[y][column] != 9) {
            pairs.add(new Pair(y, column));
            --y;
        }
        y = row + 1;
        while (y != getMatrix().length && getMatrix()[y][column] != 9) {
            pairs.add(new Pair(y, column));
            ++y;
        }
        return pairs;
    }

    private boolean isLowPoint(int row, int column) {
        int curVal = getMatrix()[row][column];

        if (row > 0 && getMatrix()[row - 1][column] <= curVal) {
            return false;
        }
        if (column > 0 && getMatrix()[row][column - 1] <= curVal) {
            return false;
        }
        if (row < getMatrix().length - 1 && getMatrix()[row + 1][column] <= curVal) {
            return false;
        }
        if (column < getMatrix()[0].length - 1) {
            return getMatrix()[row][column + 1] > curVal;
        }
        return true;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    static class Pair {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return row == pair.row && column == pair.column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }

        int row;
        int column;

        Pair(int r, int c) {
            row = r;
            column = c;
        }
    }
}
