package com.small.advent2021;

import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

@Log4j
public class SevenSegmentDisplay {
    final List<String> valueList = new ArrayList<>();
    /*
    1 = ab
    2 = cdfbe, (gcdfa), fbcad
    3 = cdfbe, gcdfa, (fbcad)
    4 = eafb
    5 = (cdfbe), gcdfa, fbcad
    6 = (cdfgeb), cagedb, cefabd
    7 = dab
    8 = acedgfb
    9 = cdfgeb, cagedb, (cefabd)
    0 = cdfgeb, (cagedb), cefabd

    L = ef

    1 = 2 long
    4 = 4 long - contains 1 and top left L
    7 = 3 long -
    8 = 7 long -

    3 = 5 long - contains 1
    5 = 5 long - contain top left L
    2 = 5 long -

    9 = 6 long - contains 4
    0 = 6 long - contains 1
    6 = 6 long -
    */
    @SuppressWarnings("SpellCheckingInspection")
    SevenSegmentDisplay(String fileName) {
        processFile(fileName);
    }

    private void processFile(String fileName) {
        Path path = Path.of(fileName);
        log.info("Path =  " + path.toAbsolutePath());
        try (Stream<String> stream = Files.lines(path)) {
            var keyList = stream.map(s -> s.split("\\|"))
                    .toList();
            for (var arr : keyList) {
                valueList.add(decode(arr[0], arr[1]));
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    private String decode(String key, String segments) {
        var keys = key.split(" ");
        Map<String, String> translate = populateTranslate(keys);

        StringBuilder sb = new StringBuilder();
        for (var segment : segments.split(" ")) {
            for(var tr : translate.entrySet()) {
                if (equalsDeep(tr.getValue(), segment)) {
                    sb.append(tr.getKey());
                }
            }
        }
        return sb.toString();
    }

    private Map<String, String> populateTranslate(String[] keys) {
        Map<String, String> translator = new HashMap<>();

        simpleTransEntries(keys, translator);
        complexTransEntries(keys, translator);

        return translator;
    }

    private void complexTransEntries(String[] keys, Map<String, String> translator) {
        String one = translator.get("1");
        String four = translator.get("4");
        String upperLeftL = four.replaceAll("["+one+"]","");

        for (var seg : keys) {
            if (seg.length() == 5) { // 2, 3, 5
                if (containsAllChars(seg, one)) {
                    translator.put("3", seg);
                } else if (containsAllChars(seg, upperLeftL)) {
                    translator.put("5", seg);
                } else {
                    translator.put("2", seg);
                }
            } else if (seg.length() == 6) {
                if (containsAllChars(seg, four)) {
                    translator.put("9", seg);
                } else if (containsAllChars(seg, one)) {
                    translator.put("0", seg);
                } else {
                    translator.put("6", seg);
                }
            }
        }
    }

    private void simpleTransEntries(String[] keys, Map<String, String> translator) {
        for (var seg : keys) {
            switch (seg.length()) {
                case 2 -> translator.put("1", seg);
                case 4 -> translator.put("4", seg);
                case 3 -> translator.put("7", seg);
                case 7 -> translator.put("8", seg);
                default -> {/*nothing to do here*/}
            }
        }
    }

    public int total() {
        return valueList
                .stream().mapToInt(Integer::parseInt).sum();
    }

    private Set<Character> stringToCharacterSet(String s) {
        Set<Character> set = new HashSet<>();
        for (char c : s.toCharArray()) {
            set.add(c);
        }
        return set;
    }

    private boolean containsAllChars
            (String container, String containee) {
        return stringToCharacterSet(container).containsAll
                (stringToCharacterSet(containee));
    }

    private boolean equalsDeep(String s1, String s2) {
        return (containsAllChars(s1, s2) && containsAllChars(s2, s1)) ;
    }

    public static void main(String[] args) {
        SevenSegmentDisplay sevenSegmentDisplay = new SevenSegmentDisplay("day8/input.txt");
        log.info(sevenSegmentDisplay.valueList);
        log.info("Total = " + sevenSegmentDisplay.total());
    }
}
