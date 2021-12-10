package com.small.advent2021;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
): 3 points.
]: 57 points.
}: 1197 points.
>: 25137 points.
 */

@Slf4j
public class SyntaxScoring {
    private List<String> stringList;

    SyntaxScoring(String fileName) {
        processFile(fileName);
    }

    private void processFile(String fileName) {
        Path path = Path.of(fileName);
        try (Stream<String> s = Files.lines(path)) {
            stringList = s
                    .map(String::new)
                    .toList();
            log.info("Inside processFile")  ;
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public int scoreLine(String line) {
        int subTotal;
        Map<String, Integer> chunkErrors = new HashMap<>();

        Stack<String> delimiterStack = new Stack<>();

        chunkErrors.put(")", 0);
        chunkErrors.put("]", 0);
        chunkErrors.put("}", 0);
        chunkErrors.put(">", 0);

        for (String ch : line.split("")) {
            String delimiter;
            switch (ch) {
                case "<", "{", "[", "(" -> delimiterStack.push(ch);
                case ")" -> {
                    delimiter = delimiterStack.pop();
                    if (!delimiter.equals("(")) {
                        chunkErrors.put(")", chunkErrors.get(ch) + 3);
                    }
                }
                case "]" -> {
                    delimiter = delimiterStack.pop();
                    if (!delimiter.equals("[")) {
                        chunkErrors.put("]", chunkErrors.get(ch) + 57);
                    }
                }
                case "}" -> {
                    delimiter = delimiterStack.pop();
                    if (!delimiter.equals("{")) {
                        chunkErrors.put("}", chunkErrors.get(ch) + 1197);
                    }
                }
                case ">" -> {
                    delimiter = delimiterStack.pop();
                    if (!delimiter.equals("<")) {
                        chunkErrors.put(">", chunkErrors.get(ch) + 25137);
                    }
                }
                default -> {/*do nothing*/}
            }
        }

        subTotal = chunkErrors.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum()
        ;

        return subTotal;
    }

    public String showGetLine(int lineNumber) {
        return stringList.get(lineNumber);
    }

    public int getScore() {
        int total = 0;
        for (var line : stringList) {
            total += scoreLine(line);
        }
        return total;
    }

    public static void main(String[] args) {
        SyntaxScoring syntaxScoring = new SyntaxScoring("day10/input.txt");
        log.info("Score = " + syntaxScoring.getScore());
    }
}
