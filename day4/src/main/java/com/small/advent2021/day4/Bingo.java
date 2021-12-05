package com.small.advent2021.day4;


import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Bingo {

    private BallCall ballCall;
    private Cards cards;
    private boolean hasWinner = false;

    private static final Logger log = Logger.getLogger(Bingo.class);

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

    public Status getStatus() {
        log.info("getStatus");
        if (hasWinner) {
            return Status.SUCCESS;
        }
        return Status.FAILURE;
    }

    public enum Status {
        SUCCESS,
        FAILURE
    }

    public static void main(String[] args) {
        String fileName = "input.txt";
        Bingo bingo = new Bingo(fileName);

        bingo.run();

        log.info(bingo.getStatus());
    }

    private class BallCall {
        private final String[] ballCallArray;

        public BallCall(String balls) {
            ballCallArray = balls.split(",");
        }
    }

    private class Cards {
        private final List<String[][]> cardDeck = new ArrayList<>();

        int score(int index, int call) {
            int theScore = 0;

            String[][] card = cardDeck.get(index);
            for (String[] row : card) {
                for (String column : row) {
                    if (! column.contains("*")) {
                        theScore += Integer.parseInt(column);
                    }
                }
            }
            return theScore * call;
        }

        void daub(String call) {
            int deckIndex = 0;
            for (String [][] card : cardDeck) {
                int rowIndex = 0;
                for (String [] row : card) {
                    int columnIndex = 0;
                    for (String column : row) {
                        if (column.equals(call)) {
                            cardDeck.get(deckIndex)[rowIndex][columnIndex] = "*"+column+"*";

                            if (isWin(cardDeck.get(deckIndex), columnIndex, rowIndex)) {
                                hasWinner = true;

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

        public Cards(List<String> cards) {
            for(int index = 0; index < cards.size(); index += 6) {
                cardDeck.add(
                        cards.subList(index, index + 5)
                        .stream()
                                .map(row -> row.trim()
                                        .split(" +") )
                                .toArray(String[][]::new)
                );
            }
        }

        public void clearCard(int winner) {
            for (int rowIndex=0; rowIndex<5;++rowIndex) {
                for (int columnIndex = 0; columnIndex<5;++columnIndex) {
                    cardDeck.get(winner)[rowIndex][columnIndex] = "";
                }
            }
        }
    }
}
