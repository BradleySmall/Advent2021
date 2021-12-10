package com.small.advent2021;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
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

    public static void main(String[] args) {
        SyntaxScoring syntaxScoring = new SyntaxScoring("day10/input.txt");
        log.info("Corrupt Score = " + syntaxScoring.getCorruptScore());
        log.info("Incomplete Score = " + syntaxScoring.getIncompleteScore());
        log.info("Total Score = " + syntaxScoring.getTotalScore());
    }

    private void processFile(String fileName) {
        Path path = Path.of(fileName);
        try (Stream<String> s = Files.lines(path)) {
            stringList = s
                    .map(String::new)
                    .toList();
            log.info("Inside processFile");
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public String showGetLine(int lineNumber) {
        return stringList.get(lineNumber);
    }

    public long scoreIncompleteLine(String line) {
        Deque<String> delimiterStack = new ArrayDeque<>();

        for (String ch : line.split("")) {
            /*do nothing*/
            if ("<".equals(ch) || "{".equals(ch) || "[".equals(ch) || "(".equals(ch)) {
                delimiterStack.push(ch);
            } else if ((")".equals(ch) || "]".equals(ch) || "}".equals(ch) || ">".equals(ch)) && (!delimiterStack.pop().equals(getMatch(ch)))) {
                return 0L;
            }
        }
        long subTotal = 0L;
        while (!delimiterStack.isEmpty()) {
            subTotal = subTotal * 5 + scoreIncompleteFromDelimiter(delimiterStack.pop());
        }

        return subTotal;
    }

    public long getIncompleteScore() {
        List<Long> scoreList = new ArrayList<>();

        for (var line : stringList) {
            long score = scoreIncompleteLine(line);
            if (score > 0) {
                scoreList.add(score);
            }
        }

        log.info("Incomplete count = " + scoreList.size());
        Collections.sort(scoreList);
        return scoreList.get(scoreList.size() / 2);
    }

    private String getMatch(String closingDelimiter) {
        return switch (closingDelimiter) {
            case ")" -> "(";
            case "}" -> "{";
            case "]" -> "[";
            case ">" -> "<";
            default -> null;
        };
    }

    private long scoreIncompleteFromDelimiter(String delimiter) {
        return switch (delimiter) {
            case "(" -> 1;
            case "[" -> 2;
            case "{" -> 3;
            case "<" -> 4;
            default -> 0;
        };

    }
    private long scoreCorruptFromDelimiter(String delimiter) {
        return switch (delimiter) {
            case ")" -> 3;
            case "]" -> 57;
            case "}" -> 1197;
            case ">" -> 25137;
            default -> 0;
        };

    }

    public long scoreCorruptLine(String line) {
        Map<String, Long> chunkErrors = new HashMap<>();
        Deque<String> delimiterStack = new ArrayDeque<>();

        chunkErrors.put(")", 0L);
        chunkErrors.put("]", 0L);
        chunkErrors.put("}", 0L);
        chunkErrors.put(">", 0L);

        for (String ch : line.split("")) {
            /*do nothing*/
            if ("<".equals(ch) || "{".equals(ch) || "[".equals(ch) || "(".equals(ch)) {
                delimiterStack.push(ch);
            } else if ((">".equals(ch) || "}".equals(ch) || "]".equals(ch) || ")".equals(ch)) && !delimiterStack.pop().equals(getMatch(ch))) {
                chunkErrors.put(ch, chunkErrors.get(ch) + scoreCorruptFromDelimiter(ch));
            }
        }

        return chunkErrors
                .values()
                .stream()
                .mapToLong(Long::longValue)
                .sum()
                ;
    }

    public long getCorruptScore() {
        List<Long> scoreList = new ArrayList<>();
        for (var line : stringList) {
            long subTotal = scoreCorruptLine(line);
            if (subTotal > 0) {
                scoreList.add(subTotal);
            }
        }
        return scoreList.stream()
                .mapToLong(Long::longValue)
                .sum();
    }

    public long getTotalScore() {
        List<Long> scoreList = new ArrayList<>();
        for (var line : stringList) {

            long scoreCorrupt = scoreCorruptLine(line);
            long scoreIncomplete = scoreIncompleteLine(line);

            if (scoreCorrupt == 0 && scoreIncomplete == 0) {
                log.info("Clean Line " + line);
            } else if (scoreCorrupt != 0 && scoreIncomplete != 0) {
                log.info("Both Errors True (not valid) - " + line);
            } else if (scoreCorrupt > 0) {
                scoreList.add(scoreCorrupt);
            } else {
                scoreList.add(scoreIncomplete);
            }
        }
        Collections.sort(scoreList);
        log.info(scoreList.toString());
        return scoreList.get(scoreList.size() / 2);
    }
}
