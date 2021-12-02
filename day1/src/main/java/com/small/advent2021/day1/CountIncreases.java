package com.small.advent2021.day1;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CountIncreases {
    private static final Logger LOGGER = Logger.getLogger(CountIncreases.class);
    public static void main(String[] args) {
        CountIncreases ci = new CountIncreases();
        try {
            int count = ci.countIncreases("day1/input.txt");
            LOGGER.info("Increases " + count);
        } catch (FileNotFoundException e) {
            LOGGER.info(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            int count = ci.countIncreases("day1/input.txt", 3);
            LOGGER.info("Increases " + count);
        } catch (FileNotFoundException e) {
            LOGGER.info(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int countIncreases(String fileName) throws IOException {
        return count(getListFromFile(fileName));
    }

    public int countIncreases(String fileName, int slideCount) throws IOException {
        return count(slideCountList(getListFromFile(fileName), slideCount));
    }

    public int count(List<Integer> list) {
        int count = 0;
        Integer last = list.get(0);

        for (Integer element : list) {
            if (element > last) ++count;
            last = element;
        }
        return count;
    }

    public List<Integer> getListFromFile(String fileName) throws IOException {
        Path path = Path.of(fileName);
        try(Stream<String> lines = Files.lines(path)) {
            return lines
                    .map(Integer::parseInt)
                    .toList();
        }
    }

    public List<Integer> slideCountList(List<Integer> l, int count) {
        List<Integer> list = new ArrayList<>();

        int current = 0;
        int value = 0;

        while (current <= l.size() - count) {
            for (int x = 0; x < count; ++x) {
                value += l.get(current + x);
            }
            list.add(value);
            value = 0;
            current += 1;
        }

        return list;
    }

}
