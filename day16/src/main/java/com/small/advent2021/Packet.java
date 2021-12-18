package com.small.advent2021;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Map.*;

/*
0 = 0000
1 = 0001
2 = 0010
3 = 0011
4 = 0100
5 = 0101
6 = 0110
7 = 0111
8 = 1000
9 = 1001
A = 1010
B = 1011
C = 1100
D = 1101
E = 1110
F = 1111
 */
/*
000 - 0 - sum
001 - 1 - product
010 - 2 - minimum
011 - 3 - maximum
101 - 4 - literal
101 - 5 - gt
110 - 6 - lt
111 - 7 - eq
 */

@Slf4j
public class Packet {
    public static final String PUSH_D = "PUSH (%d)";
    Deque<Long> stack = new ArrayDeque<>();
    String xlate = "0 = 0000, 1 = 0001, 2 = 0010, 3 = 0011, 4 = 0100, 5 = 0101, 6 = 0110, 7 = 0111, 8 = 1000, 9 = 1001, A = 1010, B = 1011, C = 1100, D = 1101, E = 1110, F = 1111";
    private final Map<String, String> translateMap;
    private final Map<String, String> encodeMap;
    private final Map<String, String> operatorsMap =
            Map.of( "000", "SUM",
                    "001", "PRD",
                    "010", "MIN",
                    "011", "MAX",
                    "100", "LIT",
                    "101", "GT ",
                    "110", "LT ",
                    "111", "EQ ");
    private String packetString;

    public String publicShowAnswer() {
        return stack.getFirst().toString();
    }

