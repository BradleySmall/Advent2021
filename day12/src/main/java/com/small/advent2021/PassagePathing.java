package com.small.advent2021;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

@Slf4j
public class PassagePathing {

    public static final String START = "start";
    public static final String END = "end";
    private List<String> paths;

    public Integer getCount() {
        return paths.size();
    }

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
        int visited = 0;
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
            log.debug(e.getMessage());
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
            if (seg.from.equals(start) && !seg.to.equals(START) && !seg.from.equals(END)) {
                segs.add(new Pair<>(seg.from, seg.to));
            } else if (seg.to.equals(start) && !seg.from.equals(START) && !seg.to.equals(END)) {
                segs.add(new Pair<>(seg.to, seg.from));
            }
        }
        return segs;
    }

    void makePaths () {

        paths = new ArrayList<>();
        makePathsRecursiveHelper(tree, paths, new ArrayList<>(), new StringBuilder());
        log.debug("done count=("+ paths.size() +") " + paths);
    }


    private void makePathsRecursiveHelper(Node node, List<String> paths, ArrayList<Node> visitedSmallRooms, StringBuilder sb) {

        if (isAlreadyVisited(node, visitedSmallRooms)) {
            return;
        }

        if (node.room.equals(START)) {
            log.debug("Node == START");
            sb.append(node.room).append(",");
            for (Node room : node.doors) {
                makePathsRecursiveHelper(room, paths, new ArrayList<>(visitedSmallRooms), new StringBuilder(sb));
            }
            log.debug("After looping doors START " + sb);
            log.debug("Paths  "+paths);
            log.info("Paths count "+paths.size());
            return;
        }
        if (node.room.equals(END)) {
            sb.append(node.room);
            paths.add(sb.toString());
            log.debug("After adding path " + sb);
            visitedSmallRooms.clear();
            log.debug("Clearing At the END");
            return;
        }

        if (isAlreadyVisited(node, visitedSmallRooms)) {
            return;
        }

        addOrBump(visitedSmallRooms, node);

        log.debug("Before looping doors " + sb + " node " + node.room);
        if (!isAlreadyVisited(node, visitedSmallRooms)) {
            sb.append(node.room).append(",");
            for (Node room : node.doors) {
                makePathsRecursiveHelper(room, paths, new ArrayList<>(visitedSmallRooms), new StringBuilder(sb));
            }
        }
    }

    private boolean isSmallRoom(Node node) {
        return Character.isLowerCase(node.room.charAt(0))
                && !node.room.equals(END);
    }

    private void addOrBump(ArrayList<Node> visitedSmallRooms, Node node) {
        if (isSmallRoom(node)) {
            visitedSmallRooms.add(node);
        }
    }

    private boolean isAlreadyVisited(Node node, ArrayList<Node> visitedSmallRooms) {
        ConcurrentMap<String, Long> map = visitedSmallRooms.stream()
                .collect(Collectors.groupingByConcurrent(e -> e.room, Collectors.counting()));
        log.debug(String.valueOf(map));
        long curCount = map.getOrDefault(node.room, 0L);
        long dblCount = map.values().stream().filter(e -> e >= 2).count();

        if (curCount < 2L) {
            return false;
        }
        if (curCount > 2L) {
            return true;
        }
        return dblCount > 1;
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
            var retRoom = usedRooms .stream()
                    .filter(r -> r.room.equals(door))
                    .findFirst()
                    .orElse(null);

            if (retRoom == null) {
                usedRooms.add(node);
                node.doors.add(buildNode(door, usedRooms));
            } else {
                node.doors.add(retRoom);
            }
        }
        return node;
    }

    public static void main(String[] args) {
        log.info("done");
    }
}
