package com.small.advent2021;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class TrickShot {
    TrickShot(String fileName) {
        processFile(fileName);

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
