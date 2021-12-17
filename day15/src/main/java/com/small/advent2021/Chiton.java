package com.small.advent2021;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Long.max;
import static java.lang.Long.min;
import static java.lang.Math.abs;

@Slf4j
public class Chiton {
    private int[][] grid;
    private int gridHeight;
    private int gridWidth;

    Chiton(String fileName) {
        processFile(fileName);
    }

    long calcBoxCost(int xCol, int yRow, int size) {
        long hCost = 0;
        long vCost = 0;

        if (size == 2) {
            hCost = grid[yRow][xCol + 1];
            vCost = grid[yRow + 1][xCol + 1];
        } else if (size == 3) {
            if (yRow + 2 > gridHeight) {
                vCost = grid[yRow + 1][xCol] +
                        grid[yRow + 1][xCol + 1] +
                        grid[yRow + 1][xCol + 2];
            } else {
                vCost = grid[yRow + 1][xCol] +
                        grid[yRow + 2][xCol] +
                        grid[yRow + 2][xCol + 1];
            }

            if (xCol + 2 > gridWidth) {
                hCost = grid[yRow][xCol + 1] +
                        grid[yRow + 1][xCol + 1] +
                        grid[yRow + 2][xCol + 1];
            } else {
                hCost = grid[yRow][xCol + 1] +
                        grid[yRow][xCol + 2] +
                        grid[yRow + 1][xCol + 2];
            }
        } else if (size == 4) {
            if (yRow + 2 > gridHeight) {
                vCost = grid[yRow + 1][xCol] +
                        grid[gridHeight][xCol + 1] +
                        grid[gridHeight][xCol + 2] +
                        grid[gridHeight][xCol + 3] +
                        grid[gridHeight][xCol + 4] ;

            } else if (yRow + 3 > gridHeight) {
                vCost = grid[yRow + 1][xCol] +
                        grid[yRow + 2][xCol] +
                        grid[yRow + 2][xCol + 1] +
                        grid[gridHeight][xCol + 2] +
                        grid[gridHeight][xCol + 3] ;
            } else {
                vCost = grid[yRow + 1][xCol] +
                        grid[yRow + 2][xCol] +
                        grid[yRow + 3][xCol] +
                        grid[yRow + 3][xCol + 1];
                if(xCol + 2 > gridWidth) {
                    vCost += grid[yRow + 4][xCol + 1];
                } else {
                    vCost += grid[yRow + 3][xCol + 2];

                }
            }

            if (xCol + 2 > gridWidth) {
                hCost = grid[yRow][xCol + 1] +
                        grid[yRow + 1][gridWidth] +
                        grid[yRow + 2][gridWidth] +
                        grid[yRow + 3][gridWidth] +
                        grid[yRow + 4][gridWidth];
            }else if (xCol + 3 > gridWidth) {
                hCost = grid[yRow][xCol + 1] +
                        grid[yRow][xCol + 2] +
                        grid[yRow + 1][xCol + 2] +
                        grid[yRow + 2][gridWidth] +
                        grid[yRow + 3][gridWidth];

            } else {
                hCost = grid[yRow][xCol + 1] +
                        grid[yRow][xCol + 2] +
                        grid[yRow][xCol + 3] +
                        grid[yRow + 1][xCol + 3];
                if(yRow + 2 > gridHeight) {
                    hCost += grid[yRow + 1][xCol + 4];
                }
                hCost += grid[yRow + 2][xCol + 3];
            }
        }

        if (abs(hCost - vCost) < 2) return 0;
        return hCost - vCost;
    }

