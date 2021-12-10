package com.small.advent2021;

import lombok.extern.log4j.Log4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Log4j
public class CountIncreases {
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
        log.info( path.toAbsolutePath());
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

    public static void main(String[] args) {
        CountIncreases ci = new CountIncreases();
        try {
            int count = ci.countIncreases("input.txt");
            log.info("Increases " + count);
        } catch (FileNotFoundException e) {
            log.info(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            int count = ci.countIncreases("input.txt", 3);
            log.info("Increases " + count);
        } catch (FileNotFoundException e) {
            log.info(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
