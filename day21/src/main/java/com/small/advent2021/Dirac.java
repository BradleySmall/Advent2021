package com.small.advent2021;


import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.lang.Long.max;
import static java.lang.Math.min;
import static java.lang.String.format;

@Slf4j
public class Dirac {
    Map<Integer, Long> dieRoll = Map.of(
            3,1L, 9,1L,
            4,3L, 8, 3L,
            5,6L, 7,6L,
            6, 7L
            );

    Game game;
    Map<Game, Long> universe;
    Map<Game, Long> won = new HashMap<>();

    private final Die die = new Die();

    int player1Score = 0;
    int player2Score = 0;

    int player1CurrentPosition=8;
    int player2CurrentPosition=1;

    public Dirac() {
        game = new Game(new Player(0, 8), new Player(0, 1));
        universe = new HashMap<>();
        universe.put(game, 1L);
    }

    long playDirac() {
        while(universe.size() > 0) {
            playDiracPlayer(1);
            if(universe.size() > 0) {
                playDiracPlayer(2);
            }
        }

        long p1Wins = 0;
        long p2Wins = 0;

        for (var a : won.entrySet()) {
            var g = a.getKey();
            if (g.p1.score >= 21) {
                p1Wins += a.getValue();
            } else if (g.p2.score >= 21) {
                p2Wins += a.getValue();
            }
        }
        log.info(format("P1-%d P2-%d Winner-%d",p1Wins, p2Wins, max(p1Wins, p2Wins)));
        return max(p1Wins, p2Wins);
    }

    private void playDiracPlayer(int p) {
        Map<Game, Long> tmpUniverse = new HashMap<>(universe);
        universe.clear();
        for (var aGame : tmpUniverse.entrySet()) {
            for (var roll : dieRoll.entrySet()) {
                long count = roll.getValue() * aGame.getValue();
                Game g = new Game(aGame);
                int pos = getPos(p, roll, g);
                setGamePos(p, g, pos);
                scoreGame(count, g);
            }
        }
    }

    private void scoreGame(long count, Game g) {
        if (g.p1.score >= 21 || g.p2.score >= 21) {
            won.put(g, won.getOrDefault(g, 0L) + count);
        } else {
            universe.put(g, universe.getOrDefault(g, 0L) + count);
        }
    }

    private void setGamePos(int p, Game g, int pos) {
        if (p == 1) {
            g.p1.position = pos;
            g.p1.score += g.p1.position;
        } else {
            g.p2.position = pos;
            g.p2.score += g.p2.position;
        }
    }

    private int getPos(int p, Map.Entry<Integer, Long> roll, Game g) {
        int pos;

        if (p == 1)
            pos = (roll.getKey() + g.p1.position) % 10;
        else
            pos = (roll.getKey() + g.p2.position) % 10;

        if (pos == 0) pos = 10;
        return pos;
    }

    int play() {

        while (player1Score < 1000 && player2Score < 1000) {
            playPlayer1();
            if(player1Score >= 1000) break;
            playPlayer2();
        }

        return die.count * min(player1Score, player2Score);
    }

    private void playPlayer2() {
        player2CurrentPosition += (die.getNextValue() % 10);
        player2CurrentPosition = player2CurrentPosition % 10;
        if (player2CurrentPosition == 0) player2CurrentPosition = 10;
        player2Score += player2CurrentPosition;
    }

    private void playPlayer1() {
        player1CurrentPosition += (die.getNextValue() % 10);
        player1CurrentPosition = player1CurrentPosition % 10;
        if (player1CurrentPosition == 0) player1CurrentPosition = 10;
        player1Score += player1CurrentPosition;
    }

    private static class Die {
        int count = 0;
        int current = 1;


        public int getNextValue() {
            var val = current;

            current++;
            if (current > 100) current = 1;
            val += current;

            current++;
            if (current > 100) current = 1;
            val += current;

            current++;
            if (current > 100) current = 1;

            count += 3;
            return val;
        }
    }

    private static class Player {
        int score;
        int position;

        public Player(int score, int position) {
            this.score = score;
            this.position = position;
        }

        public Player(Player player) {
            this(player.score, player.position);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Player player = (Player) o;
            return score == player.score && position == player.position;
        }

        @Override
        public int hashCode() {
            return Objects.hash(score, position);
        }
    }

    private static class Game {
        private final Player p1;
        private final Player p2;

        public Game(Map.Entry<Game, Long> game) {
            p1 = new Player(game.getKey().p1);
            p2 = new Player(game.getKey().p2);
        }
        public Game(Player p1, Player p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Game aGame = (Game) o;
            return Objects.equals(p1, aGame.p1) && Objects.equals(p2, aGame.p2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(p1, p2);
        }
    }
}
