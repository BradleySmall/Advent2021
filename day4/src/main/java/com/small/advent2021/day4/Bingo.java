package com.small.advent2021.day4;


import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Bingo {

    private static final Logger log = Logger.getLogger(Bingo.class);
    private BallCall ballCall;
    private Cards cards;

    Bingo(String fileName) {
        processFile(fileName);
    }

    private void processFile(String fileName) {
        Path path = Path.of(fileName);
        log.info("Path =  " + path.toAbsolutePath());
        try (Stream<String> s = Files.lines(path)) {
            List<String> stringList = s.toList();

            ballCall = new BallCall(stringList.get(0));
            cards = new Cards(stringList.subList(2, stringList.size()));
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    public void run() {
        log.info("run");
        for (String call : ballCall.ballCallArray) {
            cards.daub(call);
        }
    }

    public String showCard(int card) {
        return Arrays.deepToString(cards.cardDeck.get(card))
                .replace("[[", "\n [")
                .replace("],", "]\n")
                .replace("]]", "]");
    }

    public String showCards() {
        return Arrays.deepToString(cards.cardDeck.toArray()).replace("]],", "]\n\n")
                .replace("[[[", "\n [")
                .replace("[[", "[")
                .replace("]]", "]")
                .replace("],", "]\n");
    }

    public String showBallCall() {
        return Arrays.toString(ballCall.ballCallArray);
    }

    private static class BallCall {

        private final String[] ballCallArray;
        public BallCall(String balls) {
            ballCallArray = balls.split(",");
        }

    }
    private class Cards {
        private final List<String[][]> cardDeck = new ArrayList<>();
        public Cards(List<String> cards) {
            for (int index = 0; index < cards.size(); index += 6) {
                cardDeck.add(
                        cards.subList(index, index + 5)
                                .stream()
                                .map(row -> row.trim()
                                        .split(" +"))
                                .toArray(String[][]::new)
                );
            }
        }

        int score(int index, int call) {
            int theScore = 0;

            String[][] card = cardDeck.get(index);
            for (String[] row : card) {
                for (String column : row) {
                    if (!column.contains("*")) {
                        theScore += Integer.parseInt(column);
                    }
                }
            }
            return theScore * call;
        }

        void daub(String call) {
            int deckIndex = 0;
            for (String[][] card : cardDeck) {
                int rowIndex = 0;
                for (String[] row : card) {
                    int columnIndex = 0;
                    for (String column : row) {
                        if (column.equals(call)) {
                            cardDeck.get(deckIndex)[rowIndex][columnIndex] = "*" + column + "*";

                            if (isWin(cardDeck.get(deckIndex), columnIndex, rowIndex)) {
                                log.info("Winner = " + deckIndex + " Winning Score =" + score(deckIndex, Integer.parseInt(call)) + " last called=" + call);
                                cards.clearCard(deckIndex);
                            }
                        }
                        columnIndex += 1;
                    }
                    rowIndex += 1;
                }
                deckIndex += 1;
            }
        }

        private boolean isWin(String[][] card, int columnIndex, int rowIndex) {
            return IntStream.range(0, 5).allMatch(r -> card[r][columnIndex].contains("*")) ||
                    IntStream.range(0, 5).allMatch(c -> card[rowIndex][c].contains("*"));
        }

        public void clearCard(int winner) {
            for (int rowIndex = 0; rowIndex < 5; ++rowIndex) {
                for (int columnIndex = 0; columnIndex < 5; ++columnIndex) {
                    cardDeck.get(winner)[rowIndex][columnIndex] = "";
                }
            }
        }

    }

    public static void main(String[] args) {
        String fileName = "input.txt";
        Bingo bingo = new Bingo(fileName);
        log.info(bingo.showCard(0));
        log.info(bingo.showCards());
        log.info(bingo.showBallCall());

        bingo.run();

    }
}
