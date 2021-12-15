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
        Map<String, Long> cntMap = new HashMap<>();
        Map<String, Long> polymerPairs = new HashMap<>();
        for(int index = 0; index < polymerTemplate.length() - 1; ++index) {
            String lS = polymerString.substring(index, index + 1);
            String rS = polymerString.substring(index + 1, index + 2);
            String pair = lS + rS;

            addToCntMap(cntMap, lS, 1L);
            if (index == polymerTemplate.length()-2) {
                addToCntMap(cntMap, rS, 1L);
            }
            polymerPairs.put(pair, polymerPairs.getOrDefault(pair,0L)+1L);
        }
        log.info("Pass #" + 0 + " \n"+polymerPairs.toString());
        log.debug(polymerPairs.toString());
        log.info("CntMap "+cntMap.toString());

        IntStream.range(1, passes+1).forEach(pass -> {
            var changesMap = new HashMap<String, Long>();

            for( var pair : new HashSet<>(polymerPairs.entrySet())) {
                var key = pair.getKey();
                var value = pair.getValue();
                if (value == 0L) {
                    continue;
                }
                String insertionElement = pairInsertionRules.getOrDefault(key, "");
                if (insertionElement.equals("")) {
                    continue;
                }
                var sp = pair.getKey().split("");
                var newPairL = sp[0] + insertionElement;
                var newPairR = insertionElement + sp[1];

                addToCntMap(cntMap, insertionElement, value);

                changesMap.put(newPairL, value);
                changesMap.put(newPairR, value);
                changesMap.put(key, -value);
            }
            changesMap.forEach(
                    (key, value) -> polymerPairs.merge(key, value, Long::sum)
                    );
            polymerPairs.values().removeIf(val -> val == 0);

            log.info("Pass #" + pass + " \n"+polymerPairs.toString());
            log.info("CntMap "+cntMap.toString());
            log.info("Length " + cntMap.values().stream().collect(Collectors.summingLong(Long::longValue)));
        });
    }

    private void addToCntMap(Map<String, Long> cntMap, String keyName, long count) {
        cntMap.put(keyName, cntMap.getOrDefault(keyName, 0L) + count);
    }

    private void countElements(Map<String, Long> polymerPairs) {
        Map<String, Long> cntMap = new HashMap<>();
        for(var p : polymerPairs.entrySet()) {
            var pair = p.getKey().split("");
            cntMap.put(pair[0], cntMap.getOrDefault(pair[0], 0L)+p.getValue());
            cntMap.put(pair[1], cntMap.getOrDefault(pair[1], 0L)+p.getValue());
        }

        log.info(cntMap.toString());
    }

    public void performPass(int passes) {
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

    public long showMostMinusLeast() {
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
}
