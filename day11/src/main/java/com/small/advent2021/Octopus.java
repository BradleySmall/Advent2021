package com.small.advent2021;

import groovy.lang.Tuple;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class Octopus {
    int [][] matrix;

    public Octopus(String fileName) {
        processFile(fileName);
    }

    private void processFile(String fileName) {
        Path path = Path.of(fileName);
        try(Stream<String> s = Files.lines(path)) {
           matrix =  s
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
        for (int row = 0; row < 10;++row) {
            for(int column=0;column < 10;++column) {
                int tmp = matrix[row][column];
                tmp =  tmp + 1;
                matrix[row][column] = tmp;
            }
        }
        //log.info("after pass\n"+show());

        while(show().contains("10")) {
            for (int row = 0; row < 10; ++row) {
                for (int column = 0; column < 10; ++column) {
                    int tmp = matrix[row][column];
                    if (tmp == 10) {
                        flashTheNeighbors(row, column);
                    }
                }
            }
        }
        //log.info("After Flash\n"+show());
        for (int r = 0; r < 10; ++r) {
            for (int c = 0; c < 10; ++c) {
                int tmp = matrix[r][c];
                if (tmp >= 10) {
                    tmp = 0;
                }
                matrix[r][c]=tmp;
            }
        }
        log.info("After Flash\n"+show());

    }

    private void flashTheNeighbors(int row, int column) {
        int beginRow = (row > 0) ? row -1 : row;
        int beginColumn = (column > 0) ? column-1 : column;
        int endRow = (row < 9) ? row + 1 : 9;
        int endColumn = (column < 9) ? column + 1 : 9;


            for (int r = beginRow; r <= endRow; ++r) {
                for (int c = beginColumn; c <= endColumn; ++c) {
                    int tmp = matrix[r][c];

                    tmp = tmp+1;
                    matrix[r][c] = tmp;
                }
            }
    }

    public String show() {
        return Arrays
                .deepToString(matrix)
                .replace("[[","\n [")
                .replace("],","]\n")
                .replace("]]","]\n");
    }

    public static void main(String[] args) {
        Octopus octopus = new Octopus("day11/input_test.txt");
        //log.info(octopus.show());
        octopus.passStep();
        //log.info(octopus.show());
        octopus.passStep();
        //log.info(octopus.show());
        octopus.passStep();
        //log.info(octopus.show());
        octopus.passStep();
        //log.info(octopus.show());
        octopus.passStep();
        //log.info(octopus.show());
        octopus.passStep();
        //log.info(octopus.show());
        octopus.passStep();
        //log.info(octopus.show());
    }
}