    Packet(String fileName) {
        translateMap =
                stream(xlate.split(", "))
                        .map(entry -> entry.split(" = "))
                        .collect(Collectors.toMap(rule -> rule[0], rule -> rule[1]));
        log.debug(String.valueOf(translateMap));


        encodeMap = translateMap
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Entry::getValue, Entry::getKey));
        log.debug(String.valueOf(encodeMap));

        processFile(fileName);
    }

    public List<String> processPackets() {
        List<String> versions = new ArrayList<>();
        String packet = decodeToBinary(packetString);
        while (packet.length() > 8) {
            packet = decodePacket(packet, versions);
        }
        var total = versions
                .stream()
                .mapToInt(v -> Integer.parseInt(v,2))
                .sum();
        log.info(String.valueOf(total));
        return versions;
    }

    private void processFile(String fileName) {
        Path path = Path.of(fileName);
        try {
            packetString = Files.readString(path);
            log.debug(packetString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String decodeToBinary(String encodedHex) {
        return stream(encodedHex.split(""))
                .map(translateMap::get)
                .toList()
                .toString()
                .replace(", ", "")
                .replace("[", "")
                .replace("]", "");
    }

    public String decodePacket(String packet, List<String> versions) {
        String version = packet.substring(0, 3);
        packet = packet.substring(3);
        versions.add(version);
        long literalValue = 0;
        int packetCount = 0;

        String operator = packet.substring(0, 3);
        packet = packet.substring(3);
        String opCode = operatorsMap.get(operator);
        log.debug(String.format("V:%s Opr:%s BEGIN", version, opCode));

        if (operator.equals("100")) { // literal number
            List<String> pieces = new ArrayList<>();
            packet = readLiteral(packet, pieces);
            literalValue = Long.parseLong(String.join("", pieces),16);
            log.debug(String.format("V:%s T:%s (%d)", version, opCode, literalValue));
        } else {
            String lengthId = packet.substring(0, 1);
            packet = packet.substring(1);
            log.debug("LengthId=" + lengthId);

            List<Integer> packetCountList = new ArrayList<>();
            if (lengthId.equals("0")) {
                packet = getSubpacketByLength(packet, versions, packetCountList);
                log.debug(String.format("V:%s T:%s Complete", version, opCode));
                packetCount = packetCountList.get(0);
                packetCountList.clear();
            } else if (lengthId.equals("1")) {
                packet = getSubpacketByCount(packet, versions, packetCountList);
                packetCount = packetCountList.get(0);
                packetCountList.clear();
                log.debug(String.format("V:%s T:%s Complete", version, opCode));
            }
        }
        processOpCode(version, literalValue, packetCount, opCode);
        return packet;
    }

    private void processOpCode(String version, long literalValue, int packetCount, String opCode) {
        log.info(String.format("PERFORM V:%s T:%s packet count %d", version, opCode, packetCount));
        switch (opCode) {
            case "LIT" -> doLIT(literalValue);
            case "SUM" -> doSUM(packetCount);
            case "PRD" -> doPRD(packetCount);
            case "MIN" -> doMIN(packetCount);
            case "MAX" -> doMAX(packetCount);
            case "GT " -> doGT();
            case "LT " -> doLT();
            case "EQ " -> doEQ();
            default -> throw new IllegalStateException("Unexpected value: " + opCode);
        }
    }

    private void doLIT(long literalValue) {
        stack.push(literalValue);
        log.info(String.format(PUSH_D, literalValue));
    }

    private void doEQ() {
        long v1;
        long v2;
        v2 = stack.pop();
        v1 = stack.pop();
        if (v1 == v2) {
            stack.push(1L);
            log.info(String.format(PUSH_D, 1));
        } else {
            stack.push(0L);
            log.info(String.format(PUSH_D, 0));
        }
    }

    private void doLT() {
        long v1;
        long v2;
        v2 = stack.pop();
        v1 = stack.pop();
        if (v1 < v2) {
            stack.push(1L);
            log.info(String.format(PUSH_D, 1));
        } else {
            stack.push(0L);
            log.info(String.format(PUSH_D, 0));
        }
    }

    private void doGT() {
        long v2;
        long v1;
        v2 = stack.pop();
        v1 = stack.pop();
        if (v1 > v2) {
            stack.push(1L);
            log.info(String.format(PUSH_D, 1));
        } else {
            stack.push(0L);
            log.info(String.format(PUSH_D, 0));
        }
    }

    private void doMAX(int packetCount) {
        long v1;
        long v2;
        v1 = stack.pop();
        --packetCount;
        for (; packetCount > 0; --packetCount) {
            v2 = stack.pop();
            v1 = Math.max(v2, v1);
        }
        stack.push(v1);
        log.info(String.format(PUSH_D, v1));
    }

    private void doMIN(int packetCount) {
        long v1;
        long v2;
        v1 = stack.pop();
        --packetCount;
        for (; packetCount > 0; --packetCount) {
            v2 = stack.pop();
            v1 = Math.min(v2, v1);
        }
        stack.push(v1);
        log.info(String.format(PUSH_D, v1));
    }

    private void doPRD(int packetCount) {
        long v1;
        long v2;
        v1 = stack.pop();
        --packetCount;
        for (; packetCount > 0; --packetCount) {
            v2 = stack.pop();
            v1 *= v2;
        }
        stack.push(v1);
        log.info(String.format(PUSH_D, v1));
    }

    private void doSUM(int packetCount) {
        long v1;
        long v2;
        v1 = stack.pop();
        --packetCount;
        for (; packetCount > 0; --packetCount) {
            v2 = stack.pop();
            v1 += v2;
        }
        stack.push(v1);
        log.info(String.format(PUSH_D, v1));
    }

    private String getSubpacketByCount(String packet, List<String> versions, List<Integer> packetCount) {
        var subPacketLength = packet.substring(0, 11);
        packet = packet.substring(11);
        int count = Integer.parseInt(subPacketLength, 2);

        packetCount.add(count);
        for (int x = 0; x < count; ++x) {
            packet = decodePacket(packet, versions);
            log.info("PACKET "+ x);
        }
        return packet;
    }

    private String getSubpacketByLength(String packet, List<String> versions, List<Integer> packetCount) {
        var subPacketLength = packet.substring(0, 15);
        packet = packet.substring(15);
        int length = Integer.parseInt(subPacketLength, 2);

        var subPacket = packet.substring(0, length);
        packet = packet.substring(length);

        int count = 0;
        while(subPacket.length() != 0) {
            log.info("PACKETL "+ count);
            ++count;
            subPacket = decodePacket(subPacket, versions);
        }
        packetCount.add(count);

        return packet;
    }

    private String readLiteral(String packet, List<String> pieces) {
        // walk every 5 bits looking for 0 and the next 4 bits end the packet
        boolean isLast;
        do {
            isLast = packet.charAt(0) == '0';
            packet = packet.substring(1);
            pieces.add(encodeMap.get(packet.substring(0, 4)));
            packet = packet.substring(4);
        } while (!isLast);

        return packet;
    }

    public static void main(String[] args) {
        log.info("Done!");
    }
}
