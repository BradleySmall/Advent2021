package com.small.advent2021;

import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j
public class Crabs {

    private Map<String, Long> fishCounter;
    private List<Long> fuelUsages;

    @SuppressWarnings("SameParameterValue")
    Crabs(String fileName) {
        processFile(fileName);
    }

    private void processFile(String fileName) {
        Path path = Path.of(fileName);
        log.info("Path =  " + path.toAbsolutePath());
        try {
            String s = Files.readString(path);
            fishCounter = Arrays.stream(s.trim().split(","))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    private long fuelUsed(long distance) {
        long extent = distance % 2 == 0 ? distance : distance + 1;
        long count = distance % 2 == 0 ? distance + 1 : distance;
        return extent / 2 * count;
    }

    public long getMaxFuelUsage() {
        return fuelUsages.stream().max(Long::compare).orElse(0L);
    }

    public long getMinFuelUsage() {
        return fuelUsages.stream().min(Long::compare).orElse(0L);
    }

    public void calculateFuelUsage() {
        long max = fishCounter.keySet()
                .stream()
                .map(Long::parseLong)
                .max(Long::compare)
                .orElse(0L);

        fuelUsages = new ArrayList<>();
        for (long pos = 0; pos <= max; ++pos) {
            long totFuel = 0;
            for (var crab : fishCounter.entrySet()) {
                long distance = Math.abs(Long.parseLong(crab.getKey()) - pos);
                long sub = fuelUsed(distance);
                sub *= crab.getValue();
                totFuel += sub;
            }
            fuelUsages.add(totFuel);
        }
    }

    public static void main(String[] args) {
        Crabs crabs = new Crabs("input.txt");
        crabs.calculateFuelUsage();

        log.info(String.format("Fuel Usage Minimum=%d Maximum=%d",crabs.getMinFuelUsage(),crabs.getMaxFuelUsage()));
    }
}
