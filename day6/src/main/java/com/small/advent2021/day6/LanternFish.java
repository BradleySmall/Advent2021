package com.small.advent2021.day6;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LanternFish {
    private static final Logger log = Logger.getLogger(LanternFish.class);

    private List<Integer> fishList;
    // TODO -> "private Map<String, Integer> fishCounter;"

    LanternFish(String fileName) {
        log.info("Constructor");
        processFile(fileName);
    }

    private void processFile(String fileName) {
        Path path = Path.of(fileName);
        log.info("Path =  " + path.toAbsolutePath());
        try {
            String s = Files.readString(path);
            Stream<Integer> si = Arrays.stream(s.trim()
                            .split(","))
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    ;
            fishList = si
                    .toList();
        } catch(IOException e ) {
            log.info(e.getMessage());
        }
        log.info("done");
    }


    public int countFish() {
        return fishList.size();
    }

    public static void main(String[] args) {
        log.info("Begin main");
        LanternFish lanternFish = new LanternFish("input.txt");
        lanternFish.dayTick(80);
        log.info("Count = " + lanternFish.countFish());
    }

    public void dayTick(Integer tick) {
        for (int x = 0; x < tick;++x) {

            List<Integer> newList =  fishList.stream().map(e-> e = e-1).collect(Collectors.toList());
            List<Integer> newFish = newList.stream()
                    .filter(e -> e == -1)
                    .map(e -> 8)
                    .toList();
            newList.addAll(newFish);
            fishList = newList.stream().map(e -> e = (e == -1)?6:e).toList();

        }
    }
}
