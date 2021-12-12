package com.small.advent2021;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.lang.String.format;

@Slf4j
public class Octopus {
    int[][] matrix;
    private int flashes;

    public Octopus(String fileName) {
        processFile(fileName);
    }

    private void processFile(String fileName) {
        Path path = Path.of(fileName);
        try (Stream<String> s = Files.lines(path)) {
            matrix = s
                    .map(str -> str.chars()
                            .map(ch -> ch - '0')
                            .toArray()
                    )
                    .toArray(int[][]::new)
            ;
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
    }

    void passStep() {
        for (int row = 0; row < matrix.length; ++row) {
            for (int column = 0; column < matrix[0].length; ++column) {
                ++matrix[row][column];
            }
        }

        while (show().contains("10")) {
            for (int row = 0; row < matrix.length; ++row) {
                for (int column = 0; column < matrix[0].length; ++column) {
                    if (matrix[row][column] == 10) {
                        flashTheNeighbors(row, column);
                    }
                }
            }
        }
        log.debug("After Step\n" + show());

    }

    private void flashTheNeighbors(int row, int column) {
        int beginRow = (row > 0) ? row - 1 : row;
        int beginColumn = (column > 0) ? column - 1 : column;
        int endRow = (row < matrix.length - 1) ? row + 1 : matrix.length - 1;
        int endColumn = (column < matrix[0].length - 1) ? column + 1 : matrix[0].length - 1;

        for (int r = beginRow; r <= endRow; ++r) {
            for (int c = beginColumn; c <= endColumn; ++c) {
                if (matrix[r][c] != 0 && matrix[r][c] != 10) {
                    ++matrix[r][c];
                }
            }
        }
        matrix[row][column] = 0;
        ++flashes;
    }

    public String show() {
        return Arrays
                .deepToString(matrix)
                .replace("[[", "\n [")
                .replace("],", "]\n")
                .replace("]]", "]\n");
    }

    public boolean allFlashed() {
        return Arrays
                .stream(matrix)
                .flatMapToInt(Arrays::stream)
                .allMatch(power -> power == 0);
    }

    public static void main(String[] args) {
        Octopus octopus = new Octopus("day11/input.txt");

        int step;
        for (step = 0; !octopus.allFlashed(); step++) {
            octopus.passStep();
            log.debug(format("All Flashed? %b", octopus.allFlashed()));
            if (step == 99) {
                log.info(format("After Step %d %n%s%nFlashes=%d", step+1, octopus.show(), octopus.flashes));
            }
        }
        log.info(format("After Step %d %n%s%n", step, octopus.show()));
    }
}
