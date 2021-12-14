package com.small.advent2021;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.String.format;

@Slf4j
public class PassagePathing {

    public static final String START = "start";

    private static class Pair<T, T1> {
        Pair(T f, T1 t) {
            from = f;
            to = t;
        }
        T from;
        T1 to;
        public String toString() {
            return format("(%s,%s)", from, to);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return Objects.equals(from, pair.from) && Objects.equals(to, pair.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }
    }
    private static class Node {
        String room;
        List<Node> doors = new ArrayList<>();
        int visited = 1;
    }

    List<Pair<String, String>> segments;
    List<List<Pair<String, String>>> legs = new ArrayList<>();

    Node tree;

    private Set<String> distinctRooms;

    public PassagePathing(String fileName) {
        processFile(fileName);
    }

    private void processFile(String fileName) {
        Path path = Path.of(fileName);
        try(Stream<String> s = Files.lines(path)) {
            segments = s
                    .map(str -> str.split("-"))
                    .map(sub -> new Pair<>(sub[0], sub[1]))
                    .toList();
            populateDistinctRooms();
            populateLegs();

            tree = buildNode(START, new HashSet<>());
            makePaths();
            log.debug("done");
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
    }
    private void populateDistinctRooms() {
        distinctRooms = new HashSet<>();
        for (var p : segments) {
            distinctRooms.add(p.from);
            distinctRooms.add(p.to);
        }
    }
    private void populateLegs() {
        for (var room : distinctRooms) {
            var leg = getLeg(room);
            if (!leg.isEmpty()) {
                legs.add(leg);
            }
        }
    }
    String show(List<Pair<String, String>> segs) {
        return segs.toString()
                .replace("[","\n[")
                .replace(", ", "")
                .replace("\n]", "]\n");
    }
    String show() {
        return show(segments);
    }

    public String showDistinctRooms() {
        return distinctRooms.toString();
    }
    List<Pair<String, String>> getLeg(String start) {
        List<Pair<String, String>> segs = new ArrayList<>();
        for (var seg : segments) {
            if (seg.from.equals(start) && !seg.to.equals(START) && !seg.from.equals("end")) {
                segs.add(new Pair<>(seg.from, seg.to));
            } else if (seg.to.equals(start) && !seg.from.equals(START) && !seg.to.equals("end")) {
                segs.add(new Pair<>(seg.to, seg.from));
            }
        }
        return segs;
    }

    public static void main(String[] args) {
        PassagePathing passagePathing = new PassagePathing("day12/input_test.txt");
        log.info("done");
    }

    void makePaths () {

        List<String> paths = new ArrayList<>();
        makePathsRecursiveHelper(tree, paths, new HashSet<>(), new StringBuilder());
        log.info("done count=("+paths.size() +") " + paths);
    }


    private void makePathsRecursiveHelper(Node node, List<String> paths, HashSet<Node> dontReturn, StringBuilder sb) {
        StringBuilder localSb = new StringBuilder();

        HashSet<Node> localDontReturn = new HashSet<>(dontReturn);
        localSb.append(sb);

        if (!isDontReturn(node, localDontReturn)) {

            if (node.room.equals(START)) {
                localDontReturn.clear();
                dontReturn.clear();
                localSb.append(node.room + ",");
                for (Node room : node.doors) {
                    makePathsRecursiveHelper(room, paths, localDontReturn, localSb);
                }
            } else if (node.room.equals("end")) {
                localSb.append(node.room);
                paths.add(localSb.toString());
            } else {

                if (Character.isLowerCase(node.room.charAt(0)) && !node.room.equals("end") && !node.room.equals(START)) {
                    addOrBump(localDontReturn, node);
                }

                if (!isDontReturn(node, localDontReturn)) {
                    localSb.append(node.room + ",");
                    for (Node room : node.doors) {
                        makePathsRecursiveHelper(room, paths, localDontReturn, localSb);
                    }
                }
            }
        }
    }

    private void addOrBump(HashSet<Node> dontReturn, Node node) {
        Node retRoom = dontReturn.stream()
                .filter(n -> n.room.equals(node.room))
                .findFirst()
                .orElse(null);
        if (retRoom == null) {
            node.visited = 1;
            dontReturn.add(node);
        } else {
            retRoom.visited += 1;
        }
    }

    private boolean isDontReturn(Node node, HashSet<Node> dontReturn) {
        Node retRoom = dontReturn.stream()
                .filter(n -> n.room.equals(node.room))
                .findFirst()
                .orElse(null);
        if (retRoom == null) {
            return false;
        } else {
            return retRoom.visited > 2;
        }
    }

    private List<String> getDoorsForRoom(String roomName) {
        var l = getLeg(roomName);
        return l
                .stream()
                .map(leg -> leg.to)
                .toList();
    }

    private Node buildNode(String roomName, Set<Node> returnRoom) {
        Set<Node> usedRooms = new HashSet<>(returnRoom);

        Node node = new Node();
        node.room = roomName;
        var doors = getDoorsForRoom(roomName);

        for (String door : doors) {
            var retRoom = usedRooms.stream().filter(r -> r.room.equals(door)).findFirst().orElse(null);

            if (retRoom == null) {
                usedRooms.add(node);
                node.doors.add(buildNode(door, usedRooms));
            } else {
                node.doors.add(retRoom);
            }
        }
        return node;
    }
}