    long calcCost(int yRow, int xCol) {
        long hCost = 0;
        long vCost = 0;

        if (xCol + 1 == gridWidth && yRow + 1 < gridHeight) {
            for (int vStep = 0;vStep < gridHeight - yRow - 1; vStep++) {
                hCost += grid[yRow+vStep][gridWidth];
                vCost += grid[yRow+vStep+1][gridWidth-1];
                if(vCost < hCost) return -1;
            }
            return 1;
        }

        if (yRow + 1 == gridHeight && xCol + 1 < gridWidth) {
            for (int hStep = 0;hStep < gridWidth - xCol - 1; hStep++) {
                hCost += grid[gridHeight][xCol+hStep];
                vCost += grid[gridHeight-1][xCol+hStep+1];
                if(hCost < vCost) return 1;
            }
            return -1;
        }

        for (int step = 1; step < 9; ++step) {
            if (abs(hCost - vCost) < 2) {
                if (yRow <= gridHeight - step && xCol <= gridWidth - step) {
                    hCost += grid[yRow + step][xCol] + ((step > 1) ? grid[yRow + step][xCol + step - 1] : 0L);
                    vCost += grid[yRow][xCol + step] + ((step > 1) ? grid[yRow + step - 1][xCol + step] : 0L);
                } else if (yRow <= gridHeight - step) {
                    vCost += grid[yRow][gridWidth] + ((step > 1) ? grid[yRow + step - 1][gridWidth] : 0L);
                    hCost += grid[yRow + step][xCol] + ((step > 1) ? grid[yRow + step][xCol + step - 1] : 0L);
                } else {
                    hCost += grid[gridHeight][xCol] + ((step > 1) ? grid[gridHeight][xCol + step - 1] : 0L);
                    vCost += grid[yRow][xCol + step] + ((step > 1) ? grid[yRow + step - 1][xCol + step] : 0L);
                }
            } else {
                break;
            }
        }
        return hCost - vCost;
    }

    long calcCost_old(int yRow, int xCol) {
        long hCost = 0;
        long vCost = 0;

        if (yRow == 6 && xCol == 8) {
            return -1;
        }

        for (int step = 1; step < 9; ++step) {
            if (abs(hCost - vCost) < 2) {
                if (yRow <= gridHeight - step && xCol <= gridWidth - step) {
                    hCost += grid[yRow + step][xCol] + ((step > 1) ? grid[yRow + step][xCol + step - 1] : 0L);
                    vCost += grid[yRow][xCol + step] + ((step > 1) ? grid[yRow + step - 1][xCol + step] : 0L);
                } else if (yRow <= gridHeight - step) {
                    vCost += grid[yRow][gridWidth] ;//+ ((step > 1) ? grid[yRow + step - 1][gridWidth] : 0L);
                    hCost += grid[yRow + step][xCol] ;//+ ((step > 1) ? grid[yRow + step][xCol + step - 1] : 0L);
                } else {
                    hCost += grid[gridHeight][xCol] ;//+ ((step > 1) ? grid[gridHeight][xCol + step - 1] : 0L);
                    vCost += grid[yRow][xCol + step] ;//+ ((step > 1) ? grid[yRow + step - 1][xCol + step] : 0L);
                }
            } else {
                break;
            }
        }
        return hCost - vCost;
    }

    long tryWalk() {
        long score = 0;
        int yRow = 0;
        int xCol = 0;

        do {
            if (yRow == gridHeight) {
                ++xCol;
            } else if (xCol == gridWidth) {
                ++yRow;
            } else {

                 long cost = calcCost(yRow, xCol);

                if (cost < 0) {
                    ++yRow;
                } else if (cost > 0) {
                    ++xCol;
                } else {
                    ++yRow;
                }
            }
            score += grid[yRow][xCol];
            log.info(String.format("Choosing %d,%d val(%d) Running Score =%d", yRow, xCol, grid[yRow][xCol], score));
        } while (yRow != gridWidth || xCol != gridHeight);
        return score;
    }

    long score2StepPaths() {
        List<GridPath> paths = get2StepPaths();
        long accumulator = 0;
        long min = 100000000;
        long max = 0L;

        for (var path : paths) {
            long score = scorePath(path);
            min = min(min, score);
            max = max(max, score);
            log.debug(String.format("Score = %d", score));
            accumulator += score;
        }
        log.info(String.format("Total = %d Minimum = %d  Maximum = %d", accumulator, min, max));
        return accumulator;
    }

