package com.small.advent2021;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@Slf4j
public class TrickShot {
    List<String> list = new ArrayList<>();
    TrickShot(String fileName) {
        processFile(fileName);

    }


    private class Trajectory {
        int x;
        int y;

        int xInc;
        int yInc;

        void setVelocity(int xVelocity, int yVelocity) {
            xInc = xVelocity;
            yInc = yVelocity;
            x = 0;
            y = 0;
        }

        void step() {
            x += xInc;
            y += yInc;

            if (xInc > 0) {
                --xInc;
            }
            --yInc;
        }

        boolean inRange(int xBeginRange, int yTop, int xEndRange, int yBottom) {
            while (true) {
                step();
                if (x >= xBeginRange && x <= xEndRange && y <= yTop && y >= yBottom) {
                    list.add(String.format("(%d,%d)", x, y));
                    return true;
                }
                if (x > xEndRange || y < yBottom) {
                    return false;
                }
            }
        }

    }

    int countValidStarts(int xBeginRange, int xEndRange, int yTop, int yBottom) {
        Trajectory trajectory = new Trajectory();
        int count = 0;
        for (int x = 24; x <= xEndRange; ++x) {
            for (int y = 80; y >= -80; --y) {
                trajectory.setVelocity(x, y);
                if(trajectory.inRange(xBeginRange, yTop, xEndRange, yBottom)) {
                    log.info(format("Valid Trajectory (%d, %d)", x, y));
                    ++count;
                }
            }
        }
        return count;
    }

    int vert(int vel, int rangeTop, int rangeBottom) {
        int x = vel;
        int pos = 0;
        for (; x > 0; --x) {
            pos += x;
            log.info("^V:" + pos);

        }

        int apogee = pos;

        for (; pos >= rangeBottom; x++) {
            pos -= x;
            log.info("V:" + pos);
            if (pos < rangeTop) {
                break;
            }
        }

        return apogee;
    }

    int func(int velocity, int rangeBegin, int rangeEnd) {
        int pos = 0;

        for (int x = velocity; x >= 0; --x) {
            pos += x;
            log.debug("H:" + pos);
            if (pos > rangeBegin) {
                break;
            }
        }
        if (pos >= rangeBegin && pos <= rangeEnd) {
            return pos;
        } else {
            return 0;
        }
    }

    int findH(int rangeBegin, int rangeEnd) {
        for (int velocity = 0; velocity < rangeEnd; ++velocity) {
            if (0 != func(velocity, rangeBegin, rangeEnd)) {
                log.info("Valid Velocity: " + velocity);
            }
        }
        return 7;
    }

    private void processFile(String fileName) {
        Path path = Path.of(fileName);
        try {
            String stringData = Files.readString(path);
            log.info(stringData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
