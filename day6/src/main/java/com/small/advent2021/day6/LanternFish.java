package com.small.advent2021.day6;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("SameParameterValue")
public class LanternFish {
    private static final Logger log = Logger.getLogger(LanternFish.class);

    private Map<String, Long> fishCounter;

    LanternFish(String fileName) {
        log.info("Constructor");
        processFile(fileName);
    }

    private void processFile(String fileName) {
        Path path = Path.of(fileName);
        log.info("Path =  " + path.toAbsolutePath());
        try {
            String s = Files.readString(path);
            fishCounter = Arrays.stream(s.trim().split(","))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        } catch(IOException e ) {
            log.info(e.getMessage());
        }
    }

    public void displayFishCounter() {
        for (var entry : fishCounter.entrySet()) {
            log.info(entry.getKey() + " = " + entry.getValue());
        }
    }

    public long countFish() {
        return fishCounter.values()
                .stream()
                .mapToLong(Long::longValue)
                .sum();
    }

    public static void main(String[] args) {
        log.info("Begin main");
        LanternFish lanternFish = new LanternFish("input.txt");
        lanternFish.displayFishCounter();
        lanternFish.dayTick(256);
        log.info("Count = " + lanternFish.countFish());
    }

    public void dayTick(Integer tick) {
        for (int x = 0; x < tick;++x) {

            long tmp = fishCounter.getOrDefault("0", 0L);
            fishCounter.put("0", fishCounter.getOrDefault("1", 0L) );
            fishCounter.put("1", fishCounter.getOrDefault("2",0L));
            fishCounter.put("2", fishCounter.getOrDefault("3",0L));
            fishCounter.put("3", fishCounter.getOrDefault("4",0L));
            fishCounter.put("4", fishCounter.getOrDefault("5",0L));
            fishCounter.put("5", fishCounter.getOrDefault("6",0L));
            fishCounter.put("6", fishCounter.getOrDefault("7",0L) + tmp);
            fishCounter.put("7", fishCounter.getOrDefault("8",0L));
            fishCounter.put("8",  tmp);
        }
    }
}
