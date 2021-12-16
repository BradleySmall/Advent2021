package com.small.advent2021;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Slf4j
public class Polymer {

    private String polymerTemplate;
    private Map<String, String> pairInsertionRules;
    private String polymerString;
    private Map<String, Long> cntMap;
    private Long polymerLength;
    private long maxElement;
    private long minElement;

    public Polymer(String fileName) {
        processFile(fileName);
    }

    public static void main(String[] args) {
        log.info("Done!");
    }

    private void processFile(String fileName) {
        Path path = Path.of(fileName);
        try (Stream<String> s = Files.lines(path)) {
            List<String> stringList = s.toList();
            polymerTemplate = stringList.get(0);
            pairInsertionRules = stringList.subList(2, stringList.size())
                    .stream()
                    .map(r -> r.split(" -> "))
                    .collect(Collectors.toMap(ch -> ch[0], ch -> ch[1]));
            polymerString = polymerTemplate;

            log.debug(MessageFormat.format("PolymerTemplate - {0}", polymerTemplate));
            log.debug(MessageFormat.format("Polymer Rules - {0}", pairInsertionRules));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String showTemplate() {
        return polymerTemplate;
    }

    public String showPolymer() {
        return polymerString;
    }

    public void performPassWithMap(int passes) {
        cntMap = new HashMap<>();
        Map<String, Long> polymerPairs = new HashMap<>();
        for(int index = 0; index < polymerTemplate.length() - 1; ++index) {
            String lS = polymerTemplate.substring(index, index + 1);
            String rS = polymerTemplate.substring(index + 1, index + 2);
            String pair = lS + rS;

            addToCountedMap(cntMap, lS, 1L);
            if (index == polymerTemplate.length()-2) {
                addToCountedMap(cntMap, rS, 1L);
            }
            polymerPairs.put(pair, polymerPairs.getOrDefault(pair,0L)+1L);
        }
        log.debug("Pass #" + 0 + " \n"+ polymerPairs);
        log.debug(polymerPairs.toString());
        log.debug("CntMap "+ cntMap.toString());

        for (int pass = 1; pass <= passes; pass++) {
            var changesMap = new HashMap<String, Long>();
            for (var pair : polymerPairs.entrySet()) {
                var key = pair.getKey();
                var value = pair.getValue();

                String insertionElement = pairInsertionRules.getOrDefault(key, "");
                if (insertionElement.equals("") || value == 0L) {
                    continue;
                }
                var splitPair = key.split("");
                var newPairL = splitPair[0] + insertionElement;
                var newPairR = insertionElement + splitPair[1];

                addToCountedMap(cntMap, insertionElement, value);

                addToCountedMap(changesMap, newPairL, value);
                addToCountedMap(changesMap, newPairR, value);
            }
            polymerPairs.clear();
            polymerPairs.putAll(changesMap);
            polymerLength = cntMap.values().stream().mapToLong(Long::longValue).sum();
            maxElement = cntMap.values().stream().max(Comparator.comparingLong(Long::longValue)).orElse(0L);
            minElement = cntMap.values().stream().min(Comparator.comparingLong(Long::longValue)).orElse(0L);
            log.debug("Pass #" + pass + " \n" + polymerPairs);
            log.debug("CntMap " + cntMap.toString());
            log.debug("Length " + polymerLength);
        }
    }

    private void addToCountedMap(Map<String, Long> map, String keyName, long count) {
        map.put(keyName, map.getOrDefault(keyName, 0L) + count);
    }

    public void performPassWithPolymerString(int passes) {
        polymerString = polymerTemplate;
        IntStream.range(0, passes).forEach(z -> {
            StringBuilder sb = new StringBuilder();
            for (int index = 0; index < polymerString.length(); ++index) {
                String lS = polymerString.substring(index, index + 1);
                if (index != polymerString.length() - 1) {
                    String rS = polymerString.substring(index + 1, index + 2);
                    String pair = lS + rS;

                    sb.append(lS).append(pairInsertionRules.getOrDefault(pair, ""));
                } else {
                    sb.append(lS);
                }
            }
            polymerString = sb.toString();
        });
    }

    public long showMostMinusLeastFromPolymerString() {
        var tmp = Arrays.stream(polymerString
                        .split(""))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                ;
        long maxCount = 0L;
        long minCount = 10000000L;

        for (var e : tmp.entrySet()) {
            maxCount = max(maxCount, e.getValue());
            minCount = min(minCount, e.getValue());
        }

        return maxCount - minCount;
    }

    public long getLength() {
        return polymerLength;
    }

    public long getCount(String key) {
        return cntMap.get(key);
    }

    public long getMinCount() {
        return minElement;
    }
    public long getMaxCount() {
        return maxElement;
    }

    public long getMaxMinusMin() {
        return maxElement - minElement;
    }
}