    long scorePath(GridPath gridPath) {
        long score = 0;

        score = gridPath.walk();

        return score;
    }

    List<GridPath> get2StepPaths() {
        List<GridPath> paths = new ArrayList<>();

        for (int x = gridWidth; x >= 0; --x) {
            for (int y = gridHeight; y >= 0; --y) {
                if (x != 0 && y != 0) {
                    GridPath gridPath = new GridPath();
                    StringBuilder sb = new StringBuilder();
                    if (x != 0) {
                        gridPath.addRule(new GridPath.Rule("R", x));
                        sb.append("R" + x + "->");
                    }
                    if (y != 0) {
                        gridPath.addRule(new GridPath.Rule("D", y));
                        sb.append("D" + y);
                        if (x != gridWidth) {
                            gridPath.addRule(new GridPath.Rule("R", gridWidth - x));
                            sb.append("->R" + (gridWidth - x));

                            if (y != gridHeight) {
                                gridPath.addRule(new GridPath.Rule("D", gridHeight - y));
                                sb.append("->D" + (gridWidth - y));
                            }
                        }
                    }
                    log.debug("Rule = " + sb);
                    paths.add(gridPath);
                }

                if (gridHeight - y == 0 && gridWidth - x == 0) {
                    break;
                }
            }
        }
        return paths;
    }

    private void processFile(String fileName) {
        Path path = Path.of(fileName);
        try (Stream<String> s = Files.lines(path)) {
            grid = s
                    .map(str -> str.chars()
                            .map(Character::getNumericValue)
                            .toArray())
                    .toArray(int[][]::new);
            log.debug(Arrays.deepToString(grid));

            gridHeight = grid.length - 1;
            gridWidth = grid[0].length - 1;
            get2StepPaths();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    enum Direction {
        HORIZONTAL("H"),
        VERTICAL("V");

        private final String shortName;

        Direction(String shortName) {
            this.shortName = shortName;
        }

        public String getShortName() {
            return shortName;
        }
    }

    private class GridPath {
        List<Rule> rules = new ArrayList<>();

        void addRule(Rule rule) {
            rules.add(rule);
        }

        public long walk() {
            int startX = 0;
            int startY = 0;
            int currentX = 0;
            int currentY = 0;
            int extentX = 0;
            int extentY = 0;

            long accumulator = 0;

            for (var rule : rules) {
                log.debug(String.format("Walking Rule %s", rule));
                extentX = rule.extentX(currentX);
                extentY = rule.extentY(currentY);
                currentX = startX + rule.incrementX();
                currentY = startY + rule.incrementY();

                for (; currentX <= extentX && currentY <= extentY;
                     currentX += rule.incrementX(), currentY += rule.incrementY()) {

                    accumulator += grid[currentX][currentY];

                    startX = currentX;
                    startY = currentY;
                }
            }

            return accumulator;
        }

        private static class Rule {
            Direction direction;
            int distance;

            Rule(String direction, int distance) {
                switch (direction) {
                    case "D":
                        this.direction = Direction.VERTICAL;
                        this.distance = distance;
                        break;
                    case "R":
                        this.direction = Direction.HORIZONTAL;
                        this.distance = distance;
                        break;
                    case "U":
                        this.direction = Direction.VERTICAL;
                        this.distance = -distance;
                        break;
                    case "L":
                        this.direction = Direction.HORIZONTAL;
                        this.distance = -distance;
                        break;
                }
            }

            public int extentX(int beginX) {

                return beginX + ((direction == Direction.HORIZONTAL) ? distance : 0);
            }

            public int extentY(int beginY) {

                return beginY + ((direction == Direction.VERTICAL) ? distance : 0);
            }

            public int incrementX() {
                return (direction == Direction.HORIZONTAL) ? 1 : 0;
            }

            public int incrementY() {
                return (direction == Direction.VERTICAL) ? 1 : 0;
            }

            public String toString() {
                return String.format("%s%d", direction.shortName, distance);
            }
        }
    }
}
